package model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordHasher {
	public static String hashPassword(String password) throws Exception{
		String hashPass = null;
				
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			byte[] hashBytes = md.digest();
			hashPass = Base64.getEncoder().encodeToString(hashBytes);
		}catch(NoSuchAlgorithmException e) {
			throw new Exception();
		}
		
		return hashPass;
	}
}
