package com.booflamoo.eye;

import org.log4j.Logger;
import org.log4j.LoggerFactory;

import java.lang.Exception;
import java.util.Map;
import java.util.TreeMap;


/**
 * A main class, to hold a main().  Sets up parameters and orchestrates all other processes/threads
 */
public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Conductor.class);

    public static void main(String[] args) {
        try {
            PicHunt ph  = new PicHunt(args[0],true);
            ph.run();

        } catch(Exception e) {
            LOG.error("exception caught in main " + e.getClass().getName(),e);
            System.exit(1);
        }
    }

    public static void main2(String[] args) {
        //first get some parameters
        //  directory, basename, countingscheme
        //next scan directory for files
        //  for each file
        //    rename according to the rules (basename, countingscheme, oldname)
    }

}
