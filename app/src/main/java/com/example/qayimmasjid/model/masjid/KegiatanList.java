package com.example.qayimmasjid.model.masjid;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class KegiatanList {
    @SerializedName("listkegiatan")
    private ArrayList<Kegiatan> mkegiatan;
    @SerializedName("error")
    private String merror;

    public KegiatanList (){

    }

    public KegiatanList(ArrayList<Kegiatan> kegiatan, String error){

        mkegiatan=kegiatan;
        this.merror=error;
    }

    public String getMerror() {
        return merror;
    }

    public int getNumber(){
        return mkegiatan.size();
    }

    public ArrayList<Kegiatan> getkegiatan() {
        return mkegiatan;
    }
}
