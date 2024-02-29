package com.example.teachly;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class HomeTeacher extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ListView listOfClasses;
    ImageButton btnAdd;

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

        btnAdd = findViewById(R.id.btn_teacher_add_class);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_teacher_add_class, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeTeacher.this);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();

                Spinner spinner = dialogView.findViewById(R.id.spinnerTeacherClassCategory);
                List<String> categories = new ArrayList<String>();
                categories.add("French");
                categories.add("Math");
                categories.add("Tefaq");
                categories.add("C#");
                categories.add("IELTS");
                ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(HomeTeacher.this, android.R.layout.simple_spinner_item, categories);
                adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapterSpinner);
                spinner.setOnItemSelectedListener(HomeTeacher.this);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}