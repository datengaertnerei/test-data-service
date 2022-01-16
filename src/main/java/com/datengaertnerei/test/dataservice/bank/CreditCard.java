package com.datengaertnerei.test.dataservice.bank;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Credit card entity
 *
 */
@Schema(description = "credit card info with type, number and cvc")
public class CreditCard {

	private String number;
	private String type;
	private String cvc;
	private LocalDate expiry;

	/**
	 * ctor
	 * 
	 * @param number
	 * @param type
	 * @param cvc
	 * @param expiry
	 */
	public CreditCard(String number, String type, String cvc, LocalDate expiry) {
		this.number = number;
		this.type = type;
		this.cvc = cvc;
		this.setExpiry(expiry);
	}

	/**
	 * @return getter
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @return getter
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return getter
	 */
	public String getCvc() {
		return cvc;
	}

	/**
	 * @return getter
	 */
	public LocalDate getExpiry() {
		return expiry;
	}

	/**
	 * @param expiry setter
	 */
	public void setExpiry(LocalDate expiry) {
		this.expiry = expiry;
	}

}
