package com.example.qayimmasjid.model.masjid;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProfileMasjid {
    @SerializedName("masjid")
    private Masjid masjid;

    @SerializedName("list_pengurus")
    private List<Pengurus> listPengurus;

    @SerializedName("error")
    private String merror;

    @SerializedName("foto_masjid")
    private Bitmap foto_masjid;

    public Masjid getMasjid() {
        return masjid;
    }

    public void setMasjid(Masjid masjid) {
        this.masjid = masjid;
    }

    public String getMerror() {
        return merror;
    }

    public void setMerror(String merror) {
        this.merror = merror;
    }

    public List<Pengurus> getListPengurus() {
        return listPengurus;
    }

    public void setListPengurus(List<Pengurus> listPengurus) {
        this.listPengurus = listPengurus;
    }

    public Bitmap getFoto_masjid() {
        return foto_masjid;
    }

    public void setFoto_masjid(Bitmap foto_masjid) {
        this.foto_masjid = foto_masjid;
    }
}
