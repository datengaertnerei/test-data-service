package com.datengaertnerei.test.dataservice.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String AUTH_REALM_NAME = "Test Data Service";
	private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

	@Value("${security.auth.enableBasicAuth}")
	private boolean enableBasicAuth;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if (enableBasicAuth) {
			log.info("enabling basic auth");
			http.authorizeRequests().antMatchers("/auth/*").hasRole(UserDetailsServiceImpl.ROLE_ADMIN).anyRequest()
					.authenticated().and().httpBasic().realmName(AUTH_REALM_NAME);
		} else {
			log.warn("basic auth disabled");
		}
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		log.info("setting up user auth details");
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());

	}

}
