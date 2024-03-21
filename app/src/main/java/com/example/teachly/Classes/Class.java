package com.example.teachly.Classes;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.teachly.HomeStudent;
import com.example.teachly.HomeTeacher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Class implements Serializable {

    private static final long serialVersionUID = 123456789L;
    private String classId;
    private String name;
    private String description;
    private String teacherUId;
    private String color;
    private EnumCategoryClass category;

    private ArrayList<String> students = new ArrayList<String>();

    private ArrayList<Activity> activities = new ArrayList<Activity>();

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

    public ArrayList<String> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<String> students) {
        this.students = students;
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }

    public static void createClassOnDatabase(Context context, String className, String classDescription, String uId, String colorOfClass, EnumCategoryClass category) {
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

                    Class.loadAllClassesByTeacherUId(context, uId);
                }
                else {
                    Class newClass = new Class("1", className,classDescription, uId, colorOfClass,category);

                    DatabaseReference databaseReferenceClasses = FirebaseDatabase.getInstance().getReference("classes");
                    databaseReferenceClasses.child(newClass.getClassId().toString()).setValue(newClass);
                    Class.loadAllClassesByTeacherUId(context, uId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static void loadAllClassesByTeacherUId(Context context, String uId) {
        DatabaseReference databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("classes");
        Query findClasses = databaseReferenceUsers.orderByChild("teacherUId").equalTo(uId);

        findClasses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<Class> listOfClasses = new ArrayList<>();
                    for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                        String classId = classSnapshot.child("classId").getValue(String.class);
                        String className = classSnapshot.child("name").getValue(String.class);
                        String classDesc = classSnapshot.child("description").getValue(String.class);
                        String category = classSnapshot.child("category").getValue(String.class);
                        String color = classSnapshot.child("color").getValue(String.class);

                        ArrayList<String> students = new ArrayList<>();
                        DataSnapshot studentsSnapshot = classSnapshot.child("students");
                        for (DataSnapshot studentSnapshot : studentsSnapshot.getChildren()) {
                            String studentId = studentSnapshot.getValue(String.class);
                            students.add(studentId);
                        }

                        ArrayList<Activity> activities1 = new ArrayList<>();
                        DataSnapshot activitiesSnapshot = classSnapshot.child("activities");
                        for (DataSnapshot activitySnapshot : activitiesSnapshot.getChildren()) {
                            Activity activity = activitySnapshot.getValue(Activity.class);
                            activities1.add(activity);
                        }

                        Class newClass = new Class(classId, className, classDesc, uId, color, EnumCategoryClass.valueOf(category));
                        newClass.setStudents(students);
                        newClass.setActivities(activities1);
                        listOfClasses.add(newClass);
                    }
                    HomeTeacher.loadAllClassesInList(context, listOfClasses);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void loadAllClassesByStudentUId(Context context, String uId) {
        DatabaseReference databaseReferenceClasses = FirebaseDatabase.getInstance().getReference("classes");
        //Query findClasses = databaseReferenceClasses.child("students").ge.orderByChild("students").equalTo(uId);

        databaseReferenceClasses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<Class> listOfClasses = new ArrayList<>();
                    Class newClass = new Class();
                    for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot studentSnapshot : classSnapshot.child("students").getChildren()) {
                            //checking if student is in class
                            String studentId = studentSnapshot.getValue(String.class);
                            if (studentId.equals(uId)) {
                                String classId = classSnapshot.child("classId").getValue(String.class);
                                String className = classSnapshot.child("name").getValue(String.class);
                                String classDesc = classSnapshot.child("description").getValue(String.class);
                                String classTeacher = classSnapshot.child("teacherUId").getValue(String.class);
                                String category = classSnapshot.child("category").getValue(String.class);
                                String color = classSnapshot.child("color").getValue(String.class);

                                newClass = new Class(classId,className,classDesc,classTeacher,color,EnumCategoryClass.valueOf(category));

                                //getting activities
                                ArrayList<Activity> activities1 = new ArrayList<>();
                                DataSnapshot activitiesSnapshot = classSnapshot.child("activities");
                                for (DataSnapshot activitySnapshot : activitiesSnapshot.getChildren()) {
                                    Activity activity = activitySnapshot.getValue(Activity.class);
                                    activities1.add(activity);
                                }
                                newClass.setActivities(activities1);
                                listOfClasses.add(newClass);
                            }
                        }
                    }
                    HomeStudent.loadAllClassesInList(context,listOfClasses);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
