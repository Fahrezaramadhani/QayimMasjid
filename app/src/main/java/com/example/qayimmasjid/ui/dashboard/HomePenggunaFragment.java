package com.example.qayimmasjid.ui.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.qayimmasjid.MainActivityPengurusMasjid;
import com.example.qayimmasjid.R;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.apiWaktuSholat.WaktuSholatViewModel;
import com.example.qayimmasjid.model.masjid.Masjid;
import com.example.qayimmasjid.model.waktusholatapi.Results;
import com.example.qayimmasjid.ui.profil.EditProfilMasjidActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class HomePenggunaFragment extends Fragment {
    WaktuSholatViewModel waktuSholatViewModel;
    FusedLocationProviderClient fusedLocationProviderClient;
    private TextView waktuSubuh;
    private TextView waktuDhuhur;
    private TextView waktuAshar;
    private TextView waktuMagrib;
    private TextView waktuIsya;
    private TextView waktuSelanjutnya;
    private TextView currentCity;
    private TextView sholatSelanjutnya;
    private TextView tanggalIslam;
    private TextView tanggalSekarang;
    public String kota = "";
    private SharedPreferences sharedPreferences;
    private String email;
//    private Results results;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_pengguna, container, false);

        waktuSubuh = view.findViewById(R.id.waktu_shubuh);
        waktuDhuhur = view.findViewById(R.id.waktu_dzuhur);
        waktuAshar = view.findViewById(R.id.waktu_ashar);
        waktuMagrib = view.findViewById(R.id.waktu_maghrib);
        waktuIsya = view.findViewById(R.id.waktu_isya);
        currentCity = view.findViewById(R.id.daerah_sholat);
        waktuSelanjutnya = view.findViewById(R.id.jam_mundur);
        sholatSelanjutnya = view.findViewById(R.id.waktu_sholat);
        tanggalIslam = view.findViewById(R.id.date_in_hijriah);
        tanggalSekarang = view.findViewById(R.id.date_in_masehi);

        waktuSholatViewModel = ViewModelProviders.of(this).get(WaktuSholatViewModel.class);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        setDashboard();

//        sharedPreferences = getContext().getSharedPreferences("OurMasjidPref", Context.MODE_PRIVATE);

        if (SharedPrefManager.getInstance(getActivity()).isLoggedIn()){
            Masjid masjid = null;
            SharedPrefManager.getInstance(getActivity()).userLogin(SharedPrefManager.getInstance(getActivity()).getUser());
            startActivity(new Intent(getActivity(), MainActivityPengurusMasjid.class));
        }

        return view;
    }

    public void setSholat(String longitude, String latitude, String altitude){
//        waktuSholatViewModel.getPrayByCity(city);
        waktuSholatViewModel.getPray(longitude, latitude, altitude);
        waktuSholatViewModel.getWaktuSholat().observe(getViewLifecycleOwner(), responseApi -> {
            waktuSubuh.setText(responseApi.getDatetime().get(0).getTimes().getFajr());
            waktuDhuhur.setText(responseApi.getDatetime().get(0).getTimes().getDhuhr());
            waktuAshar.setText(responseApi.getDatetime().get(0).getTimes().getAsr());
            waktuMagrib.setText(responseApi.getDatetime().get(0).getTimes().getMaghrib());
            waktuIsya.setText(responseApi.getDatetime().get(0).getTimes().getIsha());
            Log.d("Waktu isha : ", responseApi.getDatetime().get(0).getTimes().getIsha());
            setDateIslam(responseApi);
        });
    }

    public void setDashboard(){
        setCurrentLocation();
        setDate();
    }

    @SuppressLint("MissingPermission")
    private void setCurrentLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                    try {
                        List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                        latitude = address.get(0).getLatitude();
//                        longitude = address.get(0).getLongitude();
//                        address.get(0).
                        kota = address.get(0).getSubAdminArea();
                        currentCity.setText("Waktu Sholat Untuk " + address.get(0).getSubAdminArea());
                        setSholat(String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()), String.valueOf(location.getAltitude()));
                        setWaktuTersisa(String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()), String.valueOf(location.getAltitude()));
                        Log.d("Kota : ", kota);
