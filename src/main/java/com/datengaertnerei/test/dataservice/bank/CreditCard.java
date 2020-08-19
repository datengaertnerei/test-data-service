package com.datengaertnerei.test.dataservice.bank;

public class CreditCard {
	
	private String number;
	private String type;
	private String cvc;

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
