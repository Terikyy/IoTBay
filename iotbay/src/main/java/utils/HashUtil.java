package utils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class HashUtil {

    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    /**
     * Hashes a password using PBKDF2 with a salt.
     *
     * @param password the password to hash
     * @param salt the salt to use
     * @return the hashed password as a Base64-encoded string
     * @throws NoSuchAlgorithmException if the algorithm is not available
     * @throws InvalidKeySpecException if the key specification is invalid
     */
    public static String hashPassword(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Verifies a password against a hashed password.
     *
     * @param password the password to verify
     * @param salt the salt used during hashing
     * @param hashedPassword the hashed password to compare against
     * @return true if the password matches, false otherwise
     * @throws NoSuchAlgorithmException if the algorithm is not available
     * @throws InvalidKeySpecException if the key specification is invalid
     */
    public static boolean verifyPassword(String password, String salt, String hashedPassword)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String hashedInput = hashPassword(password, salt);
        return hashedInput.equals(hashedPassword);
    }
}
