package com.datengaertnerei.test.dataservice.cucumber;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.datengaertnerei.test.dataservice.bank.BankAccount;

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

	@Autowired
	protected TestRestTemplate restTemplate;

	@LocalServerPort
	protected int serverPort;

	@When("the client asks for bank account in {word}")
	public void getBankAccount(String city) {
		this.city = city;
		response = restTemplate.getForEntity("http://localhost:" + serverPort + "/api/v1/account/" + city,
				BankAccount.class);
	}

	@Then("the client receives status code OK")
	public void theClientReceivesStatus() {
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@And("the client receives bank account in this city")
	public void theClientReceivesProduct() {
		assertThat(response.getBody().getBank().getCity()).isEqualToIgnoringCase(city);
	}

}
