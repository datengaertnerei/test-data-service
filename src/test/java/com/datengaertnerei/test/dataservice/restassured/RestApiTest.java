package com.datengaertnerei.test.dataservice.restassured;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import com.datengaertnerei.test.dataservice.person.DataImportTest;
import com.datengaertnerei.test.dataservice.person.PostalAddressRepository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

/**
 * REST assured based test
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Tag("rest")
class RestApiTest {

    @LocalServerPort
    private int port;

	@Autowired
	private PostalAddressRepository repository;

	@BeforeEach
	public void before() {
		DataImportTest.ensureDataAvailability(repository);
	}
    
    @BeforeEach // BeforeAll runs ahead of local port injection 
    void setup() {
		RestAssured.port = port;
    }
    
	@Test
	void shouldReturnCreditCard() {
		ValidatableResponse response = get("/api/v1/creditcard").then().assertThat().statusCode(200).
		        and().contentType(ContentType.JSON).and().body("number", notNullValue()).body("type", notNullValue())
				.body("cvc", notNullValue());
		String expiry = response.extract().jsonPath().getString("expiry");
		LocalDate localDate = LocalDate.parse(expiry, DateTimeFormatter.ISO_LOCAL_DATE);
		assertThat(localDate).isAfter(LocalDate.now());
	}
}
