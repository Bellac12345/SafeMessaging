package com.example.safemessaging.Model;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private boolean isseen;
    //Decryption info
    private final String initVector = "encryptionIntVec";
    private IvParameterSpec iv=new IvParameterSpec(initVector.getBytes("UTF-8"));
    private String key = "cryptologyfinala";
    private Cipher decipher =Cipher.getInstance("AES/CBC/PKCS5PADDING");
    private SecretKeySpec secretKeySpec=new SecretKeySpec(key.getBytes("UTF-8"), "AES");

    public Chat(String sender, String receiver, String message,boolean isseen) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isseen = isseen;
    }

    public Chat() throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException {

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public String getMessage() throws UnsupportedEncodingException {
        return AESDecryptionMethod(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String AESDecryptionMethod(String string) throws UnsupportedEncodingException {
        byte[] EncryptedByte = string.getBytes("ISO-8859-1");
        String decryptedString = string;

        byte[] decryption;

        try {
            decipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
            decryption = decipher.doFinal(EncryptedByte);
            decryptedString = new String(decryption);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedString;
    }
}
