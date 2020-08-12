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

package com.datengaertnerei.test.dataservice.phone;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

@Service
public class PhoneGenerator implements IPhoneGenerator {
	private static final String[] prefixes = { "1511", "1512", "1514", "1515", "1516", "1517", "1520", "1521", "1522",
			"1525", "1526", "1570", "1573", "1575", "1577", "1578", "1579", "160", "162", "163", "170", "171", "172",
			"173", "174", "175", "176", "177", "178", "179" };
	private Map<String, String> cityPrefixes;
	private Random rnd;

	public PhoneGenerator() {
		rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());

		cityPrefixes = new HashMap<>();
		try {
			InputStream inStream = getClass().getResourceAsStream("prefixlist.csv");
			Reader in = new InputStreamReader(inStream);

			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withSkipHeaderRecord().parse(in);
			for (CSVRecord record : records) {
				if (record.size() >= 2) {
					String prefix = record.get(0);
					String city = record.get(1);
					cityPrefixes.put(city.toLowerCase(), prefix);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String generateNumber(String prefix) {
		String phoneNum = Integer.toString(rnd.nextInt(99999999));
		phoneNum = "123456789".substring(0, 9 - phoneNum.length()) + phoneNum; // padding
		phoneNum = phoneNum.substring(prefix.length() - 2);

		return "+49 " + prefix + " " + phoneNum;
	}

	@Override
	public String generateMobileNumber() {

		int index = rnd.nextInt(prefixes.length);
		String prefix = prefixes[index];

		return generateNumber(prefix);
	}

	@Override
	public String generatePhoneNumber(String city) {
		String prefix = "032";
		String cityLower = city.toLowerCase();
		if (cityPrefixes.containsKey(cityLower)) {
			prefix = cityPrefixes.get(cityLower);
		}

		return generateNumber(prefix);
	}
}
