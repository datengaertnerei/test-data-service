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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonGenerator implements IPersonGenerator {
	private static Log log = LogFactory.getLog(PersonGenerator.class);

	private static final String RES_EYECOLORS = "eyecolors.txt";
	private static final String RES_MALE = "male.txt";
	private static final String RES_FEMALE = "female.txt";
	private static final String RES_SURNAMES = "surnames.txt";
	private static final String FEMALE = "female";
	private static final String MALE = "male";
	private static final Object EMAIL_TEST = "@email.test";

	private Random random;
	private List<String> surnames;
	private List<String> femaleNames;
	private List<String> maleNames;
	private List<String> eyecolors;
	private Long count;
	private Long offset;

	@Autowired
	private PostalAddressRepository repository;

	@PostConstruct
	public void init() {
		try {
			surnames = loadValues(RES_SURNAMES);
			femaleNames = loadValues(RES_FEMALE);
			maleNames = loadValues(RES_MALE);
			eyecolors = loadValues(RES_EYECOLORS);
		} catch (IOException e) {
			log.error("Could not init value list:", e);
		}

		random = new Random(System.currentTimeMillis());
		Long minAddressId = repository.min();
		Long maxAddressId = repository.max();
	
		String importFile = System.getenv("OSM_IMPORT_FILE");
		if (null != importFile) {
			repository.deleteAll(); // does not reset JPA ID generator
			OsmPbfAddressImportUtil.importAddresses(importFile, repository);
			minAddressId = repository.min();
			maxAddressId = repository.max();
		}
		offset = minAddressId - 1L; // needed for random access by ID
		count = repository.count();

		// address record IDs should be without gap to avoid errors
		if(count < (maxAddressId - minAddressId)) {
			log.info("Postal address database contains gaps. Please reimport OSM data.");
		}
		log.info("Address count: " + count);
	}

	/**
	 * Creates a single random person object with linked address.
	 *
	 * @return the new person object
	 */
	@Override
	public Person createRandomPerson() {
		Person randomPerson = createBasicPerson();
		Optional<PostalAddress> address = repository.findById(randomId());

		randomPerson.setAddress(address.get());

		return randomPerson;
	}

	/**
	 * Creates a single random person object with linked address inside a given city.
	 *
	 * @param city the city to fetch random address from
	 *
	 * @return the new person object
	 */
	@Override
	public Person createRandomPersonInCity(String city) {
		Person randomPerson = createBasicPerson();
		List<PostalAddress> addresses = repository.findByAddressLocalityIgnoreCase(city);
		PostalAddress address = null;
		if (null != addresses && 0 < addresses.size()) {
			address = addresses.get(random.nextInt(addresses.size()));
		} else {
			Optional<PostalAddress> addressContainer = repository.findById(randomId());
			address = addressContainer.get();
			randomPerson.setComment("city not found in address base");
		}

		randomPerson.setAddress(address);

		return randomPerson;
	}

	/**
	 * Creates a single random person object with linked address inside a given postal code area.
	 *
	 * @param postalCode  the postal code marking the area to fetch random address from
	 *
	 * @return the new person object
	 */
	@Override
	public Person createRandomPersonInArea(String postalCode) {
		Person randomPerson = createBasicPerson();
		List<PostalAddress> addresses = repository.findByPostalCodeStartsWith(postalCode);
		PostalAddress address = null;
		if (null != addresses && 0 < addresses.size()) {
			address = addresses.get(random.nextInt(addresses.size()));
		} else {
			Optional<PostalAddress> addressContainer = repository.findById(randomId());
			address = addressContainer.get();
			randomPerson.setComment("postal code not found in address base");
		}

		randomPerson.setAddress(address);

		return randomPerson;
	}
	
	/**
	 * Creates a valid and (most probably) unique email address at a test domain.
	 * Since the top level domain .test is reserved, these email addresses will
	 * never be routed to any real world email server. You have to configure your
	 * test email server for this.
	 *
	 * @param firstname   the given name of the person
	 * @param surname     the family name of the person
	 * @param dateOfBirth the date of birth of the person (to avoid duplicates)
	 * @return the new email address as string
	 */
	private String createEmailAddress(String firstname, String surname, LocalDate dateOfBirth) {
		StringBuilder email = new StringBuilder().append(firstname).append(surname).append(dateOfBirth.getYear())
				.append(EMAIL_TEST);

		return Normalizer.normalize(email.toString(), Form.NFKC).replaceAll("[^\\p{ASCII}]", "");
	}

	/**
	 * Creates a reasonable human height.
	 *
	 * @param gender male or female (add diverse if you like)
	 * @return the new height as int
	 */
	private int createRandomHeight(String gender) {
		double height;
		do {
			height = random.nextGaussian() * 10.0 + 165.0;
		} while (height < 150.0 || height > 190.0);
		if (MALE.equals(gender)) {
			height += 10.0;
		}
		return (int) Math.round(height);
	}

	/**
	 * Creates a valid and reasonable human date of birth.
	 *
	 * @return the date of birth
	 */
	private LocalDate createRandomDateOfBirth() {
		double age;
		do {
			age = random.nextGaussian() * 22.0 + 45.0;
		} while (age < 1.0 || age > 100.0);

		int month = random.nextInt(11) + 1;
		int day = 0;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = random.nextInt(30) + 1;
			break;
		case 2:
			day = random.nextInt(27) + 1;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			day = random.nextInt(29) + 1;
			break;
		default: // there is no other month
			break;
		}

		return LocalDate.of(LocalDate.now().getYear() - (int) Math.round(age), month, day);
	}

	/**
	 * @return
	 */
	private Long randomId() {
		Long result = 1L;
		
		// it is unlikely to have more than 2 billion addresses
		if(count < Integer.MAX_VALUE) {			
			result = Integer.valueOf(random.nextInt(count.intValue())).longValue();
		}else {
			// but one of 2 billion will suffice for our test data
			result = Integer.valueOf(random.nextInt(Integer.MAX_VALUE)).longValue();
		}
		
		// add offset for first ID
		return result + offset;
	}

	/**
	 * @return
	 */
	private Person createBasicPerson() {
		String gender = random.nextBoolean() ? MALE : FEMALE; // (add diverse if you like)
		String firstname;
		if (MALE.equals(gender)) {
			firstname = maleNames.get(random.nextInt(maleNames.size())).trim();
		} else {
			firstname = femaleNames.get(random.nextInt(femaleNames.size())).trim();
		}
		String surname = surnames.get(random.nextInt(surnames.size())).trim();
		
		String eyecolor = eyecolors.get(random.nextInt(eyecolors.size())).trim();

		LocalDate dateOfBirth = createRandomDateOfBirth();
		String emailAddress = createEmailAddress(firstname, surname, dateOfBirth);
		int height = createRandomHeight(gender);

		Person randomPerson = new Person(firstname, surname, gender, dateOfBirth, height, eyecolor, emailAddress);
		if(1 == random.nextInt(5)) {
			String birthName = surnames.get(random.nextInt(surnames.size())).trim();
			randomPerson.setBirthName(birthName);
		}
		return randomPerson;
	}
	
	/**
	 * Loads value list with classloader for human-readable random person
	 * attributes.
	 *
	 * @param fileName name of the files
	 * @return list of values
	 * @throws IOException values could not be loaded
	 */
	private List<String> loadValues(String fileName) throws IOException {
		InputStream input = getClass().getResourceAsStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
		ArrayList<String> values = new ArrayList<>();
		String line;
		while ((line = reader.readLine()) != null) {
			values.add(line);
		}
		reader.close();

		return values;
	}
}
