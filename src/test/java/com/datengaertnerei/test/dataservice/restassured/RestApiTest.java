package com.datengaertnerei.test.dataservice.restassured;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

/**
 * REST assured based test
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class RestApiTest {

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
