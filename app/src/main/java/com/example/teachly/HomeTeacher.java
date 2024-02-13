package com.example.teachly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

public class HomeTeacher extends AppCompatActivity {

    ImageButton menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_teacher);

        menu = findViewById(R.id.btnMenu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuUtils.showMenu(HomeTeacher.this, v);
            }
        });
    }
}