package com.example.periodtracker.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.periodtracker.R;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //for toolbar
        toolbar = findViewById(R.id.taskbar_common);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);

    }
}