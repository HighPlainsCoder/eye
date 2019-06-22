/**
 * Reads data from [hive] for the distinct domains
 * puts them into a Q for processing
 */
package com.sovrn.dp.bias;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class DataReader {
    static final Logger logger = LogManager.getLogger(DataReader.class);

    private Connection connection=null;
    private String hiveHost="hadoop-prd.aws.lijit.com:10000";
    private String hiveUser = "hadoop";
    private String hivePassword = "";

    private String query="select distinct domain from  sourcedata.ad_activity where dt between '%s' and '%s'";
    // "select domain from sourcedata.ad_activity where dt='2019022608' limit 20;" // for testing

    DataReader() {
        hiveHost = System.getProperty("hive.host",hiveHost);
        hiveUser = System.getProperty("hive.user",hiveUser);
        hivePassword = System.getProperty("hive.password",hivePassword);
        query = System.getProperty("hive.query",query);
    }

    /**
     * given a starting and ending partition (maybe the same), return all the unique domains into the Q
     */
    public void processQuery(String startpartition, String endpartition) throws Exception {
        long startTime = new Date().getTime();
        Set<String> unique = new HashSet<>();
        long linecount = 0;
        ResultSet results = runHiveQuery(String.format(query,startpartition,endpartition));

        logger.info("Received results for partitions {} through {}", startpartition, endpartition);

        while (results.next()) {
            String domain = results.getString(1);
            if (domain == null) continue;
            if (domain.isEmpty()) continue;
            ++linecount;

            domain = Domain.cleanup(domain);
            if (domain == null) continue;//NOTE domain.cleanup will reject some inputs entirely
            unique.add(domain);
        }

/**** TEST unique.addAll(Arrays.asList(
                "wiadomosci.wp.pl",
                "mesewcrazy.com",
                "ijustmadethisup.florb",
                "rosenheim24.de",
                "history.com",
                "planetware.com",
                "elconfidencial.com"));
****/

        for (String domain : unique) {
                Bias.writeToReaderQ(domain);
        }
        close();
        long endTime = new Date().getTime() - startTime;

        Metrics.addStat("queryRecords",linecount);
        Metrics.addStat("UniqueDomains",(long)unique.size());
        Metrics.addStat("processQueryTime",endTime);
    }

    ResultSet runHiveQuery(String query)  throws Exception {
        logger.debug("Executing hive query: " + query);
        //TODO maybe: create an ephemeral hive or spark for this query
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        //TODO maybe: move pubrollup, user and password to properties
        connection = DriverManager.getConnection("jdbc:hive2://" + hiveHost,hiveUser,hivePassword);
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    void close() {
        if (connection!=null) {
            logger.debug("Closing hive connection.");
            try {
                connection.close();
            } catch (SQLException se){
                logger.warn("exception on close, soldiering on",se);
            }
        }
    }



}
