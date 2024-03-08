package com.example.taskappty.model;

public class Appointment {

    private String date, time, id, teacherName, teacherEmail;

    public Appointment(String date, String time, String id, String teacherName, String teacherEmail) {
        this.date = date;
        this.time = time;
        this.id = id;
        this.teacherName = teacherName;
        this.teacherEmail = teacherEmail;
    }
    public Appointment() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }
}
