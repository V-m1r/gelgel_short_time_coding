package com.example.gelgel_short_time_coding.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gelgel_short_time_coding.MainActivity;
import com.example.gelgel_short_time_coding.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            progressBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            Toast.makeText(this, "please wait you are already logged in", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void login(View view) {
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
    }

    public void registration(View view) {
        startActivity(new Intent(HomeActivity.this, RegistrationActivity.class));
    }
}