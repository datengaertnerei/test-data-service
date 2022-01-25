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

	/**
	 * @return new API credentials
	 */
	@GetMapping(path = "/new")
	public ApiCredentials generateNew() {
		return userDetailsService.generateApiCredentials();
	}

	/**
	 * @return list of all users with stored hashes
	 */
	@GetMapping(path = "/export")
	public List<TestDataUser> exportUsers() {
		return userDetailsService.exportUsers();
	}

	/**
	 * @param users list of user objects to import
	 * @return OK if successful
	 */
	@PutMapping(path = "/import")
	public String importUsers(List<TestDataUser> users) {
		return userDetailsService.importUsers(users);
	}

}
