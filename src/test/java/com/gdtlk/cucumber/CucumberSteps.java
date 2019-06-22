package com.sovrn.dp.cucumber;


import java.io.*;
import java.util.*;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import cucumber.api.DataTable;


import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import com.sovrn.dp.bias.Bias;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CucumberSteps {
    private static final Logger logger = LogManager.getLogger(Bias.class);

    Set<String> domains = new HashSet<>();



    @And("^we have system properties$")
    public void weHaveSystemProperties(DataTable inputs) throws Exception {
        logger.info("we have system properties");

        List<List<String>> data = inputs.cells(0);
        for (List<String> row : data) {
            System.setProperty(row.get(0), row.get(1));
        }
    }


    @When("^we call bias main with params \"(.*)\"$")
    public void weCallBiasMainWithParams(String args) throws Throwable {
        logger.info("we call the bias program");
        List<String> lastargs = (List<String>)Arrays.asList(args.split("[ \t\n]+"));

        Bias.main(lastargs.toArray(new String[lastargs.size()]));
    }


    @Then("^we fill in test data in hive partition \"(.*)\"$")
    public void weFillInTestDomains(String partition, DataTable inputs) throws Exception {
        logger.info("we fill in test hive");
        HiveHelper hive = new HiveHelper(partition);

        List<List<String>> data = inputs.cells(0);
        for (List<String> row : data) {
            hive.write(row.get(0));
            domains.add(row.get(0));
        }

        hive.close();
        hive=null;
    }

    @Then("^we fill in aerospike data$")
    public void weFillInAerospikeData(DataTable inputs) throws Exception {
        logger.info("fill in aerospike ...");
        AeroHelper aero = new AeroHelper();

        List<List<String>> data = inputs.cells(0);
        for (List<String> row : data) {
            aero.put(row.get(0), Integer.parseInt(row.get(1)), row.get(2));
            domains.add(row.get(0));
        }

        aero.close();
    }

    @Then("^nothing$")
    public void nothing() {
        logger.info("nothing is expected");
    }



    @Then("^the data matches for (.*)$")
    public void theDataIsPresentInTheOutput(String checkfile) throws Exception {
        logger.info("the data matches...");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(checkfile), "UTF-8"));

        AeroHelper aero = new AeroHelper();

        while(true) {
            String domain = reader.readLine();
            if (domain==null) break;
            domain = "http://"+domain+"/";
            String payload = aero.get(domain);
            assertThat(payload,matchesPattern("\\{\"2\":.+\\}"));//TODO get more specific about pattern
        }

        aero.close();
    }


    @Then("^the data matches this$")
    public void theDataIsPresentInTheOutput(DataTable table) throws Exception {
        logger.info("the data matches THIS");
        AeroHelper aero = new AeroHelper();

        List<List<String>> data = table.cells(0);
        for (List<String> row : data) {
            String domain = row.get(0);
            String payload = row.get(1);
            String testpayload = aero.get("http://"+domain+"/");

            assertNotNull(testpayload);
        }

        aero.close();
    }



}
