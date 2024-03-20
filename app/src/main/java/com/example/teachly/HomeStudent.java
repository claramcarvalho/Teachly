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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.teachly.Classes.Class;
import com.example.teachly.Classes.EnumCategoryClass;
import com.example.teachly.Classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeStudent extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    static ListView listOfClasses;
    ListView listOfClassesSearchListView;
    SharedPreferences sharedPreferences;
    static String uId;

    ImageButton btnAddClass;

    Button btnSearchClass;
    FirebaseDatabase database;
    DatabaseReference referenceTeachers, referenceClasses;

    String selectedCourse = "ALL CATEGORIES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);

        sharedPreferences = getSharedPreferences("Teachly", Context.MODE_PRIVATE);
        uId = sharedPreferences.getString("uId", "");

        database = FirebaseDatabase.getInstance();
        referenceTeachers = database.getReference("users");
        referenceClasses = database.getReference("classes");

        MenuBar menuBar = new MenuBar(this);
        menuBar.setupActionBar();

        Class.loadAllClassesByStudentUId(getApplicationContext(),uId);

        listOfClasses = findViewById(R.id.listOfClassStudent);

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


                btnSearchClass = dialogView.findViewById(R.id.btnSearchClassStudentView);

                btnSearchClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadTeachersByCategory(selectedCourse);
                    }
                });
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCourse = parent.getItemAtPosition(position).toString();
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
            Query findClass = referenceClasses.orderByChild("category").equalTo(category);
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
                    else {
                        listOfClassesSearchListView.setAdapter(null);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public static void loadAllClassesInList(Context context, ArrayList<Class> classes){
        CustomAdapterListOfClasses adapter = new CustomAdapterListOfClasses(context,classes, "Student");
        listOfClasses.setAdapter(adapter);
    }
}