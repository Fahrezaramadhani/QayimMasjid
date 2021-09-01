package com.example.qayimmasjid.kegiatan;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
import com.example.qayimmasjid.RequestHandler;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.URLs;
import com.example.qayimmasjid.model.masjid.Kegiatan;
import com.example.qayimmasjid.model.masjid.KegiatanList;
import com.example.qayimmasjid.model.masjid.Masjid;
import com.example.qayimmasjid.model.masjid.MasjidList;
import com.example.qayimmasjid.model.masjid.Pengumuman;
import com.example.qayimmasjid.model.masjid.PengumumanList;
import com.example.qayimmasjid.model.masjid.ProfileMasjid;
import com.example.qayimmasjid.pengumuman.PengumumanRepository;
import com.example.qayimmasjid.profil.ProfilMasjidRepository;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class KegiatanRepository {
    Context context;
    private static KegiatanRepository kegiatanRepository;
    MutableLiveData<KegiatanList> listKegiatan = new MutableLiveData<>();

    public MutableLiveData<KegiatanList> editKegiatan(Kegiatan kegiatan){
        EditKegiatan editKegiatan = new EditKegiatan(listKegiatan, kegiatan);
        editKegiatan.execute();

        return listKegiatan;
    }

    public MutableLiveData<KegiatanList> deleteKegiatan(String idKegiatan){
        DeleteKegiatan deleteKegiatan = new DeleteKegiatan(listKegiatan, idKegiatan);
        deleteKegiatan.execute();

        return listKegiatan;
    }

    public MutableLiveData<KegiatanList> tambahKegiatan(Kegiatan kegiatan){
        TambahKegiatan tambahKegiatan = new TambahKegiatan(listKegiatan, kegiatan);
        tambahKegiatan.execute();

        return listKegiatan;
    }

    public MutableLiveData<KegiatanList> searchKegiatan(String keyword, String idMasjid){
        SearchKegiatan searchKegiatan = new SearchKegiatan(listKegiatan, keyword, idMasjid);
        searchKegiatan.execute();

        return listKegiatan;
    }

    class SearchKegiatan extends AsyncTask<Void, Void, String> {
        private String keyword;
        private String idMasjid;
        private MutableLiveData<KegiatanList> kegiatanList;

        public SearchKegiatan(MutableLiveData<KegiatanList> kegiatanList, String keyword, String idMasjid) {
            this.idMasjid = idMasjid;
            this.keyword = keyword;
            this.kegiatanList = kegiatanList;
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
                    this.kegiatanList.setValue(gson.fromJson(s, KegiatanList.class));
                } else {
                    this.kegiatanList.setValue(null);
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
            return requestHandler.sendPostRequest(URLs.URL_SEARCHKEGIATAN, params);
        }
    }

    class TambahKegiatan extends AsyncTask<Void, Void, String> {
        private Kegiatan kegiatan;
        private MutableLiveData<KegiatanList> kegiatanList;

        public TambahKegiatan(MutableLiveData<KegiatanList> kegiatanList, Kegiatan kegiatan) {
            this.kegiatan = kegiatan;
            this.kegiatanList = kegiatanList;
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
                    this.kegiatanList.setValue(new KegiatanList());
                } else {
                    this.kegiatanList.setValue(null);
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
            params.put("id_masjid", kegiatan.getMidmasjid());
            params.put("nama_kegiatan", kegiatan.getMnamakegiatan());
            params.put("tanggal_kegiatan", kegiatan.getTanggalKegiatan());
            params.put("waktu_kegiatan", kegiatan.getMwaktukegiatan());
            params.put("lokasi_kegiatan", kegiatan.getMlokasikegiatan());
            params.put("pemateri", kegiatan.getMpemateri());
            params.put("pj_kegiatan", kegiatan.getMpenanggungjawab());
            params.put("tempat_kegiatan", kegiatan.getTempatKegiatan());

            //returing the response
            return requestHandler.sendPostRequest(URLs.URL_TAMBAHKEGIATAN, params);
        }
    }

    class EditKegiatan extends AsyncTask<Void, Void, String> {
        private Kegiatan kegiatan;
        private MutableLiveData<KegiatanList> profilMasjid;

        public EditKegiatan(MutableLiveData<KegiatanList> profilMasjid, Kegiatan kegiatan) {
            this.kegiatan = kegiatan;
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
                    this.profilMasjid.setValue(new KegiatanList());
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
            params.put("id_kegiatan", kegiatan.getMidkegiatan());
            params.put("nama_kegiatan", kegiatan.getMnamakegiatan());
            Log.d("namaKegiatan", "doInBackground: " + kegiatan.getMnamakegiatan());
            params.put("tanggal_kegiatan", kegiatan.getTanggalKegiatan());
            params.put("waktu_kegiatan", kegiatan.getMwaktukegiatan());
            params.put("lokasi_kegiatan", kegiatan.getMlokasikegiatan());
            params.put("pemateri", kegiatan.getMpemateri());
            params.put("pj_kegiatan", kegiatan.getMpenanggungjawab());
            params.put("tempat_kegiatan", kegiatan.getTempatKegiatan());

            //returing the response
            return requestHandler.sendPostRequest(URLs.URL_EDITKEGIATAN, params);
        }
    }

    class DeleteKegiatan extends AsyncTask<Void, Void, String> {
        private String idKegiatan;
        private MutableLiveData<KegiatanList> profilMasjid;

        public DeleteKegiatan(MutableLiveData<KegiatanList> profilMasjid, String idKegiatan) {
            this.idKegiatan = idKegiatan;
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
                    this.profilMasjid.setValue(new KegiatanList());
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
            params.put("id_kegiatan", idKegiatan);

            //returing the response
            return requestHandler.sendPostRequest(URLs.URL_DELETEKEGIATAN, params);
        }
    }

    public MutableLiveData<KegiatanList> getListKegiatan(Context context , String idMasjid){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            String URL = URLs.URL_LISTKEGIATAN + "?id_masjid="+ idMasjid;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Title", "Android Volley Demo");
            jsonBody.put("Author", "BNK");
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("VOLLEY", response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        //if no error in response
                        if (!obj.getBoolean("error")) {

                            Gson gson = new Gson();
                            listKegiatan.setValue(gson.fromJson(response, KegiatanList.class));
                        } else {
                            listKegiatan.setValue(null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    listKegiatan.setValue(gson.fromJson(response, KegiatanList.class));
//                    Log.d("response", "onResponse: " + listKegiatan.getValue().getkegiatan());

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        String statusCode = String.valueOf(response.statusCode);
                        //Handling logic
                        return super.parseNetworkResponse(response);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listKegiatan;
    }
}
