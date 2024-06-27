package com.example.securityallocator.models;

public class TaskModel {
    private String taskID;
    private String locationName;
    private String locationUrl;
    private String sTime;
    private String eTime;
    private String date;
    private String contact;

    public TaskModel() {
    }

    public TaskModel(String taskID, String locationName, String locationUrl, String sTime, String eTime, String date, String contact) {
        this.taskID = taskID;
        this.locationName = locationName;
        this.locationUrl = locationUrl;
        this.sTime = sTime;
        this.eTime = eTime;
        this.date = date;
        this.contact = contact;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
