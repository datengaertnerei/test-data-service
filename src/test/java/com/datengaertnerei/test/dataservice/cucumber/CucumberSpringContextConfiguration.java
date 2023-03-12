package com.datengaertnerei.test.dataservice.cucumber;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.datengaertnerei.test.dataservice.bank.BankAccount;
import com.datengaertnerei.test.dataservice.person.DataImportTest;
import com.datengaertnerei.test.dataservice.person.PostalAddressRepository;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

/**
 * Example Cucumber context and step definitions for REST API testing
 *
 */
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CucumberSpringContextConfiguration {
	private ResponseEntity<BankAccount> response;
	private String city;

	// autowired test template (see Spring Boot doc)
	@Autowired
	protected TestRestTemplate restTemplate;

	// local server port will be dynamic
	@LocalServerPort
	protected int serverPort;

	// test object
	@Autowired
	private PostalAddressRepository repository;

	// setup test env
	@BeforeEach
	public void before() {
		DataImportTest.ensureDataAvailability(repository);
	}
	
	/**
	 * feature file matching "When"
	 * 
	 * @param city the city to fetch a bank for
	 */
	@When("the client asks for bank account in {word}")
	public void getBankAccount(String city) {
		this.city = city;
		response = restTemplate.getForEntity("http://localhost:" + serverPort + "/api/v1/account/" + city,
				BankAccount.class);
	}

	/**
	 * feature file matching "Then"
	 */
	@Then("the client receives status code OK")
	public void theClientReceivesStatus() {
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	/**
	 * feature file matching "And"
	 */
	@And("the client receives bank account in this city")
	public void theClientReceivesProduct() {
		assertThat(response.getBody().getBank().getCity()).isEqualToIgnoringCase(city);
	}

}
