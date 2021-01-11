package com.beyondid.scimConnector.jfgcp.util;

import java.security.SecureRandom;

public class UniqueNumber {
	public static String generateRandomString(int length) {
	    // You can customize the characters that you want to add into
	    // the random strings
	    String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
	    String CHAR_SPECIAL = "_!@#$%";
//	    String CHAR_UPPER = CHAR_LOWER.toUpperCase();
	    String NUMBER = "0123456789";

	    String DATA_FOR_RANDOM_STRING = CHAR_LOWER + NUMBER + CHAR_SPECIAL;
	    SecureRandom random = new SecureRandom();

	    if (length < 1) throw new IllegalArgumentException();

	    StringBuilder sb = new StringBuilder(length);
	    
	    for (int i = 0; i < length; i++) {
	        // 0-62 (exclusive), random returns 0-61
	        int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
	        char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

	        sb.append(rndChar);
	    }

	    return sb.toString();
	}
}
