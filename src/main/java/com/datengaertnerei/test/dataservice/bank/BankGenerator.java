package com.datengaertnerei.test.dataservice.bank;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.stereotype.Service;

@Service
public class BankGenerator implements IBankGenerator {
	private Map<String, Map<String, Bank>> bankDirectory;
	private Random rnd;

	public BankGenerator() {
		rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());

		bankDirectory = new HashMap<>();
		Map<String, String> bics = new HashMap<>();
		try {
			InputStream inStream = getClass().getResourceAsStream("banklist.csv");
			Reader in = new InputStreamReader(inStream);

			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withSkipHeaderRecord().parse(in);
			for (CSVRecord record : records) {
				if (record.size() >= 5) {
					String bankCode = record.get(0);
					String desc = record.get(3);
					String city = record.get(2);
					String bic = record.get(4);

					if (null == bic) {
						bic = bics.get(bankCode);
					} else {
						bics.put(bankCode, bic);
					}

					Bank b = new Bank(bankCode, desc, bic);
					if (bankDirectory.containsKey(city)) {
						Map<String, Bank> cityMap = bankDirectory.get(city);
						cityMap.put(bankCode, b);
					} else {
						Map<String, Bank> cityMap = new HashMap<>();
						cityMap.put(bankCode, b);
						bankDirectory.put(city.toLowerCase(), cityMap);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public String generateAccount(String city) {
		Integer num = rnd.nextInt(999999999);
		int[] toCalc = Integer.toString(num).chars().map(c -> c - '0').toArray();
		Integer crc = calculateCheckDigit(toCalc);

		String rawAccount = num.toString() + crc.toString();
		String account = "0000000000".substring(rawAccount.length()) + rawAccount;

		String cityLow = city.toLowerCase();
		Map<String, Bank> cityMap = bankDirectory.getOrDefault(cityLow, bankDirectory.get("berlin"));

		int bankIndex = rnd.nextInt(cityMap.keySet().size());
		Iterator<Bank> itr = cityMap.values().iterator();
		Iban iban = null;
		for (int i = 0; itr.hasNext(); i++) {
			Bank b = itr.next();
			if (i == bankIndex) {
				iban = new Iban.Builder().countryCode(CountryCode.DE).bankCode(b.getBankCode()).accountNumber(account)
						.build();
				break;
			}
		}

		return iban.toString();
	}

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
