package com.datengaertnerei.test.dataservice.person;

import java.util.Arrays;
import java.util.Random;

/**
 * Generator for german tax id
 *
 */
public class TaxIdGenerator {
	/** default tax id to match length */
	public static final String DEFAULT_TAX_ID = "12345678911";
	private Random random;

	/**
	 * ctor
	 * 
	 * @param random random number generator
	 */
	protected TaxIdGenerator(Random random) {
		this.random = random;
	}

	/**
	 * @return fictituous but valid german tax id
	 */
	protected String createTaxId() {
		int[] digits = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
		int[] result = new int[11];
		int limit = 9;

		// fill first 9 digits of result
		for (int i = 0; i < 9; i++) {
			int idx = random.nextInt(limit);
			// pick random digit
			result[i] = digits[idx];
			// and replace it as it has to be unique in the result
			digits[idx] = digits[limit];
			limit--;
		}
		// one digit has to be part of the result twice
		result[9] = result[random.nextInt(8)];

		result[10] = calcChecksum(result);
		return Arrays.stream(result).mapToObj(String::valueOf).reduce(String::concat).orElse(DEFAULT_TAX_ID);
	}

	// checksum algorithm as described in
	// https://de.wikipedia.org/wiki/Steuerliche_Identifikationsnummer#Aufbau_der_Identifikationsnummer
	private int calcChecksum(final int[] tin) {
		int sum = 0;
		int product = 10;
		for (int i = 0; i < 10; i++) {
			int c = tin[i];
			sum = (c + product) % 10;
			if (sum == 0)
				sum = 10;
			product = (sum * 2) % 11;
		}
		int check = 11 - product;
		if (check == 10)
			check = 0;
		return check;
	}

}
