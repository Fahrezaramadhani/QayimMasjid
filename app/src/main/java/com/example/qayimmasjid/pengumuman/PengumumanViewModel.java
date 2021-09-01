package com.example.qayimmasjid.pengumuman;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qayimmasjid.model.masjid.Kegiatan;
import com.example.qayimmasjid.model.masjid.KegiatanList;
import com.example.qayimmasjid.model.masjid.Pengumuman;
import com.example.qayimmasjid.model.masjid.PengumumanList;

public class PengumumanViewModel extends ViewModel {
    private MutableLiveData<PengumumanList> mutableLiveData;
    private PengumumanRepository pengumumanRepository;

    public void getListPengumuman(String idMasjid){
        pengumumanRepository = new PengumumanRepository();
        mutableLiveData = pengumumanRepository.getListPengumuman(idMasjid);
    }

    public void tambahPengumuman(Pengumuman pengumuman){
        pengumumanRepository = new PengumumanRepository();
        mutableLiveData = pengumumanRepository.tambahPengumuman(pengumuman);
    }

    public void editPengumuman(Pengumuman pengumuman){
        pengumumanRepository = new PengumumanRepository();
        mutableLiveData = pengumumanRepository.editPengumuman(pengumuman);
    }

    public void deletePengumuman(String idPengumuman){
        pengumumanRepository = new PengumumanRepository();
        mutableLiveData = pengumumanRepository.deletePengumuman(idPengumuman);
    }

    public void searchPengumuman(String keyword, String idMasjid){
        pengumumanRepository = new PengumumanRepository();
        mutableLiveData = pengumumanRepository.searchPengumuman(keyword, idMasjid);
    }

    public LiveData<PengumumanList> getData() {
        return mutableLiveData;
    }
}
