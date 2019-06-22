package com.sovrn.dp.cucumber;

import com.aerospike.client.*;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;

import java.util.Map;

public class AeroHelper {

    AerospikeClient client = null;//NOT static
    WritePolicy wp;//also not static

    static String aeroHost="aero1.dfw3.lijit.com"; // pull from the ctor
    static String namespace="addelivery";
    static String aerospikeReadSet="pagecontent";
    static String aerospikeWriteSet="testpagecontent";
    static int port = 3000;

    public AeroHelper() {
        aeroHost = System.getProperty("aero.host",aeroHost);
        namespace = System.getProperty("aero.namespace",namespace);
        aerospikeReadSet = System.getProperty("aero.read.set",aerospikeReadSet);
        aerospikeWriteSet = System.getProperty("aero.write.set",aerospikeWriteSet);
        port = Integer.parseInt(System.getProperty("aero.port",String.valueOf(port)));

        ClientPolicy policy = new ClientPolicy();
        client = new AerospikeClient(policy, new Host(aeroHost, port));
        wp = client.getWritePolicyDefault();
        wp.recordExistsAction = RecordExistsAction.REPLACE;

    }

    public void put(String domain, int ttl, String payload) {
        wp.expiration = ttl;
        Key kk = new Key(namespace, aerospikeWriteSet, domain);
        Bin bin1 = new Bin("categories", payload);
        client.put(wp, kk, bin1);
    }


    public String get(String domain) {
        Key kk = new Key(namespace, aerospikeWriteSet, domain);
        Record rec = client.get(wp, kk);
        int expiration = rec.expiration;
        Map<String,Object> m = rec.bins;
        String payload = (String) m.get("categories");
        return payload;
    }

    public void close() {
        client.close();client=null;
        wp=null;
    }
}
