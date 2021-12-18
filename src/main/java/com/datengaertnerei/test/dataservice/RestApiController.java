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

import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.Max;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datengaertnerei.test.dataservice.avatar.IAvatarGenerator;
import com.datengaertnerei.test.dataservice.bank.BankAccount;
import com.datengaertnerei.test.dataservice.bank.CreditCard;
import com.datengaertnerei.test.dataservice.bank.IBankGenerator;
import com.datengaertnerei.test.dataservice.person.AgeRange;
import com.datengaertnerei.test.dataservice.person.IPersonGenerator;
import com.datengaertnerei.test.dataservice.person.Person;
import com.datengaertnerei.test.dataservice.phone.IPhoneGenerator;
import com.datengaertnerei.test.dataservice.phone.PhoneNumber;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestApiController {

	private static final String MALE_PARAM_VALUE = "male";

	@Autowired
	private IPersonGenerator personGen;

	@Autowired
	private IBankGenerator bankGen;

	@Autowired
	private IPhoneGenerator phoneGen;

	@Autowired
	private IAvatarGenerator avatarGen;

	@GetMapping(value = "/bundles")
	@Operation(summary = "random adult persons inc. phone and payment data")
	@ApiResponse(responseCode = "200", description = "OK")
	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
	public List<Bundle> bundles(@RequestParam(required = false, defaultValue = "1") @Max(50) int size) {
		List<Bundle> result = new LinkedList<>();

		for (int i = 0; i < size; i++) {
			Bundle entry = new Bundle();
			entry.setPerson(personGen.createRandomPerson(AgeRange.ADULT));
			entry.setLandline(phoneGen.generatePhoneNumber(entry.getPerson().getAddress().getAddressLocality()));
			entry.setMobile(phoneGen.generateMobileNumber());
			entry.setBankAccount(bankGen.generateAccount(entry.getPerson().getAddress().getAddressLocality()));
			entry.setCreditCard(bankGen.generateCreditCard());
			result.add(entry);
		}

		return result;
	}

	@GetMapping(value = "/mobile")
	@Operation(summary = "random german mobile number")
	@ApiResponse(responseCode = "200", description = "OK")
	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
	public PhoneNumber mobile() {
		return phoneGen.generateMobileNumber();
	}

	@GetMapping(value = "/landline")
	@Operation(summary = "random german landline number with generic area code")
	@ApiResponse(responseCode = "200", description = "OK")
	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
	public PhoneNumber landline() {
		return phoneGen.generatePhoneNumber("unknown");
	}

	@GetMapping(value = "/landline/{city}")
	@Operation(summary = "random german landline number with area code for the given city (fallback to generic area code if city not found)")
	@ApiResponse(responseCode = "200", description = "OK")
	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
	public PhoneNumber landlineForCity(
			@Parameter(description = "the city to look for an area code", required = true) @PathVariable("city") String city) {
		return phoneGen.generatePhoneNumber(city);
	}

	@GetMapping(value = "/creditcard")
	@Operation(summary = "random credit card (major types, german issuers)")
	@ApiResponse(responseCode = "200", description = "OK")
	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
	public CreditCard creditcard() {
		return bankGen.generateCreditCard();
	}

	@GetMapping(value = "/account")
	@Operation(summary = "random german bank account (uses Frankfurt am Main as default location)")
	@ApiResponse(responseCode = "200", description = "OK")
	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
	public BankAccount account() {
		return bankGen.generateAccount("unknown");
	}

	@GetMapping(value = "/account/{city}")
	@Operation(summary = "random german bank account for given city (fallback to Frankfurt am Main if no bank found for given city)")
	@ApiResponse(responseCode = "200", description = "OK")
	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
	public BankAccount accountForCity(
			@Parameter(description = "the city to look for a random bank", required = true) @PathVariable("city") String city) {
		return bankGen.generateAccount(city);
	}

	@GetMapping(value = "/person")
	@Operation(summary = "random person with a valid german postal address")
	@ApiResponse(responseCode = "200", description = "OK")
	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
	public Person person(@RequestParam(required = false) AgeRange age) {
		if (null == age) {
			return personGen.createRandomPerson(AgeRange.ALL);
		} else {
			return personGen.createRandomPerson(age);
		}
	}

	@GetMapping(value = "/person/city/{city}")
	@Operation(summary = "random person with a valid german postal address within the given city")
	@ApiResponse(responseCode = "200", description = "OK")
	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
	public Person personForCity(
			@Parameter(description = "the city to look for random postal address", required = true) @PathVariable("city") String city,
			@RequestParam(required = false) AgeRange age) {
		if (null == age) {
			return personGen.createRandomPersonInCity(city, AgeRange.ALL);
		} else {
			return personGen.createRandomPersonInCity(city, age);
		}
	}

	@GetMapping(value = "/person/postalcode/{postalcode}")
	@Operation(summary = "random person with a valid german postal address within the given postal code (zip) area (1-5 digits)")
	@ApiResponse(responseCode = "200", description = "OK")
	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
	public Person personForPostcode(
			@Parameter(description = "the postal code to look for random postal address", required = true) @PathVariable("postalcode") String postalCode,
			@RequestParam(required = false) AgeRange age) {
		if (null == age) {
			return personGen.createRandomPersonInArea(postalCode, AgeRange.ALL);
		} else {
			return personGen.createRandomPersonInArea(postalCode, age);
		}
	}

	@GetMapping(value = "/avatar", produces = MediaType.IMAGE_PNG_VALUE)
	@Operation(summary = "random female or male avatar PNG image")
	@ApiResponse(responseCode = "200", description = "OK")
	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
	public ResponseEntity<byte[]> avatar(
			@Parameter(description = "male or female, female is default", required = false) @RequestParam(defaultValue = "female", required = false) String gender) {
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG)
				.body(avatarGen.getAvatar(!MALE_PARAM_VALUE.equalsIgnoreCase(gender)));
	}
}
