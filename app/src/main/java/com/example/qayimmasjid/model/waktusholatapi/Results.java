package com.example.qayimmasjid.model.waktusholatapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Results {
    @SerializedName("datetime")
    @Expose
    private List<DateTime> datetime = null;
    @SerializedName("location")
    @Expose
    private Locations location;
    @SerializedName("settings")
    @Expose
    private Setting settings;

    public Locations getLocation() {
        return location;
    }

    public void setLocation(Locations location) {
        this.location = location;
    }

    public Setting getSettings() {
        return settings;
    }

    public void setSettings(Setting settings) {
        this.settings = settings;
    }

    public List<DateTime> getDatetime() {
        return datetime;
    }

    public void setDatetime(List<DateTime> datetime) {
        this.datetime = datetime;
    }
}
