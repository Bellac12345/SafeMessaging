package com.example.safemessaging;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class RegisterActivity extends AppCompatActivity {

    //Encryption Setup
    private final String initVector = "encryptionIntVec";
    private IvParameterSpec iv;
    private String key = "cryptologyfinala";
    private Cipher cipher;
    private SecretKeySpec secretKeySpec;

    //Widgets
    EditText userET, passET, emailET;
    Button registerBtn;

    //Firebase
    FirebaseAuth auth;
    DatabaseReference myRef;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        // Initializing Widgets:
        userET = findViewById(R.id.userEditText);
        passET = findViewById(R.id.passEditText);
        emailET= findViewById(R.id.emailEditText);
        registerBtn= findViewById(R.id.buttonRegister);

        //Firebase Auth
        auth = FirebaseAuth.getInstance();

        try {
            iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");


        } catch (Exception e) {
            e.printStackTrace();
        }


        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){

                //Encrypting Username
                String username_text =  AESEncryptionMethod(userET.getText().toString());
                String email_text = emailET.getText().toString();
                String password_text = AESEncryptionMethod(passET.getText().toString());




                if(TextUtils.isEmpty(username_text) ||TextUtils.isEmpty(email_text) || TextUtils.isEmpty(password_text)){
                    Toast.makeText(RegisterActivity.this, "Please Fill All Fields", Toast.LENGTH_SHORT ).show();

                }else{
                    RegisterNow(username_text, email_text, password_text);
                }




            }

        });
    }

    private String AESEncryptionMethod(String string){

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec,iv);
            byte[] encryptedByte =cipher.doFinal(string.getBytes());
            return new String(encryptedByte, "ISO-8859-1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private void RegisterNow(final String username, String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){

                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            //Storing list of users in Node called MyUsers. Every user is child inside MyUser Node
                            myRef = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);

                            //HashMaps
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL","default");
                            hashMap.put("status","offline");

                            //Opening Main Activity after Successful Registration
                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task){
                                    if(task.isSuccessful()){
                                        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    }


                                }

                            });



                        } else{


                            Toast.makeText(RegisterActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                        }

                    }

                });

    }

}