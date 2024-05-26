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

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.datengaertnerei.test.dataservice.bank.CreditCard;
import com.datengaertnerei.test.dataservice.bank.IBankGenerator;
import com.datengaertnerei.test.dataservice.person.AgeRange;
import com.datengaertnerei.test.dataservice.person.IPersonGenerator;
import com.datengaertnerei.test.dataservice.person.Person;
import com.datengaertnerei.test.dataservice.phone.IPhoneGenerator;

@Controller
public class FrontEndController {

	@Autowired
	private IPersonGenerator persong;

	@Autowired
	private IPhoneGenerator phoneg;

	@Autowired
	private IBankGenerator bankg;

	@GetMapping(path = "/")
	public String index(Model model) {
		Person p = persong.createRandomPerson(AgeRange.ADULT);
		model.addAttribute("givenName", p.getGivenName());
		model.addAttribute("familyName", p.getFamilyName());
		model.addAttribute("gender", p.getGender());
		model.addAttribute("birthDate", p.getBirthDate()
				.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMAN)));
		model.addAttribute("email", p.getEmail());
		model.addAttribute("addressLocality", p.getAddress().getAddressLocality());
		model.addAttribute("postalCode", p.getAddress().getPostalCode());
		model.addAttribute("streetAddress", p.getAddress().getStreetAddress());
		model.addAttribute("houseNumber", p.getAddress().getHouseNumber());
		model.addAttribute("taxId", p.getTaxId());

		model.addAttribute("landline", phoneg.generatePhoneNumber(p.getAddress().getAddressLocality()).getPhoneNumber());
		model.addAttribute("mobile", phoneg.generateMobileNumber().getPhoneNumber());

		CreditCard cc = bankg.generateCreditCard();
		model.addAttribute("cctype", cc.getType());
		model.addAttribute("ccno", cc.getNumber());
		model.addAttribute("iban", bankg.generateAccount(p.getAddress().getAddressLocality()).getIban());

		return "front";
	}

}