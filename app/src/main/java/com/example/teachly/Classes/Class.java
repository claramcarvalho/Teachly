package com.example.teachly.Classes;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.teachly.HomeTeacher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Class {
    private String classId;
    private String name;
    private String description;
    private String teacherUId;
    private String color;
    private EnumCategoryClass category;

    public static ArrayList<Class> listOfClasses;

    public Class() {
        // DO NOT DELETE
        //EMPTY CONSTRUCTOR FOR FIREBASE
    }
    public Class(String classId, String name, String description, String teacher, String color, EnumCategoryClass category) {
        this.classId = classId;
        this.name = name;
        this.description = description;
        this.teacherUId = teacher;
        this.color = color;
        this.category = category;
    }

    public Class(String name, String description, String teacher, String color, EnumCategoryClass category) {
        this.name = name;
        this.description = description;
        this.teacherUId = teacher;
        this.color = color;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeacherUId() {
        return teacherUId;
    }

    public void setTeacherUId(String teacherUId) {
        this.teacherUId = teacherUId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public EnumCategoryClass getCategory() {
        return category;
    }

    public void setCategory(EnumCategoryClass category) {
        this.category = category;
    }

    public String getClassId() {
        return classId;
    }

    public static void createClassOnDatabase(String className, String classDescription, String uId, String colorOfClass, EnumCategoryClass category) {
        DatabaseReference databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("classes");
        Query findClasses = databaseReferenceUsers.orderByChild("classId");

        findClasses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sequenceId = 0;
                if (snapshot.exists()) {
                    ArrayList<Integer> classIds = new ArrayList<>();
                    for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                        String classId = classSnapshot.child("classId").getValue(String.class);
                        Integer classIdInt = Integer.parseInt(classId);
                        classIds.add(classIdInt);
                    }

                    sequenceId = Collections.max(classIds);


                    Class newClass = new Class(String.valueOf(sequenceId +1), className,classDescription, uId, colorOfClass,category);

                    DatabaseReference databaseReferenceClasses = FirebaseDatabase.getInstance().getReference("classes");
                    databaseReferenceClasses.child(newClass.getClassId().toString()).setValue(newClass);

                    HomeTeacher.loadAllClassesByTeacherUId();
                }
                else {
                    Class newClass = new Class("1", className,classDescription, uId, colorOfClass,category);

                    DatabaseReference databaseReferenceClasses = FirebaseDatabase.getInstance().getReference("classes");
                    databaseReferenceClasses.child(newClass.getClassId().toString()).setValue(newClass);
                    HomeTeacher.loadAllClassesByTeacherUId();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}
