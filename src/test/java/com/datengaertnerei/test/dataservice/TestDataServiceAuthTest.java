package com.datengaertnerei.test.dataservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.datengaertnerei.test.dataservice.security.ApiCredentials;
import com.datengaertnerei.test.dataservice.security.AuthController;

@SpringBootTest
class TestDataServiceAuthTest {

	
	@Autowired
	AuthController controller;
	
	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}
	
	@Test
	void shouldReturnRandomPersonModel(){
		ApiCredentials credentials = controller.generateNew();
		assertThat(credentials).isNotNull();
	}	
}
