package com.datengaertnerei.test.dataservice.phone;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Phone number entity
 *
 */
@JsonInclude(Include.NON_NULL)
@Schema(description = "phone number as a single string")
public class PhoneNumber {

	private String number;
	private String comment;

	/**
	 * @param number setter
	 */
	public PhoneNumber(String number) {
		this.number = number;
	}

	/**
	 * @return getter
	 */
	public String getPhoneNumber() {
		return number;
	}

	/**
	 * @return getter
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment comment
	 * @return this
	 */
	public PhoneNumber setComment(String comment) {
		this.comment = comment;
		return this;
	}
}
