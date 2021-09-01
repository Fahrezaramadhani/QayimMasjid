package com.example.qayimmasjid.model.masjid;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TransaksiList {
    @SerializedName("listTransaksi")
    private ArrayList<Transaksi> listTransaksi;

    public TransaksiList (){

    }

    public TransaksiList(ArrayList<Transaksi> transaksi){
        listTransaksi= transaksi;
    }

    public int getNumber(){
        return listTransaksi.size();
    }

    public ArrayList<Transaksi> getListTransaksi() {
        return listTransaksi;
    }
}
