package com.example.teachly;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.teachly.Classes.Activity;
import com.example.teachly.Classes.User;

import java.util.ArrayList;

public class ViewPagerTeacherAdapter extends FragmentStateAdapter {

    ArrayList<User> listStudents;
    ArrayList<Activity> listActivities;
    String classId;
    public ViewPagerTeacherAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<User> listStudents, ArrayList<Activity> listActivities, String classId) {
        super(fragmentActivity);
        this.listStudents = listStudents;
        this.listActivities = listActivities;
        this.classId = classId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentTeacherActivity(classId, listActivities);
            case 1:
                return new FragmentTeacherStudent(listStudents, classId);
            case 2:
                return new FragmentTeacherChat();
        };
        return new FragmentTeacherActivity(classId, listActivities);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
