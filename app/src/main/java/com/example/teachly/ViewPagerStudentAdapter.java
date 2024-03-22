package com.example.teachly;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.teachly.Classes.Class;
import com.example.teachly.Classes.User;

public class ViewPagerStudentAdapter extends FragmentStateAdapter {

    Class myClass;
    User myTeacher;

    public ViewPagerStudentAdapter(@NonNull FragmentActivity fragmentActivity, Class myClass, User myTeacher) {
        super(fragmentActivity);
        this.myClass = myClass;
        this.myTeacher = myTeacher;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentStudentActivity(myClass.getActivities(), myClass.getClassId());
            case 1:
                return new FragmentTeacherChat();
            case 2:
                return new FragmentStudentClassDetail(myClass, myTeacher);
        };
        return new FragmentTeacherActivity();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
