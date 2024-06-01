package com.datengaertnerei.test.dataservice.junit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.iban4j.IbanFormatException;
import org.iban4j.IbanUtil;
import org.iban4j.InvalidCheckDigitException;
import org.iban4j.UnsupportedCountryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import com.datengaertnerei.test.dataservice.RestApiController;
import com.datengaertnerei.test.dataservice.bank.BankAccount;
import com.datengaertnerei.test.dataservice.bank.CreditCard;
import com.datengaertnerei.test.dataservice.person.AgeRange;
import com.datengaertnerei.test.dataservice.person.DataImportTest;
import com.datengaertnerei.test.dataservice.person.Person;
import com.datengaertnerei.test.dataservice.person.PostalAddress;
import com.datengaertnerei.test.dataservice.person.PostalAddressRepository;
import com.datengaertnerei.test.dataservice.person.TaxIdGenerator;
import com.datengaertnerei.test.dataservice.phone.PhoneNumber;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("unit")
class TestDataServiceApiTests {
	private static final double THRESHOLD_SENIOR = 65.0;
	private static final double THRESHOLD_ADULT = 18.0;

	@LocalServerPort
	private int port;

	@Autowired
	private RestApiController restController;

	@Autowired
	private PostalAddressRepository repository;

	@BeforeEach
	public void before() {
		DataImportTest.ensureDataAvailability(repository);
	}

	@Test
	void contextLoads() {
		assertThat(restController).isNotNull();
	}
	
	@Test
	void checkBoundaries() {
		long offset = repository.min() - 1L; 
		long amount = repository.count();
		assertThat(repository.getReferenceById(offset + 1L)).isNotNull();
		assertThat(repository.getReferenceById(offset + amount)).isNotNull();
	}

	/**
	 * Consecutive calls should create different (random) results for
	 * Person/PostalAddress
	 */
	@Test
	void shouldReturnRandomPerson() {

		Set<String> persons = new HashSet<>();
		SortedSet<PostalAddress> addresses = new TreeSet<>();
		// create a few more persons to hit more date of birth branches
		for (int i = 1; i < 10; i++) {
			assertPerson(persons, addresses, null);
			assertPerson(persons, addresses, AgeRange.ALL);
			assertPerson(persons, addresses, AgeRange.ADULT);
			assertPerson(persons, addresses, AgeRange.MINOR);
			assertPerson(persons, addresses, AgeRange.SENIOR);
		}
	}

	private void assertPerson(Set<String> persons, SortedSet<PostalAddress> addresses, AgeRange range) {
		Person person = restController.person(range);
		assertThat(person).isNotNull();
		assertThat(person.getAddress()).isNotNull();
		assertNotEquals(TaxIdGenerator.DEFAULT_TAX_ID, person.getTaxId());
		assertEquals(TaxIdGenerator.DEFAULT_TAX_ID.length(), person.getTaxId().length());
		String personString = stringifyPerson(person);
		assertThat(persons).doesNotContain(personString);
		addresses.add(person.getAddress());
		persons.add(personString);

		if (null != range) {
			long yearsBetween = ChronoUnit.YEARS.between(person.getBirthDate(), LocalDate.now());

			switch (range) {
			case ADULT:
				assertTrue(yearsBetween >= THRESHOLD_ADULT);
				break;

			case MINOR:
				assertTrue(yearsBetween <= THRESHOLD_ADULT);
				break;

			case SENIOR:
				assertTrue(yearsBetween >= THRESHOLD_SENIOR);
				break;

			case ALL:
			default:
				break;
			}
		}
	}

	/**
	 * Consecutive calls should create different (random) results for
	 * Person/PostalAddress
	 */
	@Test
	void shouldReturnRandomPersonForCity() {

		Set<String> checkList = new HashSet<>();

		// get first record for unknown city
		Person result = restController.personForCity("xxx", null);
		assertThat(result).isNotNull();
		String resultString = stringifyPerson(result);
		assertThat(checkList).doesNotContain(resultString);
		checkList.add(resultString);

		for (int i = 1; i < 5; i++) {
			result = restController.personForCity("hamburg", AgeRange.ALL);
			assertThat(result).isNotNull();
			resultString = stringifyPerson(result);
			assertThat(checkList).doesNotContain(resultString);
			checkList.add(resultString);
		}
	}

	/**
	 * Consecutive calls should create different (random) results for
	 * Person/PostalAddress
	 */
	@Test
	void shouldReturnRandomPersonForArea() {

		Set<String> checkList = new HashSet<>();

		// get first record for unknown area
		Person result = restController.personForPostcode("xx", null);
		assertThat(result).isNotNull();
		String resultString = stringifyPerson(result);
		assertThat(checkList).doesNotContain(resultString);
		checkList.add(resultString);

		for (int i = 1; i < 5; i++) {
			result = restController.personForPostcode("20", AgeRange.ALL);
			assertThat(result).isNotNull();
			resultString = stringifyPerson(result);
			assertThat(checkList).doesNotContain(resultString);
			checkList.add(resultString);
		}
	}

	private String stringifyPerson(Person person) {
		String resultString = new StringBuilder().append(person.getFamilyName()).append(person.getGivenName())
				.append(person.getGender()).append(person.getHeight()).append(person.getBirthDate())
				.append(person.getEyecolor()).append(person.getEmail()).append(person.getAddress().hashCode())
				.toString();
		return resultString;
	}

