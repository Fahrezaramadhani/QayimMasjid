package com.example.qayimmasjid.pengumuman;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.qayimmasjid.R;
import com.example.qayimmasjid.RequestHandler;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.URLs;
import com.example.qayimmasjid.kegiatan.KegiatanRepository;
import com.example.qayimmasjid.model.masjid.Kegiatan;
import com.example.qayimmasjid.model.masjid.KegiatanList;
import com.example.qayimmasjid.model.masjid.Pengumuman;
import com.example.qayimmasjid.model.masjid.PengumumanList;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class PengumumanRepository {
    Context context;
    private static PengumumanRepository pengumumanRepository;
    MutableLiveData<PengumumanList> listPengumuman = new MutableLiveData<>();

    public MutableLiveData<PengumumanList> getListPengumuman(String idMasjid){
        ListPengumuman updateprofilMasjid = new ListPengumuman(listPengumuman, idMasjid);
        updateprofilMasjid.execute();

        return listPengumuman;
    }

    public MutableLiveData<PengumumanList> tambahPengumuman(Pengumuman pengumuman){
        TambahPengumuman tambahPengumuman = new TambahPengumuman(listPengumuman, pengumuman);
        tambahPengumuman.execute();

        return listPengumuman;
    }

    public MutableLiveData<PengumumanList> editPengumuman(Pengumuman pengumuman){
        EditPengumuman editPengumuman = new EditPengumuman(listPengumuman, pengumuman);
        editPengumuman.execute();

        return listPengumuman;
    }

    public MutableLiveData<PengumumanList> deletePengumuman(String idPengumuman){
        DeletePengumuman deletePengumuman = new DeletePengumuman(listPengumuman, idPengumuman);
        deletePengumuman.execute();

        return listPengumuman;
    }

    public MutableLiveData<PengumumanList> searchPengumuman(String keyword, String idMasjid){
        SearchPengumuman searchPengumuman = new SearchPengumuman(listPengumuman, keyword, idMasjid);
        searchPengumuman.execute();

        return listPengumuman;
    }

    class SearchPengumuman extends AsyncTask<Void, Void, String> {
        private String keyword;
        private String idMasjid;
        private MutableLiveData<PengumumanList> pengumumanList;

        public SearchPengumuman(MutableLiveData<PengumumanList> pengumumanList, String keyword, String idMasjid) {
            this.idMasjid = idMasjid;
            this.keyword = keyword;
            this.pengumumanList = pengumumanList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("Response", "onPostExecute: " + s);

            try {
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {

                    Gson gson = new Gson();
                    this.pengumumanList.setValue(gson.fromJson(s, PengumumanList.class));
                } else {
                    this.pengumumanList.setValue(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("kata_kunci", keyword);
            params.put("id_masjid", idMasjid);

            //returing the response
            return requestHandler.sendPostRequest(URLs.URL_SEARCHPENGUMUMAN, params);
        }
    }

    class TambahPengumuman extends AsyncTask<Void, Void, String> {
        private Pengumuman pengumuman;
        private MutableLiveData<PengumumanList> pengumumanList;

        public TambahPengumuman(MutableLiveData<PengumumanList> pengumumanList, Pengumuman pengumuman) {
            this.pengumuman = pengumuman;
            this.pengumumanList = pengumumanList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("Response", "onPostExecute: " + s);

            try {
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {

                    Gson gson = new Gson();
                    this.pengumumanList.setValue(new PengumumanList());
                } else {
                    this.pengumumanList.setValue(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("id_masjid", pengumuman.getIdMasjid());
            params.put("nama_pengumuman", pengumuman.getMnamapengumuman());
            params.put("tanggal_pengumuman", pengumuman.getTanggalPengumuman());
            params.put("waktu_pengumuman", pengumuman.getWaktuPengumuman());
            params.put("deskripsi_pengumuman", pengumuman.getDeskripsiPengumuman());
            params.put("cp_pengumuman", pengumuman.getContactPerson());

            //returing the response
            return requestHandler.sendPostRequest(URLs.URL_TAMBAHPENGUMUMAN, params);
        }
    }

    class EditPengumuman extends AsyncTask<Void, Void, String> {
        private Pengumuman pengumuman;
        private MutableLiveData<PengumumanList> pengumumanList;

        public EditPengumuman(MutableLiveData<PengumumanList> pengumumanList, Pengumuman pengumuman) {
            this.pengumuman = pengumuman;
            this.pengumumanList = pengumumanList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("Response", "onPostExecute: " + s);

            try {
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {

                    Gson gson = new Gson();
                    this.pengumumanList.setValue(new PengumumanList());
                } else {
                    this.pengumumanList.setValue(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("id_pengumuman", pengumuman.getMidpengumuman());
            params.put("nama_pengumuman", pengumuman.getMnamapengumuman());
            params.put("tanggal_pengumuman", pengumuman.getTanggalPengumuman());
            params.put("waktu_pengumuman", pengumuman.getWaktuPengumuman());
            params.put("deskripsi_pengumuman", pengumuman.getDeskripsiPengumuman());
            params.put("cp_pengumuman", pengumuman.getContactPerson());

            //returing the response
            return requestHandler.sendPostRequest(URLs.URL_EDITPENGUMUMAN, params);
        }
    }

    class DeletePengumuman extends AsyncTask<Void, Void, String> {
        private String idPengumuman;
        private MutableLiveData<PengumumanList> profilMasjid;

        public DeletePengumuman(MutableLiveData<PengumumanList> profilMasjid, String idPengumuman) {
            this.idPengumuman = idPengumuman;
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

            try {
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {

                    Gson gson = new Gson();
                    this.profilMasjid.setValue(new PengumumanList());
                } else {
                    this.profilMasjid.setValue(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("id_pengumuman", idPengumuman);

            //returing the response
            return requestHandler.sendPostRequest(URLs.URL_DELETEPENGUMUMAN, params);
        }
    }

    class ListPengumuman extends AsyncTask<Void, Void, String> {
        private String idMasjid;
        private MutableLiveData<PengumumanList> profilMasjid;

        public ListPengumuman(MutableLiveData<PengumumanList> profilMasjid, String idMasjid) {
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

            try {
                JSONObject obj = new JSONObject(s);
                //if no error in response
                if (!obj.getBoolean("error")) {

                    Gson gson = new Gson();
                    this.profilMasjid.setValue(gson.fromJson(s, PengumumanList.class));
                } else {
                    this.profilMasjid.setValue(null);
                }
            } catch (JSONException e) {
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
            return requestHandler.sendPostRequest(URLs.URL_LISTPENGUMUMAN, params);
        }
    }


}
