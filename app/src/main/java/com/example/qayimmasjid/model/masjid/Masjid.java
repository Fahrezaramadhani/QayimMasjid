package com.example.qayimmasjid.model.masjid;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

public class Masjid {

    @SerializedName("id_masjid")
    private String mid_masjid;

    @SerializedName("nama_masjid")
    private String mnamamasjid;

    @SerializedName("alamat_masjid")
    private String malamat;

    @SerializedName("tahun_berdiri")
    private String tahunBerdiri;

    @SerializedName("daya_tampung")
    private int dayaTampung;

    @SerializedName("longitude")
    private double mlongitude;

    @SerializedName("latitude")
    private double mlatidue;

    @SerializedName("foto_masjid")
    private String foto_masjid;

    @SerializedName("distance")
    private double distance;

    public Masjid(){

    }

    public Masjid(String id_masjid, String namamasjid, String alamat, String tahunBerdiri, int dayaTampung, double longitude, double latitude, String foto){
        this.setMid_masjid(id_masjid);
        this.setMnamamasjid(namamasjid);
        this.setMalamat(alamat);
        this.tahunBerdiri = tahunBerdiri;
        this.dayaTampung = dayaTampung;
        this.setMlongitude(longitude);
        this.setMlatidue(latitude);
        this.foto_masjid = foto;
    }

    public String getMalamat() {
        return malamat;
    }

    public double getMlatidue() {
        return mlatidue;
    }

    public String getMid_masjid() {
        return mid_masjid;
    }

    public double getMlongitude() {
        return mlongitude;
    }

    public String getMnamamasjid() {
        return mnamamasjid;
    }

    public String getTahunBerdiri() {
        return tahunBerdiri;
    }

    public void setTahunBerdiri(String tahunBerdiri) {
        this.tahunBerdiri = tahunBerdiri;
    }

    public int getDayaTampung() {
        return dayaTampung;
    }

    public void setDayaTampung(int dayaTampung) {
        this.dayaTampung = dayaTampung;
    }

    public String getFoto_masjid() {
        return foto_masjid;
    }

    public void setFoto_masjid(String foto_masjid) {
        this.foto_masjid = foto_masjid;
    }

    public void setMlongitude(double mlongitude) {
        this.mlongitude = mlongitude;
    }

    public void setMlatidue(double mlatidue) {
        this.mlatidue = mlatidue;
    }

    public void setMalamat(String malamat) {
        this.malamat = malamat;
    }

    public void setMid_masjid(String mid_masjid) {
        this.mid_masjid = mid_masjid;
    }

    public void setMnamamasjid(String mnamamasjid) {
        this.mnamamasjid = mnamamasjid;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
