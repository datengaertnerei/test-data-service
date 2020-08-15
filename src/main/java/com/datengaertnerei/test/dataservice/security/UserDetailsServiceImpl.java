package com.datengaertnerei.test.dataservice.security;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	private static final GrantedAuthority[] role = { new SimpleGrantedAuthority("user") };

	private Random rnd;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails userDetails = null;
		Optional<TestDataUser> u = userRepository.findById(username);

		if (u.isPresent()) {
			TestDataUser appUser = u.get();
			userDetails = (UserDetails) new User(appUser.getUsername(), //
					appUser.getPassword(), Arrays.asList(role));
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
		newAccount.setUsername("admin");
		newAccount.setPassword(bCryptPasswordEncoder.encode(result.getPassword()));
		userRepository.saveAndFlush(newAccount);

		return result;
	}

	@PostConstruct
	private void setup() {
		log.info("setting up user details service");
		rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());
		bCryptPasswordEncoder = new BCryptPasswordEncoder();

		UserDetails admin = loadUserByUsername("admin");
		if (null == admin) {
			String pwd = UUID.randomUUID().toString();
			log.info("creating default admin account\r\n\r\nusing generated password for admin account: {}\r\n", pwd);
			TestDataUser newAdmin = new TestDataUser();
			newAdmin.setUsername("admin");
			newAdmin.setPassword(bCryptPasswordEncoder.encode(pwd));
			userRepository.saveAndFlush(newAdmin);
		}
	}
}
