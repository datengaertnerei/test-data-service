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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Postal address entity class for persistence inspired by <a
 * href="https://schema.org/PostalAddress">postal address schema</a>.
 *
 * @author Jens Dibbern
 */
@Entity
public class PostalAddress implements Comparable<PostalAddress> {

  private int id;
  private String addressCountry;
  private String addressLocality;
  private String postalCode;
  private String streetAddress;
  private String houseNumber;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getAddressCountry() {
    return addressCountry;
  }

  public void setAddressCountry(String addressCountry) {
    this.addressCountry = addressCountry;
  }

  public String getAddressLocality() {
    return addressLocality;
  }

  public void setAddressLocality(String addressLocality) {
    this.addressLocality = addressLocality;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getStreetAddress() {
    return streetAddress;
  }

  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
  }

  public String getHouseNumber() {
    return houseNumber;
  }

  public void setHouseNumber(String houseNumber) {
    this.houseNumber = houseNumber;
  }

  public PostalAddress() {}

  /**
   * Ctor with values.
   *
   * @param addressCountry country attribute value
   * @param addressLocality city attribute value
   * @param postalCode postal code attribute value
   * @param streetAddress street address attribute value
   * @param houseNumber house number attribute value (if applicable)
   */
  public PostalAddress(
      String addressCountry,
      String addressLocality,
      String postalCode,
      String streetAddress,
      String houseNumber) {
    this.addressCountry = addressCountry;
    this.addressLocality = addressLocality;
    this.postalCode = postalCode;
    this.streetAddress = streetAddress;
    this.houseNumber = houseNumber;
  }

  @Override
  public int compareTo(PostalAddress target) {
    StringBuilder match =
        new StringBuilder()
            .append(this.getAddressCountry())
            .append(this.getAddressLocality())
            .append(this.getPostalCode())
            .append(this.getStreetAddress())
            .append(this.getHouseNumber());
    StringBuilder other =
        new StringBuilder()
            .append(target.getAddressCountry())
            .append(target.getAddressLocality())
            .append(target.getPostalCode())
            .append(target.getStreetAddress())
            .append(target.getHouseNumber());

    return match.toString().compareTo(other.toString());
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
    StringBuilder content =
        new StringBuilder()
            .append(this.getAddressCountry())
            .append(this.getAddressLocality())
            .append(this.getPostalCode())
            .append(this.getStreetAddress())
            .append(this.getHouseNumber());
    return content.hashCode();
  }
}
