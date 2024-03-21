package com.example.teachly.Classes;

import java.io.Serializable;
import java.time.LocalDate;

public class Activity implements Serializable {
    private static final long serialVersionUID = 123456798L;
    private String id;
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

    public Activity(String id, String name, String description, Long dueDate, EnumTypeActivity type) {
        this.id = id;
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

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
