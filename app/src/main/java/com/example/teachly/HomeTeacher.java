package com.example.teachly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teachly.Classes.Activity;
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

public class HomeTeacher extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ListView listOfClasses;
    ImageButton btnAdd;
    EditText edtClassName, edtClassDescription;
    Spinner spinner;
    RadioGroup radioGroup;
    TextView btnSaveNewClass;
    String selectedCategory;

    ArrayList<Class> listAllClasses = new ArrayList<>();

    User teacher;
    String colorOfClass;

    String[] colors = {"#fef8a0","#ff8a84","#75a9f9"};
    String[] names = {"French Class Intermediate", "French Class Basic I", "Math With Luc"};

    String[] nbStu = {"5 students", "4 students", "7 students"};

    SharedPreferences sharedPreferences;
    static String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_teacher);

        sharedPreferences = getSharedPreferences("Teachly", Context.MODE_PRIVATE);
        uId = sharedPreferences.getString("uId", "");

        MenuBar menuBar = new MenuBar(this);
        menuBar.setupActionBar();

        loadAllClassesByTeacherUId();

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

                edtClassName = dialogView.findViewById(R.id.edtClassNameAddNewClass);
                edtClassDescription = dialogView.findViewById(R.id.edtDescriptionAddNewClass);
                spinner = dialogView.findViewById(R.id.spinnerTeacherClassCategory);
                radioGroup = dialogView.findViewById(R.id.radioGroupColorAddNewClass);
                btnSaveNewClass = dialogView.findViewById(R.id.btnSaveNewClassTeacher);


                int idBtn =  radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = dialogView.findViewById(idBtn);

                colorOfClass = radioButton.getText().toString();
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = group.findViewById(checkedId);
                        colorOfClass = radioButton.getText().toString();
                    }
                });

                List<String> categories = new ArrayList<String>();
                for (EnumCategoryClass value : EnumCategoryClass.values()){
                    categories.add(value.name());
                }
                ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(HomeTeacher.this, android.R.layout.simple_spinner_item, categories);
                adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapterSpinner);
                spinner.setOnItemSelectedListener(HomeTeacher.this);

                btnSaveNewClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createClassOnDatabase(dialog, colorOfClass);
                    }
                });
            }
        });

    }

    public static void loadAllClassesByTeacherUId() {
        DatabaseReference databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("classes");
        Query findClasses = databaseReferenceUsers.orderByChild("teacherUId").equalTo(uId);

        findClasses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<Class> listOfClasses = new ArrayList<>();
                    for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                        String classId = classSnapshot.child("classId").getValue(String.class);
                        String className = classSnapshot.child("name").getValue(String.class);
                        String classDesc = classSnapshot.child("description").getValue(String.class);
                        String category = classSnapshot.child("category").getValue(String.class);
                        String color = classSnapshot.child("color").getValue(String.class);

                        Class newClass = new Class(classId, className, classDesc, uId, color, EnumCategoryClass.valueOf(category));
                        listOfClasses.add(newClass);
                    }
                    for (Class item : listOfClasses){
                        System.out.println(item.getName());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCategory = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void createClassOnDatabase (AlertDialog dialog, String colorOfClass) {

        String className = edtClassName.getText().toString().trim();
        String classDescription = edtClassDescription.getText().toString().trim();

        if (className.isEmpty()){
            Toast.makeText(this, "Please enter a name for the class", Toast.LENGTH_SHORT).show();
            return;
        }

        if (classDescription.isEmpty()){
            Toast.makeText(this, "Please enter a description for the class", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Class.createClassOnDatabase(className, classDescription, uId, colorOfClass, EnumCategoryClass.valueOf(selectedCategory));
            Toast.makeText(HomeTeacher.this, "Class " + className + " was successfully created", Toast.LENGTH_SHORT).show();


        }
        catch (Exception ex){
            Toast.makeText(HomeTeacher.this, "Class was not created, try again!", Toast.LENGTH_SHORT).show();
        }
        finally {
            dialog.dismiss();
        }

    }

}