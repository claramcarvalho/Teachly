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
import com.example.teachly.Classes.Class;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_page_teacher);

        MenuBar menuBar = new MenuBar(this);
        menuBar.setupActionBar();

        Class myClass = (Class) getIntent().getSerializableExtra("myClass");

        classShape = findViewById(R.id.classShape);
        className = findViewById(R.id.classTitle);

        className.setText(myClass.getName());
        classShape.setColorFilter(Color.parseColor(myClass.getColor()), PorterDuff.Mode.SRC_IN);

        editClass = findViewById(R.id.editClass);

        editClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassPageTeacher.this, EditClass.class);

                intent.putExtra("myClass", myClass);

                startActivity(intent);
                finish();
            }
        });

        tabTeacher = findViewById(R.id.tabLayoutTeacher);
        viewPagerTeacher = findViewById(R.id.viewPagerTeacher);

        ///////////////////////////// GET ALL STUDENTS BY CLASS ID HERE
        ArrayList<User> listStudents = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("classes/"+myClass.getClassId()+"/students");
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

                                    loadClassPageTeacher(listStudents, myClass.getActivities(), myClass.getClassId());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                else {
                    loadClassPageTeacher(null, myClass.getActivities(), myClass.getClassId());
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