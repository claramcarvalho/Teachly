package com.example.teachly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.teachly.Classes.Class;
import com.example.teachly.Classes.EnumCategoryClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeStudent extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ListView listOfClasses;
    ListView listOfClassesSearchListView;

    String[] colors = {"#fef8a0","#ff8a84","#75a9f9"};
    String[] names = {"French Class Intermediate", "French Class Basic I", "Math With Luc"};

    String[] nbStu = {"5 students", "4 students", "7 students"};

    SharedPreferences sharedPreferences;

    ImageButton btnAddClass;

    FirebaseDatabase database;
    DatabaseReference referenceTeachers, referenceClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);

        database = FirebaseDatabase.getInstance();
        referenceTeachers = database.getReference("users");
        referenceClasses = database.getReference("classes");

        sharedPreferences = getSharedPreferences("Teachly", Context.MODE_PRIVATE);

        MenuBar menuBar = new MenuBar(this);
        menuBar.setupActionBar();

       /* listOfClasses = findViewById(R.id.listOfClassStudent);
        CustomAdapterListOfClasses adapter = new CustomAdapterListOfClasses(getApplicationContext(),colors,names,nbStu);
        listOfClasses.setAdapter(adapter);*/

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

                listOfClassesSearchListView = dialogView.findViewById(R.id.listTutors);
                Spinner spinner = dialogView.findViewById(R.id.spinnerStudentSearchCategory);
                List<String> categories = new ArrayList<String>();
                categories.add("ALL CATEGORIES");
                for (EnumCategoryClass value : EnumCategoryClass.values()){
                    categories.add(value.name());
                }
                ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(HomeStudent.this, android.R.layout.simple_spinner_item, categories);
                adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapterSpinner);
                spinner.setOnItemSelectedListener(HomeStudent.this);
                spinner.setSelection(0);

                loadTeachersByCategory("ALL CATEGORIES");
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

    public void loadTeachersByCategory(String category) {
        if (category.equals("ALL CATEGORIES")) {
            Query findClass = referenceClasses.orderByChild("classId");
            findClass.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ArrayList<Class> listOfClassesSearch = new ArrayList<>();
                        for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                            String classId = classSnapshot.child("classId").getValue(String.class);
                            String name = classSnapshot.child("name").getValue(String.class);
                            String description = classSnapshot.child("description").getValue(String.class);
                            String color = classSnapshot.child("color").getValue(String.class);
                            String category = classSnapshot.child("category").getValue(String.class);
                            String teacherUId = classSnapshot.child("teacherUId").getValue(String.class);

                            Class newClass = new Class(classId,name,description,teacherUId,color,EnumCategoryClass.valueOf(category));
                            listOfClassesSearch.add(newClass);
                        }

                        CustomAdapterListOfTutors adapter = new CustomAdapterListOfTutors(getApplicationContext(),listOfClassesSearch);
                        listOfClassesSearchListView.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {

        }
    }
}