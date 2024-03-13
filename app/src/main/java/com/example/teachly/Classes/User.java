package com.example.teachly.Classes;

import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class User {

    private static Integer sequenceId = 1;
    private String userId;
    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private Uri photo;
    private String type;

    public User() {
        // DO NOT DELETE
        //EMPTY CONSTRUCTOR FOR FIREBASE
    }

    public User(String userId, String email, String password, String fullName, String phoneNumber, Uri photo, String type) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
        this.type = type;
    }

    public User(String email, String password, String fullName, String phoneNumber, String type) {
        setSequenceId();
        this.userId = String.valueOf(sequenceId);
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.photo = null;
        this.type = type;
    }

    // Getters e Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static void setSequenceId() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        //Query checkUserId = reference.orderByKey().limitToLast(1);
        Query checkUserId = reference.child("users");
        checkUserId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User last = snapshot.getValue(User.class);
                    User.sequenceId = Integer.parseInt(last.userId)+1;
                } else {
                    User.sequenceId = 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
