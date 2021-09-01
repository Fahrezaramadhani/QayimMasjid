package com.example.qayimmasjid.model.masjid;

import com.google.gson.annotations.SerializedName;

public class Pengurus {
    @SerializedName("id_pengurus")
    private String midpengurus;

    @SerializedName("id_masjid")
    private String midmasjid;

    @SerializedName("nama_pengurus")
    private String mnamapengurus;

    @SerializedName("alamat_pengurus")
    private String alamatPengurus;

    @SerializedName("email")
    private String memailpengurus;

    @SerializedName("password")
    private String passwordPengurus;

    public Pengurus (){

    }

    public Pengurus(String idpengurus, String idmasjid, String namapengurus, String alamatPengurus, String emailpengurus, String password){
        this.setMidpengurus(idpengurus);
        this.midmasjid=idmasjid;
        this.setMnamapengurus(namapengurus);
        this.setAlamatPengurus(alamatPengurus);
        this.setMemailpengurus(emailpengurus);
        this.setPasswordPengurus(password);
    }

    public String getMidmasjid() {
        return midmasjid;
    }

    public String getMidpengurus() {
        return midpengurus;
    }

    public String getMemailpengurus() {
        return memailpengurus;
    }

    public String getMnamapengurus() {
        return mnamapengurus;
    }

    public String getAlamatPengurus() {
        return alamatPengurus;
    }

    public void setAlamatPengurus(String alamatPengurus) {
        this.alamatPengurus = alamatPengurus;
    }

    public String getPasswordPengurus() {
        return passwordPengurus;
    }

    public void setPasswordPengurus(String passwordPengurus) {
        this.passwordPengurus = passwordPengurus;
    }

    public void setMidpengurus(String midpengurus) {
        this.midpengurus = midpengurus;
    }

    public void setMnamapengurus(String mnamapengurus) {
        this.mnamapengurus = mnamapengurus;
    }

    public void setMemailpengurus(String memailpengurus) {
        this.memailpengurus = memailpengurus;
    }
}
