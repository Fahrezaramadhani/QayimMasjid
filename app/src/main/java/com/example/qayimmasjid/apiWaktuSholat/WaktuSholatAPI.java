package com.example.qayimmasjid.apiWaktuSholat;

import com.example.qayimmasjid.model.waktusholatapi.ResponseApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WaktuSholatAPI {
    @GET("today.json")
    Call<ResponseApi> getToday(@Query("longitude") String longitude, @Query("latitude") String latitude, @Query("elevation") String elevation);

    @GET("today.json")
    Call<ResponseApi> getTodayByCity(@Query("city") String city);
}
