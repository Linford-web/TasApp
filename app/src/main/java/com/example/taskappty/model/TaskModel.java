package com.example.taskappty.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TaskModel implements Parcelable {

    String taskId, taskName, taskStatus, userId, taskDescription, doneTaskUserId;

    public TaskModel(String doneTaskUserId) {
        this.doneTaskUserId = doneTaskUserId;
    }

    public String getDoneTaskUserId() {
        return doneTaskUserId;
    }

    public void setDoneTaskUserId(String doneTaskUserId) {
        this.doneTaskUserId = doneTaskUserId;
    }

    public TaskModel(){

    }

    public TaskModel(String taskId, String taskName, String taskStatus, String userId, String taskDescription) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.userId = userId;
        this.taskDescription = taskDescription;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    protected TaskModel(Parcel in) {
        taskId = in.readString();
        taskName = in.readString();
        taskStatus = in.readString();
        userId = in.readString();
        taskDescription = in.readString();
        doneTaskUserId = in.readString();
    }

    public static final Creator<TaskModel> CREATOR = new Creator<TaskModel>() {
        @Override
        public TaskModel createFromParcel(Parcel in) {
            return new TaskModel(in);
        }

        @Override
        public TaskModel[] newArray(int size) {
            return new TaskModel[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(taskId);
        dest.writeString(taskName);
        dest.writeString(taskStatus);
        dest.writeString(userId);
        dest.writeString(taskDescription);
        dest.writeString(doneTaskUserId);
    }
}
