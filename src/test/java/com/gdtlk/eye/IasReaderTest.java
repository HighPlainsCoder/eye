package com.sovrn.dp.bias;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

import static org.junit.Assert.*;

public class IasReaderTest {

    @Test
    public void getIasPayload() throws Exception {
        String s0 = "http://api.adsafeprotected.com/db2/client/12113/absit?adsafe_url=estesathletics.org";
        String r0 = "{\"action\":\"passed\",\"bsc\":{\"adt\":1000,\"dlm\":999,\"drg\":998,\"alc\":997,\"hat\":996,\"off\":995,\"sam\":994},\"iab1\":[\"iab_sports\"],\"iab2\":[\"iab_t2_cheerlea\"],\"traq\":null,\"ttl\":\"2019-03-16T00:37-0400\"}";

        IasReader reader = new IasReader();
        final String result = reader.reformat(r0);

        JsonNode top = new ObjectMapper().readTree(result);
        JsonNode inner = top.get("2");

        assertEquals(1000,((IntNode)inner.get("217")).intValue());
        assertEquals(997,((IntNode)inner.get("218")).intValue());
        assertEquals(996,((IntNode)inner.get("219")).intValue());
        assertEquals(998,((IntNode)inner.get("220")).intValue());
        assertEquals(999,((IntNode)inner.get("221")).intValue());
        assertEquals(995,((IntNode)inner.get("222")).intValue());
        assertEquals(994,((IntNode)inner.get("223")).intValue());


        String s1 = "http://api.adsafeprotected.com/db2/client/12113/absit?adsafe_url=mainlandlax.com";
        String r1 = "{\"action\":\"passed\",\"bsc\":{\"adt\":1000,\"dlm\":1000,\"drg\":1000,\"alc\":1000,\"hat\":1000,\"off\":1000},\"iab1\":[\"iab_sports\"],\"iab2\":[],\"traq\":null,\"ttl\":\"2019-03-16T00:39-0400\"}";
        reader.reformat(r1);

        String s2 = "http://api.adsafeprotected.com/db2/client/12113/absit?adsafe_url=myjcpress.com";
        String r2 = "{\"action\":\"passed\",\"bsc\":{\"adt\":1000},\"iab1\":[\"iab_lawgovt\"],\"iab2\":[],\"traq\":null,\"ttl\":\"2019-03-16T00:41-0400\"}";
        reader.reformat(r2);

        String s3 = "http://api.adsafeprotected.com/db2/client/12113/absit?adsafe_url=d-11866719591041522168.ampproject.net";
        String r3 = "{\"action\":\"passed\",\"bsc\":{},\"iab1\":[],\"iab2\":[],\"traq\":null,\"ttl\":\"2019-03-16T00:43-0400\"}";
        reader.reformat(r3);

    }
}