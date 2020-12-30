package com.example.periodtracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.periodtracker.R;
import com.example.periodtracker.fragments.CalendarFragment;
import com.example.periodtracker.fragments.HistoryFragment;
import com.example.periodtracker.fragments.OverviewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mauth;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mauth = FirebaseAuth.getInstance();

        //for toolbar
        toolbar = findViewById(R.id.taskbar_common);
        setSupportActionBar(toolbar);

        //instance of bottomNavigation
        BottomNavigationView btnNav = findViewById(R.id.bottom_navigation);

        //make home fragement the default fragment this is for bottom navigation

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new OverviewFragment()).commit();
        btnNav.setOnNavigationItemSelectedListener(navListener);
    }

    //Listener bottom navigation bar
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.overview:
                    selectedFragment = new OverviewFragment();
                    break;
                case R.id.calendar:
                    selectedFragment = new CalendarFragment();
                    break;
                case R.id.history:
                    selectedFragment = new HistoryFragment();
                    break;
            }
            // fragment transaction
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case R.id.logout:
                mauth.signOut();
                startActivity(new Intent(MainActivity.this, SplashScreenActivity.class));
                finish();
                break;
            case R.id.safety:
                startActivity(new Intent(MainActivity.this, SafetyActivity.class));
                finish();
                break;
            case R.id.find:
                startActivity(new Intent(MainActivity.this, GetLocationActivity.class));
                finish();
                break;
            case R.id.exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}