package com.datengaertnerei.test.dataservice.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IbanController {
	
	@Autowired
	private IBankGenerator gen;
		
	@RequestMapping("/account")
	public String account() {
		return gen.generateAccount("unknown");
	}

	@RequestMapping("/account/{city}")
	public String accountForCity(@PathVariable("city") String city) {
		return gen.generateAccount(city);
	}	
}
