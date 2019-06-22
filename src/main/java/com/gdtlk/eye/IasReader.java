/**
 * IasReader sends HttpRequest to the IAS endpoint for a domain score, and reformats it into the output format
 */
package com.sovrn.dp.bias;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class IasReader {

    //NOTE absit is the code for just bsc score; absuit gives more
    //NOTE iasUrl needs a domain pasted at the end of it
    String iasUrl = "http://api.adsafeprotected.com/db2/client/12113/absit?adsafe_url=";


    //TODO is fasterxml variety jackson the right one? check
    final ObjectMapper mapper = new ObjectMapper();//MUST NOT be static, cause many threads

    //This is the magic mapping from IAS string score types to Blackbird 3-digit codes
    private static Map<String, String> keyMapping;
    static {
        keyMapping = new HashMap<>();
        keyMapping.put("adt", "217");
        keyMapping.put("alc", "218");
        keyMapping.put("hat", "219");
        keyMapping.put("drg", "220");
        keyMapping.put("dlm", "221");
        keyMapping.put("off", "222");
        keyMapping.put("sam", "223");
    }

    public IasReader() {
        iasUrl = System.getProperty("ias.url",iasUrl);
    }

    /**
     * The primary interface.  Given a domain,
     * get a safety score in the format Aerospike (blackbird) wants
     */
    public String getIasPayload(String domain) throws Exception {
        String escapedDomain = URLEncoder.encode(domain, "UTF-8");
        String url = iasUrl + escapedDomain;
        String payload = getIasSafetyScore(url);
        String formattedPayload = reformat(payload);
        return formattedPayload;
    }

    public void close() {
    }

    /**
     * call IAS for a score, read the result into a string
     */
    String getIasSafetyScore(String url) throws Exception {
        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");

        int responseCode = con.getResponseCode();
        if (responseCode/100 != 2) {// the 2xx codes
            //TODO ? maybe handle certain 3xx codes, like 'permanent redirect'?
            throw new RuntimeException("IAS Response code " + responseCode + ": " + con.getResponseMessage());
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    /**
     * reformat the payload from IAS format to the format Blackbird expects
     */
    String reformat(String payload) throws Exception {

        JsonNode node = mapper.readTree(payload);
        if (node==null || node.getNodeType() != JsonNodeType.OBJECT)
            throw new Exception("payload not JSON object");

        JsonNode inner = node.get("bsc");
        if (inner == null || inner.getNodeType()!=JsonNodeType.OBJECT)
            throw new Exception("bsc element not Json Object");

        if (inner.size()==0)
            return null;


        ObjectNode outerResult = mapper.createObjectNode();
        ObjectNode innerResult = mapper.createObjectNode();

        Iterable<Map.Entry<String,JsonNode>> iterable = () -> inner.fields();
        iterable.forEach(kvp -> innerResult.put(keyMapping.get(kvp.getKey()),kvp.getValue()));

        outerResult.set("2",innerResult);//2 is a magic #, but not very magic.  Blackbird demands it


        return outerResult.toString();
    }
}
