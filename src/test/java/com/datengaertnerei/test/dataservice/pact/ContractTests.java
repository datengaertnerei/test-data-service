package com.datengaertnerei.test.dataservice.pact;

import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.datengaertnerei.test.dataservice.DataServiceApplication;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DataServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Provider("test-data-provider")
@PactFolder("src/test/resources/pacts")
class ContractTests {

	@TestTemplate
	@ExtendWith(PactVerificationSpringProvider.class)
	void pactVerificationTestTemplate(PactVerificationContext context) {
		context.verifyInteraction();
	}
}