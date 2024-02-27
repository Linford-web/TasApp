package com.example.taskappty.model;
import android.os.Parcel;
import android.os.Parcelable;

public class AppointmentModel implements Parcelable{

    String  dayOfWeek, teacherEmail,teacherName, appointmentId, startTime, endTime;

    public AppointmentModel(){

    }

    public AppointmentModel(String teacherName, String dayOfWeek, String teacherEmail, String startTime, String endTime) {
        this.appointmentId = appointmentId;
        this.teacherName = teacherName;
        this.dayOfWeek = dayOfWeek;
        this.teacherEmail = teacherEmail;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public AppointmentModel(String teacherName, String teacherEmail, String dayOfWeek, String timeSpan, String appointmentId, String startTime, String endTime) {
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    protected AppointmentModel(Parcel in) {
        teacherName = in.readString();
        dayOfWeek = in.readString();
        teacherEmail = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        appointmentId = in.readString();
    }

    public static final Creator<AppointmentModel> CREATOR = new Creator<AppointmentModel>() {
        @Override
        public AppointmentModel createFromParcel(Parcel in) {
            return new AppointmentModel(in);
        }

        @Override
        public AppointmentModel[] newArray(int size) {
            return new AppointmentModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(teacherName);
        dest.writeString(dayOfWeek);
        dest.writeString(teacherEmail);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(appointmentId);
    }
}
