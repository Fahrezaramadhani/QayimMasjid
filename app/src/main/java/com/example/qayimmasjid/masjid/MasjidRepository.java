package com.example.qayimmasjid.masjid;

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
import com.example.qayimmasjid.URLs;
import com.example.qayimmasjid.model.masjid.MasjidList;
import com.example.qayimmasjid.model.masjid.Pengumuman;
import com.example.qayimmasjid.model.masjid.PengumumanList;
import com.example.qayimmasjid.pengumuman.PengumumanRepository;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class MasjidRepository {
    Context context;
    private static MasjidRepository masjidRepository;
    MutableLiveData<MasjidList> listMasjid = new MutableLiveData<>();

    public MutableLiveData<MasjidList> searchMasjid(String keyword){
        SearchMasjid searchMasjid = new SearchMasjid(listMasjid, keyword);
        searchMasjid.execute();

        return listMasjid;
    }

    class SearchMasjid extends AsyncTask<Void, Void, String> {
        private String keyword;
        private MutableLiveData<MasjidList> masjidList;

        public SearchMasjid(MutableLiveData<MasjidList> masjidList, String keyword) {
            this.keyword = keyword;
            this.masjidList = masjidList;
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
                    this.masjidList.setValue(gson.fromJson(s, MasjidList.class));
                } else {
                    this.masjidList.setValue(null);
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

            //returing the response
            return requestHandler.sendPostRequest(URLs.URL_SEARCHMASJID, params);
        }
    }

    public MutableLiveData<MasjidList> getListKegiatan(Context context){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            String URL = URLs.URL_LISTMASJID;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Title", "Android Volley Demo");
            jsonBody.put("Author", "BNK");
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("VOLLEY", response);
                    Gson gson=new Gson();
                    listMasjid.setValue(gson.fromJson(response, MasjidList.class));
                    Log.d("Repos", "onResponse: " + gson.fromJson(response, MasjidList.class));

                }
            }, new com.android.volley.Response.ErrorListener() {
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
                protected com.android.volley.Response<String> parseNetworkResponse(NetworkResponse response) {
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
        return listMasjid;
    }
}
