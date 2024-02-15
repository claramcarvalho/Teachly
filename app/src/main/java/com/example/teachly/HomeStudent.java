package com.example.teachly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class HomeStudent extends AppCompatActivity {

    ListView listOfClasses;

    String[] colors = {"#fef8a0","#ff8a84","#75a9f9"};
    String[] names = {"French Class Intermediate", "French Class Basic I", "Math With Luc"};

    String[] nbStu = {"5 students", "4 students", "7 students"};

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);

        ///////////////////////////////////////////////////////// Testando pegar info da session
        sharedPreferences = getSharedPreferences("Teachly", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");
        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, password, Toast.LENGTH_SHORT).show();
        //////////////////////////////////////////////////////////

        MenuBar menuBar = new MenuBar(this);
        menuBar.setupActionBar();

        listOfClasses = findViewById(R.id.listOfClassStudent);
        CustomAdapterListOfClasses adapter = new CustomAdapterListOfClasses(getApplicationContext(),colors,names,nbStu);
        listOfClasses.setAdapter(adapter);
    }
}