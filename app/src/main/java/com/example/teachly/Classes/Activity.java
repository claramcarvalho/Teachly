package com.example.teachly.Classes;

import java.time.LocalDate;

public class Activity {
    private Class course;
    private String name;
    private String description;
    private LocalDate dueDate;
    private EnumTypeActivity type;

    public Activity() {
        // DO NOT DELETE
        //EMPTY CONSTRUCTOR FOR FIREBASE
    }
    public Activity(Class course, String name, String description, LocalDate dueDate, EnumTypeActivity type) {
        this.course = course;
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.type = type;
    }

    public Class getCourse() {
        return course;
    }

    public void setCourse(Class course) {
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public EnumTypeActivity getType() {
        return type;
    }

    public void setType(EnumTypeActivity type) {
        this.type = type;
    }
}
