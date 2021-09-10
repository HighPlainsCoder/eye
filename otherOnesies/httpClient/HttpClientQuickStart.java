/*
 * Http example
 */



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


import java.net.HttpURLConnection;

public class HttpClientQuickStart {

    public static void main(String[] args) throws Exception {

        URL urlObj = new URL("http://eeewert.org/");
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        if (responseCode/100 != 2) {// the 2xx codes
            throw new RuntimeException("Response code " + responseCode + ": " + con.getResponseMessage());
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println( response.toString() );


    }

}
