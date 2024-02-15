package com.example.teachly;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerStudentAdapter extends FragmentStateAdapter {
    public ViewPagerStudentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
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
                return new FragmentStudentClassDetail();
        };
        return new FragmentTeacherActivity();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
