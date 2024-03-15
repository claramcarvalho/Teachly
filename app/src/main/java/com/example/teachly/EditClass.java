package com.example.teachly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teachly.Classes.EnumCategoryClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditClass extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView btnSave, btnDelete;
    String classId, className, classDescription, classCategory, classColor, newClassCategory;
    EditText edtClassName, edtDescription;
    Spinner spinner;
    RadioGroup radioGroup;
    String txtNewColor;

    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);

        btnSave = findViewById(R.id.btnSaveClassOnEditClass);
        btnDelete = findViewById(R.id.btnDeleteClassOnEditClass);

        classId = getIntent().getStringExtra("editClassId");

        edtClassName = findViewById(R.id.edtEditClassName);
        className = getIntent().getStringExtra("editClassName");
        edtClassName.setText(className);

        edtDescription = findViewById(R.id.edtEditClassDescription);
        classDescription = getIntent().getStringExtra("editClassDescription");
        edtDescription.setText(classDescription);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("classes");

        ////// CATEGORIA
        classCategory = getIntent().getStringExtra("editClassCategory");
        spinner = findViewById(R.id.spinnerTeacherEditClassCategory);
        List<String> categories = new ArrayList<String>();
        Integer selectedIndex = 0;
        for (EnumCategoryClass value : EnumCategoryClass.values()){
            categories.add(value.name());
        }
        for (int i = 0;i<categories.size();i++) {
            if (categories.get(i).equals(classCategory)) {
                selectedIndex = i;
            }
        }
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(EditClass.this, android.R.layout.simple_spinner_item, categories);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(EditClass.this);
        spinner.setSelection(selectedIndex);

        ///////SETTING ORIGINAL COLOR
        ImageView edtClassColor = findViewById(R.id.editClassColor);
        classColor = getIntent().getStringExtra("editClassColor");
        edtClassColor.setColorFilter(Color.parseColor(classColor), PorterDuff.Mode.SRC_IN);

        /////CHANGING COLOR IF RADIOBUTTON IS SELECTED
        radioGroup = findViewById(R.id.radioColorEditClass);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                txtNewColor = radioButton.getText().toString();
                edtClassColor.setColorFilter(Color.parseColor(txtNewColor), PorterDuff.Mode.SRC_IN);
            }
        });

        ///////CHANGING COLOR OF RADIO BUTTON
        for (int i = 0; i<radioGroup.getChildCount() ; i++) {
            View view = radioGroup.getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) view;
                String textRadioButton = radioButton.getText().toString();
                if (textRadioButton.equals(classColor)) {
                    radioButton.setChecked(true);
                    break;
                }
            }
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClassNameChanged()) {
                    Toast.makeText(EditClass.this, "Class Name was updated!", Toast.LENGTH_SHORT).show();
                }
                if (isClassDescriptionChanged()) {
                    Toast.makeText(EditClass.this, "Class Description was updated!", Toast.LENGTH_SHORT).show();
                }
                if (isClassCategoryChanged()) {
                    Toast.makeText(EditClass.this, "Class Category was updated!", Toast.LENGTH_SHORT).show();
                }
                if (isClassColorChanged()) {
                    Toast.makeText(EditClass.this, "Class Color was updated!", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(EditClass.this, HomeTeacher.class);
                startActivity(intent);
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditClass.this);
                builder.setTitle("Delete Class");
                builder.setMessage("Are you sure you want to delete this class?\nThis action cannot be undone!");
                builder.setPositiveButton("Delete Account", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Query findClass = reference.orderByChild("classId").equalTo(classId);

                        findClass.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    snapshot.child(classId).getRef().removeValue();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditClass.this);
                                    builder.setTitle("Class deleted");
                                    builder.setMessage("Your class " + className + " was deleted!!");

                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(EditClass.this, HomeTeacher.class);
                                            startActivity(intent);
                                            finish();
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.setIcon(R.drawable.icon_check_30);
                                    builder.setCancelable(false);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                } else {
                                    Toast.makeText(EditClass.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        newClassCategory = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean isClassNameChanged (){
        if (!className.equals(edtClassName.getText().toString().trim())) {
            reference.child(classId).child("name").setValue(edtClassName.getText().toString().trim());
            return true;
        }
        return false;
    }
    private boolean isClassDescriptionChanged (){
        if (!classDescription.equals(edtDescription.getText().toString().trim())) {
            reference.child(classId).child("description").setValue(edtDescription.getText().toString().trim());
            return true;
        }
        return false;
    }
    private boolean isClassCategoryChanged (){
        if (!classCategory.equals(newClassCategory)) {
            reference.child(classId).child("category").setValue(newClassCategory);
            return true;
        }
        return false;
    }
    private boolean isClassColorChanged (){
        if (!classColor.equals(txtNewColor)) {
            reference.child(classId).child("color").setValue(txtNewColor);
            return true;
        }
        return false;
    }
}