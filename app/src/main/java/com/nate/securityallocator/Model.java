package com.nate.securityallocator;

public class Model {
    private String location;
    private String longitude;
    private String latitude;
    private String tel;
    private String startTime;
    private String EndTime;


    public Model() {
    }

    public Model(String location, String longitude, String latitude, String tel, String startTime, String endTime) {
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.tel = tel;
        this.startTime = startTime;
        EndTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }
}
