package com.datengaertnerei.test.dataservice.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot controller to handle user account management
 *
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@GetMapping(path = "/new")
	public ApiCredentials generateNew() {
		return userDetailsService.generateApiCredentials();
	}

	@GetMapping(path = "/export")
	public List<TestDataUser> exportUsers() {
		return userDetailsService.exportUsers();
	}

	@PutMapping(path = "/import")
	public String importUsers(List<TestDataUser> users) {
		return userDetailsService.importUsers(users);
	}

}
