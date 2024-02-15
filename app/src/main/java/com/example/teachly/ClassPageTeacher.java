package com.example.teachly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class ClassPageTeacher extends AppCompatActivity {

    TabLayout tabTeacher;
    ViewPager2 viewPagerTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_page_teacher);

        MenuBar menuBar = new MenuBar(this);
        menuBar.setupActionBar();

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