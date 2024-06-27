package com.example.securityallocator.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ReportModel implements Parcelable {
    private String userID;
    private String username;
    private String userEmail;
    private String profileImg;
    private String report;

    public ReportModel() {
    }

    public ReportModel(String userID, String username, String userEmail, String profileImg, String report) {
        this.userID = userID;
        this.username = username;
        this.userEmail = userEmail;
        this.profileImg = profileImg;
        this.report = report;
    }

    protected ReportModel(Parcel in) {
        userID = in.readString();
        username = in.readString();
        userEmail = in.readString();
        profileImg = in.readString();
        report = in.readString();
    }

    public static final Creator<ReportModel> CREATOR = new Creator<ReportModel>() {
        @Override
        public ReportModel createFromParcel(Parcel in) {
            return new ReportModel(in);
        }

        @Override
        public ReportModel[] newArray(int size) {
            return new ReportModel[size];
        }
    };

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "ReportModel{" +
                "userID='" + userID + '\'' +
                ", username='" + username + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", profileImg='" + profileImg + '\'' +
                ", report='" + report + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(username);
        dest.writeString(userEmail);
        dest.writeString(profileImg);
        dest.writeString(report);
    }
}
