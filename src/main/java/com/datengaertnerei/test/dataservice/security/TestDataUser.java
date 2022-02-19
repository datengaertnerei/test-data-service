package com.datengaertnerei.test.dataservice.security;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * User entity
 *
 */
@Entity
public class TestDataUser {

	private String name;
	private String passwordCrypt;
	private String role;

	/**
	 * @return getter
	 */
	public String getPassword() {
		return passwordCrypt;
	}

	/**
	 * @param passwordCrypt setter
	 */
	public void setPassword(String passwordCrypt) {
		this.passwordCrypt = passwordCrypt;
	}

	/**
	 * @return getter
	 */
	@Id
	public String getUsername() {
		return name;
	}

	/**
	 * @param name setter
	 */
	public void setUsername(String name) {
		this.name = name;
	}

	/**
	 * @return getter
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role setter
	 */
	public void setRole(String role) {
		this.role = role;
	}

}
