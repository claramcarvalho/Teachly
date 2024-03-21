package com.example.teachly;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.teachly.Classes.Activity;

import java.util.ArrayList;

public class ViewPagerStudentAdapter extends FragmentStateAdapter {

    String classDescription, classCategory, teacherName, teacherEmail, teacherPhone, classId;
    ArrayList<Activity> listActivities;

    public ViewPagerStudentAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Activity> listActivities, String classDescription, String classCategory, String teacherName, String teacherEmail, String teacherPhone, String classId) {
        super(fragmentActivity);
        this.listActivities = listActivities;
        this.classDescription = classDescription;
        this.classCategory = classCategory;
        this.teacherName = teacherName;
        this.teacherEmail = teacherEmail;
        this.teacherPhone = teacherPhone;
        this.classId = classId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentStudentActivity(listActivities, classId);
            case 1:
                return new FragmentTeacherChat();
            case 2:
                return new FragmentStudentClassDetail(classDescription, classCategory, teacherName, teacherEmail, teacherPhone, classId);
        };
        return new FragmentTeacherActivity();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
