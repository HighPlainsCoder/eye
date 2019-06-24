package org.eeewert.primes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Exception;




/**
 * A main class, to hold a main()
 */
public class FakeLoad {

    private static final Logger LOG = LoggerFactory.getLogger(FakeLoad.class);

    public static void main(String[] args) {
    	try {


            PrimeHunter ph  = new PrimeHunter();
            ph.run();

		} catch(Exception e) {
            LOG.error("exception caught in main " + e.getClass().getName(),e);
		}
    }

}
