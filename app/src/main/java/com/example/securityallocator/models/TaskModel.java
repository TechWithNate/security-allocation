package com.example.securityallocator.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TaskModel implements Parcelable {
    private String taskID;
    private String userID;
    private String locationName;
    private String locationUrl;
    private String shift;
    private String sTime;
    private String eTime;
    private String sDate;
    private String eDate;
    private String contact;


    public TaskModel() {
    }

    public TaskModel(String taskID, String userID, String locationName, String locationUrl, String shift, String sTime, String eTime, String sDate, String eDate, String contact) {
        this.taskID = taskID;
        this.userID = userID;
        this.locationName = locationName;
        this.locationUrl = locationUrl;
        this.shift = shift;
        this.sTime = sTime;
        this.eTime = eTime;
        this.sDate = sDate;
        this.eDate = eDate;
        this.contact = contact;
    }

    protected TaskModel(Parcel in) {
        taskID = in.readString();
        userID = in.readString();
        locationName = in.readString();
        locationUrl = in.readString();
        shift = in.readString();
        sTime = in.readString();
        eTime = in.readString();
        sDate = in.readString();
        eDate = in.readString();
        contact = in.readString();
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

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationUrl() {
        return locationUrl;
    }

    public void setLocationUrl(String locationUrl) {
        this.locationUrl = locationUrl;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String geteTime() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public String geteDate() {
        return eDate;
    }

    public void seteDate(String eDate) {
        this.eDate = eDate;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(taskID);
        dest.writeString(userID);
        dest.writeString(locationName);
        dest.writeString(locationUrl);
        dest.writeString(shift);
        dest.writeString(sTime);
        dest.writeString(eTime);
        dest.writeString(sDate);
        dest.writeString(eDate);
        dest.writeString(contact);
    }
}
