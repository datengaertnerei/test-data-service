package com.datengaertnerei.test.dataservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.datengaertnerei.test.dataservice.person.IPersonGenerator;
import com.datengaertnerei.test.dataservice.person.Person;
import com.datengaertnerei.test.dataservice.phone.IPhoneGenerator;

@Controller
public class FrontController {

	@Autowired
	IPersonGenerator persong;
	
	@Autowired
	IPhoneGenerator phoneg;	
	
	@RequestMapping("/")
	public String index(Model model) {
		Person p = persong.createRandomPerson();
		model.addAttribute("givenName", p.getGivenName());
		model.addAttribute("familyName", p.getFamilyName());
		model.addAttribute("gender", p.getGender());
		model.addAttribute("birthDate", p.getBirthDate());
		model.addAttribute("email", p.getEmail());
		model.addAttribute("addressLocality", p.getAddress().getAddressLocality());
		model.addAttribute("postalCode", p.getAddress().getPostalCode());
		model.addAttribute("streetAddress", p.getAddress().getStreetAddress());
		model.addAttribute("houseNumber", p.getAddress().getHouseNumber());

		model.addAttribute("landline", phoneg.generatePhoneNumber(p.getAddress().getAddressLocality()));
		model.addAttribute("mobile", phoneg.generateMobileNumber());

		return "front";
	}

}