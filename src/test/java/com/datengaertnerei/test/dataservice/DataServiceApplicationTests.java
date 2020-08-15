package com.datengaertnerei.test.dataservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.datengaertnerei.test.dataservice.bank.BankAccount;
import com.datengaertnerei.test.dataservice.person.Person;
import com.datengaertnerei.test.dataservice.phone.PhoneNumber;

@SpringBootTest
class DataServiceApplicationTests {

	@Autowired
	private RestApiController restController;

	@Test
	void contextLoads() {
		assertThat(restController).isNotNull();
	}

	/**
	 * Consecutive calls should create different (random) results for Person/PostalAddress
	 */
	@Test
	void shouldReturnRandomPerson() {

		Set<String> checkList = new HashSet<>();
		for (int i = 1; i < 10; i++) {
			Person result = restController.person();
			assertThat(result).isNotNull();
			String resultString = new StringBuilder().append(result.getFamilyName()).append(result.getGivenName())
					.append(result.getEmail()).append(result.getAddress().getAddressLocality())
					.append(result.getAddress().getPostalCode()).toString();
			assertThat(checkList.contains(resultString)).isFalse();
			checkList.add(resultString);
		}
	}
	
	/**
	 * Consecutive calls should create different (random) results for PhoneNumber
	 */
	@Test
	void shouldReturnRandomPhoneNumber() {

		Set<String> checkList = new HashSet<>();
		for (int i = 1; i < 10; i++) {
			PhoneNumber result = restController.landline();
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
		for (int i = 1; i < 10; i++) {
			BankAccount result = restController.account();
			assertThat(result).isNotNull();
			assertThat(checkList.contains(result.getIban())).isFalse();
			checkList.add(result.getIban());
		}
		
	}
	
}
