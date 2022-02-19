package com.datengaertnerei.test.dataservice.junit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import com.datengaertnerei.test.dataservice.security.ApiCredentials;
import com.datengaertnerei.test.dataservice.security.AuthController;
import com.datengaertnerei.test.dataservice.security.TestDataUser;
import com.datengaertnerei.test.dataservice.security.UserDetailsServiceImpl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestDataServiceAuthTests {

	@Autowired
	private AuthController controller;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;	
	
	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

	@Test
	void shouldReturnNewCredentials() {
		ApiCredentials credentials = controller.generateNew();
		assertThat(credentials).isNotNull();
		UserDetails details = userDetailsService.loadUserByUsername(credentials.getAccountId());
		assertThat(details).isNotNull();
	}

	@Test
	void shouldExportAndImportUserList() {
		List<TestDataUser> users = controller.exportUsers();
		assertThat(users).isNotNull();
		assertThat(controller.importUsers(users)).isEqualTo(UserDetailsServiceImpl.RESULT_OK);
	}
	
}
