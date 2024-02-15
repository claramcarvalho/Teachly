package com.example.teachly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class HomeStudent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);

        MenuBar menuBar = new MenuBar(this);
        menuBar.setupActionBar();
    }
}