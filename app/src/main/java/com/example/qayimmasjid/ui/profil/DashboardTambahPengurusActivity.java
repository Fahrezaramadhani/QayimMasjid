package com.example.qayimmasjid.ui.profil;

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

import com.example.qayimmasjid.R;
import com.example.qayimmasjid.RequestHandler;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.TambahMasjidActivity;
import com.example.qayimmasjid.TambahPengurusMasjidActivity;
import com.example.qayimmasjid.masjid.MasjidViewModel;
import com.example.qayimmasjid.profil.ProfilMasjidViewModel;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardTambahPengurusActivity extends AppCompatActivity {
    EditText etNamaPengurus, etAlamat, etEmail, etPassword, etKonfirmasiPassword;
    TextView tvDaftarMasjid;
    AutoCompleteTextView atMasjid;
    MaterialButton mbCancel, mbTambah;
    private ProfilMasjidViewModel profilMasjidViewModel;
    private List<String> listNamaMasjid = new ArrayList<>();
    private List<String> listIdMasjid = new ArrayList<>();
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pengurus_masjid);


        profilMasjidViewModel = new ViewModelProvider(this).get(ProfilMasjidViewModel.class);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setupUI();
        setupListeners();
        setNamaMasjid();
    }

    public void setNamaMasjid(){
        profilMasjidViewModel.getProfilMasjid(SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
        profilMasjidViewModel.getData().observe(this, profileMasjid -> {
            if (profileMasjid != null){
                atMasjid.setText(profileMasjid.getMasjid().getMnamamasjid());
            }
        });
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
        String idMasjid       = SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid();
        String password     = etPassword.getText().toString().trim();
        String konfirmasiPassword = etKonfirmasiPassword.getText().toString().trim();
        boolean isValidNamaPengurus = true, isValidEmail = true, isValidPassword = true, isValidConfirmationPassword = true, isValidAlamat = true;

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
        if(isValidNamaPengurus && isValidEmail && isValidPassword && isValidConfirmationPassword && isValidAlamat) {
            //API tambah pengurus masjid
            registerPengurusMasjidBaru(namaPengurus, idMasjid, alamat, email, password);
        } else {
            Toast.makeText(getApplicationContext(), "Mohon untuk mengisi data dengan benar", Toast.LENGTH_SHORT).show();
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
