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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class PhoneGenerator implements IPhoneGenerator {
	private static Log log = LogFactory.getLog(PhoneGenerator.class);

	// german mobile phone area codes
	private static final String[] MOBILE_AREA_CODES = { "1511", "1512", "1514", "1515", "1516", "1517", "1520", "1521",
			"1522", "1525", "1526", "1570", "1573", "1575", "1577", "1578", "1579", "160", "162", "163", "170", "171",
			"172", "173", "174", "175", "176", "177", "178", "179" };
	private static final String COUNTRY_CALLING_CODE = "+49 ";
	private static final String GENERIC_AREA_CODE = "32";
	private Map<String, String> areaCodeList;
	private Random rnd;

	public PhoneGenerator() {
		rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());

		areaCodeList = new HashMap<>();
		try {
			// get german landline area code list from
			// https://www.bundesnetzagentur.de/DE/Sachgebiete/Telekommunikation/Unternehmen_Institutionen/Nummerierung/Rufnummern/ONRufnr/ON_Einteilung_ONB/ON_ONB_ONKz_ONBGrenzen_node.html
			InputStream inStream = getClass().getResourceAsStream("prefixlist.csv");
			Reader in = new InputStreamReader(inStream);

			Iterable<CSVRecord> records = CSVFormat.Builder.create().setDelimiter(';').setSkipHeaderRecord(true).build()
					.parse(in);
			for (CSVRecord r : records) {
				if (r.size() >= 2) {
					String areaCode = r.get(0);
					String city = r.get(1);
					areaCodeList.put(city.toLowerCase(), areaCode);
				}
			}
		} catch (IOException e) {
			log.fatal("could not load area code directory", e);
		}

	}

	private PhoneNumber generateNumber(String areaCode) {
		String phoneNum = Integer.toString(rnd.nextInt(99999999));
		phoneNum = "123456789".substring(0, 9 - phoneNum.length()) + phoneNum; // padding
		phoneNum = phoneNum.substring(areaCode.length() - 2);

		return new PhoneNumber(COUNTRY_CALLING_CODE + areaCode + " " + phoneNum);
	}

	@Override
	public PhoneNumber generateMobileNumber() {

		int index = rnd.nextInt(MOBILE_AREA_CODES.length);
		String areaCode = MOBILE_AREA_CODES[index];

		return generateNumber(areaCode);
	}

	@Override
	public PhoneNumber generatePhoneNumber(String city) {
		String areaCode = GENERIC_AREA_CODE;
		String comment = "";
		String cityLower = city.toLowerCase();
		if (areaCodeList.containsKey(cityLower)) {
			areaCode = areaCodeList.get(cityLower);
		} else {
			comment = "city not found in area code directory";
		}

		return generateNumber(areaCode).setComment(comment);
	}
}
