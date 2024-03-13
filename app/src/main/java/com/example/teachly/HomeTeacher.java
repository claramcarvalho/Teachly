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

                edtClassName = dialogView.findViewById(R.id.edtClassNameAddNewClass);
                edtClassDescription = dialogView.findViewById(R.id.edtDescriptionAddNewClass);
                spinner = dialogView.findViewById(R.id.spinnerTeacherClassCategory);
                radioGroup = dialogView.findViewById(R.id.radioGroupColorAddNewClass);
                btnSaveNewClass = dialogView.findViewById(R.id.btnSaveNewClassTeacher);

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
                        createClassOnDatabase(dialogView);
                    }
                });
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

    public void createClassOnDatabase (View dialogView) {
        final User[] teacher = new User[1];
        String className = edtClassName.getText().toString().trim();
        String classDescription = edtClassDescription.getText().toString().trim();
        final String[] colorOfClass = new String[1];

        /////GETTING COLOR OF CLASS
        radioGroup = dialogView.findViewById(R.id.radioGroupColorAddNewClass);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                colorOfClass[0] = radioButton.getText().toString();
                Toast.makeText(HomeTeacher.this, colorOfClass[0], Toast.LENGTH_SHORT).show();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("Teachly", Context.MODE_PRIVATE);
        String uId = sharedPreferences.getString("uId", "");


        /////////////////////////////////////////PARAMOS AQUI//////////////////////////////////////////////////////////////
/*
        /////////////finding user by id
        DatabaseReference databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("users");
        Query findUser = databaseReferenceUsers.orderByChild("userId").equalTo(uId);

        findUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nameDB = snapshot.child(uId).child("fullName").getValue(String.class);
                    String emailDB = snapshot.child(uId).child("email").getValue(String.class);
                    String passwordDB = snapshot.child(uId).child("password").getValue(String.class);
                    String phoneDB = snapshot.child(uId).child("phoneNumber").getValue(String.class);
                    String typeDB = snapshot.child(uId).child("type").getValue(String.class);
                    teacher[0] = new User(uId,emailDB,passwordDB,nameDB,phoneDB,typeDB);

                    Class newClass = new Class(className,classDescription,teacher[0],colorOfClass[0],EnumCategoryClass.valueOf(selectedCategory));

                    DatabaseReference databaseReferenceClasses = FirebaseDatabase.getInstance().getReference("classes");
                    databaseReferenceClasses.child(newClass.getClassId().toString()).setValue(newClass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/

    }
}