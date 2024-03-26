package com.example.teachly;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.teachly.Classes.Activity;
import com.example.teachly.Classes.Class;
import com.example.teachly.Classes.User;

import java.util.ArrayList;

public class ViewPagerTeacherAdapter extends FragmentStateAdapter {

    ArrayList<User> listStudents;
    Class myClass;
    public ViewPagerTeacherAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<User> listStudents, Class myClass) {
        super(fragmentActivity);
        this.listStudents = listStudents;
        this.myClass = myClass;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentTeacherActivity(myClass.getClassId(), myClass.getActivities());
            case 1:
                return new FragmentTeacherStudent(listStudents, myClass.getClassId());
            case 2:
                return new FragmentGroupChat(myClass);
        };
        return new FragmentTeacherActivity(myClass.getClassId(), myClass.getActivities());
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
