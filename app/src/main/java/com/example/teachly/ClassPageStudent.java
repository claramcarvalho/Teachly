package com.example.teachly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

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

        ViewPagerStudentAdapter pagerStudentAdapter = new ViewPagerStudentAdapter(this, classDescription, classCategory, teacherName, teacherEmail, teacherPhone, classId);
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