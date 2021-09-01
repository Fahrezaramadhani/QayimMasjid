package com.example.qayimmasjid.transaksi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qayimmasjid.model.masjid.Pengumuman;
import com.example.qayimmasjid.model.masjid.PengumumanList;
import com.example.qayimmasjid.model.masjid.Transaksi;
import com.example.qayimmasjid.model.masjid.TransaksiList;

public class TransaksiViewModel extends ViewModel {
    private MutableLiveData<TransaksiList> mutableLiveData;
    private TransaksiRepository transaksiRepository;

    public void getListTransaki(String idMasjid){
        transaksiRepository = new TransaksiRepository();
        mutableLiveData = transaksiRepository.getListTransaksi(idMasjid);
    }

//    public void tambahPengumuman(Pengumuman pengumuman){
//        transaksiRepository = new TransaksiRepository();
//        mutableLiveData = transaksiRepository.tambahPengumuman(pengumuman);
//    }
//
    public void editTransaksi(Transaksi transaksi){
        transaksiRepository = new TransaksiRepository();
        mutableLiveData = transaksiRepository.editTransaksi(transaksi);
    }

    public void deletePengumuman(String idTransaksi){
        transaksiRepository = new TransaksiRepository();
        mutableLiveData = transaksiRepository.deleteTransaksi(idTransaksi);
    }

    public void searchTransaksi(String keyword, String idMasjid){
        transaksiRepository = new TransaksiRepository();
        mutableLiveData = transaksiRepository.searchTransaksi(keyword, idMasjid);
    }

    public LiveData<TransaksiList> getData() {
        return mutableLiveData;
    }
}
