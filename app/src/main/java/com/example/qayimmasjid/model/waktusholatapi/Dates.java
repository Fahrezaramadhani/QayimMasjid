package com.example.qayimmasjid.model.waktusholatapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dates {
    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;
    @SerializedName("gregorian")
    @Expose
    private String gregorian;
    @SerializedName("hijri")
    @Expose
    private String hijri;

    public String getGregorian() {
        return gregorian;
    }

    public void setGregorian(String gregorian) {
        this.gregorian = gregorian;
    }

    public String getHijri() {
        return hijri;
    }

    public void setHijri(String hijri) {
        this.hijri = hijri;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }
}
