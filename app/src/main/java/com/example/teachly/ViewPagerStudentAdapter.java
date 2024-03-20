package com.example.teachly;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerStudentAdapter extends FragmentStateAdapter {

    String classDescription, classCategory, teacherName, teacherEmail, teacherPhone, classId;

    public ViewPagerStudentAdapter(@NonNull FragmentActivity fragmentActivity, String classDescription, String classCategory, String teacherName, String teacherEmail, String teacherPhone, String classId) {
        super(fragmentActivity);
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
                return new FragmentStudentActivity();
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
