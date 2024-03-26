package com.example.teachly.Classes;

import java.util.Comparator;

public class ActivityDateComparator implements Comparator<Activity> {
    @Override
    public int compare(Activity o1, Activity o2) {
        return o1.getDueDate().compareTo(o2.getDueDate());
    }
}
