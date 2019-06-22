/**
 * This thread worker processes a Q of domains,
 * asks [Aerospike] if they are near expiration,
 * and writes the winners into the WriterQ
 */

package com.sovrn.dp.bias;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ReaderQProcessor implements Runnable {
    static final Logger logger = LogManager.getLogger(ReaderQProcessor.class);

    private final ConcurrentLinkedDeque<String> readerQ;
    private final long targetDate;// the date used to select "soon to expire" records, in funky format

    AeroReadWriter aero = new AeroReadWriter();

    ReaderQProcessor(ConcurrentLinkedDeque<String> qq, long date) {
        readerQ=qq;
        targetDate=date;
    }

    @Override
    public void run() {
        Bias.adjustReaderCount(+1);

        while (true) {
            // look in list for a record to read
            // if not, pause, continue, or exit
            while (readerQ.isEmpty() && !Bias.readerQIsCompleted() && !Bias.everybodyOutOfTheProgram()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // no matter
                }
            }

            if ((readerQ.isEmpty() && Bias.readerQIsCompleted())
            || Bias.everybodyOutOfTheProgram())
                break;

            processQEntry();

        }

        Bias.adjustReaderCount(-1);
    }

    void processQEntry() {
        String domain=null;
        try {
            domain = readerQ.pop();

            Metrics.addStat("readerProcessed",+1L);
            String aerodomain = "http://" + domain + "/";
            if (aero.getExpiration(aerodomain) < targetDate){
                Bias.writeToWriterQ(domain);
            }

        } catch (NoSuchElementException no) {
            // so there were none; grabby other threads, nothing to do
        } catch (Exception any) {
            Metrics.addStat("readerExceptions",+1L);
            //todo log the exception, flag the file for reprocessing, soldier on
            logger.error ("Exception processing {}", domain, any);
        }
    }
}

