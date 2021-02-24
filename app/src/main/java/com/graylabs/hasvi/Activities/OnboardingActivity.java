package com.graylabs.hasvi.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import com.graylabs.hasvi.Fragments.LoginFragment;

import com.graylabs.hasvi.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        navigateFragments();
    }

    private void navigateFragments() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.onboarding_fcv, new LoginFragment());
        ft.commitAllowingStateLoss();
    }

}