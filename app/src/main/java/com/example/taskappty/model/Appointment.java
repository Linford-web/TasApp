package com.example.taskappty.model;

public class Appointment  {

    private String timeSlot, selectedTimeSlot, appointmentDate, creatorId, teacherName, teacherEmail, confirmingUserId;

    public Appointment(String timeSlot, String selectedTimeSlot, String appointmentDate, String creatorId, String teacherName, String teacherEmail, String confirmingUserId) {
        this.timeSlot = timeSlot;
        this.selectedTimeSlot = selectedTimeSlot;
        this.appointmentDate = appointmentDate;
        this.creatorId = creatorId;
        this.teacherName = teacherName;
        this.teacherEmail = teacherEmail;
        this.confirmingUserId = confirmingUserId;
    }

    public Appointment() {
    }


    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getSelectedTimeSlot() {
        return selectedTimeSlot;
    }

    public void setSelectedTimeSlot(String selectedTimeSlot) {
        this.selectedTimeSlot = selectedTimeSlot;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
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

    public String getConfirmingUserId() {
        return confirmingUserId;
    }

    public void setConfirmingUserId(String confirmingUserId) {
        this.confirmingUserId = confirmingUserId;
    }
}
