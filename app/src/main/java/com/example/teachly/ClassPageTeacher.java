package com.example.teachly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teachly.Classes.Activity;
import com.example.teachly.Classes.EnumTypeActivity;
import com.example.teachly.Classes.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClassPageTeacher extends AppCompatActivity {

    TabLayout tabTeacher;
    ViewPager2 viewPagerTeacher;
    ImageView classShape, editClass;
    TextView className;
    String classId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_page_teacher);

        MenuBar menuBar = new MenuBar(this);
        menuBar.setupActionBar();

        classShape = findViewById(R.id.classShape);
        className = findViewById(R.id.classTitle);

        classId = getIntent().getStringExtra("classId");
        className.setText(getIntent().getStringExtra("className"));
        String classDescription = getIntent().getStringExtra("classDescription");
        String classCategory = getIntent().getStringExtra("classCategory");
        classShape.setColorFilter(Color.parseColor(getIntent().getStringExtra("classColor")), PorterDuff.Mode.SRC_IN);

        editClass = findViewById(R.id.editClass);

        editClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassPageTeacher.this, EditClass.class);
                intent.putExtra("editClassId", classId);
                intent.putExtra("editClassName", className.getText());
                intent.putExtra("editClassDescription", classDescription);
                intent.putExtra("editClassCategory", classCategory);
                intent.putExtra("editClassColor", getIntent().getStringExtra("classColor"));
                startActivity(intent);
            }
        });

        tabTeacher = findViewById(R.id.tabLayoutTeacher);
        viewPagerTeacher = findViewById(R.id.viewPagerTeacher);

        ///////////////////////////// GET ALL STUDENTS BY CLASS ID HERE
        ArrayList<User> listStudents = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("classes/"+classId+"/students");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<String> studentIds = new ArrayList<>();
                    for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
                        String studentId = studentSnapshot.getValue(String.class);
                        studentIds.add(studentId);
                    }

                    for (String item : studentIds){
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("users");
                        Query findStudents = databaseReference1.orderByChild("userId").equalTo(item);

                        findStudents.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    for (DataSnapshot userSnapshot : snapshot.getChildren()){
                                        String fullname = userSnapshot.child("fullName").getValue(String.class);
                                        String email = userSnapshot.child("email").getValue(String.class);
                                        String password = userSnapshot.child("password").getValue(String.class);
                                        String phoneNumber = userSnapshot.child("phoneNumber").getValue(String.class);
                                        String type = userSnapshot.child("type").getValue(String.class);
                                        User newUser = new User(item, email, password, fullname, phoneNumber, type);

                                        listStudents.add(newUser);
                                    }


                                    ///////////////////////////// GET ALL ACTIVITIES BY CLASS ID HERE
                                    DatabaseReference databaseReferenceAct = FirebaseDatabase.getInstance().getReference("classes/"+classId+"/activities");
                                    databaseReferenceAct.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                ArrayList<Activity> activitiesList = new ArrayList<>();
                                                for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                                                    String id = activitySnapshot.getKey();
                                                    String nameAct = activitySnapshot.child("name").getValue(String.class);
                                                    String descAct = activitySnapshot.child("description").getValue(String.class);
                                                    Long dueDateAct = activitySnapshot.child("dueDate").getValue(Long.class);
                                                    String typeAct = activitySnapshot.child("type").getValue(String.class);

                                                    Activity newActivity = new Activity(id, nameAct, descAct, dueDateAct, EnumTypeActivity.valueOf(typeAct));
                                                    activitiesList.add(newActivity);
                                                }
                                                loadClassPageTeacher(listStudents, activitiesList, classId);
                                            }
                                            else {
                                                loadClassPageTeacher(listStudents, null, classId);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            System.out.println("Error: " + error.getMessage());
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                else {
                    ///////////////////////////// GET ALL ACTIVITIES BY CLASS ID HERE
                    DatabaseReference databaseReferenceAct = FirebaseDatabase.getInstance().getReference("classes/"+classId+"/activities");
                    databaseReferenceAct.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                ArrayList<Activity> activitiesList = new ArrayList<Activity>();
                                for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                                    String id = activitySnapshot.getKey();
                                    String nameAct = activitySnapshot.child("name").getValue(String.class);
                                    String descAct = activitySnapshot.child("description").getValue(String.class);
                                    Long dueDateAct = activitySnapshot.child("dueDate").getValue(Long.class);
                                    String typeAct = activitySnapshot.child("type").getValue(String.class);

                                    Activity newActivity = new Activity(id, nameAct, descAct, dueDateAct, EnumTypeActivity.valueOf(typeAct));
                                    activitiesList.add(newActivity);
                                }
                                loadClassPageTeacher(null, activitiesList, classId);
                            }
                            else {
                                loadClassPageTeacher(null, null, classId);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            System.out.println("Error: " + error.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error: " + error.getMessage());
            }
        });
    }

    public void loadClassPageTeacher(ArrayList<User> listStudents, ArrayList<Activity> listActivities, String classId){
        ViewPagerTeacherAdapter pagerTeacherAdapter = new ViewPagerTeacherAdapter(this, listStudents, listActivities, classId);
        viewPagerTeacher.setAdapter(pagerTeacherAdapter);

        viewPagerTeacher.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabTeacher.getTabAt(position).select();
            }
        });

        tabTeacher.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerTeacher.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}