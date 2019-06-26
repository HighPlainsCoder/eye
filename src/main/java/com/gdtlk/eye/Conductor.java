package com.gdtlk.eye;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Exception;




/**
 * A main class, to hold a main().  Sets up parameters and orchestrates all other processes/threads
 */
public class Conductor {

    private static final Logger LOG = LoggerFactory.getLogger(Conductor.class);

    public static void main(String[] args) {
        try {
            PrimeHunter ph  = new PrimeHunter();
            ph.run();

        } catch(Exception e) {
            LOG.error("exception caught in main " + e.getClass().getName(),e);
            System.exit(1);
        }
    }

}
