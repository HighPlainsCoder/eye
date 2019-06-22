package com.sovrn.dp.bias;


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class Domain {


    /**
     * DANGER ALERT NOTE
     * this code has to function like the matching code in Blackbird that will be retrieving
     * data from aerospike
     * This is what was in the old ias:
     * https://github.com/sovrn/ias-flink/blob/master/src/test/java/com/sovrn/cucumber/AerospikeSteps.java#L79
     *
     * This is what is in Blackbird:
     * https://github.com/sovrn/blackbird/blob/master/blackbird-txn-model/src/main/java/com/sovrn/blackbird/model/bidrequest/URLNormalizer.java#L30
     * https://github.com/sovrn/blackbird/blob/master/blackbird-lookup/src/main/java/com/sovrn/blackbird/lookup/AdSafeLookup.java#L44
     *
     *
     * This is in AL:
     * https://github.com/sovrn/AL/blob/master/hive/preroll.hql#L32
     *
     *
     * Note how they are not the same.
     */


    private static final Pattern guid = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
    private static final Pattern ip10dot = Pattern.compile("[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+");
    private static final Pattern nohttp = Pattern.compile("[ \t]*https?", Pattern.CASE_INSENSITIVE);
    private static final Pattern amp = Pattern.compile("d-[0-9]+\\.ampproject\\.net");
    private static final Pattern amp2 = Pattern.compile(".+\\.cdn\\.ampproject\\.org");
    private static final Pattern path = Pattern.compile("%2f%2f.*\\.html", Pattern.CASE_INSENSITIVE);




    private Domain() {} // cause you never make an object of this

    public static String cleanup(String domain) {

        //remove nulls, empties, guids
        if (StringUtils.isEmpty(domain))
            return null;

        if (guid.matcher(domain).matches())
            return null;

        if (ip10dot.matcher(domain).matches())
            return null;

        if (nohttp.matcher(domain).matches())
            return null;

        if (amp.matcher(domain).matches())
            return null;

        if (amp2.matcher(domain).matches())
            return null;

        if (path.matcher(domain).matches())
            return null;


        String result = domain.toLowerCase();
        result = result.replaceAll("[ \t\n\r]","");
        result = result.replaceAll("^www\\.", "");
        if (result.endsWith("."))
            result = result.substring(0,result.length()-1);

        //TODO when can coordinate with AL, and Blackbird, also do like
        // remove leading i. and m.
        // remove CDN stuff like "c123."
        // maybe allow non-ascii, to the extent they are valid in domains



        return result;
    }
}
