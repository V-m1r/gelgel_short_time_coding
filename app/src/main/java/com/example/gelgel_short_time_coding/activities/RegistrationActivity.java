package com.example.gelgel_short_time_coding.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gelgel_short_time_coding.MainActivity;
import com.example.gelgel_short_time_coding.R;
import com.example.gelgel_short_time_coding.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {

    Button signUp;
    EditText name, email, password, phone;
    TextView signIn;

    FirebaseAuth auth;
    private Dialog dialog;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();


        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        phone = findViewById(R.id.phoneAuth);

        signUp = findViewById(R.id.reg_btn);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email_reg);
        password = findViewById(R.id.password_reg);
        signIn = findViewById(R.id.sign_in);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createUser();
                progressBar.setVisibility(View.VISIBLE);

            }
        });


    }

    private void signInUser(PhoneAuthCredential credential, String userEmail, String userPassword, String userName, String phoneNum) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Create User
                            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                                    .

                                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        UserModel userModel = new UserModel(userName, userEmail, userPassword, phoneNum);
                                                        String id = task.getResult().getUser().getUid();

                                                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

                                                        database.child("Users").child(id).setValue(userModel);

                                                        progressBar.setVisibility(View.GONE);

                                                        Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                                        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                                                        finish();
                                                    } else {
                                                        progressBar.setVisibility(View.GONE);
                                                        Toast.makeText(RegistrationActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                            dialog.dismiss();
                            finish();
                        } else {
                            Log.e("MainActiv", "onComplete: " + task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    private void createUser() {

        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        String phoneNum = phone.getText().toString();
        if (phoneNum.isEmpty()) {
            Toast.makeText(this, "Phone is Empty!", Toast.LENGTH_SHORT).show();
            return;
        } else {

            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phoneNum)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onCodeSent(String verificationId,
                                                       PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    dialog = new Dialog(RegistrationActivity.this);
                                    dialog.setContentView(R.layout.verify_popup);
                                    EditText codeVer = dialog.findViewById(R.id.editTextNumberDecimalCode);
                                    Button btnVerifyCode = dialog.findViewById(R.id.buttonCode);
                                    btnVerifyCode.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String code = codeVer.getText().toString();
                                            if (code.isEmpty()) {
                                                Toast.makeText(RegistrationActivity.this, "Empty Code", Toast.LENGTH_SHORT).show();
                                                return;
                                            } else {
                                                PhoneAuthCredential cred = PhoneAuthProvider.getCredential(verificationId, code);
                                                signInUser(cred,userEmail, userPassword, userName, phoneNum);
                                            }
                                        }
                                    });
                                    dialog.show();
                                }


                                @Override
                                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                    signInUser(phoneAuthCredential, userEmail, userPassword, userName, phoneNum);
                                    Toast.makeText(RegistrationActivity.this, "Sent code", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onVerificationFailed(FirebaseException e) {
                                    Toast.makeText(RegistrationActivity.this, "Failed to verify", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);

        }
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "Name is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Email is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Password is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userPassword.length() < 6) {
            Toast.makeText(this, "Password Length must be greater then 6 letter ", Toast.LENGTH_SHORT).show();
            return;
        }


    }
}