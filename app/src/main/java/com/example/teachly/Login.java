package com.example.teachly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

public class Login extends AppCompatActivity {

    ToggleButton togglePassword;
    EditText edtPassword, edtEmail;
    Button login;
    TextView signup, loginForgetPassword;
    SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = getSharedPreferences("Teachly", Context.MODE_PRIVATE);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            if (sharedPreferences.getString("type", "").equals("Student")){
                Intent intent = new Intent(Login.this, HomeStudent.class);
                startActivity(intent);
                finish();
            }
            if (sharedPreferences.getString("type", "").equals("Teacher")){
                Intent intent = new Intent(Login.this, HomeTeacher.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        togglePassword = findViewById(R.id.showPasswordToggleButton);
        edtPassword = findViewById(R.id.edtPassword);
        login = findViewById(R.id.btnLogin);
        signup = findViewById(R.id.btnSignup);
        loginForgetPassword = findViewById(R.id.loginForgetPassword);
        mAuth = FirebaseAuth.getInstance();
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

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        loginForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailResetPassword();
            }
        });
    }

    private void sendEmailResetPassword(){
        String email = edtEmail.getText().toString().trim();
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Please enter a valid email address");
            edtEmail.requestFocus();
            return;
        }
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setTitle("E-mail sent!");
                    builder.setMessage("An e-mail has been sent to " + email + ", in order to change your password");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setIcon(R.drawable.icon_check_30);
                    builder.setCancelable(false);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    Toast.makeText(Login.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser(){
        String email = edtEmail.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "All the field should be filled!!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", email);
                    editor.putString("password", pass);

                    if (email.equals("student@teachly.com")) {
                        editor.putString("type", "Student");
                    }
                    else if (email.equals("teacher@teachly.com")){
                        editor.putString("type", "Teacher");
                    }
                    editor.apply();

                    Toast.makeText(Login.this, "Login Success :)", Toast.LENGTH_SHORT).show();
                    if (sharedPreferences.getString("type", "").equals("Student")){
                        Intent intent = new Intent(Login.this, HomeStudent.class);
                        startActivity(intent);
                        finish();
                    }
                    if (sharedPreferences.getString("type", "").equals("Teacher")){
                        Intent intent = new Intent(Login.this, HomeTeacher.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(Login.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}