package com.example.qayimmasjid.model.waktusholatapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateTime {
    @SerializedName("times")
    @Expose
    private Times times;
    @SerializedName("date")
    @Expose
    private Dates date;

    public Times getTimes() {
        return times;
    }

    public void setTimes(Times times) {
        this.times = times;
    }

    public Dates getDate() {
        return date;
    }

    public void setDate(Dates date) {
        this.date = date;
    }
}
