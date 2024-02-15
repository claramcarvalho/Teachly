package com.example.teachly;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class HomeStudent extends AppCompatActivity {

    ListView listOfClasses;

    String[] colors = {"#fef8a0","#ff8a84","#75a9f9"};
    String[] names = {"French Class Intermediate", "French Class Basic I", "Math With Luc"};

    String[] nbStu = {"5 students", "4 students", "7 students"};

    SharedPreferences sharedPreferences;

    ImageButton btnAddClass;

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

        ///////////////////////////////////////////////////////// Testando abrir modal
        btnAddClass = findViewById(R.id.btn_student_add_class);
        btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_student_add_class, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeStudent.this);
                builder.setView(dialogView);
                //builder.setTitle("Search new tutors");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}