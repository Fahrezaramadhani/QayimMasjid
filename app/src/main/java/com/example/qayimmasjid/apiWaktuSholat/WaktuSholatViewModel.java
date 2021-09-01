package com.example.qayimmasjid.apiWaktuSholat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qayimmasjid.model.waktusholatapi.Results;

public class WaktuSholatViewModel extends ViewModel {
    private MutableLiveData<Results> mutableLiveData;
    private WaktuSholatRepository waktuSholatRepository;

    public void getPray(String longitude, String latitude, String altitude){
        waktuSholatRepository = WaktuSholatRepository.getInstance();
        mutableLiveData = waktuSholatRepository.getWaktuSholat(longitude, latitude, altitude);
    }

    public void getPrayByCity(String city){
        waktuSholatRepository = WaktuSholatRepository.getInstance();
        mutableLiveData = waktuSholatRepository.getWaktuSholatByCity(city);
    }

    public LiveData<Results> getWaktuSholat() {
        return mutableLiveData;
    }
}
