package com.example.taximap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
	
	static public String hashString(String string) {
		MessageDigest messageDigest;
		String encryptedString;
		try {
			messageDigest = MessageDigest.getInstance("SHA-512");
			messageDigest.update(string.getBytes());
			encryptedString = new String(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			int hash = string.hashCode();
			encryptedString = Integer.toString(hash);
		}
		return encryptedString;
	}

}
