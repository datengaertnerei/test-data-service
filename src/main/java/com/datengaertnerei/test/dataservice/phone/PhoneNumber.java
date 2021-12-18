package com.datengaertnerei.test.dataservice.phone;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(Include.NON_NULL)
@Schema(description = "phone number as a single string")
public class PhoneNumber {

	private String number;
	private String comment;

	public PhoneNumber(String number) {
		this.number = number;
	}

	public String getPhoneNumber() {
		return number;
	}

	public String getComment() {
		return comment;
	}

	public PhoneNumber setComment(String comment) {
		this.comment = comment;
		return this;
	}
}
