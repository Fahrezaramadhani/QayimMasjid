package com.example.qayimmasjid.model.masjid;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PengumumanList {
    @SerializedName("listpengumuman")
    private ArrayList<Pengumuman> mpengumuman;
    @SerializedName("error")
    private String merror;

    public PengumumanList (){

    }

    public PengumumanList(ArrayList<Pengumuman> pengumuman, String error){

        mpengumuman=pengumuman;
        this.merror=error;
    }


    public String getMerror() {
        return merror;
    }
    public int getNumber(){
        return mpengumuman.size();
    }

    public ArrayList<Pengumuman> getpengumuman() {
        return mpengumuman;
    }
}
