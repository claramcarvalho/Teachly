package com.example.teachly;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
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
import android.window.OnBackInvokedDispatcher;

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

    static ListView listOfClasses;
    ImageButton btnAdd;
    EditText edtClassName, edtClassDescription;
    Spinner spinner;
    RadioGroup radioGroup;
    TextView btnSaveNewClass;
    String selectedCategory;
    String colorOfClass;

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

        Class.loadAllClassesByTeacherUId(getApplicationContext(), uId);

        listOfClasses = findViewById(R.id.listOfClassTeacher);


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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCategory = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public static void loadAllClassesInList(Context context, ArrayList<Class> classes){
        CustomAdapterListOfClasses adapter = new CustomAdapterListOfClasses(context,classes, "Teacher");
        listOfClasses.setAdapter(adapter);
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
            Class.createClassOnDatabase(getApplicationContext(), className, classDescription, uId, colorOfClass, EnumCategoryClass.valueOf(selectedCategory));
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