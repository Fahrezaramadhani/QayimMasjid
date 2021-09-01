package com.example.qayimmasjid.kegiatan;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qayimmasjid.model.masjid.Kegiatan;
import com.example.qayimmasjid.model.masjid.KegiatanList;

public class KegiatanViewModel extends ViewModel {
    private MutableLiveData<KegiatanList> mutableLiveData;
    private KegiatanRepository kegiatanRepository;

    public void getListKegiatan(Context context, String idMasjid){
        kegiatanRepository = new KegiatanRepository();
        mutableLiveData = kegiatanRepository.getListKegiatan(context, idMasjid);
    }

    public void editKegiatan(Kegiatan kegiatan){
        kegiatanRepository = new KegiatanRepository();
        mutableLiveData = kegiatanRepository.editKegiatan(kegiatan);
    }

    public void deleteKegiatan(String idKegiatan){
        kegiatanRepository = new KegiatanRepository();
        mutableLiveData = kegiatanRepository.deleteKegiatan(idKegiatan);
    }

    public void tambahKegiatan(Kegiatan kegiatan){
        kegiatanRepository = new KegiatanRepository();
        mutableLiveData = kegiatanRepository.tambahKegiatan(kegiatan);
    }

    public void searchKegiatan(String keyword, String idMasjid){
        kegiatanRepository = new KegiatanRepository();
        mutableLiveData = kegiatanRepository.searchKegiatan(keyword, idMasjid);
    }

    public LiveData<KegiatanList> getData() {
        return mutableLiveData;
    }
}
