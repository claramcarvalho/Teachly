package com.example.teachly.Classes;

import java.time.LocalDate;

public class Activity {
    private String name;
    private String description;
    private Long dueDate;
    private EnumTypeActivity type;

    public Activity() {
        // DO NOT DELETE
        //EMPTY CONSTRUCTOR FOR FIREBASE
    }
    public Activity(String name, String description, Long dueDate, EnumTypeActivity type) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.type = type;
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

    public Long getDueDate() {
        return dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    public EnumTypeActivity getType() {
        return type;
    }

    public void setType(EnumTypeActivity type) {
        this.type = type;
    }

}
