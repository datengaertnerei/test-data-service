/*MIT License

Copyright (c) 2018 Jens Dibbern

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

/**
 * Person entity class for persistence inspired by <a href="https://schema.org/Person">person
 * schema</a>.
 *
 * @author Jens Dibbern
 */
public class Person {

  private String givenName;
  private String familyName;
  private String birthName;
  private String gender;
  private LocalDate birthDate;
  private int height;
  private String eyecolor;
  private String email;

  private PostalAddress address;

  public String getGivenName() {
    return givenName;
  }

  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  public String getBirthName() {
    return birthName;
  }

  public void setBirthName(String birthName) {
    this.birthName = birthName;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public String getEyecolor() {
    return eyecolor;
  }

  public void setEyecolor(String eyecolor) {
    this.eyecolor = eyecolor;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public PostalAddress getAddress() {
    return address;
  }

  public void setAddress(PostalAddress address) {
    this.address = address;
  }

  public Person() {}

  /**
   * Ctor with values.
   *
   * @param givenName given name attribute value
   * @param familyName family name attribute value
   * @param gender gender attribute value
   * @param birthDate date of birth attribute value
   * @param height height attribute value
   * @param eyecolor eyecolor attribute value
   * @param email email address attribute value
   */
  public Person(
      String givenName,
      String familyName,
      String gender,
      LocalDate birthDate,
      int height,
      String eyecolor,
      String email) {
    this.givenName = givenName;
    this.familyName = familyName;
    this.gender = gender;
    this.birthDate = birthDate;
    this.height = height;
    this.eyecolor = eyecolor;
    this.email = email;
  }
}
