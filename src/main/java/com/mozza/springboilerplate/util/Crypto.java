package com.mozza.springboilerplate.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Crypto {

    public static String encrypt(String key, String salt) throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);

        String keyAndSalt = key + salt;

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(keyAndSalt.getBytes());
        return String.format("%064x", new BigInteger(1, messageDigest.digest()));
    }

    public static String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

}
