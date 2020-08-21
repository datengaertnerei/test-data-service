package com.datengaertnerei.test.dataservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.datengaertnerei.test.dataservice.bank.BankAccount;
import com.datengaertnerei.test.dataservice.bank.CreditCard;
import com.datengaertnerei.test.dataservice.person.Person;
import com.datengaertnerei.test.dataservice.phone.PhoneNumber;

@SpringBootTest
class TestDataServiceApiTests {

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

		Set<String> checkList = new HashSet<>();
		for (int i = 1; i < 10; i++) {
			Person result = restController.person();
			assertThat(result).isNotNull();
			String resultString = stringifyPerson(result);
			assertThat(checkList.contains(resultString)).isFalse();
			checkList.add(resultString);
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
		Person result = restController.personForCity("xxx");
		assertThat(result).isNotNull();
		String resultString = stringifyPerson(result);
		assertThat(checkList.contains(resultString)).isFalse();
		checkList.add(resultString);
		
		for (int i = 1; i < 5; i++) {
			result = restController.personForCity("hamburg");
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
		Person result = restController.personForPostcode("xx");
		assertThat(result).isNotNull();
		String resultString = stringifyPerson(result);
		assertThat(checkList.contains(resultString)).isFalse();
		checkList.add(resultString);
		
		for (int i = 1; i < 5; i++) {
			result = restController.personForPostcode("20");
			assertThat(result).isNotNull();
			resultString = stringifyPerson(result);
			assertThat(checkList.contains(resultString)).isFalse();
			checkList.add(resultString);
		}
	}

	private String stringifyPerson(Person person) {
		String resultString = new StringBuilder().append(person.getFamilyName()).append(person.getGivenName())
				.append(person.getGender()).append(person.getHeight()).append(person.getBirthDate())
				.append(person.getEyecolor()).append(person.getEmail()).append(person.getAddress().getAddressLocality())
				.append(person.getAddress().getPostalCode()).toString();
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
			assertThat(checkList.contains(result.getPhoneNumer())).isFalse();
			checkList.add(result.getPhoneNumer());
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
		assertThat(checkList.contains(result.getPhoneNumer())).isFalse();
		checkList.add(result.getPhoneNumer());
		
		for (int i = 1; i < 4; i++) {
			result = restController.landlineForCity("hamburg");
			assertThat(result).isNotNull();
			assertThat(checkList.contains(result.getPhoneNumer())).isFalse();
			checkList.add(result.getPhoneNumer());
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
			assertThat(checkList.contains(result.getPhoneNumer())).isFalse();
			checkList.add(result.getPhoneNumer());
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
			assertThat(result).isNotNull();
			assertThat(checkList.contains(result.getIban())).isFalse();
			checkList.add(result.getIban());
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
		assertThat(result).isNotNull();
		assertThat(checkList.contains(result.getIban())).isFalse();
		checkList.add(result.getIban());
		
		for (int i = 1; i < 5; i++) {
			result = restController.accountForCity("hamburg");
			assertThat(result).isNotNull();
			assertThat(checkList.contains(result.getIban())).isFalse();
			checkList.add(result.getIban());
		}

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

}
