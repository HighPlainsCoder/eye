/** Template
 * Pointless class just demonstrating various techniques
 */

package com.booflamoo.template;


import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;


import static java.lang.System.exit;

public class Template  {
    private static final Logger errorlogger = LogManager.getLogger(Template.class);
    private static final Logger statslogger = LogManager.getLogger("MERLE");//matches a logger name found in log4j2.properties

    private static long numRecords=1;
    private static String topic="wheel";
    private static String kafkaHost = "kafka.gdtlk.com:9092";

    private static AtomicBoolean shutdownIsHappening = new AtomicBoolean(false);

    private Random rnd = new Random();

    public static void main(String args[]) {
        Template b=null;
        try {
            setupShutdownHook();

            if (!parseArgs(args))
                return;

            b = new Template();
            b.process();
        } catch (Exception e) {
            errorlogger.error("fatal error {}" , e.getLocalizedMessage());
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
                     errorlogger.debug("shutdown hook for graceful shutdown");
                     try {
                         // this is in case the pools take time to interrupt; they should not take much
                         Thread.sleep(1000);
                     } catch (InterruptedException e) {
                         errorlogger.info("shutdown interrupted {}",e.getLocalizedMessage());
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
        options.addOption("t", "topic", true, "kafka topic to write to (default: wheel)");
        options.addOption("k", "kafka", true, "kafka host:port (default: kafka.gdtlk.com:9092)");
        options.addOption("n", "numrecords", true, "Number of records to write (default: 1)");

        CommandLineParser parser = new PosixParser();
        CommandLine cl = parser.parse(options, args, false);

        if (cl.hasOption("h")) {

            HelpFormatter formatter = new HelpFormatter();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            String syntax = Template.class.getName() + " [<options>]";
            formatter.printHelp(pw, 100, syntax, "options:", options, 0, 2, null);
            sw.append("\nThere are also several rare options, set as system properties like -Daero.host=xxx");
            sw.append("\nScan the source code for those");
            errorlogger.info(sw.toString());

            return false;
        }

        topic = cl.getOptionValue("t", topic);
        kafkaHost = cl.getOptionValue("k", kafkaHost);

        if (cl.hasOption("n")){
            numRecords  = Integer.parseInt(cl.getOptionValue("n"));
        }

        return true;
    }


    /**
     * process does all the work, calling the right methods
     */
    void process() throws Exception {
        long startTime = new Date().getTime();

        //sampleProducer();
        oalgProducer();
    }


    void sampleProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaHost);
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");//ByteArraySerializer

        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < numRecords; i++) {
            producer.send(new ProducerRecord<String, String>(topic, Integer.toString(i), Integer.toString(i)));
        }

        producer.close();
    }

    //TODO compile wheel avro into a Wheel class
    void wheelProducer() throws Exception {

        ByteBuffer version = ByteBuffer.allocate(4);
        version.putInt(34);
        byte[] vv = version.array();


        final Wheel.Builder builder = Wheel.newBuilder();

        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaHost);
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        final Producer<String, byte[]> adDeliveryKafkaProducer = new KafkaProducer<>(props);

        for (int i=0;i<numRecords;++i) {

            //TODO remove the need for this, but keep the code around in an example method
            //NOTE first, use reflection to null out ALL the fields, because
            // many of them must be nulled (because they have no 'default',
            // and this is cleaner than 200 setX(null);
            Method[] methods = Wheel.Builder.class.getMethods();
            for (Method m : methods) {
                if (m.getName().startsWith("set")) {
                    //NOTE newObject[] {null} is the way to pass "null" in through the varargs
                    m.invoke(builder, new Object[] { null } );
                }
            }

            //TODO replace with wheel members
            bidder.setIsThrottledEligible(false);

            builder.setRtbBidders(new ArrayList(Arrays.asList(bidder)));


            final OaLGStream record = builder.build();
            DatumWriter writer = new SpecificDatumWriter<OaLGStream>(OaLGStream.getClassSchema());
            ByteArrayOutputStream outstream = new ByteArrayOutputStream();

            //TODO replace with Kafka Schema registry header (data & code)
            outstream.write(0);
            outstream.write(vv);
            Encoder encoder = EncoderFactory.get().directBinaryEncoder(outstream, null);
            writer.write(record,encoder);


            {
                try {
                    adDeliveryKafkaProducer.send(new ProducerRecord<String, byte[]>(topic, record.getTransactionId(), outstream.toByteArray() ));
                    logStats(record.getTransactionId(),record.getDomain());
                } catch (final Exception e) {
                    System.out.println("Failed to send");
                }
            }
        }
    }

    long getRandomNumber(long lowerbound, long upperbound) {
        return rnd.longs(1,lowerbound,upperbound).iterator().next();
    }

    String getRandomString(int minsize, int maxsize) {
        final int unicode_a = 97;
        final int unicode_A = 65;
        final int unicode_0 = 48;
        StringBuilder result = new StringBuilder();
        int size = (int)getRandomNumber(minsize,maxsize);
        for (int ix=0;ix<size;++ix) {
            int code = rnd.nextInt(62);
            if (code<26)
                result.append(Character.toString(unicode_a+code));
            else if (code<52)
                result.append(Character.toString(unicode_A-26+code));
            else
                result.append(Character.toString(unicode_0-52+code));
        }
        return result.toString();
    }

    //TODO expand to all English babble, also more flexi rule/token set (consonants, vowels, cv,cvc, cvvc, ...)
    String getRandomBabble(int syllables) {
        final ArrayList<String> bits = new ArrayList(Arrays.asList("bee","boo","foo","bar","baz","zoo","flim","flam","biz","bor","fat","hat","rat","cat","moo","noo","coo","hoo","hu","woo","poo","pa","pay","pee","pep","jim","jam","jar","jeer","kik","klak","kek","kook"));
        StringBuilder result = new StringBuilder();
        for (int ix=0;ix<syllables;++ix) {
            result.append(bits.get(rnd.nextInt(bits.size())));
        }
        return result.toString();
    }

    void logStats(String transaction_id,String domain) {
        statslogger.info("wheel written {} {}",transaction_id,domain);
    }

    //TODO hook up to loops above
    public static boolean everybodyOutOfTheProgram() { return shutdownIsHappening.get(); }

}
