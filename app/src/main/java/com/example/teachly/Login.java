package com.example.teachly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Login extends AppCompatActivity {

    ToggleButton togglePassword;
    EditText edtPassword, edtEmail;
    Button login;
    TextView signup;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        togglePassword = findViewById(R.id.showPasswordToggleButton);
        edtPassword = findViewById(R.id.edtPassword);
        login = findViewById(R.id.btnLogin);
        signup = findViewById(R.id.btnSignup);

        sharedPreferences = getSharedPreferences("Teachly", Context.MODE_PRIVATE);

        // Verifica se o usuário já fez login anteriormente
        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");
        // Testando se tem sessão ativa
        Toast.makeText(this, savedEmail, Toast.LENGTH_SHORT).show();

        if (!TextUtils.isEmpty(savedEmail)) {
            // O usuário já fez login anteriormente, você pode prosseguir diretamente para a próxima tela
            // ou pode preencher os campos de email e senha com as informações salvas
            edtEmail.setText(savedEmail);
        }

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
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                /////////////////////////////////////////////////// Adicionar Regex e select user
                if (email.length() != 1){
                    Toast.makeText(Login.this, "Enter email 't' to Teacher and 's' to Student", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*if (password.length() < 1){
                    return;
                }*/
                //////////////////////////////////////////////////////

                ////////////////////////////////////////////////////// Alterar pra salvar na sessão user object
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", email);
                editor.putString("password", password);

                ////////////////////////////////////////////////////// Testando apenas pra funcionar agora quando formos logar, escolher tipo de user
                if (email.equals("s")) {
                    editor.putString("type", "Student");
                }
                else if (email.equals("t")){
                    editor.putString("type", "Teacher");
                }
                editor.apply();
                //////////////////////////////////////////////////////

                //////////////////////////////////////////////////////
                if (sharedPreferences.getString("type", "").equals("Student")){
                    Intent intent = new Intent(Login.this, HomeStudent.class);
                    startActivity(intent);
                }
                if (sharedPreferences.getString("type", "").equals("Teacher")){
                    Intent intent = new Intent(Login.this, HomeTeacher.class);
                    startActivity(intent);
                }
                //////////////////////////////////////////////////////
            }
        });



    }
}