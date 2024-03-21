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
import com.example.teachly.Classes.Class;
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

        Class myClass = (Class) getIntent().getSerializableExtra("myClass");
        User myTeacher = (User) getIntent().getSerializableExtra("myTeacher");

        classShape = findViewById(R.id.class_shape);
        className = findViewById(R.id.class_title);
        className.setText(myClass.getName());
        classShape.setColorFilter(Color.parseColor(myClass.getColor()), PorterDuff.Mode.SRC_IN);

        tabStudent = findViewById(R.id.tabLayoutStudent);
        viewPagerStudent = findViewById(R.id.viewPagerStudent);

        loadClassPageStudent(myClass,myTeacher);
    }

    public void loadClassPageStudent(Class myClass, User myTeacher){
        ViewPagerStudentAdapter pagerStudentAdapter = new ViewPagerStudentAdapter(this, myClass, myTeacher);
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