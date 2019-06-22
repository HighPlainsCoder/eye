/**
 * The AeroReadWriter reads from and writes to Aeroapike
 * Read for the expiration, Write for the new data
 */


package com.sovrn.dp.bias;

import com.aerospike.client.*;

import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


class AeroReadWriter  {
    static final Logger logger = LogManager.getLogger(AeroReadWriter.class);
    String aeroHost="aero1.dfw3.lijit.com"; // pull from the ctor
    String namespace="addelivery";
    String bin = "categories";
    int timeout = 60*1000;//milliseconds
    int port = 3000;
    int ttl = 31*86400; // 31 days in seconds
    int retries = 5;
    String aerospikeReadSet="pagecontent";
    String aerospikeWriteSet="testpagecontent";
    AerospikeClient client = null;//NOT static
    WritePolicy wp;//also not static

    public AeroReadWriter() {
        aeroHost = System.getProperty("aero.host",aeroHost);
        namespace = System.getProperty("aero.namespace",namespace);
        bin = System.getProperty("aero.bin",bin);
        timeout = Integer.parseInt(System.getProperty("aero.timeout",String.valueOf(timeout)));
        port = Integer.parseInt(System.getProperty("aero.port",String.valueOf(port)));
        ttl = Integer.parseInt(System.getProperty("aero.ttl",String.valueOf(ttl)));
        retries = Integer.parseInt(System.getProperty("aero.retries",String.valueOf(retries)));
        aerospikeReadSet = System.getProperty("aero.read.set",aerospikeReadSet);
        aerospikeWriteSet = System.getProperty("aero.write.set",aerospikeWriteSet);

        // creates Aerospike client and policy to write to Aerospike
        ClientPolicy policy = new ClientPolicy();
        policy.timeout = timeout;
        logger.info("Connecting to aerospike at {}",aeroHost);
        try {
            client = new AerospikeClient(policy, new Host(aeroHost, port));
        } catch (AerospikeException e) {
            logger.error("Aerospike not valid because {}", e.getLocalizedMessage());
            throw e; // rethrow so main can catch it
        }
        wp = client.getWritePolicyDefault();
        wp.recordExistsAction = RecordExistsAction.REPLACE;
        wp.expiration = ttl;
        wp.maxRetries = retries;
    }

    public void aeroWrite(String domain,String payload) {
        Key kk = new Key(namespace, aerospikeWriteSet, domain);
        Bin bin1 = new Bin(bin, payload);
        client.put(wp, kk, bin1);
    }


    public long getExpiration(String domain) {
        Key kk = new Key(namespace, aerospikeReadSet, domain);
        Record record = client.get(wp,kk);

        if (record==null)
            return 0;

        return record.expiration;
    }

    public void close() {
        if (client!=null)
            client.close();
    }
}
