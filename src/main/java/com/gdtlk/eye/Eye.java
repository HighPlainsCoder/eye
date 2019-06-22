/** BIAS is Batchy IAS
 * it wakes up, reads [some] hours worth of [preroll],
 * select distinct domains,
 * checks [aerospike] for which domains will expire in [8] hours,
 * then queries [IAS] for fresh data for the expiring ones,
 * and writes the results to [aerospike]
 */

package com.sovrn.dp.bias;


import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.exit;

public class Bias  {
    private static final Logger logger = LogManager.getLogger(Bias.class);

    //NOTE this is the safe test destination, production always overrides to pagecontent
    private static String aeroWriteSet ="testpagecontent";

    //NOTE these must always be given, end will default to == start
    private static String startpartition;
    private static String endpartition;

    private static long targetDate; // calculated from endpartition, or ...

    //TODO find useful default values for these threadcounts
    private static int readThreadCount = 1;
    private static int writeThreadCount = 1;

    private static ConcurrentLinkedDeque<String> readerQ = new ConcurrentLinkedDeque<>();
    private static ConcurrentLinkedDeque<String> writerQ = new ConcurrentLinkedDeque<>();

    private static ThreadGroup readerPool =  new ThreadGroup("readers");
    private static ThreadGroup writerPool =  new ThreadGroup("writers");

    private static AtomicInteger readerCount = new AtomicInteger(0);
    private static AtomicInteger writerCount = new AtomicInteger(0);

    private static AtomicBoolean theReaderQisCompleted = new AtomicBoolean(false);
    private static AtomicBoolean theWriterQisCompleted = new AtomicBoolean(false);
    private static AtomicBoolean shutdownIsHappening = new AtomicBoolean(false);


    public static void main(String args[]) {
        Bias b=null;
        try {
            setupShutdownHook();

            if (!parseArgs(args))
                return;

            b = new Bias();
            b.process();
        } catch (Exception e) {
            logger.error("fatal error {}" , e.getLocalizedMessage());
            exit(1);
        }
    }

