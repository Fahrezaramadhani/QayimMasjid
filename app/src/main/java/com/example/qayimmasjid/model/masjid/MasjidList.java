package com.example.qayimmasjid.model.masjid;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MasjidList {
    @SerializedName("listmasjid")
    private ArrayList<Masjid> mmasjid;
    @SerializedName("error")
    private String merror;

    public MasjidList(ArrayList<Masjid> masjid){
        setMmasjid(masjid);
    }

    public int getNumber(){
        return getMmasjid().size();
    }

    public ArrayList<Masjid> getMmasjid() {
        return mmasjid;
    }

    public void setMmasjid(ArrayList<Masjid> mmasjid) {
        this.mmasjid = mmasjid;
    }
}
