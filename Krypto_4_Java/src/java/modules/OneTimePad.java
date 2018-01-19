package modules;

import com.Cipher;
import com.InvalidKeyException;
import com.Utility;

public class OneTimePad implements Cipher{
	String description = "One time pad.";
	String name = "One time pad.";

	@Override
	public String encrypt(String plaintext, String key) throws InvalidKeyException {
		checkKey(key, plaintext.length());
		StringBuilder out = new StringBuilder();

		int counter = 0;
		for (Character c: plaintext.toCharArray()) {
			int temp = (c - 'a') + (key.charAt(counter) - 'a');
			out.append(Utility.alphabet.charAt(temp % 26));
		}

		return out.toString();
	}

	@Override
	public String decrypt(String ciphertext, String key) throws InvalidKeyException {
		checkKey(key, ciphertext.length());
		StringBuilder out = new StringBuilder();

		int counter = 0;
		for (Character c: ciphertext.toCharArray()) {
			int temp = (c - 'a') - (key.charAt(counter) - 'a');
			if (temp < 0) temp += 26;
			out.append(Utility.alphabet.charAt(temp % 26));
		}

		return out.toString();
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Boolean checkKey(String key) throws InvalidKeyException {
		return null;
	}

	private Boolean checkKey (String key, int textLength) throws InvalidKeyException {
		if (key.length() != textLength) throw new InvalidKeyException("Key must be same length as the plaintext!");

		return true;
	}
}
