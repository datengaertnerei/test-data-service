package com.datengaertnerei.test.dataservice;

import com.datengaertnerei.test.dataservice.bank.BankAccount;
import com.datengaertnerei.test.dataservice.bank.CreditCard;
import com.datengaertnerei.test.dataservice.person.Person;
import com.datengaertnerei.test.dataservice.phone.PhoneNumber;

/**
 * Container class to combine all elements of a fictitious person
 *
 */
public class Bundle {

	private Person person;
	private PhoneNumber mobile;
	private PhoneNumber landline;
	private CreditCard creditCard;
	private BankAccount bankAccount;

	/**
	 * @return
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person setter
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * @return getter
	 */
	public PhoneNumber getMobile() {
		return mobile;
	}

	/**
	 * @param mobile setter
	 */
	public void setMobile(PhoneNumber mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return getter
	 */
	public PhoneNumber getLandline() {
		return landline;
	}

	/**
	 * @param landline setter
	 */
	public void setLandline(PhoneNumber landline) {
		this.landline = landline;
	}

	/**
	 * @return getter
	 */
	public CreditCard getCreditCard() {
		return creditCard;
	}

	/**
	 * @param creditCard setter
	 */
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	/**
	 * @return getter
	 */
	public BankAccount getBankAccount() {
		return bankAccount;
	}

	/**
	 * @param bankAccount setter
	 */
	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

}
