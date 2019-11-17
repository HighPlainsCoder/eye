package com.sovrn.dp.cucumber;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class HiveHelper {
    private Connection connection=null;
    private Statement statement=null;
    private String hiveHost="hadoop-prd.aws.lijit.com:10000";
    private String hiveUser = "hadoop";
    private String hivePassword = "";
    private String partition=null;

    public HiveHelper(String partition) throws Exception {
        hiveHost = System.getProperty("hive.host",hiveHost);
        hiveUser = System.getProperty("hive.user",hiveUser);
        hivePassword = System.getProperty("hive.password",hivePassword);
        this.partition = partition;

        Class.forName("org.apache.hive.jdbc.HiveDriver");
        connection = DriverManager.getConnection("jdbc:hive2://" + hiveHost,hiveUser,hivePassword);
        statement = connection.createStatement();

        //NOTE if we use this helper, we want to create a clean data set, so drop everything else
        statement.executeQuery("create database test");
        statement.executeQuery("create table test(domain string) PARTITIONED BY (dt string)");
    }

    public void write(String domain) throws Exception {
        statement.executeQuery(String.format("INSERT INTO ad_activity PARTITION(dt='%s') (domain) VALUES ('%s')",partition,domain));
    }

    public void close() throws Exception {
        statement.close();statement=null;
        connection.close();connection=null;
    }
}
