package com.datengaertnerei.test.dataservice;

import com.datengaertnerei.test.dataservice.bank.BankAccount;
import com.datengaertnerei.test.dataservice.bank.CreditCard;
import com.datengaertnerei.test.dataservice.person.Person;
import com.datengaertnerei.test.dataservice.phone.PhoneNumber;

public class Bundle {

	private Person person;
	private PhoneNumber mobile;
	private PhoneNumber landline;
	private CreditCard creditCard;
	private BankAccount bankAccount;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public PhoneNumber getMobile() {
		return mobile;
	}

	public void setMobile(PhoneNumber mobile) {
		this.mobile = mobile;
	}

	public PhoneNumber getLandline() {
		return landline;
	}

	public void setLandline(PhoneNumber landline) {
		this.landline = landline;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

}
