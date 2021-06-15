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

public class Users {
    private String id,username,imageURL;
    private String status;


    //Decryption info
    private final String initVector = "encryptionIntVec";
    private IvParameterSpec iv=new IvParameterSpec(initVector.getBytes("UTF-8"));
    private String key = "cryptologyfinala";
    private Cipher decipher =Cipher.getInstance("AES/CBC/PKCS5PADDING");
    private SecretKeySpec secretKeySpec=new SecretKeySpec(key.getBytes("UTF-8"), "AES");

    //Constructors
    public Users() throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException {

    }
    public Users(String id, String username, String imageURL, String status) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException {
       this.id =id;
       this.username = username;
       this.imageURL=imageURL;
       this.status = status;

    }

    //Getters and Setters methods
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id=id;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername() throws UnsupportedEncodingException {
        return AESDecryptionMethod(username);
    }
    public String getImageURL(){
        return imageURL;
    }
    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
