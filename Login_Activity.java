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

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Login_Activity extends AppCompatActivity {

    EditText userETLogin, passETLogin;
    Button LoginBtn, RegisterBtn;

    //Encryption Setup
    private final String initVector = "encryptionIntVec";
    private IvParameterSpec iv;
    private String key = "cryptologyfinala";
    private Cipher cipher;
    private SecretKeySpec secretKeySpec;


    // Firebase
    FirebaseAuth auth;
    FirebaseUser firebaseUser;


    @Override
    protected void onStart(){
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Checking for users existence  (allows to save current user)
        if(firebaseUser !=null){
            Intent i = new Intent(Login_Activity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        userETLogin = findViewById(R.id.editTextTextEmailAddress);
        passETLogin = findViewById(R.id.editTextTextPassword);
        LoginBtn = findViewById(R.id.buttonLogin);
        RegisterBtn = findViewById(R.id.registerBtn);

        //Firebase Auth
        auth = FirebaseAuth.getInstance();

        try {
            iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");


        } catch (Exception e) {
            e.printStackTrace();
        }



        //Register Button
        RegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i= new Intent(Login_Activity.this, RegisterActivity.class);
                startActivity(i);
            }


        });


        //Login Button
        LoginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                String email_text = userETLogin.getText().toString();
                String pass_text = AESEncryptionMethod(passETLogin.getText().toString());


                //Checking if empty
                if(TextUtils.isEmpty(email_text )|| TextUtils.isEmpty(pass_text)){
                    Toast.makeText(Login_Activity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.signInWithEmailAndPassword(email_text, pass_text)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task){
                                    if(task.isSuccessful()){
                                        Intent i = new Intent(Login_Activity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    }
                                    else{


                                        Toast.makeText(Login_Activity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }


                                }


                            });


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

}