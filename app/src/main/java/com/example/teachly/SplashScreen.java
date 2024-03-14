package com.example.teachly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.teachly.Classes.Class;
import com.example.teachly.Classes.EnumCategoryClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
/*

        Class newClass = new Class("0", "0", "0", "0", "0", EnumCategoryClass.ENGLISH);

        DatabaseReference databaseReferenceClasses = FirebaseDatabase.getInstance().getReference("classes");
        databaseReferenceClasses.child(newClass.getClassId()).setValue(newClass);
*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, 500);
    }
}