	/**
	 * Consecutive calls should create different (random) results for PhoneNumber
	 */
	@Test
	void shouldReturnRandomPhoneNumber() {

		Set<String> checkList = new HashSet<>();
		for (int i = 1; i < 5; i++) {
			PhoneNumber result = restController.landline();
			assertThat(result).isNotNull();
			assertThat(checkList).doesNotContain(result.getPhoneNumber());
			checkList.add(result.getPhoneNumber());
		}

	}

	/**
	 * Consecutive calls should create different (random) results for PhoneNumber
	 */
	@Test
	void shouldReturnRandomPhoneNumberForCity() {

		Set<String> checkList = new HashSet<>();

		// get first record for unknown city
		PhoneNumber result = restController.landlineForCity("xxx");
		assertThat(result).isNotNull();
		assertThat(checkList).doesNotContain(result.getPhoneNumber());
		checkList.add(result.getPhoneNumber());

		for (int i = 1; i < 4; i++) {
			result = restController.landlineForCity("hamburg");
			assertThat(result).isNotNull();
			assertThat(checkList).doesNotContain(result.getPhoneNumber());
			checkList.add(result.getPhoneNumber());
		}

	}

	/**
	 * Consecutive calls should create different (random) results for PhoneNumber
	 */
	@Test
	void shouldReturnRandomMobileNumber() {

		Set<String> checkList = new HashSet<>();
		for (int i = 1; i < 5; i++) {
			PhoneNumber result = restController.mobile();
			assertThat(result).isNotNull();
			assertThat(checkList).doesNotContain(result.getPhoneNumber());
			checkList.add(result.getPhoneNumber());
		}

	}

	/**
	 * Consecutive calls should create different (random) results for BankAccount
	 */
	@Test
	void shouldReturnRandomBankAccount() {

		Set<String> checkList = new HashSet<>();
		for (int i = 1; i < 5; i++) {
			BankAccount result = restController.account();
			bankAssertions(checkList, result);
		}

	}

	/**
	 * Consecutive calls should create different (random) results for BankAccount
	 */
	@Test
	void shouldReturnRandomBankAccountForCity() {

		Set<String> checkList = new HashSet<>();

		// get first record for unknown city
		BankAccount result = restController.accountForCity("xxx");
		assertThat(result.getComment()).isNotNull();
		bankAssertions(checkList, result);

		for (int i = 1; i < 5; i++) {
			result = restController.accountForCity("hamburg");
			bankAssertions(checkList, result);
		}

	}

	private void bankAssertions(Set<String> checkList, BankAccount result) {
		assertThat(result).isNotNull();
		assertThat(result.getBank()).isNotNull();
		assertThat(result.getBank().getBic()).hasSize(11);
		assertThat(result.getBank().getDesc()).isNotNull();
		assertThat(checkList).doesNotContain(result.getIban());
		try {
			IbanUtil.validate(result.getIban());
		} catch (IbanFormatException | InvalidCheckDigitException | UnsupportedCountryException e) {
			fail(e);
		}
		checkList.add(result.getIban());
	}

	/**
	 * Consecutive calls should create different (random) results for CreditCard
	 */
	@Test
	void shouldReturnRandomCreditCard() {

		Set<String> checkList = new HashSet<>();
		for (int i = 1; i < 5; i++) {
			CreditCard result = restController.creditcard();
			assertThat(result).isNotNull();
			assertThat(checkList).doesNotContain(result.getNumber());
			checkList.add(result.getNumber());
		}

	}

	// test data for avatar api test
	private static Stream<String> avatarParams() {
		return Stream.of("male", "female", "xxx", "", null);
	}

	/**
	 * test different parameters and ensure it is a visible image check for male or
	 * female has to be executed by a human
	 */
	@ParameterizedTest
	@MethodSource("avatarParams")
	void shouldReturnRandomAvatarImage(String param) {
		ResponseEntity<byte[]> avatar = restController.avatar(param);
		ByteArrayInputStream bais = new ByteArrayInputStream(avatar.getBody());
		try {
			BufferedImage avatarImage = ImageIO.read(bais);
			assertThat(avatar).isNotNull();
			assertThat(avatarImage.getHeight()).isPositive();
			assertThat(avatarImage.getWidth()).isPositive();
		} catch (IOException e) {
			fail(e);
		}

	}

	/**
	 * Consecutive calls should create different (random) results for CreditCard
	 */
	@Test
	void shouldReturnOpenApi() {

		try {
			//
			URL url = new URL("http://localhost:" + port + "/v3/api-docs");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			int status = con.getResponseCode();
			if (HttpURLConnection.HTTP_OK != status) {
				fail("OpenAPI download failed with status " + status);
				con.disconnect();
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();

			JsonFactory factory = new JsonFactory();
			JsonParser parser = factory.createParser(content.toString());
			while (parser.nextToken() != null) {
			}
			parser.close();

			BufferedWriter writer = new BufferedWriter(new FileWriter("target/test-data-service-oas.json", Charset.forName("UTF-8")));
			writer.write(content.toString());
			writer.close();

		} catch (IOException e) {
			fail(e);
		}
	}

}