    /**
     *  attempt a graceful shutdown
     */
    static void setupShutdownHook() {
        // Setup hook to shutdown threads and close off files correctly.
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                     shutdownIsHappening.set(true);
                     readerPool.interrupt();
                     writerPool.interrupt();
                     logger.debug("shutdown hook for graceful shutdown");
                     try {
                         // this is in case the pools take time to interrupt; they should not take much
                         Thread.sleep(1000);
                     } catch (InterruptedException e) {
                         logger.info("shutdown interrupted {}",e.getLocalizedMessage());
                     }
                }
            }
        );

    }

    /**
     * check the args for needed values,
     * store also optional values
     */
    static boolean parseArgs(String[] args) throws ParseException {
        // includes options, args , System.properties, System.env
        //TODO get consensus on how these should be specified, how collected (this way or another),
        // and how a deep down function (like AeroReadWrite) should get one it might need
        Options options = new Options();
        options.addOption("h", "help", false, "Print help");
        options.addOption("aws", "aerospikewriteset", true, "aerospike set (default: testpagecontent)");
        options.addOption("wt", "writethreads", true, "Number of writer threads (default: 1)");
        options.addOption("rt", "readthreads", true, "Number of reader threads (default: 1)");
        options.addOption("st", "startpartition", true, "first partition to read, format yyyymmddhh");
        options.addOption("en", "endpartition", true, "last partition to read, format yyyymmddhh (default: startpartition)");

        CommandLineParser parser = new PosixParser();
        CommandLine cl = parser.parse(options, args, false);

        if (args.length == 0 || cl.hasOption("h") || !cl.hasOption("st")) {

            HelpFormatter formatter = new HelpFormatter();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            String syntax = Bias.class.getName() + " [<options>]";
            formatter.printHelp(pw, 100, syntax, "options:", options, 0, 2, null);
            sw.append("\n startpartition is mandatory");
            sw.append("\nThere are also several rare options, set as system properties like -Daero.host=xxx");
            sw.append("\nScan the source code for those, or look into the wiki");
            logger.info(sw.toString());

            return false;
        }

        aeroWriteSet = cl.getOptionValue("aws", aeroWriteSet);
        System.setProperty("aero.write.set",aeroWriteSet);//NOTE putting it here so the aero class, way down below, can find it

        startpartition = cl.getOptionValue("st");
        endpartition = cl.getOptionValue("en",startpartition);

        if (cl.hasOption("wt")){
            writeThreadCount  = Integer.parseInt(cl.getOptionValue("wt"));
        }

        if (cl.hasOption("rt")){
            readThreadCount  = Integer.parseInt(cl.getOptionValue("rt"));
        }

        return true;
    }


    /**
     * process does all the work, calling the right methods
     */
    void process() throws Exception {
        long startTime = new Date().getTime();

        targetDate = calculateDate(endpartition);

        setupQs(readThreadCount,writeThreadCount,readerPool,writerPool,targetDate);


        DataReader reader = new DataReader();

        reader.processQuery(startpartition, endpartition);

        Bias.theReaderQisCompleted.set(true);
        waitForWorkersToFinish();

        Metrics.addStat("TotalTime",new Date().getTime() - startTime);
        Metrics.printStatistics();
    }

    /**
     * Creates the threads and pools of threads, using todays favorite thread methods
     */
    void setupQs(int readThreadCount,int writeThreadCount, ThreadGroup readerPool, ThreadGroup writerPool, long targetDate) {

        Thread thread=null;
        for (int ix=0;ix<readThreadCount; ++ix) {
            thread = new Thread(readerPool, new ReaderQProcessor(readerQ,targetDate));
            thread.setName("reader_" + String.valueOf(ix));
            thread.start();
        }
        Metrics.addStat("readThreads",(long)readThreadCount);

        thread=null;
        for (int ix=0;ix<writeThreadCount; ++ix) {
            thread = new Thread(writerPool, new WriterQProcessor(writerQ));
            thread.setName("writer_" + String.valueOf(ix));
            thread.start();
        }
        Metrics.addStat("writeThreads",(long)writeThreadCount);
    }

    /**
     * Since the program is multithreaded, once the main processing is mostly
     * done, it has to wait for workers to finish.  This happens in phases,
     * with supporting variables as seen.
     */
    void waitForWorkersToFinish() {
        while (!readerQ.isEmpty() && !Bias.theReaderQisCompleted.get() &&  !Bias.shutdownIsHappening.get()) {
            try {
                Thread.sleep(1000);//millis
            } catch (InterruptedException e) {
                // The while will take care of us
            }
        }

        while (readerCount.get()>0 && !Bias.shutdownIsHappening.get()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // ditto
            }
        }

        Bias.theWriterQisCompleted.set(true);

        while (writerCount.get()>0 && !Bias.shutdownIsHappening.get()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // ditto
            }
            // update stats
        }

    }

    /**
     * Look at the current time, and calculate the effectiveDate for updating, based on the current expiration logic
     * also must convert the date from normal java time into weird aerospike time
     */
    long calculateDate(String partition) {//NOTE partitions

        long effectiveDate = new Date().getTime();//NONO, use NOW, not then.


        //NOW the logic
        effectiveDate += 8 * 3600 * 1000;//TODO use fancy date arithmetic
        effectiveDate /= 1000; // from millis to seconds
        effectiveDate -= java.time.Duration.between(Instant.parse("1970-01-01T00:00:00Z"),Instant.parse("2010-01-01T00:00:00Z" )).getSeconds();//[seconds between Jan 1 1070 and Jan 1 2010 00:00:00 UTC]

        return effectiveDate;
    }


    public static void writeToReaderQ(String domain){
        readerQ.add(domain);
    }

    public static void writeToWriterQ(String domain){
        writerQ.add(domain);
    }

    public static void adjustReaderCount(int delta) {
        readerCount.addAndGet(delta);
    }

    public static void adjustWriterCount(int delta) {
        writerCount.addAndGet(delta);
    }

    public static boolean readerQIsCompleted() { return theReaderQisCompleted.get(); }

    public static boolean writerQIsCompleted() { return theWriterQisCompleted.get(); }

    public static boolean everybodyOutOfTheProgram() { return shutdownIsHappening.get(); }


}
