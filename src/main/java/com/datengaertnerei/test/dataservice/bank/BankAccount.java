package com.datengaertnerei.test.dataservice.bank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Bank account entity
 *
 */
@JsonInclude(Include.NON_NULL)
@Schema(description = "SEPA area bank account number and bank info")
public class BankAccount {

	private Bank bank;
	private String iban;
	private String comment;

	/**
	 * @return getter
	 */
	public Bank getBank() {
		return bank;
	}

	/**
	 * @param bank setter
	 */
	public void setBank(Bank bank) {
		this.bank = bank;
	}

	/**
	 * @return getter
	 */
	public String getIban() {
		return iban;
	}

	/**
	 * @param iban setter
	 */
	public void setIban(String iban) {
		this.iban = iban;
	}

	/**
	 * @return getter
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment setter
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
}
