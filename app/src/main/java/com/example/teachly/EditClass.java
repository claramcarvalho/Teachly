package com.example.teachly;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.List;

public class EditClass extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView btnSave, btnDelete;
    String classId, className, classDescription, classCategory, classColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);

        btnSave = findViewById(R.id.btnSaveClassOnEditClass);
        btnDelete = findViewById(R.id.btnDeleteClassOnEditClass);

        classId = getIntent().getStringExtra("editClassId");

        EditText edtClassName = findViewById(R.id.edtEditClassName);
        className = getIntent().getStringExtra("editClassName");
        edtClassName.setText(className);

        EditText edtDescription = findViewById(R.id.edtEditClassDescription);
        classDescription = getIntent().getStringExtra("editClassDescription");
        edtDescription.setText(classDescription);

        ////// CATEGORIA
        classCategory = getIntent().getStringExtra("editClassCategory");
        Spinner spinner = findViewById(R.id.spinnerTeacherEditClassCategory);
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
        RadioGroup radioGroup = findViewById(R.id.radioColorEditClass);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                String text = radioButton.getText().toString();
                edtClassColor.setColorFilter(Color.parseColor(text), PorterDuff.Mode.SRC_IN);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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