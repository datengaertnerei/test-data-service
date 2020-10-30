package com.datengaertnerei.test.dataservice.security;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private static final String NAME_ADMIN = "admin";
	public static final String ROLE_ADMIN = "admin";
	public static final String ROLE_USER = "user";

	private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		UserDetails userDetails = null;
		Optional<TestDataUser> u = userRepository.findById(username);

		if (u.isPresent()) {
			TestDataUser appUser = u.get();
			userDetails = User.withUsername(appUser.getUsername()).password(appUser.getPassword())
					.roles(appUser.getRole()).build();
		}

		return userDetails;
	}

	protected ApiCredentials generateApiCredentials() {
		ApiCredentials result = new ApiCredentials();

		// generate new account id that is not in use
		String newAccountId = RandomStringUtils.randomAlphabetic(10);
		UserDetails account = loadUserByUsername(newAccountId);
		while (null != account) {
			newAccountId = RandomStringUtils.randomAlphabetic(10);
			account = loadUserByUsername(newAccountId);
		}

		// fill credentials
		result.setAccountId(newAccountId);
		result.setPassword(UUID.randomUUID().toString());

		// and save to authorization database
		TestDataUser newAccount = new TestDataUser();
		newAccount.setUsername(newAccountId);
		newAccount.setPassword(bCryptPasswordEncoder.encode(result.getPassword()));
		newAccount.setRole(ROLE_USER);
		userRepository.saveAndFlush(newAccount);

		return result;
	}
	
	protected List<TestDataUser> exportUsers() {
		return userRepository.findAll();
	}
	
	protected String importUsers(List<TestDataUser> users) {
		try {
			userRepository.deleteAll();
			userRepository.saveAll(users);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "OK";
	}	

	@PostConstruct
	private void setup() {
		log.info("setting up user details service");
		bCryptPasswordEncoder = new BCryptPasswordEncoder();

		// allow admin password sideloading, esp. for container environments
		String adminPasswd = System.getenv("TD_ADMIN_PASSWD");
		UserDetails admin = loadUserByUsername(NAME_ADMIN);
		if (null == admin) {
			if (null == adminPasswd || adminPasswd.isEmpty()) {
				adminPasswd = UUID.randomUUID().toString();
				log.info("creating default admin account\r\n\r\nusing generated password for admin account: {}\r\n",
						adminPasswd);

			}
			TestDataUser newAdmin = new TestDataUser();
			newAdmin.setUsername(NAME_ADMIN);
			newAdmin.setPassword(bCryptPasswordEncoder.encode(adminPasswd));
			newAdmin.setRole(ROLE_ADMIN);
			userRepository.saveAndFlush(newAdmin);
		} else if (null != adminPasswd && !adminPasswd.isEmpty()) {
			Optional<TestDataUser> u = userRepository.findById(NAME_ADMIN);

			if (u.isPresent()) {
				TestDataUser adminUser = u.get();
				adminUser.setPassword(bCryptPasswordEncoder.encode(adminPasswd));
				userRepository.saveAndFlush(adminUser);
			}
		}
	}
}
