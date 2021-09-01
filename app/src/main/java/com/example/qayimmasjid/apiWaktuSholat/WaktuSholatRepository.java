package com.example.qayimmasjid.apiWaktuSholat;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.qayimmasjid.model.waktusholatapi.ResponseApi;
import com.example.qayimmasjid.model.waktusholatapi.Results;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaktuSholatRepository {
    private static WaktuSholatRepository waktuSholatRepository;
    MutableLiveData<Results> waktuData = new MutableLiveData<>();
    private WaktuSholatAPI waktuSholatAPI;

    public static WaktuSholatRepository getInstance(){
        if (waktuSholatRepository == null){
            waktuSholatRepository = new WaktuSholatRepository();
        }
        return waktuSholatRepository;
    }

    public WaktuSholatRepository(){
        waktuSholatAPI = RetrofitService.createService(WaktuSholatAPI.class);
    }

    public MutableLiveData<Results> getWaktuSholat(String longitude, String latitude, String altitude){
        waktuSholatAPI.getToday(longitude, latitude, altitude).enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.isSuccessful()){
                    Log.d("Test respon code", "onResponseCode: " + response.body().getStatus());
                    waktuData.setValue(response.body().getResult());
                }
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                waktuData.setValue(null);
            }
        });
        return waktuData;
    }

    public MutableLiveData<Results> getWaktuSholatByCity(String city){
        waktuSholatAPI.getTodayByCity(city).enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.isSuccessful()){
                    Log.d("Test respon code", "onResponseCode: " + response.body().getResult());
                    waktuData.setValue(response.body().getResult());
                }
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                waktuData.setValue(null);
            }
        });
        return waktuData;
    }
}
