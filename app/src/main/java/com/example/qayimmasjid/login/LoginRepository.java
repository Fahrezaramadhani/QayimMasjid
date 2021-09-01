package com.example.qayimmasjid.login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.qayimmasjid.MainActivityPengurusMasjid;
import com.example.qayimmasjid.RequestHandler;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.URLs;
import com.example.qayimmasjid.apiWaktuSholat.RetrofitService;
import com.example.qayimmasjid.apiWaktuSholat.WaktuSholatAPI;
import com.example.qayimmasjid.apiWaktuSholat.WaktuSholatRepository;
import com.example.qayimmasjid.model.masjid.Login;
import com.example.qayimmasjid.model.masjid.Masjid;
import com.example.qayimmasjid.model.masjid.Pengurus;
import com.example.qayimmasjid.model.waktusholatapi.ResponseApi;
import com.example.qayimmasjid.model.waktusholatapi.Results;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private static LoginRepository loginRepository;
    MutableLiveData<Login> loginData = new MutableLiveData<>();

    public MutableLiveData<Login> getLoginData(String email, String password){
        UserLogin userLogin = new UserLogin(loginData, email, password);
        userLogin.execute();

        return loginData;
    }

    class UserLogin extends AsyncTask<Void, Void, String> {
        private MutableLiveData<Login> logindata;
        private String email;
        private String password;

        public UserLogin (MutableLiveData<Login> logindata,String email, String password){
            this.logindata = logindata;
            this.email = email;
            this.password = password;
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
                    this.logindata.setValue(gson.fromJson(s,Login.class));
                } else {
                    this.logindata.setValue(null);
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
            params.put("email_pengurus", email);
            params.put("password", password);

            //returing the response
            return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
        }
    }
}
