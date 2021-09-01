package com.example.qayimmasjid.model.masjid;

import com.google.gson.annotations.SerializedName;

public class Transaksi {
    @SerializedName("id_transaksi")
    private String midtransaksi;

    @SerializedName("id_masjid")
    private String idMasjid;

    @SerializedName("id_kegiatan")
    private String midkegiatan;

    @SerializedName("nama_transaksi")
    private String mnamatransaksi;

    @SerializedName("tanggal_transaksi")
    private String mtanggal;

    @SerializedName("jenis_transaksi")
    private String mjenistransaksi;

    @SerializedName("nominal_transaksi")
    private int mnominal;

    public Transaksi (){

    }

    public Transaksi(String idkeuangan, String idMasjid, String idkegiatan, String namatransaksi, String jeniskeuangan,
                     int nominal, String tanggal){
        setMidtransaksi(idkeuangan);
        setIdMasjid(idMasjid);
        setMidkegiatan(idkegiatan);
        setMnamatransaksi(namatransaksi);
        setMjenistransaksi(jeniskeuangan);
        setMnominal(nominal);
        setMtanggal(tanggal);
    }


    public String getMidtransaksi() {
        return midtransaksi;
    }

    public void setMidtransaksi(String midtransaki) {
        this.midtransaksi = midtransaki;
    }

    public String getIdMasjid() {
        return idMasjid;
    }

    public void setIdMasjid(String idMasjid) {
        this.idMasjid = idMasjid;
    }

    public String getMidkegiatan() {
        return midkegiatan;
    }

    public void setMidkegiatan(String midkegiatan) {
        this.midkegiatan = midkegiatan;
    }

    public String getMnamatransaksi() {
        return mnamatransaksi;
    }

    public void setMnamatransaksi(String mnamatransaksi) {
        this.mnamatransaksi = mnamatransaksi;
    }

    public String getMtanggal() {
        return mtanggal;
    }

    public void setMtanggal(String mtanggal) {
        this.mtanggal = mtanggal;
    }

    public String getMjenistransaksi() {
        return mjenistransaksi;
    }

    public void setMjenistransaksi(String mjenistransaksi) {
        this.mjenistransaksi = mjenistransaksi;
    }

    public int getMnominal() {
        return mnominal;
    }

    public void setMnominal(int mnominal) {
        this.mnominal = mnominal;
    }
}
