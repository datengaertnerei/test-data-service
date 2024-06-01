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
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

/**
 * Generates random persons
 *
 */
@Service
public class PersonGenerator implements IPersonGenerator {

	private static Log log = LogFactory.getLog(PersonGenerator.class);

	private static final String RES_EYECOLORS = "eyecolors.txt";
	private static final String RES_MALE = "male.txt";
	private static final String RES_FEMALE = "female.txt";
	private static final String RES_SURNAMES = "surnames.txt";
	private static final String RES_PROFESSIONS = "profession.txt";
	private static final String FEMALE = "female";
	private static final String NON_BINARY = "non-binary";
	private static final String MALE = "male";
	private static final Object EMAIL_TEST = "@email.test";

	private Random random;
	private List<String> surnames;
	private List<String> femaleNames;
	private List<String> maleNames;
	private List<String> eyecolors;
	private List<String> professions;
	private TaxIdGenerator taxIdGenerator;
	
	@Value("${application.defaultfile}")
	private String defaultFile;

	@Autowired
	private PostalAddressRepository repository;

	/**
	 * 
	 */
	@PostConstruct
	public void init() {
		try {
			surnames = loadValues(RES_SURNAMES);
			femaleNames = loadValues(RES_FEMALE);
			maleNames = loadValues(RES_MALE);
			eyecolors = loadValues(RES_EYECOLORS);
			professions = loadValues(RES_PROFESSIONS);
		} catch (IOException e) {
			log.error("Could not init value list:", e);
		}

		random = new Random(System.currentTimeMillis());
		Long minAddressId = null == repository.min() ? 0L : repository.min();
		Long maxAddressId = null == repository.max() ? 0L : repository.max();

		String importFile = System.getenv("OSM_IMPORT_FILE");
		if(0L == maxAddressId && null == importFile) {
			importFile = defaultFile;
		}
		
		try {
			if (null != importFile && importFile.length() != 0) {
				log.info("Importing OSM dump");
				repository.deleteAll(); // does not reset JPA ID generator
				OsmPbfAddressImportUtil.importAddresses(importFile, repository);
				minAddressId = repository.min();
				maxAddressId = repository.max();
			}
		} catch (MalformedURLException e) {
			log.error("Could not import OSM data:", e);
		}

		// address record IDs should be without gap to avoid errors
		if (getCount() < (maxAddressId - minAddressId)) {
			log.info("Postal address database contains gaps. Please reimport OSM data.");
		}
		log.info("Address count: " + getCount());

		taxIdGenerator = new TaxIdGenerator(random);
	}

	private Long getCount() {
		return repository.count() < Integer.MAX_VALUE ? repository.count() : Integer.MAX_VALUE;
	}

	/**
	 * Creates a single random person object with linked address.
	 *
	 * @return the new person object
	 */
	@Override
	public Person createRandomPerson(AgeRange range) {
		Person randomPerson = createBasicPerson(range);
		Optional<PostalAddress> address = repository.findById(randomId());
		if (address.isPresent()) {
			randomPerson.setAddress(address.orElseThrow());
		}

		return randomPerson;
	}

	/**
	 * Creates a single random person object with linked address inside a given
	 * city.
	 *
	 * @param city the city to fetch random address from
	 *
	 * @return the new person object
	 */
	@Override
	public Person createRandomPersonInCity(String city, AgeRange range) {
		Person randomPerson = createBasicPerson(range);
		List<PostalAddress> addresses = repository.findByAddressLocalityIgnoreCase(city);
		return compileResult(randomPerson, addresses);
	}

	/**
	 * Creates a single random person object with linked address inside a given
	 * postal code area.
	 *
	 * @param postalCode the postal code marking the area to fetch random address
	 *                   from
	 *
	 * @return the new person object
	 */
	@Override
	public Person createRandomPersonInArea(String postalCode, AgeRange range) {
		Person randomPerson = createBasicPerson(range);
		List<PostalAddress> addresses = repository.findByPostalCodeLike(postalCode);
		return compileResult(randomPerson, addresses);
	}

	private Person compileResult(Person randomPerson, List<PostalAddress> addresses) {
		PostalAddress address = null;
		if (null != addresses && !addresses.isEmpty()) {
			address = addresses.get(random.nextInt(addresses.size()));
		} else {
			Optional<PostalAddress> addressContainer = repository.findById(randomId());
			if (addressContainer.isPresent()) {
				address = addressContainer.orElseThrow();
				randomPerson.setComment("postal code not found in address base");
			} else {
				randomPerson.setComment("internal error while retrieving address");
			}
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

		return Normalizer.normalize(email.toString(), Form.NFD).replaceAll("[^\\p{ASCII}]", "");
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
	private LocalDate createRandomDateOfBirth(AgeRange range) {
		double age;
		age = createAge(range);

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

	private double createAge(AgeRange range) {
		double age;
		double min = 1.0; // you have to be born
		double max = 100.0; // a reasonable limit

		switch (range) {
		case ADULT:
			min = 19.0; // tolerance due to random day and month
			max = 100.0;
			break;

		case MINOR:
			min = 1.0;
			max = 17.0; // tolerance due to random day and month
			break;

		case SENIOR:
			min = 66.0; // tolerance due to random day and month
			max = 100.0;
			break;

		case ALL:
		default:
			break;
		}

		// choose factor smaller than medium to tighten curve
		double shiftFactor = (max - min) / 3;
		double mediumAge = min + (max - min) / 2;
		age = random.nextGaussian() * shiftFactor + mediumAge;
		if (age < min || age > max) {
			// cut off long tails
			age = mediumAge;
		}
		return age;
	}

	/**
	 * @return
	 */
	private Long randomId() {

		Long offset = repository.min() - 1L; 
		Long result = (long) random.nextInt(getCount().intValue());

		// add offset for first ID
		return result + offset;
	}

	/**
	 * @param age
	 * @return
	 */
	private Person createBasicPerson(AgeRange range) {
		String gender = random.nextBoolean() ? MALE : FEMALE; 
		String firstname;
		if (MALE.equals(gender)) {
			firstname = maleNames.get(random.nextInt(maleNames.size())).trim();
		} else {
			firstname = femaleNames.get(random.nextInt(femaleNames.size())).trim();
		}
		
		// turn to non-binary for 5%
		if(random.nextFloat(1F) > 0.95F) {
			gender = NON_BINARY;
		}
		
		String surname = surnames.get(random.nextInt(surnames.size())).trim();

		String eyecolor = eyecolors.get(random.nextInt(eyecolors.size())).trim();

		LocalDate dateOfBirth = createRandomDateOfBirth(range);
		String emailAddress = createEmailAddress(firstname, surname, dateOfBirth);
		int height = createRandomHeight(gender);

		String taxId = taxIdGenerator.createTaxId();

		Person randomPerson = new Person(firstname, surname, gender, dateOfBirth, height, eyecolor, emailAddress,
				taxId);
		randomPerson.setProfession(professions.get(random.nextInt(professions.size())).trim());

		if (1 == random.nextInt(5)) {
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
