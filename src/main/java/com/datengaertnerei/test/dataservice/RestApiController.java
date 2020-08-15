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
import org.springframework.web.bind.annotation.RestController;

import com.datengaertnerei.test.dataservice.bank.IBankGenerator;
import com.datengaertnerei.test.dataservice.person.IPersonGenerator;
import com.datengaertnerei.test.dataservice.person.Person;
import com.datengaertnerei.test.dataservice.phone.IPhoneGenerator;

@RestController
@RequestMapping("/api/v1")
public class RestApiController {

	@Autowired
	private IPersonGenerator personGen;

	@Autowired
	private IBankGenerator bankGen;

	@Autowired
	private IPhoneGenerator phoneGen;
	
	@RequestMapping("/mobile")
	public String mobile() {
		return phoneGen.generateMobileNumber();
	}

	@RequestMapping("/landline")
	public String landline() {
		return phoneGen.generatePhoneNumber("unknown");
	}

	@RequestMapping("/landline/{city}")
	public String landlineForCity(@PathVariable("city") String city) {
		return phoneGen.generatePhoneNumber(city);
	}	
	@RequestMapping("/account")
	public String account() {
		return bankGen.generateAccount("unknown");
	}

	@RequestMapping("/account/{city}")
	public String accountForCity(@PathVariable("city") String city) {
		return bankGen.generateAccount(city);
	}	
	
	@RequestMapping("/person")
	public Person person() {
		return personGen.createRandomPerson();
	}	
}
