package com.example.studyupapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.studyupapp.ui.social.SocialFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.studyupapp.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static String userName;
    public static String userGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_study, R.id.navigation_home, R.id.navigation_social)
//                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        findViewById(R.id.navigation_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateHome(navController);
            }
        });

        findViewById(R.id.navigation_social).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateSocial(navController);
            }
        });

        findViewById(R.id.navigation_study).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateStudy(navController);
            }
        });

        Intent i = getIntent();
        userName = i.getStringExtra("NAME");
        userGrade = i.getStringExtra("GRADE");
        String destination = i.getStringExtra("DESTINATION_FRAGMENT");
        i.getFlags();
        if(destination.equals("SOCIAL")){
            navController.navigate(R.id.navigation_social);
        } else if(destination.equals("STUDY")) {
            navController.navigate(R.id.navigation_study);
        } else if(destination.equals("HOME")) {
            navController.navigate(R.id.navigation_home);
        }

    }

    private void navigateHome(NavController navController) {
        navController.navigate(R.id.navigation_home);
    }

    private void navigateSocial(NavController navController) {
        navController.navigate(R.id.navigation_social);
    }

    private void navigateStudy(NavController navController) {
        navController.navigate(R.id.navigation_study);
    }
}