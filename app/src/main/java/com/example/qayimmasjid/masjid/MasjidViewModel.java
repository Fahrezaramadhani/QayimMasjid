package com.example.qayimmasjid.masjid;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qayimmasjid.model.masjid.MasjidList;

public class MasjidViewModel extends ViewModel {
    private MutableLiveData<MasjidList> mutableLiveData;
    private MasjidRepository masjidRepository;

    public void getListKegiatan(Context context){
        masjidRepository = new MasjidRepository();
        mutableLiveData = masjidRepository.getListKegiatan(context);
    }

    public void searchMasjid(String keyword){
        masjidRepository = new MasjidRepository();
        mutableLiveData = masjidRepository.searchMasjid(keyword);
    }

    public LiveData<MasjidList> getList() {
        return mutableLiveData;
    }
}
