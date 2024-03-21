package com.example.teachly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClassPageStudent extends AppCompatActivity {

    TabLayout tabStudent;
    ViewPager2 viewPagerStudent;
    ImageView classShape;
    TextView className;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_page_student);

        MenuBar menuBar = new MenuBar(this);
        menuBar.setupActionBar();

        classShape = findViewById(R.id.class_shape);
        className = findViewById(R.id.class_title);
        className.setText(getIntent().getStringExtra("className"));
        classShape.setColorFilter(Color.parseColor(getIntent().getStringExtra("classColor")), PorterDuff.Mode.SRC_IN);

        String classDescription = getIntent().getStringExtra("classDescription");
        String classCategory = getIntent().getStringExtra("classCategory");
        String classId = getIntent().getStringExtra("classId");
        String classTeacherUId = getIntent().getStringExtra("classTeacherUId");
        String teacherName = getIntent().getStringExtra("teacherName");
        String teacherEmail = getIntent().getStringExtra("teacherEmail");
        String teacherPhone = getIntent().getStringExtra("teacherPhone");

        tabStudent = findViewById(R.id.tabLayoutStudent);
        viewPagerStudent = findViewById(R.id.viewPagerStudent);

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
                    loadClassPageStudent(activitiesList, classId, classDescription, classCategory, teacherName, teacherEmail, teacherPhone);
                }
                else {
                    loadClassPageStudent(null, classId, classDescription, classCategory, teacherName, teacherEmail, teacherPhone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error: " + error.getMessage());
            }
        });
    }

    public void loadClassPageStudent(ArrayList<Activity> listActivities, String classId, String classDescription, String classCategory, String teacherName, String teacherEmail, String teacherPhone){
        ViewPagerStudentAdapter pagerStudentAdapter = new ViewPagerStudentAdapter(this, listActivities, classDescription, classCategory, teacherName, teacherEmail, teacherPhone, classId);
        viewPagerStudent.setAdapter(pagerStudentAdapter);

        viewPagerStudent.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabStudent.getTabAt(position).select();
            }
        });

        tabStudent.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerStudent.setCurrentItem(tab.getPosition());
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