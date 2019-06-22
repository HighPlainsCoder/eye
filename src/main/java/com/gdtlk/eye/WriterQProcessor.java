/**
 * This thread processor reads the WriterQ,
 * Queries IAS for its updated safety score,
 * reformats the result,
 * and writes it to [Aerospike]
 */
package com.sovrn.dp.bias;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;

public class WriterQProcessor implements Runnable {
    static final Logger logger = LogManager.getLogger(WriterQProcessor.class);
    static final Logger domainLogger = LogManager.getLogger("slurpee");

    private final ConcurrentLinkedDeque<String> writerQ;
    AeroReadWriter aero = new AeroReadWriter();
    IasReader reader = new IasReader();

    WriterQProcessor(ConcurrentLinkedDeque<String> qq) {
        writerQ=qq;
    }

    @Override
    public void run() {
        Bias.adjustWriterCount(+1);

        while(true) {
            // look in list for a record to read
            // if not, pause, continue, or exit

            while (writerQ.isEmpty() && !Bias.writerQIsCompleted() && !Bias.everybodyOutOfTheProgram()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // no matter
                }
            }

            if ((writerQ.isEmpty() && Bias.writerQIsCompleted())
            || Bias.everybodyOutOfTheProgram())
                break;

            processQEntry();

        }

        reader.close();
        aero.close();
        Bias.adjustWriterCount(-1);
    }

    void processQEntry() {
        String domain=null;
        try {
            domain = writerQ.pop();

            long startTime = new Date().getTime();
            String iaspayload = reader.getIasPayload(domain);
            if (StringUtils.isEmpty(iaspayload))
                return;
            String aerodomain = "http://" + domain + "/";
            aero.aeroWrite(aerodomain, iaspayload);
            long writeTime = new Date().getTime() - startTime;
            domainLogger.info("{}\t{}",aerodomain,iaspayload);
            Metrics.addStat("writerProcessed",+1L);
            Metrics.addStat("AeroWriteTime",writeTime);
        } catch (NoSuchElementException no) {
            // so there were none; grabby other threads, nothing to do
        } catch (Exception any) {
            Metrics.addStat("writerExceptions",+1L);
            //TODO log the exception, flag the record for reprocessing, soldier on
            logger.error ("Exception processing domain {}",domain, any);
        }
    }
}
