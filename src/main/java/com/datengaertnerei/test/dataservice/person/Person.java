/*MIT License

Copyright (c) 2020 Jens Dibbern

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.datengaertnerei.test.dataservice.person;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Person entity class for persistence inspired by
 * <a href="https://schema.org/Person">person schema</a>.
 *
 * @author Jens Dibbern
 */
@JsonInclude(Include.NON_NULL)
@Schema(description = "person (see <a href=\"https://schema.org/Person\">https://schema.org/Person</a>)")
public class Person {

	private String givenName;
	private String familyName;
	private String birthName;
	private String gender;
	private LocalDate birthDate;
	private int height;
	private String eyecolor;
	private String email;
	private String taxId;
	private String profession;
	private String comment;

	private PostalAddress address;

	/**
	 * @return getter
	 */
	public String getGivenName() {
		return givenName;
	}

	/**
	 * @return getter
	 */
	public String getFamilyName() {
		return familyName;
	}

	/**
	 * @return getter
	 */
	public String getBirthName() {
		return birthName;
	}

	/**
	 * @param birthName setter
	 */
	public void setBirthName(String birthName) {
		this.birthName = birthName;
	}

	/**
	 * @return getter
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @return getter
	 */
	public LocalDate getBirthDate() {
		return birthDate;
	}

	/**
	 * @return getter
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return getter
	 */
	public String getEyecolor() {
		return eyecolor;
	}

	/**
	 * @return getter
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return getter
	 */
	public PostalAddress getAddress() {
		return address;
	}

	/**
	 * @param address setter
	 */
	public void setAddress(PostalAddress address) {
		this.address = address;
	}

	/**
	 * @return getter
	 */
	public String getTaxId() {
		return taxId;
	}

	/**
	 * @param taxId setter
	 */
	public void setTaxId(String taxId) {
		this.taxId = taxId;
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

	/**
	 * @return getter
	 */
	public int getWeight() {
		// we just assume a BMI of 25 and return the weight
		double meters = height / 100.0;
		return (int) (meters * meters * 25.0);
	}

	/**
	 * @return getter
	 */
	public String getProfession() {
		return profession;
	}

	/**
	 * @param profession setter
	 */
	public void setProfession(String profession) {
		this.profession = profession;
	}

	/**
	 * Ctor with values.
	 *
	 * @param givenName  given name attribute value
	 * @param familyName family name attribute value
	 * @param gender     gender attribute value
	 * @param birthDate  date of birth attribute value
	 * @param height     height attribute value
	 * @param eyecolor   eyecolor attribute value
	 * @param email      email address attribute value
	 * @param taxId      taxId attribute value
	 */
	public Person(String givenName, String familyName, String gender, LocalDate birthDate, int height, String eyecolor,
			String email, String taxId) {
		this.givenName = givenName;
		this.familyName = familyName;
		this.gender = gender;
		this.birthDate = birthDate;
		this.height = height;
		this.eyecolor = eyecolor;
		this.email = email;
		this.taxId = taxId;
	}
}
