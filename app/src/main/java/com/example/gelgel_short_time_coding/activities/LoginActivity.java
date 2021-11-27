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
import com.example.gelgel_short_time_coding.models.PhoneAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    Button signIn;
    EditText num, password;
    TextView signUp;

    FirebaseAuth auth;
private Dialog dialog;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        signIn = findViewById(R.id.login_btn);
        num = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        signUp = findViewById(R.id.sign_up);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginUser();
                progressBar.setVisibility(View.VISIBLE);


            }
        });


    }

    private void loginUser() {

        String userNum = num.getText().toString();
        String userPassword = password.getText().toString();


        if (TextUtils.isEmpty(userNum)) {
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




        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("phone").child(userNum);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Phone num doesn't exist", Toast.LENGTH_SHORT).show();

                } else {

                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(auth)
                                    .setPhoneNumber(userNum)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(LoginActivity.this)                 // Activity (for callback binding)
                                    .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onCodeSent(String verificationId,
                                                               PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            dialog = new Dialog(LoginActivity.this);
                                            dialog.setContentView(R.layout.verify_popup);
                                            EditText codeVer = dialog.findViewById(R.id.editTextNumberDecimalCode);
                                          dialog.setCancelable(false);
                                            Button btnVerifyCode = dialog.findViewById(R.id.buttonCode);
                                            btnVerifyCode.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    String code = codeVer.getText().toString();
                                                    if (code.isEmpty()) {
                                                        Toast.makeText(LoginActivity.this, "Empty Code", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    } else {
                                                        PhoneAuth authPhone = snapshot.getValue(PhoneAuth.class);
                                                        PhoneAuthCredential cred = PhoneAuthProvider.getCredential(verificationId, code);
                                                        signInWithPhoneAuthCredential(cred, userPassword, authPhone.getPassword());
                                                    }
                                                }
                                            });
                                            dialog.show();
                                        }


                                        @Override
                                        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                            Toast.makeText(LoginActivity.this, "Sent code", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onVerificationFailed(FirebaseException e) {
                                            // It is invoked when an invalid request is made for verification.                 //For instance, if the phone number format is not valid.
                                            Log.w("Login", "onVerificationFailed", e);


                                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                                // Invalid request
                                                // Setting error to text field
                                                num.setError("Invalid phone number.");
                                            } else if (e instanceof FirebaseTooManyRequestsException) {
                                                // The SMS quota has been exceeded for the project
                                                Toast.makeText(getApplicationContext(), "Quota exceeded", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, String passwordUser, String passwordFb) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity
                                .this,
                        new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful() && passwordUser.equals(passwordFb)) {
                                    //verification successful we will start the profile activity
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                }
                                else if(!passwordFb.equals(passwordUser)){
                                    Toast.makeText(LoginActivity.this,"Incorrect password", Toast.LENGTH_SHORT).show();

                                }

                                else {

                                    //verification unsuccessful.. display an error message

                                    String message = "Something is wrong, we will fix it soon...";

                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        message = "Invalid code entered...";
                                    }
                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }
}

