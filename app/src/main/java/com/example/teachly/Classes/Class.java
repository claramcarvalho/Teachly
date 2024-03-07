package com.example.teachly.Classes;

public class Class {
    private String name;
    private String description;
    private User teacher;
    private String color;
    private EnumCategoryClass category;

    public Class() {
        // DO NOT DELETE
        //EMPTY CONSTRUCTOR FOR FIREBASE
    }
    public Class(String name, String description, User teacher, String color, EnumCategoryClass category) {
        this.name = name;
        this.description = description;
        this.teacher = teacher;
        this.color = color;
        this.category = category;
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

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public EnumCategoryClass getCategory() {
        return category;
    }

    public void setCategory(EnumCategoryClass category) {
        this.category = category;
    }
}
