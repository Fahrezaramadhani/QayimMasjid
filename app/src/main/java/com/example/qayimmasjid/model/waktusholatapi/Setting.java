package com.example.qayimmasjid.model.waktusholatapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Setting {
    @SerializedName("timeformat")
    @Expose
    private String timeformat;
    @SerializedName("school")
    @Expose
    private String school;
    @SerializedName("juristic")
    @Expose
    private String juristic;
    @SerializedName("highlat")
    @Expose
    private String highlat;
    @SerializedName("fajr_angle")
    @Expose
    private Double fajr_angle;
    @SerializedName("isha_angle")
    @Expose
    private Double isha_angle;

    public String getTimeformat() {
        return timeformat;
    }

    public void setTimeformat(String timeformat) {
        this.timeformat = timeformat;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getJuristic() {
        return juristic;
    }

    public void setJuristic(String juristic) {
        this.juristic = juristic;
    }

    public String getHighlat() {
        return highlat;
    }

    public void setHighlat(String highlat) {
        this.highlat = highlat;
    }

    public Double getFajr_angle() {
        return fajr_angle;
    }

    public void setFajr_angle(Double fajr_angle) {
        this.fajr_angle = fajr_angle;
    }

    public Double getIsha_angle() {
        return isha_angle;
    }

    public void setIsha_angle(Double isha_angle) {
        this.isha_angle = isha_angle;
    }
}
