package com.sovrn.dp.bias;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Metrics {
    private static final Logger logger = LogManager.getLogger(Metrics.class);

    static Map<String,Long> stats = new HashMap<>();

    private Metrics() {} // cause you cant make one

    synchronized static void addStat(String s,Long l) {
        if (stats.containsKey(s)) {
            l = ((Long)stats.get(s)) + l;
        }
        stats.put(s,l);
    }

    static void printStatistics() {
        for( Map.Entry<String,Long> e : stats.entrySet()) {
            logger.info(String.format("%9d : %s", e.getValue(), e.getKey() ));
        }

    }


}
