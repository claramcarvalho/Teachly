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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditClass extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);

        EditText className = findViewById(R.id.edtEditClassName);
        className.setText(getIntent().getStringExtra("editClassName"));


        EditText description = findViewById(R.id.edtEditClassDescription);
        description.setText("This is the description of the class");

        ////// CATEGORIA
        Spinner spinner = findViewById(R.id.spinnerTeacherEditClassCategory);
        List<String> categories = new ArrayList<String>();
        categories.add("French");
        categories.add("Math");
        categories.add("Tefaq");
        categories.add("C#");
        categories.add("IELTS");
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(EditClass.this, android.R.layout.simple_spinner_item, categories);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(EditClass.this);
        spinner.setSelection(3);

        ///////SETTING ORIGINAL COLOR
        ImageView classColor = findViewById(R.id.editClassColor);
        classColor.setColorFilter(Color.parseColor(getIntent().getStringExtra("editClassColor")), PorterDuff.Mode.SRC_IN);

        /////CHANGING COLOR IF RADIOBUTTON IS SELECTED
        RadioGroup radioGroup = findViewById(R.id.radioColorEditClass);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                String text = radioButton.getText().toString();
                classColor.setColorFilter(Color.parseColor(text), PorterDuff.Mode.SRC_IN);
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