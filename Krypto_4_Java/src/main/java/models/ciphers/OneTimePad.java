package models.ciphers;

public class OneTimePad extends AbstractCipher implements CipherInterface {
    String description = "One time pad.";
    String name = "One time pad.";

    @Override
    public String encrypt(String plaintext, String key)  {
        StringBuilder out = new StringBuilder();

        int counter = 0;
        for (Character c: plaintext.toCharArray()) {
            int temp = (c - 'a') + (key.charAt(counter) - 'a');
            out.append(ALPHABETS.charAt(temp % 26));
        }

        return out.toString();
    }

    @Override
    public String decrypt(String ciphertext, String key)  {
        StringBuilder out = new StringBuilder();

        int counter = 0;
        for (Character c: ciphertext.toCharArray()) {
            int temp = (c - 'a') - (key.charAt(counter) - 'a');
            if (temp < 0) temp += 26;
            out.append(ALPHABETS.charAt(temp % 26));
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
    public Boolean checkKey(String key)  {
        return null;
    }
}

