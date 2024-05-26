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
	 * @param number number
	 * @param type type
	 * @param cvc cvc
	 * @param expiry expiry
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
	public final String getNumber() {
		return number;
	}

	/**
	 * @return getter
	 */
	public final String getType() {
		return type;
	}

	/**
	 * @return getter
	 */
	public final String getCvc() {
		return cvc;
	}

	/**
	 * @return getter
	 */
	public final LocalDate getExpiry() {
		return expiry;
	}

	/**
	 * @param expiry setter
	 */
	public final void setExpiry(LocalDate expiry) {
		this.expiry = expiry;
	}

}
