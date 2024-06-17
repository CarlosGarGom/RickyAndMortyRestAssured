package com.cucumber.stepdefs;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "json:src/test/resources/reports/cucumber.json",
                "html:src/test/resources/reports/cucumber-html-report.html"},
        glue = {"com.cucumber.stepdefs"},
        features = {"src/test/resources"}
)

public class RunCucumberTest {
}
