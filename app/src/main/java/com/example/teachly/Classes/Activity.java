package com.example.teachly.Classes;

import java.time.LocalDate;

public class Activity {

    private static Integer sequenceId = 1;
    private Integer activityId;
    private Class course;
    private String name;
    private String description;
    private LocalDate dueDate;
    private EnumTypeActivity type;

    public Activity() {
        // DO NOT DELETE
        //EMPTY CONSTRUCTOR FOR FIREBASE
    }
    public Activity(Integer activityId, Class course, String name, String description, LocalDate dueDate, EnumTypeActivity type) {
        this.activityId = activityId;
        this.course = course;
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.type = type;
    }

    public Activity(Class course, String name, String description, LocalDate dueDate, EnumTypeActivity type) {
        this.activityId = sequenceId;
        sequenceId++;
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

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }
}
