package com.datengaertnerei.test.dataservice.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

	@Autowired
	private IPersonGenerator gen;
	
	@RequestMapping("/person")
	public Person person() {
		return gen.createRandomPerson();
	}	
}
