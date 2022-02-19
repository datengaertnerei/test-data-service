package com.datengaertnerei.test.dataservice.security;

/**
 * API credentials for authentication
 *
 */
public class ApiCredentials {

	private String accountId;
	private String password;

	/**
	 * @return getter
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId setter
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return getter
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password setter
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
