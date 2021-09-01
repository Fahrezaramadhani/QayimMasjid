package com.example.qayimmasjid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qayimmasjid.model.masjid.Masjid;
import com.example.qayimmasjid.model.masjid.Pengurus;
import com.example.qayimmasjid.ui.kegiatan.ManageKegiatanMasjidActivity;


public class SharedPrefManager extends AppCompatActivity {
    //the constants
    private static final String SHARED_PREF_NAME = "OurMasjidPref";
    private static final String KEY_NAMA = "keynamapengurus";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_IDPENGURUS = "keyidpengurus";
    private static final String KEY_IDMASJID = "keyidmasjid";
    private static final String KEY_ALAMAT = "keyalamat";
    private static final String KEY_PASSWORD = "keypassword";
    public static String IDMASJID="x";
    public static String NAMAMASJID="x";
    public static String ALAMATMASJID="x";
    public static String NAMA="x";
    public static String EMAIL="x";
    public static String TAHUN_BERDIRI="x";
    public static int DAYA_TAMPUNG=1;
    public static double LONGITUDE;
    public static double LATITUDE;
    public static String IMAGE;
//    public static PengurusList pengurusList;
    public static String IDPENGURUS= "";
    public static String ALAMAT="";

    private static com.example.qayimmasjid.SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static String getKeyNama() {
        return KEY_NAMA;
    }

    public static synchronized com.example.qayimmasjid.SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new com.example.qayimmasjid.SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(Pengurus pengurus, Masjid masjid) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_IDPENGURUS, pengurus.getMidpengurus());
        editor.putString(KEY_IDMASJID, pengurus.getMidmasjid());
        editor.putString(KEY_EMAIL, pengurus.getMemailpengurus());
        editor.putString(KEY_NAMA, pengurus.getMnamapengurus());
        editor.putString(KEY_ALAMAT, pengurus.getAlamatPengurus());
        editor.apply();

        IDMASJID=pengurus.getMidmasjid();
        NAMAMASJID= masjid.getMnamamasjid();
        ALAMATMASJID=masjid.getMalamat();
        NAMA=pengurus.getMnamapengurus();
        EMAIL=pengurus.getMemailpengurus();
        TAHUN_BERDIRI=masjid.getTahunBerdiri();
        DAYA_TAMPUNG=masjid.getDayaTampung();
        LONGITUDE=masjid.getMlongitude();
        LATITUDE = masjid.getMlatidue();
        IMAGE = masjid.getFoto_masjid();
        IDPENGURUS=pengurus.getMidpengurus();
        ALAMAT=pengurus.getAlamatPengurus();
    }

    public void userLogin(Pengurus pengurus){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_IDPENGURUS, pengurus.getMidpengurus());
        editor.putString(KEY_IDMASJID, pengurus.getMidmasjid());
        editor.putString(KEY_EMAIL, pengurus.getMemailpengurus());
        editor.putString(KEY_NAMA, pengurus.getMnamapengurus());
        editor.putString(KEY_ALAMAT, pengurus.getAlamatPengurus());
        editor.apply();

        IDMASJID=pengurus.getMidmasjid();
        NAMA=pengurus.getMnamapengurus();
        EMAIL=pengurus.getMemailpengurus();
        IDPENGURUS=pengurus.getMidpengurus();
        ALAMAT=pengurus.getAlamatPengurus();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null) != null;

    }

    //this method will give the logged in user
    public Pengurus getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Pengurus(
                sharedPreferences.getString(KEY_IDPENGURUS, null),
                sharedPreferences.getString(KEY_IDMASJID, null),
                sharedPreferences.getString(KEY_NAMA, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_ALAMAT, null),
                sharedPreferences.getString(KEY_PASSWORD, null)
        );
    }

//    public Masjid getMasjid() {
//        SharedPrefManager sharedPrefManager
//    }

    //this method will logout the user
    public void logout(View view, Activity activity1) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
