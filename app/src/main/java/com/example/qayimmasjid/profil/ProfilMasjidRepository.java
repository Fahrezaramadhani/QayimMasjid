package com.example.qayimmasjid.profil;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.qayimmasjid.RequestHandler;
import com.example.qayimmasjid.URLs;
import com.example.qayimmasjid.model.masjid.Masjid;
import com.example.qayimmasjid.model.masjid.Pengurus;
import com.example.qayimmasjid.model.masjid.ProfileMasjid;
import com.google.android.gms.nearby.messages.internal.Update;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProfilMasjidRepository {
    Context context;
    private static ProfilMasjidRepository profilMasjidRepository;
    MutableLiveData<ProfileMasjid> masjidProfil = new MutableLiveData<>();

    public MutableLiveData<ProfileMasjid> getProfilMasjid(String idMasjid){
        GetProfilMasjid getprofilMasjid = new GetProfilMasjid(masjidProfil, idMasjid);
        getprofilMasjid.execute();

        return masjidProfil;
    }

    public MutableLiveData<ProfileMasjid> updateProfilMasjid(Masjid masjid){
        UpdateProfilMasjid updateprofilMasjid = new UpdateProfilMasjid(masjidProfil, masjid);
        updateprofilMasjid.execute();

        return masjidProfil;
    }

    public MutableLiveData<ProfileMasjid> uploadImage(String idMasjid, String image){
        UploadImage uploadImage = new UploadImage(masjidProfil, idMasjid, image);
        uploadImage.execute();

        return masjidProfil;
    }

    public MutableLiveData<ProfileMasjid> tambahMasjid(Masjid masjid){
        TambahMasjid tambahMasjid = new TambahMasjid(masjidProfil, masjid);
        tambahMasjid.execute();

        return masjidProfil;
    }

    public MutableLiveData<ProfileMasjid> deleteAccount(String idPengurus){
        DeleteAccount deleteAccount = new DeleteAccount(masjidProfil, idPengurus);
        deleteAccount.execute();

        return masjidProfil;
    }

    public MutableLiveData<ProfileMasjid> updateProfilPengurus(Pengurus pengurus){
        UpdateProfilPengurusMasjid updateAccount = new UpdateProfilPengurusMasjid(masjidProfil, pengurus);
        updateAccount.execute();

        return masjidProfil;
    }

    class GetProfilMasjid extends AsyncTask<Void, Void, String> {
        private String idMasjid;
        private MutableLiveData<ProfileMasjid> profilMasjid;

        public GetProfilMasjid (MutableLiveData<ProfileMasjid> profilMasjid, String idMasjid){
            this.idMasjid = idMasjid;
            this.profilMasjid = profilMasjid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("Response", "onPostExecute: " + s);

            try{
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {

                    Gson gson=new Gson();
                    this.profilMasjid.setValue(gson.fromJson(s, ProfileMasjid.class));
                } else {
                    this.profilMasjid.setValue(null);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("id_masjid", idMasjid);

            //returing the response
            return requestHandler.sendPostRequest(URLs.URL_PROFILEMASJID, params);
        }
    }

    class UpdateProfilMasjid extends AsyncTask<Void, Void, String> {
        private Masjid masjidProfil;
        private MutableLiveData<ProfileMasjid> profilMasjid;

        public UpdateProfilMasjid (MutableLiveData<ProfileMasjid> profilMasjid, Masjid masjidProfil){
            this.masjidProfil = masjidProfil;
            this.profilMasjid = profilMasjid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("Response", "onPostExecute: " + s);

            try{
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {

                    Gson gson=new Gson();
                    this.profilMasjid.setValue(gson.fromJson(s, ProfileMasjid.class));
                } else {
                    this.profilMasjid.setValue(null);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("id_masjid", masjidProfil.getMid_masjid());
            params.put("nama_masjid", masjidProfil.getMnamamasjid());
            params.put("alamat_masjid", masjidProfil.getMalamat());
            params.put("tahun_berdiri", masjidProfil.getTahunBerdiri());
            params.put("daya_tampung", String.valueOf(masjidProfil.getDayaTampung()));
            params.put("longitude", String.valueOf(masjidProfil.getMlongitude()));
            params.put("latitude", String.valueOf(masjidProfil.getMlatidue()));

            //returing the response
            return requestHandler.sendPostRequest(URLs.URL_UPDATEPROFILMASJID, params);
        }
    }

    class UploadImage extends AsyncTask<Bitmap,Void,String> {

        private RequestHandler rh = new RequestHandler();
        private MutableLiveData<ProfileMasjid> profilMasjid;
        private String image;
        private String idMasjid;

        public UploadImage (MutableLiveData<ProfileMasjid> profilMasjid, String idMasjid, String image){
            this.profilMasjid = profilMasjid;
            this.idMasjid = idMasjid;
            this.image = image;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Response", "onPostExecute: " + s);

            try{
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {

                    Gson gson=new Gson();
                    this.profilMasjid.setValue(gson.fromJson(s, ProfileMasjid.class));
                } else {
                    this.profilMasjid.setValue(null);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            String uploadImage = image;

            HashMap<String,String> data = new HashMap<>();
            data.put("id_masjid", idMasjid);
            data.put("image", uploadImage);

            String result = rh.sendPostRequest(URLs.URL_UPLOADGAMBAR,data);

            return result;
        }
    }

    class TambahMasjid extends AsyncTask<Void, Void, String> {
        private Masjid masjidProfil;
        private MutableLiveData<ProfileMasjid> profilMasjid;

        public TambahMasjid (MutableLiveData<ProfileMasjid> profilMasjid, Masjid masjidProfil){
            this.masjidProfil = masjidProfil;
            this.profilMasjid = profilMasjid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("Response", "onPostExecute: " + s);

            try{
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {

                    Gson gson=new Gson();
                    this.profilMasjid.setValue(gson.fromJson(s, ProfileMasjid.class));
                } else {
                    this.profilMasjid.setValue(null);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("nama_masjid", masjidProfil.getMnamamasjid());
            params.put("alamat_masjid", masjidProfil.getMalamat());
            params.put("tahun_berdiri", masjidProfil.getTahunBerdiri());
            params.put("daya_tampung", String.valueOf(masjidProfil.getDayaTampung()));
            params.put("longitude", String.valueOf(masjidProfil.getMlongitude()));
            params.put("latitude", String.valueOf(masjidProfil.getMlatidue()));
            params.put("foto_masjid", String.valueOf(masjidProfil.getFoto_masjid()));

            //returing the response
            return requestHandler.sendPostRequest(URLs.URL_TAMBAHMASJID, params);
        }
    }

    class DeleteAccount extends AsyncTask<Void, Void, String> {
        private String idPengurus;
        private MutableLiveData<ProfileMasjid> profilMasjid;

        public DeleteAccount (MutableLiveData<ProfileMasjid> profilMasjid, String idPengurus){
            this.idPengurus = idPengurus;
            this.profilMasjid = profilMasjid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("Response", "onPostExecute: " + s);

            try{
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {

                    Gson gson=new Gson();
                    this.profilMasjid.setValue(new ProfileMasjid());
                } else {
                    this.profilMasjid.setValue(null);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("id_pengurus", idPengurus);

            //returing the response
            return requestHandler.sendPostRequest(URLs.URL_DELETEACCOUNT, params);
        }
    }

    class UpdateProfilPengurusMasjid extends AsyncTask<Void, Void, String> {
        private Pengurus pengurus;
        private MutableLiveData<ProfileMasjid> profilMasjid;

        public UpdateProfilPengurusMasjid (MutableLiveData<ProfileMasjid> profilMasjid, Pengurus pengurus){
            this.pengurus = pengurus;
            this.profilMasjid = profilMasjid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("Response", "onPostExecute: " + s);

            try{
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {

                    Gson gson=new Gson();
                    this.profilMasjid.setValue(gson.fromJson(s, ProfileMasjid.class));
                } else {
                    this.profilMasjid.setValue(null);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("id_pengurus", pengurus.getMidpengurus());
            params.put("nama_pengurus", pengurus.getMnamapengurus());
            params.put("alamat_pengurus", pengurus.getAlamatPengurus());
            params.put("email", pengurus.getMemailpengurus());
            params.put("password", pengurus.getPasswordPengurus());

            //returing the response
            return requestHandler.sendPostRequest(URLs.URL_UPDATEPROFILPENGURUSMASJID, params);
        }
    }
}
