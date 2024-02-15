package com.example.teachly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;

public class HomeTeacher extends AppCompatActivity {

    ListView listOfClasses;

    String[] colors = {"#fef8a0","#ff8a84","#75a9f9"};
    String[] names = {"French Class Intermediate", "French Class Basic I", "Math With Luc"};

    String[] nbStu = {"5 students", "4 students", "7 students"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_teacher);

        MenuBar menuBar = new MenuBar(this);
        menuBar.setupActionBar();

        listOfClasses = findViewById(R.id.listOfClassTeacher);
        CustomAdapterListOfClasses adapter = new CustomAdapterListOfClasses(getApplicationContext(),colors,names,nbStu);
        listOfClasses.setAdapter(adapter);
    }
}