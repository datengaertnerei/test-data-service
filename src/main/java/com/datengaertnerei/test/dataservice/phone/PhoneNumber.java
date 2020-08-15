package com.datengaertnerei.test.dataservice.phone;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PhoneNumber {

	private String phoneNumer;
	private String comment;

	public PhoneNumber(String phoneNumer) {
		this.phoneNumer = phoneNumer;
	}

	public String getPhoneNumer() {
		return phoneNumer;
	}

	public String getComment() {
		return comment;
	}

	public PhoneNumber setComment(String comment) {
		this.comment = comment;
		return this;
	}
}
