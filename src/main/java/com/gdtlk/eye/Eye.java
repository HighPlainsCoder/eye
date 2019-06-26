/** Eye
 * Get a file or directory name
 * Open picture files
 * Count pixels grouped by color
 * Histogram
 */

package com.gdtlk.eye;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.exit;

public class Eye  {
    private static final Logger logger = LogManager.getLogger(Eye.class);


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
        Eye b=null;
        try {
            setupShutdownHook();

            if (!parseArgs(args))
                return;

            b = new Eye();
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
        options.addOption("wt", "writethreads", true, "Number of writer threads (default: 1)");
        options.addOption("rt", "readthreads", true, "Number of reader threads (default: 1)");

        CommandLineParser parser = new PosixParser();
        CommandLine cl = parser.parse(options, args, false);

        if (args.length == 0 || cl.hasOption("h") || !cl.hasOption("st")) {

            HelpFormatter formatter = new HelpFormatter();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            String syntax = Eye.class.getName() + " [<options>]";
            formatter.printHelp(pw, 100, syntax, "options:", options, 0, 2, null);
            sw.append("\nThere are also several rare options, set as system properties like -Daero.host=xxx");
            sw.append("\nScan the source code for those, or look into the wiki");
            logger.info(sw.toString());

            return false;
        }

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

        // TODO: set up initial threads/processes

        Eye.theReaderQisCompleted.set(true);
        waitForWorkersToFinish();

        Metrics.addStat("TotalTime",new Date().getTime() - startTime);
        Metrics.printStatistics();
    }


    /**
     * Since the program is multithreaded, once the main processing is mostly
     * done, it has to wait for workers to finish.  This happens in phases,
     * with supporting variables as seen.
     */
    void waitForWorkersToFinish() {
        while (!readerQ.isEmpty() && !Eye.theReaderQisCompleted.get() &&  !Eye.shutdownIsHappening.get()) {
            try {
                Thread.sleep(1000);//millis
            } catch (InterruptedException e) {
                // The while will take care of us
            }
        }

        while (readerCount.get()>0 && !Eye.shutdownIsHappening.get()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // ditto
            }
        }

        Eye.theWriterQisCompleted.set(true);

        while (writerCount.get()>0 && !Eye.shutdownIsHappening.get()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // ditto
            }
            // update stats
        }

    }
}
