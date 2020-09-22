package com.datengaertnerei.test.dataservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

@SpringBootTest
class TestDataServiceFrontTests {

	@Autowired
	private FrontEndController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

	@Test
	void shouldReturnRandomPersonModel() {
		Model model = new ExtendedModelMap();
		String view = controller.index(model);
		assertThat(model.getAttribute("givenName")).isNotNull();
		assertThat(model.getAttribute("familyName")).isNotNull();
		// check birthdate
		assertThat(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMAN)
				.parse(model.getAttribute("birthDate").toString()).isSupported(ChronoField.YEAR)).isTrue();
		// check email
		assertThat(model.getAttribute("email").toString().contains("@")).isTrue();
		assertThat(model.getAttribute("addressLocality")).isNotNull();
		assertThat(model.getAttribute("postalCode")).isNotNull();
		assertThat(model.getAttribute("streetAddress")).isNotNull();
		assertThat(model.getAttribute("houseNumber")).isNotNull();
		assertThat(model.getAttribute("landline")).isNotNull();
		assertThat(model.getAttribute("mobile")).isNotNull();
		assertThat(model.getAttribute("iban")).isNotNull();
		assertThat(view).isEqualTo("front");
	}

}
