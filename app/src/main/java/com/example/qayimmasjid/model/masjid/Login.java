package com.example.qayimmasjid.model.masjid;

import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("user")
    private Pengurus pengurus;

    @SerializedName("masjid")
    private Masjid masjid;

    public Pengurus getPengurus() {
        return pengurus;
    }

    public void setPengurus(Pengurus pengurus) {
        this.pengurus = pengurus;
    }

    public Masjid getMasjid() {
        return masjid;
    }

    public void setMasjid(Masjid masjid) {
        this.masjid = masjid;
    }
}
