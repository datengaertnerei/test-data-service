package com.datengaertnerei.test.dataservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@GetMapping(path = "/new")
	public ApiCredentials generateNew() {
		return userDetailsService.generateApiCredentials();
	}

}
