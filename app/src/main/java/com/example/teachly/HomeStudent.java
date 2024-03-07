package com.example.teachly;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomeStudent extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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

        sharedPreferences = getSharedPreferences("Teachly", Context.MODE_PRIVATE);

        MenuBar menuBar = new MenuBar(this);
        menuBar.setupActionBar();

        listOfClasses = findViewById(R.id.listOfClassStudent);
        CustomAdapterListOfClasses adapter = new CustomAdapterListOfClasses(getApplicationContext(),colors,names,nbStu);
        listOfClasses.setAdapter(adapter);

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

                Spinner spinner = dialogView.findViewById(R.id.spinnerStudentSearchCategory);
                List<String> categories = new ArrayList<String>();
                categories.add("French");
                categories.add("Math");
                categories.add("Tefaq");
                categories.add("C#");
                categories.add("IELTS");
                ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(HomeStudent.this, android.R.layout.simple_spinner_item, categories);
                adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapterSpinner);
                spinner.setOnItemSelectedListener(HomeStudent.this);


                String[] photos = {"#fef8a0","#ff8a84","#75a9f9","#ff8a84","#75a9f9"};
                String[] names = {"Fulano Ciclano","Fulano Ciclano","Fulano Ciclano","Fulano Ciclano","Fulano Ciclano"};
                String[] emails = {"tutor1@gmail.com","tutor11111111111@gmail.com","tutor1@gmail.com", "tutor133445455665@gmail.com", "tutor1@gmail.com"};
                String[] phones = {"4567896325", "4567896325","4567896325","4567896325", "4567896325"};
                ListView listTutors = dialogView.findViewById(R.id.listTutors);
                CustomAdapterListOfTutors adapter = new CustomAdapterListOfTutors(getApplicationContext(), photos, names, emails, phones);
                listTutors.setAdapter(adapter);

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedCourse = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, "The course selected is " + selectedCourse, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}