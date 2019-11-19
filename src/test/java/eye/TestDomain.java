package com.sovrn.dp.bias;


import org.junit.Test;
import static org.junit.Assert.*;

public class TestDomain {

    @Test
    public void testCleanup() {
        System.out.println("UT: domain");
        String result;

        result = Domain.cleanup("");
        assertNull(result);

        result = Domain.cleanup(null);
        assertNull(result);

        result = Domain.cleanup("0b50d2df-b6fd-4e70-b411-534cb9af4bd8");
        assertNull(result);

        result = Domain.cleanup("10.0.0.254");
        assertNull(result);

        result = Domain.cleanup("\t  HtTpS");
        assertNull(result);

        result = Domain.cleanup("d-2845879851545044388.ampproject.net");
        assertNull(result);

        result = Domain.cleanup("amp--co-marca-com.cdn.ampproject.org");
        assertNull(result);

        result = Domain.cleanup("%2f%2fwww.fragrantica.fr%2fdesigner%2fjean-d%60albret.html");
        assertNull(result);

        result = Domain.cleanup("www.Furry.com.  ");
        assertEquals("furry.com",result);


        result = Domain.cleanup("clean.co.uk");
        assertEquals("clean.co.uk",result);


    }

}
