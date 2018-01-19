package modules;

import com.Cipher;
import com.InvalidKeyException;
import com.Utility;

public class Kamasutra implements Cipher {
    @Override
    public String encrypt(String plaintext, String key) throws InvalidKeyException {
        key = key.toLowerCase();
        checkKey(key);

        StringBuilder out = new StringBuilder();
        for (Character c: plaintext.toCharArray()) {
            int position = find (c, key);
            if (position < 13)
                position += 13;
            else {
                position -= 13;
            }

            out.append(key.charAt(position));
        }

        return out.toString();
    }

    @Override
    public String decrypt(String ciphertext, String key) throws InvalidKeyException {
//	    key = key.toLowerCase();
//	    checkKey(key);
//
//	    StringBuilder out = new StringBuilder();
//	    for (Character c: ciphertext.toCharArray()) {
//		    int position = find (c, key);
//		    if (position < 13)
//			    position += 13;
//		    else {
//			    position -= 13;
//		    }
//
//		    out.append(key.charAt(position));
//	    }
//
//	    return out.toString();

	    return encrypt(ciphertext, key);
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Boolean checkKey(String key) throws InvalidKeyException {
        if (!(key.equals(Utility.removeDuplicates(key)))) throw new InvalidKeyException("Key cannot contain duplicate characters!");
        if (key.length() != 26) throw new InvalidKeyException("Key must be exactly 26 characters long!");
        return null;
    }

    private int find(Character in, String data) {
        int index = 0;
        for (Character c: data.toCharArray()) {
            if (c == in) break;

            ++index;
        }

        return index;
    }
}
