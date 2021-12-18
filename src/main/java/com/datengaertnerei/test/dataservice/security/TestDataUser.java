package com.datengaertnerei.test.dataservice.security;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TestDataUser {

	private String name;
	private String passwordCrypt;
	private String role;

	public String getPassword() {
		return passwordCrypt;
	}

	public void setPassword(String passwordCrypt) {
		this.passwordCrypt = passwordCrypt;
	}

	@Id
	public String getUsername() {
		return name;
	}

	public void setUsername(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
