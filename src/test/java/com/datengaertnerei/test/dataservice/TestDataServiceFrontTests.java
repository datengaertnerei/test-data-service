package com.datengaertnerei.test.dataservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

@SpringBootTest
class TestDataServiceFrontTests {
	
	@Autowired
	FrontEndController controller;
	
	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}
	
	@Test
	void shouldReturnRandomPersonModel(){
		Model model = new ExtendedModelMap();
		String view = controller.index(model);
		assertThat(model.getAttribute("givenName")).isNotNull();
		assertThat(view).isEqualTo("front");
	}

}
