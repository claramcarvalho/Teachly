package com.example.teachly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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

public class EditProfile extends AppCompatActivity {
    TextView btnDeleteAccount;
    private FirebaseAuth mAuth;
    Button btnChangeEmail;
    EditText edtEmailOnProfileEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        mAuth = FirebaseAuth.getInstance();
        btnChangeEmail = findViewById(R.id.btnChangeEmail);
        edtEmailOnProfileEdit = findViewById(R.id.edtEmailOnProfileEdit);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        MenuBar menuBar = new MenuBar(this);
        menuBar.setupActionBar();
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
                builder.setTitle("Delete Account");
                builder.setMessage("Are you sure you want to delete your account?\nThis action cannot be undone!");
                builder.setPositiveButton("Delete Account", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
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

                                } else {
                                    Toast.makeText(EditProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
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

        /*btnChangeEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                currentUser.updateEmail(edtEmailOnProfileEdit.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(EditProfile.this, "Deu certo", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Exception exception = task.getException();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
                                    builder.setTitle("Account deleted");
                                    builder.setMessage(exception.getMessage());
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
                                }
                            }
                        });
            }
        });*/
    }
}