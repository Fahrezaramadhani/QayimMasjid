package com.example.qayimmasjid.transaksi;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.qayimmasjid.RequestHandler;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.URLs;
import com.example.qayimmasjid.model.masjid.Pengumuman;
import com.example.qayimmasjid.model.masjid.PengumumanList;
import com.example.qayimmasjid.model.masjid.Transaksi;
import com.example.qayimmasjid.model.masjid.TransaksiList;
import com.example.qayimmasjid.pengumuman.PengumumanRepository;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TransaksiRepository {
    Context context;
    private static TransaksiRepository transaksiRepository;
    MutableLiveData<TransaksiList> transaksiList = new MutableLiveData<>();

    public MutableLiveData<TransaksiList> getListTransaksi(String idMasjid){
        ListTransaksi listTransaksi = new ListTransaksi(transaksiList, idMasjid);
        listTransaksi.execute();

        return transaksiList;
    }

    public MutableLiveData<TransaksiList> tambahTransaksi(Transaksi transaksi){
        TambahTransaksi tambahTransaksi = new TambahTransaksi(transaksiList, transaksi);
        tambahTransaksi.execute();

        return transaksiList;
    }

    public MutableLiveData<TransaksiList> editTransaksi(Transaksi transaksi){
        EditTransaksi editPengumuman = new EditTransaksi(transaksiList, transaksi);
        editPengumuman.execute();

        return transaksiList;
    }

    public MutableLiveData<TransaksiList> deleteTransaksi(String idTransaksi){
        DeleteTransaksi deleteTransaksi = new DeleteTransaksi(transaksiList, idTransaksi);
        deleteTransaksi.execute();

        return transaksiList;
    }

    public MutableLiveData<TransaksiList> searchTransaksi(String keyword, String idMasjid){
        SearchTransaksi searchTransaksi = new SearchTransaksi(transaksiList, keyword, idMasjid);
        searchTransaksi.execute();

        return transaksiList;
    }

    class SearchTransaksi extends AsyncTask<Void, Void, String> {
        private String keyword;
        private String idMasjid;
        private MutableLiveData<TransaksiList> transaksiList;

        public SearchTransaksi(MutableLiveData<TransaksiList> transaksiList, String keyword, String idMasjid) {
            this.idMasjid = idMasjid;
            this.keyword = keyword;
            this.transaksiList = transaksiList;
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
                    this.transaksiList.setValue(gson.fromJson(s, TransaksiList.class));
                } else {
                    this.transaksiList.setValue(null);
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
            return requestHandler.sendPostRequest(URLs.URL_SEARCHTRANSAKSI, params);
        }
    }

    class TambahTransaksi extends AsyncTask<Void, Void, String> {
        private Transaksi transaksi;
        private MutableLiveData<TransaksiList> transaksiList;

        public TambahTransaksi(MutableLiveData<TransaksiList> transaksiList, Transaksi transaksi) {
            this.transaksi = transaksi;
            this.transaksiList = transaksiList;
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
                    this.transaksiList.setValue(new TransaksiList());
                } else {
                    this.transaksiList.setValue(null);
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
            params.put("id_masjid", transaksi.getIdMasjid()); //bisa pake sharedpreferences
            params.put("id_kegiatan", transaksi.getMidkegiatan());
            params.put("nama_transaksi", transaksi.getMnamatransaksi());
            params.put("tanggal_transaksi", transaksi.getMtanggal());
            params.put("nominal_transaksi", String.valueOf(transaksi.getMnominal()));
            params.put("jenis_transaksi", transaksi.getMjenistransaksi());

            //returing the response
            return requestHandler.sendPostRequest(URLs.URL_TAMBAHTRANSAKSI, params);
        }
    }

    class EditTransaksi extends AsyncTask<Void, Void, String> {
        private Transaksi transaksi;
        private MutableLiveData<TransaksiList> transaksiList;

        public EditTransaksi(MutableLiveData<TransaksiList> transaksiList, Transaksi transaksi) {
            this.transaksi = transaksi;
            this.transaksiList = transaksiList;
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
                    this.transaksiList.setValue(new TransaksiList());
                } else {
                    this.transaksiList.setValue(null);
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
            params.put("id_transaksi", transaksi.getMidtransaksi());
            params.put("nama_transaksi", transaksi.getMnamatransaksi());
            params.put("tanggal_transaksi", transaksi.getMtanggal());
            params.put("jenis_transaksi", transaksi.getMjenistransaksi());
            params.put("nominal_transaksi", String.valueOf(transaksi.getMnominal()));

            //returing the response
            return requestHandler.sendPostRequest(URLs.URL_EDITTRANSAKSI, params);
        }
    }

    class DeleteTransaksi extends AsyncTask<Void, Void, String> {
        private String idTransaksi;
        private MutableLiveData<TransaksiList> listTransaksi;

        public DeleteTransaksi(MutableLiveData<TransaksiList> listTransaksi, String idTransaksi) {
            this.idTransaksi = idTransaksi;
            this.listTransaksi = listTransaksi;
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
                    this.listTransaksi.setValue(new TransaksiList());
                } else {
                    this.listTransaksi.setValue(null);
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
            params.put("id_transaksi", idTransaksi);

            //returing the response
            return requestHandler.sendPostRequest(URLs.URL_DELETETRANSAKSI, params);
        }
    }

    class ListTransaksi extends AsyncTask<Void, Void, String> {
        private String idMasjid;
        private MutableLiveData<TransaksiList> listTransaksi;

        public ListTransaksi(MutableLiveData<TransaksiList> listTransaksi, String idMasjid) {
            this.idMasjid = idMasjid;
            this.listTransaksi = listTransaksi;
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
                    this.listTransaksi.setValue(gson.fromJson(s, TransaksiList.class));
                } else {
                    this.listTransaksi.setValue(null);
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
            return requestHandler.sendPostRequest(URLs.URL_LISTTRANSAKSI, params);
        }
    }


}
