package com.datengaertnerei.test.dataservice.pact;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.datengaertnerei.test.dataservice.DataServiceApplication;
import com.datengaertnerei.test.dataservice.person.DataImportTest;
import com.datengaertnerei.test.dataservice.person.PostalAddressRepository;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DataServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("test-data-provider")
@PactFolder("src/test/resources/pacts")
@Tag("contract")
class ContractTests {

	@LocalServerPort
	private int port;

	@Autowired
	private PostalAddressRepository repository;

	@BeforeEach
	public void before() {
		DataImportTest.ensureDataAvailability(repository);
	}

	@BeforeEach
	void before(PactVerificationContext context) {
		context.setTarget(new HttpTestTarget("localhost", port));
	}

	@TestTemplate
	@ExtendWith(PactVerificationSpringProvider.class)
	void pactVerificationTestTemplate(PactVerificationContext context) {
		context.verifyInteraction();
	}
}