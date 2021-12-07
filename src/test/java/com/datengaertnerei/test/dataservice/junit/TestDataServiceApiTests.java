package com.datengaertnerei.test.dataservice.junit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.datengaertnerei.test.dataservice.RestApiController;
import com.datengaertnerei.test.dataservice.bank.BankAccount;
import com.datengaertnerei.test.dataservice.bank.CreditCard;
import com.datengaertnerei.test.dataservice.person.AgeRange;
import com.datengaertnerei.test.dataservice.person.Person;
import com.datengaertnerei.test.dataservice.person.PostalAddress;
import com.datengaertnerei.test.dataservice.phone.PhoneNumber;

@SpringBootTest
@Tag("unit")
class TestDataServiceApiTests {
	private static final double THRESHOLD_SENIOR = 65.0;
	private static final double THRESHOLD_ADULT = 18.0;
	@Autowired
	private RestApiController restController;

	@Test
	void contextLoads() {
		assertThat(restController).isNotNull();
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
		String personString = stringifyPerson(person);
		assertThat(addresses.contains(person.getAddress())).isFalse();
		assertThat(persons.contains(personString)).isFalse();
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
		Person result = restController.personForCity("xxx", AgeRange.ALL);
		assertThat(result).isNotNull();
		String resultString = stringifyPerson(result);
		assertThat(checkList.contains(resultString)).isFalse();
		checkList.add(resultString);

		for (int i = 1; i < 5; i++) {
			result = restController.personForCity("hamburg", AgeRange.ALL);
			assertThat(result).isNotNull();
			resultString = stringifyPerson(result);
			assertThat(checkList.contains(resultString)).isFalse();
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
		Person result = restController.personForPostcode("xx", AgeRange.ALL);
		assertThat(result).isNotNull();
		String resultString = stringifyPerson(result);
		assertThat(checkList.contains(resultString)).isFalse();
		checkList.add(resultString);

		for (int i = 1; i < 5; i++) {
			result = restController.personForPostcode("20", AgeRange.ALL);
			assertThat(result).isNotNull();
			resultString = stringifyPerson(result);
			assertThat(checkList.contains(resultString)).isFalse();
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
			assertThat(checkList.contains(result.getPhoneNumber())).isFalse();
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
		assertThat(checkList.contains(result.getPhoneNumber())).isFalse();
		checkList.add(result.getPhoneNumber());

		for (int i = 1; i < 4; i++) {
			result = restController.landlineForCity("hamburg");
			assertThat(result).isNotNull();
			assertThat(checkList.contains(result.getPhoneNumber())).isFalse();
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
			assertThat(checkList.contains(result.getPhoneNumber())).isFalse();
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
		assertThat(checkList.contains(result.getIban())).isFalse();
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
			assertThat(checkList.contains(result.getNumber())).isFalse();
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
}