//                        Toast.makeText(getContext(), "Kota" + kota, Toast.LENGTH_SHORT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void setWaktuTersisa(String longitude, String latitude, String altitude){
        waktuSholatViewModel.getPray(longitude, latitude, altitude);
        waktuSholatViewModel.getWaktuSholat().observe(getViewLifecycleOwner(), responseApi -> {
            String listSholat[] = {"SUBUH", "DZUHUR", "ASHAR", "MAGHRIB", "ISYA"};
            ArrayList<String> waktuSholat = new ArrayList<String>();
            String waktuSholatSelanjutnya = "";
            Date date1, date2 = null;
            long duration = 0;
            waktuSholat.add(0, responseApi.getDatetime().get(0).getTimes().getFajr());
            waktuSholat.add(1, responseApi.getDatetime().get(0).getTimes().getDhuhr());
            waktuSholat.add(2, responseApi.getDatetime().get(0).getTimes().getAsr());
            waktuSholat.add(3, responseApi.getDatetime().get(0).getTimes().getMaghrib());
            waktuSholat.add(4, responseApi.getDatetime().get(0).getTimes().getIsha());
            Calendar calendar1 = Calendar.getInstance();
            SimpleDateFormat formatter1 = new SimpleDateFormat("kk:mm:ss");
            String currentDate = formatter1.format(calendar1.getTime());
            try {
                date2 = formatter1.parse(currentDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date compare1 = null;
            Date compare2 = null;
            for (int i = 0; i < 4; i++) {
                try {
                    compare1 = formatter1.parse(waktuSholat.get(i) + ":00");
                    compare2 = formatter1.parse(waktuSholat.get(i + 1) + ":00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if ((date2.after(compare1)) && (date2.before(compare2))) {
                    waktuSholatSelanjutnya = waktuSholat.get(i + 1);
                    sholatSelanjutnya.setText(listSholat[i + 1]);
                }
            }
            if (waktuSholatSelanjutnya == "") {
                waktuSholatSelanjutnya = waktuSholat.get(0);
                sholatSelanjutnya.setText(listSholat[0]);
            }
            try {
                calendar1 = Calendar.getInstance();
                DateFormat formatter2 = new SimpleDateFormat("kk:mm:ss");
                currentDate = formatter2.format(calendar1.getTime());
                date1 = formatter2.parse(waktuSholatSelanjutnya + ":00");
                //            date1 = formatter2.parse("04:45:00");
                date2 = formatter2.parse(currentDate);
                duration = date1.getTime() - date2.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            CountDownTime countDownTime = new CountDownTime(duration, 1000);
            countDownTime.start();
        });
    }

    public void setDateIslam(Results results){
        String tanggal, bulan, hari, tahun;
        String listBulan[] = {"Muharam", "Safar", "Rabiul Awal", "Rabiul Akhir", "Jumadil Awal", "Jumadil Akhir", "Rajab", "Syakban"
                , "Ramadan", "Syawal", "Zulkaidah", "Zulhijah"};

        tanggal = results.getDatetime().get(0).getDate().getHijri();
        bulan = tanggal.substring(tanggal.indexOf("-") +1, tanggal.lastIndexOf("-"));
        hari = tanggal.substring(tanggal.lastIndexOf("-")+1);
        tahun = tanggal.substring(0,4);

        tanggalIslam.setText(hari + " " + listBulan[Integer.valueOf(bulan)-1] + " " + tahun);
    }

    public void setDate(){
        int tanggal, bulan, hari;
        String namabulan[] = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September",
                "Oktober", "November", "Desember"};
        String namaHari[] = {"Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"};

        GregorianCalendar date = new GregorianCalendar();
        tanggal = date.get(Calendar.DAY_OF_MONTH);
        bulan = date.get(Calendar.MONTH);
        hari = date.get(Calendar.DAY_OF_WEEK);

        tanggalSekarang.setText(namaHari[hari-1] + ", " + tanggal + " " + namabulan[Integer.valueOf(bulan)]);
    }

    class CountDownTime extends CountDownTimer{

        public CountDownTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            NumberFormat f = new DecimalFormat("00");
            long hour = (millisUntilFinished / 3600000) % 24;
            long min = (millisUntilFinished / 60000) % 60;
            long sec = (millisUntilFinished / 1000) % 60;
            waktuSelanjutnya.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
        }

        @Override
        public void onFinish() {
            Log.d("Status sholat : ", "Lanjut ke sholat selanjutnya");
            setDashboard();
        }
    }
}