package com.graylabs.hasvi;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.graylabs.hasvi.Fragments.FirstFragment;
import com.graylabs.hasvi.Fragments.SecondFragment;
import com.graylabs.hasvi.Fragments.ThirdFragment;

public class MainActivity extends AppCompatActivity {

    private FirstFragment home;
    private SecondFragment appointments;
    private ThirdFragment profile;
    private Fragment activeFragment;
    private FragmentManager fm;
    private final int containerId =  R.id.fragment_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpFragments();
        findViews();
    }

    private void setUpFragments() {
        home = new FirstFragment();
        appointments = new SecondFragment();
        profile = new ThirdFragment();
        activeFragment = home;
        fm = getSupportFragmentManager();
        fm.beginTransaction().add(containerId, profile, "profile").hide(profile).commit();
        fm.beginTransaction().add(containerId, appointments, "appts").hide(appointments).commit();
        fm.beginTransaction().add(containerId, home, "home").commit();
    }


    private void findViews() {
        BottomNavigationView bottomBar = findViewById(R.id.main_bottomnav);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.firstFragment:
                        fm.beginTransaction().hide(activeFragment).show(home).commit();
                        activeFragment = home;
                        return true;
                    case R.id.secondFragment:
                        fm.beginTransaction().hide(activeFragment).show(appointments).commit();
                        activeFragment = appointments;
                        return true;
                    case R.id.thirdFragment:
                        fm.beginTransaction().hide(activeFragment).show(profile).commit();
                        activeFragment = profile;
                        return true;
                }
                return false;
            }
        });
    }
}