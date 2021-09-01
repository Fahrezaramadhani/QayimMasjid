package com.example.qayimmasjid.ui.profil;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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

import com.example.qayimmasjid.R;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.TambahMasjidActivity;
import com.example.qayimmasjid.TambahPengurusMasjidActivity;
import com.example.qayimmasjid.masjid.MasjidViewModel;
import com.example.qayimmasjid.model.masjid.Pengurus;
import com.example.qayimmasjid.model.masjid.ProfileMasjid;
import com.example.qayimmasjid.profil.ProfilMasjidViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class EditProfilPengurusMasjidActivity extends AppCompatActivity {
    EditText etNamaPengurus, etAlamat, etEmail, etPassword, etKonfirmasiPassword;
    AutoCompleteTextView atMasjid;
    MaterialButton mbCancel, mbTambah;
    private ProfilMasjidViewModel profilMasjidViewModel;
    private MasjidViewModel masjidViewModel;
    private ArrayList<String> listNamaMasjid = new ArrayList<>();
    private ArrayList<String> listIdMasjid = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_pengurus_masjid);

        masjidViewModel = new ViewModelProvider(this).get(MasjidViewModel.class);
        profilMasjidViewModel = new ViewModelProvider(this).get(ProfilMasjidViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setupUI();
        setupListeners();
        setData();
        setupNamaMasjid();
    }

    public void setupUI() {
        etNamaPengurus          = findViewById(R.id.edit_nama_pengurus);
        etAlamat                = findViewById(R.id.edit_alamat_pengurus);
        etEmail                 = findViewById(R.id.edit_email_baru);
        etPassword              = findViewById(R.id.edit_password_baru);
        etKonfirmasiPassword    = findViewById(R.id.edit_konfirmasi_password);
        atMasjid                = findViewById(R.id.field_filter_edit_pengurus);
        mbCancel                = findViewById(R.id.btn_cancel_edit_pengurus);
        mbTambah                = findViewById(R.id.btn_simpan_pengurus);
    }

    public void setupListeners() {
        //atMasjidListener();
        mbTambahListener();
        mbCancelListener();
    }

    public void setupNamaMasjid(){
        masjidViewModel.getListKegiatan(getApplicationContext());
        masjidViewModel.getList().observe(this, masjidList -> {
            for (int i = 0; i < masjidList.getMmasjid().size(); i++){
                listNamaMasjid.add(masjidList.getMmasjid().get(i).getMnamamasjid());
                listIdMasjid.add(masjidList.getMmasjid().get(i).getMid_masjid());
            }
            atMasjid.setText(getNamaMasjid(SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid()));
        });
    }

    public String getNamaMasjid(String idMasjid){
        for (int i = 0; i < listIdMasjid.size(); i++){
            if (listIdMasjid.get(i).matches(idMasjid)){
                return listNamaMasjid.get(i);
            }
        }
        return null;
    }

    public void mbCancelListener(){
        mbCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
            Pengurus pengurus = new Pengurus();
            pengurus.setMidpengurus(SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidpengurus());
            pengurus.setMnamapengurus(namaPengurus);
            pengurus.setAlamatPengurus(alamat);
            pengurus.setMemailpengurus(email);
            pengurus.setPasswordPengurus(password);
            profilMasjidViewModel.updateAccount(pengurus);
            profilMasjidViewModel.getData().observe(this, profileMasjid -> {
                if (profileMasjid != null){
                    Toast.makeText(getApplicationContext(), "Profil pengurus masjid berhasil di update", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Profil pengurus masjid gagal di update", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Mohon untuk mengisi data dengan benar", Toast.LENGTH_SHORT).show();
        }
    }


    private void setData(){
        profilMasjidViewModel.getProfilMasjid(SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
        profilMasjidViewModel.getData().observe(this, profileMasjid -> {
            int index = getIndexPengurus(profileMasjid, SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidpengurus());
            Log.d("index", "setProfilPengurus: " + index + SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidpengurus() + profileMasjid.getListPengurus().size());
            etNamaPengurus.setText(profileMasjid.getListPengurus().get(index).getMnamapengurus());
            etAlamat.setText(profileMasjid.getListPengurus().get(index).getAlamatPengurus());
            etEmail.setText(profileMasjid.getListPengurus().get(index).getMemailpengurus());
        });
    }

    private int getIndexPengurus(ProfileMasjid profileMasjid, String idPengurus){
        int i;
        for (i = 0; i < profileMasjid.getListPengurus().size(); i++){
            Log.d("cari", "getIndexPengurus: " + profileMasjid.getListPengurus().size());
            if (profileMasjid.getListPengurus().get(i).getMidpengurus().matches(idPengurus)){
                Log.d("cari", "getIndexPengurus: " + profileMasjid.getListPengurus().get(i).getMidpengurus());
                return i;
            }
        }
        return i;
    }
}