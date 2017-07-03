package br.unicamp.ft.priva.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Encryption algorithm
 * Adapted from
 * http://www.devmedia.com.br/como-funciona-a-criptografia-hash-em-java/31139
 */
public class Encryption implements AnonymizationInterface {

    /**
     * Run Encryption technique
     */
    public String run(String value, String detail) {
        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte messageDigest[] = new byte[0];
        try {
            messageDigest = algorithm.digest(value.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            hexString.append(String.format("%02X", 0xFF & b));
        }

        return hexString.toString();
    }
}
