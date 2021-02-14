package com.datengaertnerei.test.dataservice.bank;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="credit card info with type, number and cvc")
public class CreditCard {
	
	private String number;
	private String type;
	private String cvc;
	private LocalDate expiry;

	public CreditCard(String number, String type, String cvc) {
		this.number = number;
		this.type = type;
		this.cvc = cvc;
	}

	public String getNumber() {
		return number;
	}

	public String getType() {
		return type;
	}

	public String getCvc() {
		return cvc;
	}

}
