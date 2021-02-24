package com.graylabs.hasvi.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.graylabs.hasvi.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(user != null){
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        }else {
            Intent signIntent = new Intent(this, OnboardingActivity.class);
            startActivity(signIntent);
        }
        finish();

    }
}