package com.example.walletsystem.util;

import org.apache.commons.lang3.RandomStringUtils;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Random;

public class TestDataGenerator {
    private static final String AES_SECRET_KEY = "1234567890123456";
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    public static String generateNationalID() {
        Random random = new Random();
        StringBuilder nationalId = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            nationalId.append(random.nextInt(10));
        }
        return nationalId.toString();
    }
    public static String generateAccountNumber() {
        return String.format("%022d", Math.abs(new Random().nextLong() % 1_000_000_000_000_000_000L));
    }
    public static String generateRandomName() {
        return RandomStringUtils.randomAlphabetic(5, 10);
    }
    public static String generateRandomSurname() {
        return RandomStringUtils.randomAlphabetic(5, 10);
    }
    public static String encrypt(String valueToEncrypt) throws Exception {
        SecretKey key = new SecretKeySpec(AES_SECRET_KEY.getBytes(), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(valueToEncrypt.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    public static String generateEncryptedNationalID() throws Exception {
        String nationalID = generateNationalID();
        return encrypt(nationalID);
    }
    public static String generateEncryptedAccountNumber() throws Exception {
        String accountNumber = generateAccountNumber();
        return encrypt(accountNumber);
    }
    public static String generateEncryptedName() throws Exception {
        String name = generateRandomName();
        return encrypt(name);
    }
    public static String generateEncryptedSurname() throws Exception {
        String surname = generateRandomSurname();
        return encrypt(surname);
    }
}
