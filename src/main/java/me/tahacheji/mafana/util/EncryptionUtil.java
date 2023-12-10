package me.tahacheji.mafana.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class EncryptionUtil {

    private static final String HASH_ALGORITHM = "SHA-256";

    public UUID stringToUUID(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert the hash bytes to a UUID
            long mostSigBits = 0;
            long leastSigBits = 0;
            for (int i = 0; i < 8; i++) {
                mostSigBits = (mostSigBits << 8) | (hashBytes[i] & 0xff);
            }
            for (int i = 8; i < 16; i++) {
                leastSigBits = (leastSigBits << 8) | (hashBytes[i] & 0xff);
            }

            return new UUID(mostSigBits, leastSigBits);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Handle the exception appropriately in your code
        }
    }
}
