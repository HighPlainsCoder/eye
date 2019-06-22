package com.sovrn.dp.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(glue = {"classpath:com.sovrn.dp.cucumber"}, features={"classpath:com/sovrn/dp/cucumber"})
public class CucumberRunner {
}
