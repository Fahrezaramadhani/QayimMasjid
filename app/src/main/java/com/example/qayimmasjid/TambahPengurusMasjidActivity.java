package com.example.qayimmasjid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.qayimmasjid.masjid.MasjidViewModel;
import com.example.qayimmasjid.model.masjid.Transaksi;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TambahPengurusMasjidActivity extends AppCompatActivity {

    EditText etNamaPengurus, etAlamat, etEmail, etPassword, etKonfirmasiPassword;
    TextView tvDaftarMasjid;
    AutoCompleteTextView atMasjid;
    MaterialButton mbCancel, mbTambah;
    private MasjidViewModel masjidViewModel;
    private List<String> listNamaMasjid = new ArrayList<>();
    private List<String> listIdMasjid = new ArrayList<>();
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pengurus_masjid);

        masjidViewModel = new ViewModelProvider(this).get(MasjidViewModel.class);



        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setupUI();
        setupListeners();

        setListMasjid();

//        String[] filterMasjid = getResources().getStringArray();
//        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, filterMasjid);
//        atMasjid.setAdapter(arrayAdapter);


//        atMasjid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus) {
//                    textInputLayout.setStartIconVisible(false);
//                } else {
//                    textInputLayout.setStartIconVisible(true);
//                }
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setListMasjid();
    }

    public void setListMasjid(){
        masjidViewModel.getListKegiatan(getApplicationContext());
        masjidViewModel.getList().observe(this, masjidList -> {
            Log.d("masjidList", "setListMasjid: " + masjidList);
            List<String> list1 = new ArrayList<>();
            List<String> list2 = new ArrayList<>();
            for (int i = 0; i < masjidList.getMmasjid().size(); i++){
                list1.add(masjidList.getMmasjid().get(i).getMnamamasjid());
                list2.add(masjidList.getMmasjid().get(i).getMid_masjid());
            }
            listNamaMasjid = list1;
            listIdMasjid = list2;
            arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, listNamaMasjid);
            atMasjid.setAdapter(arrayAdapter);
        });
    }

    public String getIdMasjid(String namaMasjid){
        for (int i = 0; i < listNamaMasjid.size(); i++){
            if (listNamaMasjid.get(i).matches(namaMasjid)){
                return listIdMasjid.get(i);
            }
        }
        return null;
    }


    public void setupUI() {
        etNamaPengurus          = findViewById(R.id.tambah_nama_pengurus);
        etAlamat                = findViewById(R.id.tambah_alamat_pengurus);
        etEmail                 = findViewById(R.id.tambah_email_baru);
        etPassword              = findViewById(R.id.tambah_password_baru);
        etKonfirmasiPassword    = findViewById(R.id.tambah_konfirmasi_password);
        atMasjid                = findViewById(R.id.field_filter_pengurus);
        tvDaftarMasjid          = findViewById(R.id.tv_daftar_masjid_baru);
        mbCancel                = findViewById(R.id.btn_cancel_daftar_pengurus);
        mbTambah                = findViewById(R.id.btn_tambah_pengurus);
    }

    public void setupListeners() {
        //atMasjidListener();
        tvDaftarMasjidListener();
        mbTambahListener();
        mbCancelListener();
    }

    public void mbCancelListener(){
        mbCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void tvDaftarMasjidListener() {
        tvDaftarMasjid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToNextActivity = new Intent(getApplicationContext(), TambahMasjidActivity.class);
                startActivity(goToNextActivity);
            }
        });
    }

    public void mbTambahListener() {
        mbTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateTambahPengurus();
            }
        });
    }

    public void validateTambahPengurus(){
        String namaPengurus = etNamaPengurus.getText().toString().trim();
        String email        = etEmail.getText().toString().trim();
        String alamat       = etAlamat.getText().toString().trim();
        String idMasjid       = getIdMasjid(atMasjid.getText().toString().trim());
        String password     = etPassword.getText().toString().trim();
        String konfirmasiPassword = etKonfirmasiPassword.getText().toString().trim();
        boolean isValidNamaPengurus = true, isValidEmail = true, isValidPassword = true, isValidConfirmationPassword = true, isValidAlamat = true, isValidMasjid = true;

        if (atMasjid.getText().toString().matches("pilih masjid")){
            atMasjid.setError("Masjid tidak boleh kosong!");
            isValidMasjid = false;
        }

        //Validate name
        if(TextUtils.isEmpty(namaPengurus)){
            etNamaPengurus.setError("nama pengurus tidak boleh kosong!");
            isValidNamaPengurus = false;
        } else if(namaPengurus.length() > 30){
            etNamaPengurus.setError("nama pengurus tidak boleh lebih dari 30 huruf!");
            isValidNamaPengurus = false;
        }

        //Validate email
        if (TextUtils.isEmpty(email)){
            etEmail.setError("Email tidak boleh kosong!");
            isValidEmail = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Email tidak valid!");
            isValidEmail = false;
        }

        //Validate password
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password tidak boleh kosong!");
            isValidPassword = false;
        } else if (password.length() < 8) {
            etPassword.setError("Password harus memiliki minimal 8 digit!");
            isValidPassword = false;
        }

        //Validate confirmation password
        if (TextUtils.isEmpty(konfirmasiPassword)) {
            etKonfirmasiPassword.setError("Konfirmasi password harus diisi!");
            isValidConfirmationPassword = false;
        } else if (!konfirmasiPassword.matches(password)){
            etKonfirmasiPassword.setError("Konfirmasi Password tidak sesuai!");
            isValidConfirmationPassword = false;
        }

        //Validate alamat
        if (TextUtils.isEmpty(alamat)) {
            etAlamat.setError("Alamat pengurus tidak boleh kosong!");
            isValidAlamat = false;
        } else if (alamat.length() > 100){
            etAlamat.setError("Alamat pengurus maksimal 100 digit!");
            isValidAlamat = false;
        }

        //Check that all the field is Valid
        if(isValidNamaPengurus && isValidEmail && isValidPassword && isValidConfirmationPassword && isValidAlamat && isValidMasjid) {
            //API tambah pengurus masjid
            registerPengurusMasjidBaru(namaPengurus, idMasjid, alamat, email, password);
        } else {
            Toast.makeText(TambahPengurusMasjidActivity.this, "Mohon untuk mengisi data dengan benar", Toast.LENGTH_SHORT).show();
        }
    }

    public void registerPengurusMasjidBaru(String namaPengurus, String idMasjid, String alamat, String email, String password) {

        class Tambah extends AsyncTask<Void, Void, String> {
//            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("id_masjid", idMasjid);
                params.put("nama_pengurus", namaPengurus);
                params.put("alamat_pengurus", alamat);
                params.put("email", email);
                params.put("password", password);

                //returning the response
                return requestHandler.sendPostRequest("https://qayimmasjid.com/API/qayimmasjid/tambahPengurusMasjid.php", params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
//                progressBar = (ProgressBar) findViewById(R.id.progressBarkegiatan);
//                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
//                progressBar.setVisibility(View.GONE);
                Log.d("response", "onPostExecute: " + s);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        Tambah ru = new Tambah();
        ru.execute();
    }
}