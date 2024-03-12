package com.example.teachly.Classes;

import java.time.LocalDate;

public class StudentEnrollment {

    private static Integer sequenceId = 1;
    private Integer studentEnrollmentId;
    private User student;
    private Class course;
    private LocalDate enrollmentDate;

    public StudentEnrollment() {
        // DO NOT DELETE
        //EMPTY CONSTRUCTOR FOR FIREBASE
    }
    public StudentEnrollment(Integer studentEnrollmentId, User student, Class course, LocalDate enrollmentDate) {
        this.studentEnrollmentId = studentEnrollmentId;
        this.student = student;
        this.course = course;
        this.enrollmentDate = enrollmentDate;
    }

    public StudentEnrollment(User student, Class course, LocalDate enrollmentDate) {
        this.studentEnrollmentId = sequenceId;
        sequenceId++;
        this.student = student;
        this.course = course;
        this.enrollmentDate = enrollmentDate;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Class getCourse() {
        return course;
    }

    public void setCourse(Class course) {
        this.course = course;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Integer getStudentEnrollmentId() {
        return studentEnrollmentId;
    }

    public void setStudentEnrollmentId(Integer studentEnrollmentId) {
        this.studentEnrollmentId = studentEnrollmentId;
    }


}
