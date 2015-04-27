package wikiroutes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPassword {
	
	private static String algorithm = "SHA";

	private static String toHexadecimal(byte[] digest) {

		String hash = "";

		for (byte aux : digest) {

			int b = aux & 0xff;

			if (Integer.toHexString(b).length() == 1)
				hash += "0";

			hash += Integer.toHexString(b);

		}

		return hash;

	}

	public static String generateHashPassword(String pass) {
		byte[] digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			byte[] buffer = pass.getBytes();
			md.reset();
			md.update(buffer);
			digest = md.digest();
		} catch (NoSuchAlgorithmException ex) {

			System.out.println("Error creando Digest");

		}

		return toHexadecimal(digest);

	}

	public static boolean checkPassword(String hashpass, String pass) {
		return hashpass.equals((String) generateHashPassword(pass));
	}
}
