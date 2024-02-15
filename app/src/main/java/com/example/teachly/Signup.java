package com.example.teachly;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Signup extends AppCompatActivity {

    ToggleButton togglePassword, toggleConfirmPassword;
    EditText edtEmail, edtPassword, edtConfirmPassword;
    Button signup;
    SharedPreferences sharedPreferences;
    RadioGroup accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtEmail = findViewById(R.id.edtEmail);
        togglePassword = findViewById(R.id.showPasswordToggleButton);
        toggleConfirmPassword = findViewById(R.id.showConfirmPasswordToggleButton);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        signup = findViewById(R.id.btnSignup);
        accountType = findViewById(R.id.accountType);

        togglePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Show Password
                    edtPassword.setTransformationMethod(null);
                    togglePassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_closed_eye_30,0);
                } else {
                    // Hide Password
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    togglePassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_eye_30,0);
                }
            }
        });

        toggleConfirmPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    edtConfirmPassword.setTransformationMethod(null);
                    toggleConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_closed_eye_30,0);
                } else {
                    edtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    toggleConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_eye_30,0);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///////////////////////////////////////////////////////////////////////////// Saving email, password and account type selected on session
                sharedPreferences = getSharedPreferences("Teachly", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                editor.putString("email", email);
                editor.putString("password", password);
                int type = accountType.getCheckedRadioButtonId();
                RadioButton selectedType = findViewById(type);
                String accountSelected = selectedType.getText().toString();
                editor.putString("type", accountSelected);
                editor.apply();

                AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                builder.setTitle("Account created");
                builder.setMessage("Welcome to TeachLy!");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Signup.this, Login.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Signup.this, "Cancelou", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setIcon(R.drawable.icon_check_30).setNeutralButton("OI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false);// Impede o fechamento do AlertDialog clicando fora dele
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}