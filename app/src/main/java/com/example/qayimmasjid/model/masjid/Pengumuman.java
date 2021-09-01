package com.example.qayimmasjid.model.masjid;

import com.google.gson.annotations.SerializedName;

public class Pengumuman {
    @SerializedName("id_pengumuman")
    private String midpengumuman;

    @SerializedName("id_masjid")
    private String idMasjid;

    @SerializedName("nama_pengumuman")
    private String mnamapengumuman;

    @SerializedName("tanggal_pengumuman")
    private String tanggalPengumuman;

    @SerializedName("waktu_pengumuman")
    private String waktuPengumuman;

    @SerializedName("deskripsi")
    private String deskripsiPengumuman;

    @SerializedName("contact_person")
    private String contactPerson;

    public Pengumuman (){

    }


    public Pengumuman(String idpengumuman, String idMasjid, String namaPengumuman, String tanggalPengumuman, String waktuPengumuman,
                      String deskripsiPengumuman, String contactPerson)
    {
        this.setMidpengumuman(idpengumuman);
        this.setIdMasjid(idMasjid);
        this.setMnamapengumuman(namaPengumuman);
        this.setTanggalPengumuman(tanggalPengumuman);
        this.setWaktuPengumuman(waktuPengumuman);
        this.setDeskripsiPengumuman(deskripsiPengumuman);
        this.setContactPerson(contactPerson);
    }

    public String getMidpengumuman() {
        return midpengumuman;
    }

    public void setMidpengumuman(String midpengumuman) {
        this.midpengumuman = midpengumuman;
    }

    public String getIdMasjid() {
        return idMasjid;
    }

    public void setIdMasjid(String idMasjid) {
        this.idMasjid = idMasjid;
    }

    public String getMnamapengumuman() {
        return mnamapengumuman;
    }

    public void setMnamapengumuman(String mnamapengumuman) {
        this.mnamapengumuman = mnamapengumuman;
    }

    public String getTanggalPengumuman() {
        return tanggalPengumuman;
    }

    public void setTanggalPengumuman(String tanggalPengumuman) {
        this.tanggalPengumuman = tanggalPengumuman;
    }

    public String getWaktuPengumuman() {
        return waktuPengumuman;
    }

    public void setWaktuPengumuman(String waktuPengumuman) {
        this.waktuPengumuman = waktuPengumuman;
    }

    public String getDeskripsiPengumuman() {
        return deskripsiPengumuman;
    }

    public void setDeskripsiPengumuman(String deskripsiPengumuman) {
        this.deskripsiPengumuman = deskripsiPengumuman;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
}
