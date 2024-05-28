package com.datengaertnerei.test.dataservice.browser;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.datengaertnerei.test.dataservice.person.DataImportTest;
import com.datengaertnerei.test.dataservice.person.PostalAddressRepository;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Tag("browser")
class TestDataServiceBrowserTests {

	@LocalServerPort
	int randomServerPort;

	@Autowired
	private PostalAddressRepository repository;

	@BeforeEach
	public void before() {
		DataImportTest.ensureDataAvailability(repository);
	}

	@Test
	void testMe() {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.chromium().launch();
			Page page = browser.newPage();
			Consumer<String> c = new Consumer<>() {
				@Override
				public void accept(String t) {
					fail(t);
				}
			};
			page.onPageError(c);
			page.navigate("http://localhost:" + randomServerPort);
			assertNotEquals("", page.textContent("#name"));
			assertNotEquals("", page.textContent("#street"));
			assertNotEquals("", page.textContent("#city"));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
