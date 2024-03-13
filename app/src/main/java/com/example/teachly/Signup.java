package com.example.teachly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.teachly.Classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    ToggleButton togglePassword, toggleConfirmPassword;
    EditText edtFullName, edtEmail, edtPhone, edtPassword, edtConfirmPassword;
    Button signup, goToLoginBtn;
    SharedPreferences sharedPreferences;
    RadioGroup accountType;
    private FirebaseAuth mAuth;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        togglePassword = findViewById(R.id.showPasswordToggleButton);
        toggleConfirmPassword = findViewById(R.id.showConfirmPasswordToggleButton);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        signup = findViewById(R.id.btnSignup);
        accountType = findViewById(R.id.accountType);
        mAuth = FirebaseAuth.getInstance();
        goToLoginBtn = findViewById(R.id.goToLogin);

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

        goToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser(){
        int type = accountType.getCheckedRadioButtonId();
        RadioButton selectedType = findViewById(type);
        String accountSelected = selectedType.getText().toString();
        String name = edtFullName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()){
            Toast.makeText(this, "All the field should be filled!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length() < 8){
            edtPassword.setError("Password must have at least 8 characters");
            edtPassword.requestFocus();
            return;
        }
        if(!password.equals(confirmPassword)){
            edtConfirmPassword.setError("The passwords do not match");
            edtConfirmPassword.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Please enter a valid email address");
            edtEmail.requestFocus();
            return;
        }

        if(!Patterns.PHONE.matcher(phone).matches()){
            edtPhone.setError("Please enter a valid phone number");
            edtPhone.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    createUser(name, email, phone, password, accountSelected);     //Criar usuario completo no real time database

                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                    builder.setTitle("Account created");
                    builder.setMessage("Welcome to Teachly!");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Signup.this, Login.class);
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
                    Toast.makeText(Signup.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createUser(String name, String email, String phone, String password, String type){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        FirebaseUser currentUser = mAuth.getCurrentUser();

        User user = new User(currentUser.getUid(),email, password, name, phone, type);

        reference.child(user.getUserId()).setValue(user);
    }
}