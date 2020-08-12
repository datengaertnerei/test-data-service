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
	private Integer maxAddressId;

	@Autowired
	PostalAddressRepository repository;

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
		maxAddressId = (int) repository.count();
		if (0 == maxAddressId) {
			OsmPbfAddressImport.importAddresses("germany-latest.osm.pbf", repository);
			maxAddressId = (int) repository.count();
		}

		log.info("Address count: " + maxAddressId);

		Optional<PostalAddress> dummy = repository.findById(maxAddressId);
		log.info(dummy.get().toString());

	}

	/**
	 * Creates a single random person object with linked address.
	 *
	 * @return the new person object
	 */
	public Person createRandomPerson() {
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
		Optional<PostalAddress> address = repository.findById(random.nextInt(maxAddressId));

		randomPerson.setAddress(address.get());

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
