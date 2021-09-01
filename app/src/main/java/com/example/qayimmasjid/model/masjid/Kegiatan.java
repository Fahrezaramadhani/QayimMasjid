package com.example.qayimmasjid.model.masjid;

import com.google.gson.annotations.SerializedName;

public class Kegiatan {
    @SerializedName("id_kegiatan")
    private String midkegiatan;

    @SerializedName("id_masjid")
    private String midmasjid;

    @SerializedName("nama_kegiatan")
    private String mnamakegiatan;

    @SerializedName("tanggal_kegiatan")
    private String tanggalKegiatan;

    @SerializedName("waktu_kegiatan")
    private String mwaktukegiatan;

    @SerializedName("lokasi_kegiatan")
    private String mlokasikegiatan;

    @SerializedName("pemateri")
    private String mpemateri;

    @SerializedName("pj_kegiatan")
    private String mpenanggungjawab;

    @SerializedName("tempat_kegiatan")
    private String tempatKegiatan;

    public Kegiatan (){

    }

    public Kegiatan(String idkegiatan, String namakegiatan, String tanggalKegiatan, String waktukegiatan, String lokasikegiatan, String pemateri, String penanggungjawab, String tempatKegiatan){
        this.setMidkegiatan(idkegiatan);
        this.setMnamakegiatan(namakegiatan);
        this.setTanggalKegiatan(tanggalKegiatan);
        this.setMwaktukegiatan(waktukegiatan);
        this.setMlokasikegiatan(lokasikegiatan);
        this.setMpenanggungjawab(penanggungjawab);
        this.setMpemateri(pemateri);
        this.setTempatKegiatan(tempatKegiatan);
    }

    public String getMidkegiatan() {
        return midkegiatan;
    }

    public String getMlokasikegiatan() {
        return mlokasikegiatan;
    }

    public String getMnamakegiatan() {
        return mnamakegiatan;
    }

    public String getMpemateri() {
        return mpemateri;
    }

    public String getMpenanggungjawab() {
        return mpenanggungjawab;
    }

    public String getMwaktukegiatan() {
        return mwaktukegiatan;
        //return "hariini";
    }

    public void setMidkegiatan(String midkegiatan) {
        this.midkegiatan = midkegiatan;
    }

    public String getMidmasjid() {
        return midmasjid;
    }

    public void setMidmasjid(String midmasjid) {
        this.midmasjid = midmasjid;
    }

    public void setMnamakegiatan(String mnamakegiatan) {
        this.mnamakegiatan = mnamakegiatan;
    }

    public String getTanggalKegiatan() {
        return tanggalKegiatan;
    }

    public void setTanggalKegiatan(String tanggalKegiatan) {
        this.tanggalKegiatan = tanggalKegiatan;
    }

    public void setMwaktukegiatan(String mwaktukegiatan) {
        this.mwaktukegiatan = mwaktukegiatan;
    }

    public void setMlokasikegiatan(String mlokasikegiatan) {
        this.mlokasikegiatan = mlokasikegiatan;
    }

    public void setMpemateri(String mpemateri) {
        this.mpemateri = mpemateri;
    }

    public void setMpenanggungjawab(String mpenanggungjawab) {
        this.mpenanggungjawab = mpenanggungjawab;
    }

    public String getTempatKegiatan() {
        return tempatKegiatan;
    }

    public void setTempatKegiatan(String tempatKegiatan) {
        this.tempatKegiatan = tempatKegiatan;
    }
}
