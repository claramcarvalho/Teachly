package com.example.teachly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

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

        classShape = findViewById(R.id.classShape);
        className = findViewById(R.id.classTitle);

        className.setText(getIntent().getStringExtra("className"));
        classShape.setColorFilter(Color.parseColor(getIntent().getStringExtra("classColor")), PorterDuff.Mode.SRC_IN);

        editClass = findViewById(R.id.editClass);

        editClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassPageTeacher.this, EditClass.class);
                intent.putExtra("editClassName", className.getText());
                intent.putExtra("editClassColor", getIntent().getStringExtra("classColor"));
                startActivity(intent);
            }
        });

        tabTeacher = findViewById(R.id.tabLayoutTeacher);
        viewPagerTeacher = findViewById(R.id.viewPagerTeacher);

        ViewPagerTeacherAdapter pagerTeacherAdapter = new ViewPagerTeacherAdapter(this);
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