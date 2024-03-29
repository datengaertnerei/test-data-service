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

package com.datengaertnerei.test.dataservice.bank;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.stereotype.Service;

/**
 * Spring Boot service to generate valid bank accounts
 *
 */
@Service
public class BankGenerator implements IBankGenerator {
	private static final String DEFAULT_CITY = "frankfurt am main";

	private static Log log = LogFactory.getLog(BankGenerator.class);

	private Map<String, Map<String, Bank>> bankDirectory;
	private Map<String, String> binList;
	private Random rnd;

	/**
	 * ctor
	 */
	public BankGenerator() {
		rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());

		initBankDirectory();
		initBinList();
	}

	private void initBinList() {
		binList = new HashMap<>();
		InputStream inStream = getClass().getResourceAsStream("binlist.csv");
		Reader in = new InputStreamReader(inStream);

		Iterable<CSVRecord> records;
		try {
			records = CSVFormat.Builder.create().setDelimiter(';').build().parse(in);
			for (CSVRecord r : records) {
				if (r.size() >= 2) {
					String type = r.get(0);
					String bin = r.get(1);
					binList.put(bin, type);
				}
			}
		} catch (IOException e) {
			log.fatal("could not read BIN list", e);
		}

	}

	private void initBankDirectory() {
		bankDirectory = new HashMap<>();
		try {
			// get german bank codes from
			// https://www.bundesbank.de/de/aufgaben/unbarer-zahlungsverkehr/serviceangebot/bankleitzahlen/
			// and just use entries with checksum routine 00
			InputStream inStream = getClass().getResourceAsStream("banklist.csv");
			Reader in = new InputStreamReader(inStream);

			Iterable<CSVRecord> records = CSVFormat.Builder.create().setDelimiter(';').setSkipHeaderRecord(true).build()
					.parse(in);
			for (CSVRecord r : records) {
				if (r.size() >= 5) {
					String bankCode = r.get(0);
					String desc = r.get(3);
					String city = r.get(2);
					String bic = r.get(4);

					Bank b = new Bank(bankCode, desc, bic, city);
					if (bankDirectory.containsKey(city.toLowerCase())) {
						Map<String, Bank> cityMap = bankDirectory.get(city.toLowerCase());
						cityMap.put(bankCode, b);
					} else {
						Map<String, Bank> cityMap = new HashMap<>();
						cityMap.put(bankCode, b);
						bankDirectory.put(city.toLowerCase(), cityMap);
					}
				}
			}
		} catch (IOException e) {
			log.fatal("could not read bank list", e);
		}
	}

	/**
	 * acct generation
	 */
	@Override
	public BankAccount generateAccount(String city) {
		// create valid internal account number
		Integer num = rnd.nextInt(999999999);
		int[] toCalc = Integer.toString(num).chars().map(c -> c - '0').toArray();
		Integer crc = calculateCheckDigit(toCalc);

		// and convert to string
		String rawAccount = num.toString() + crc.toString();
		String account = "0000000000".substring(rawAccount.length()) + rawAccount;

		// get banks for city or default
		Map<String, Bank> cityMap = bankDirectory.getOrDefault(city.toLowerCase(), bankDirectory.get(DEFAULT_CITY));

		// fetch random bank from list
		int bankIndex = rnd.nextInt(cityMap.keySet().size());
		Optional<Bank> bankContainer = cityMap.values().stream().skip(bankIndex).findFirst();
		Bank bank = bankContainer.orElseThrow();
		// and build IBAN
		Iban iban = new Iban.Builder().countryCode(CountryCode.DE).bankCode(bank.getBankCode()).accountNumber(account)
				.build();

		// combine to result
		BankAccount result = new BankAccount();
		result.setBank(bank);
		result.setIban(iban.toString());
		if (!result.getBank().getCity().equalsIgnoreCase(city)) {
			result.setComment("city not found in bank directory, switched to default");
		}

		return result;
	}

	/**
	 * cc generation
	 */
	@Override
	public CreditCard generateCreditCard() {
		// fetch random bin from list
		int binIndex = rnd.nextInt(binList.keySet().size());
		Optional<String> bin = binList.keySet().stream().skip(binIndex).findFirst();

		// create valid cc number
		String randomPart = Integer.toString(rnd.nextInt(999999999));
		String combined = bin.orElseThrow() + "000000000".substring(randomPart.length()) + randomPart;
		int[] toCalc = combined.chars().map(c -> c - '0').toArray();
		Integer crc = calculateCheckDigit(toCalc);

		// and convert to string
		String number = combined + crc.toString();

		// CVC must be 3 digits
		int cvc = rnd.nextInt(899) + 100;

		// create expiry date in the future, min. 6 months, max. 4 years
		LocalDate expiry = LocalDate.now().plusDays(rnd.nextInt(1250) + 210L);

		// combine to result
		return new CreditCard(number, binList.get(bin.orElseThrow()), Integer.toString(cvc), expiry);
	}

	// calculate checksum for domestic account number, IBAN checksum is included in
	// IBAN builder
	private int calculateCheckDigit(int[] digits) {

		/* double every other starting from right - jumping from 2 in 2 */
		for (int i = digits.length - 1; i >= 0; i -= 2) {
			digits[i] += digits[i];

			/* taking the sum of digits grater than 10 - simple trick by substract 9 */
			if (digits[i] >= 10) {
				digits[i] = digits[i] - 9;
			}
		}
		int sum = 0;
		for (int i = 0; i < digits.length; i++) {
			sum += digits[i];
		}
		/* multiply by 9 step */
		sum = sum * 9;

		return sum % 10;
	}
}
