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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Postal address entity class for persistence inspired by
 * <a href="https://schema.org/PostalAddress">postal address schema</a>.
 *
 * @author Jens Dibbern
 */
@Entity
@Table(indexes = { @Index(columnList = "addressLocality"), @Index(columnList = "postalCode") })
@Schema(description = "postal address (see <a href=\"https://schema.org/PostalAddress\">https://schema.org/PostalAddress</a>)")
public class PostalAddress implements Comparable<PostalAddress> {

	private Long id;
	private String addressCountry;
	private String addressLocality;
	private String postalCode;
	private String streetAddress;
	private String houseNumber;

	/**
	 * @return getter
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	public Long getId() {
		return id;
	}

	/**
	 * @param id setter
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return getter
	 */
	public String getAddressCountry() {
		return addressCountry;
	}

	/**
	 * @param addressCountry setter
	 */
	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}

	/**
	 * @return getter
	 */
	public String getAddressLocality() {
		return addressLocality;
	}

	/**
	 * @param addressLocality setter
	 */
	public void setAddressLocality(String addressLocality) {
		this.addressLocality = addressLocality;
	}

	/**
	 * @return getter
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode setter
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return getter
	 */
	public String getStreetAddress() {
		return streetAddress;
	}

	/**
	 * @param streetAddress setter
	 */
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	/**
	 * @return getter
	 */
	public String getHouseNumber() {
		return houseNumber;
	}

	/**
	 * @param houseNumber setter
	 */
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	/**
	 * ctor
	 */
	public PostalAddress() {
	}

	/**
	 * Ctor with values.
	 *
	 * @param addressCountry  country attribute value
	 * @param addressLocality city attribute value
	 * @param postalCode      postal code attribute value
	 * @param streetAddress   street address attribute value
	 * @param houseNumber     house number attribute value (if applicable)
	 */
	public PostalAddress(String addressCountry, String addressLocality, String postalCode, String streetAddress,
			String houseNumber) {
		this.addressCountry = addressCountry;
		this.addressLocality = addressLocality;
		this.postalCode = postalCode;
		this.streetAddress = streetAddress;
		this.houseNumber = houseNumber;
	}

	@Override
	public int compareTo(PostalAddress target) {
		String match = this.toString();
		String other = target.toString();

		return match.compareTo(other);
	}

	@Override
	public boolean equals(Object target) {
		if (target instanceof PostalAddress) {
			return compareTo((PostalAddress) target) == 0;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder().append(this.getAddressCountry()).append(this.getAddressLocality())
				.append(this.getPostalCode()).append(this.getStreetAddress()).append(this.getHouseNumber());
		return result.toString();
	}
}
