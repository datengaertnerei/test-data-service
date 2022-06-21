package com.datengaertnerei.test.dataservice.cucumber;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

import org.junit.jupiter.api.Tag;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("com/datengaertnerei/test/dataservice/cucumber")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.datengaertnerei.test.dataservice.cucumber")
@Tag("bdd")
public class CucumberTest {
	// empty class to start cucumber feature based tests
}
