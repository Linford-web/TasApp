package com.example.taskappty.model;

public class Appointment  {

    private String selectedTimeSlot, appointmentDate, creatorId, teacherName, teacherEmail, confirmingUserId, appointmentId;

    public Appointment( String selectedTimeSlot, String appointmentDate, String creatorId, String teacherName, String teacherEmail, String confirmingUserId) {
        this.selectedTimeSlot = selectedTimeSlot;
        this.appointmentDate = appointmentDate;
        this.creatorId = creatorId;
        this.teacherName = teacherName;
        this.teacherEmail = teacherEmail;
        this.confirmingUserId = confirmingUserId;
    }

    public Appointment(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Appointment() {
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
