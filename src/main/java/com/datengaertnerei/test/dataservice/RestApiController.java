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

package com.datengaertnerei.test.dataservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.datengaertnerei.test.dataservice.bank.BankAccount;
import com.datengaertnerei.test.dataservice.bank.CreditCard;
import com.datengaertnerei.test.dataservice.bank.IBankGenerator;
import com.datengaertnerei.test.dataservice.person.IPersonGenerator;
import com.datengaertnerei.test.dataservice.person.Person;
import com.datengaertnerei.test.dataservice.phone.IPhoneGenerator;
import com.datengaertnerei.test.dataservice.phone.PhoneNumber;

@RestController
@RequestMapping(value = "/api/v1")
public class RestApiController {

	@Autowired
	private IPersonGenerator personGen;

	@Autowired
	private IBankGenerator bankGen;

	@Autowired
	private IPhoneGenerator phoneGen;
	
	@RequestMapping(value = "/mobile", method = RequestMethod.GET)
	public PhoneNumber mobile() {
		return phoneGen.generateMobileNumber();
	}

	@RequestMapping(value = "/landline", method = RequestMethod.GET)
	public PhoneNumber landline() {
		return phoneGen.generatePhoneNumber("unknown");
	}

	@RequestMapping(value = "/landline/{city}", method = RequestMethod.GET)
	public PhoneNumber landlineForCity(@PathVariable("city") String city) {
		return phoneGen.generatePhoneNumber(city);
	}	

	@RequestMapping(value = "/creditcard", method = RequestMethod.GET)
	public CreditCard creditcard() {
		return bankGen.generateCreditCard();
	}
	
	@RequestMapping(value = "/account", method = RequestMethod.GET)
	public BankAccount account() {
		return bankGen.generateAccount("unknown");
	}

	@RequestMapping(value = "/account/{city}", method = RequestMethod.GET)
	public BankAccount accountForCity(@PathVariable("city") String city) {
		return bankGen.generateAccount(city);
	}	
	
	@RequestMapping(value = "/person", method = RequestMethod.GET)
	public Person person() {
		return personGen.createRandomPerson();
	}
	
	@RequestMapping(value = "/person/city/{city}", method = RequestMethod.GET)
	public Person personForCity(@PathVariable("city") String city) {
		return personGen.createRandomPersonInCity(city);
	}	

	@RequestMapping(value = "/person/postalcode/{postalcode}", method = RequestMethod.GET)
	public Person personForPostcode(@PathVariable("postalcode") String postalCode) {
		return personGen.createRandomPersonInArea(postalCode);
	}	
	
}
