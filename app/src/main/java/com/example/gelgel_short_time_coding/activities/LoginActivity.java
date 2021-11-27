package com.example.gelgel_short_time_coding.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button signIn;
    EditText num, password;
    TextView signUp;

    FirebaseAuth auth;

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

        // Login User
     /*   auth.signInWithEmailAndPassword(userNum, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Login Succesfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Error" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });*/


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("phone").child(userNum);

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        PhoneAuth auth = snapshot.getValue(PhoneAuth.class);

                        if (auth.getPassword().equals(userPassword)) {
                            Log.e("Login", auth.getPassword()+" "+auth.getPhoneNum());

                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Login Succesfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();

                        }
                    }else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Phone num doesn't exist", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

