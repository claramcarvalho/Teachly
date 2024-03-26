package com.example.teachly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {
    TextView btnDeleteAccount, btnSaveAccount;
    private FirebaseAuth mAuth;
    EditText edtFullName, edtPhone, edtEmail;
    String fullName, phone, email, uId, typeUser;

    FirebaseDatabase database;
    DatabaseReference referenceUsers, referenceClasses;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnSaveAccount = findViewById(R.id.btnSaveEditProfile);
        edtFullName = findViewById(R.id.edtNameOnProfileEdit);
        edtPhone = findViewById(R.id.edtTelNumberOnProfileEdit);
        edtEmail = findViewById(R.id.edtEmailOnProfileEdit);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        referenceUsers = database.getReference("users");
        referenceClasses = database.getReference("classes");
        sharedPreferences = getSharedPreferences("Teachly", Context.MODE_PRIVATE);
        uId = sharedPreferences.getString("uId", "");
        typeUser = getIntent().getStringExtra("typeUser");

        FirebaseUser currentUser = mAuth.getCurrentUser();

        MenuBar menuBar = new MenuBar(this);
        menuBar.setupActionBar();

        showUserData();
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
                builder.setTitle("Delete Account");
                builder.setMessage("Are you sure you want to delete your account?\nThis action cannot be undone!");
                builder.setPositiveButton("Delete Account", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (typeUser.equals("Student")) {
                            deleteStudent(currentUser);
                        } else {
                            deleteTeacher(currentUser);
                        }
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

        btnSaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNameChanged()) {
                    Toast.makeText(EditProfile.this, "Full Name was updated!", Toast.LENGTH_SHORT).show();
                }
                if (isPhoneChanged()) {
                    Toast.makeText(EditProfile.this, "Phone was updated!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteTeacher(FirebaseUser currentUser) {
        DatabaseReference referenceClasses = FirebaseDatabase.getInstance().getReference("classes");

        referenceClasses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean teacherHasClass = false;
                if (snapshot.exists()) {
                    for (DataSnapshot snapshotClasses : snapshot.getChildren()) {
                        String teacher = snapshotClasses.child("teacherUId").getValue(String.class);
                        if (teacher.equals(uId)) {
                            teacherHasClass = true;
                        }
                    }

                    if (teacherHasClass) {
                        Toast.makeText(EditProfile.this, "Delete ALL classes before removing your account!", Toast.LENGTH_SHORT).show();
                    } else {
                        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Query findUser = referenceUsers.orderByChild("userId").equalTo(uId);

                                    findUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                snapshot.child(uId).getRef().removeValue();

                                                removeFromCometChat(uId);

                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.remove("uId");
                                                editor.remove("type");
                                                editor.apply();

                                                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
                                                builder.setTitle("Account deleted");
                                                builder.setMessage("Thanks for using Teachly!\nCome back soon!");

                                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(EditProfile.this, Login.class);
                                                        startActivity(intent);
                                                        finish();
                                                        dialog.dismiss();
                                                    }
                                                });
                                                builder.setIcon(R.drawable.icon_check_30);
                                                builder.setCancelable(false);
                                                AlertDialog dialog = builder.create();
                                                dialog.show();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(EditProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteStudent(FirebaseUser currentUser) {
        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Query findUser = referenceUsers.orderByChild("userId").equalTo(uId);

                    findUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                snapshot.child(uId).getRef().removeValue();

                                removeFromCometChat(uId);
                                removeFromClass(uId);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("uId");
                                editor.remove("type");
                                editor.apply();

                                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
                                builder.setTitle("Account deleted");
                                builder.setMessage("Thanks for using Teachly!\nCome back soon!");

                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(EditProfile.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                        dialog.dismiss();
                                    }
                                });
                                builder.setIcon(R.drawable.icon_check_30);
                                builder.setCancelable(false);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(EditProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showUserData () {
        Query findUser = referenceUsers.orderByChild("userId").equalTo(uId);

        findUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    fullName = snapshot.child(uId).child("fullName").getValue(String.class);
                    email = snapshot.child(uId).child("email").getValue(String.class);
                    phone = snapshot.child(uId).child("phoneNumber").getValue(String.class);

                    edtFullName.setText(fullName);
                    edtEmail.setText(email);
                    edtPhone.setText(phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isNameChanged (){
        if (!fullName.equals(edtFullName.getText().toString().trim())) {
            referenceUsers.child(uId).child("fullName").setValue(edtFullName.getText().toString().trim());
            fullName = edtFullName.getText().toString().trim();
            referenceClasses.orderByChild("teacher/userId").equalTo(uId);


            return true;
        }
        return false;
    }

    private boolean isPhoneChanged (){
        if (!phone.equals(edtPhone.getText().toString().trim())) {
            referenceUsers.child(uId).child("phoneNumber").setValue(edtPhone.getText().toString().trim());
            phone = edtPhone.getText().toString().trim();
            return true;
        }
        return false;
    }

    private void removeFromCometChat(String uId){


    }


    private void removeFromClass(String uId){
        DatabaseReference referenceClasses = FirebaseDatabase.getInstance().getReference("classes");
        referenceClasses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        DataSnapshot snapshotStudents = dataSnapshot.child("students");
                        for (DataSnapshot student : snapshotStudents.getChildren()) {
                            if (student.getValue(String.class).equals(uId)){
                                student.getRef().removeValue();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}