package com.datengaertnerei.test.dataservice.bank;

public class Bank {
	String bankCode;
	String desc;
	String bic;

	public String getBankCode() {
		return bankCode;
	}

	public String getDesc() {
		return desc;
	}

	public String getBic() {
		return bic;
	}

	public Bank(String bankCode, String desc, String bic) {
		this.bankCode = bankCode;
		this.desc = desc;
		this.bic = bic;
	}
}