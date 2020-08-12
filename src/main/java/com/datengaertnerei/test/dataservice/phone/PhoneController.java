package com.datengaertnerei.test.dataservice.phone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhoneController {
	
	@Autowired
	private IPhoneGenerator gen;
	
	@RequestMapping("/mobile")
	public String mobile() {
		return gen.generateMobileNumber();
	}

	@RequestMapping("/landline")
	public String landline() {
		return gen.generatePhoneNumber("unknown");
	}

	@RequestMapping("/landline/{city}")
	public String landlineForCity(@PathVariable("city") String city) {
		return gen.generatePhoneNumber(city);
	}
		
}
