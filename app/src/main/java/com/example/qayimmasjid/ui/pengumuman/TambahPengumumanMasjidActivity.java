package com.example.qayimmasjid.ui.pengumuman;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qayimmasjid.R;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.model.masjid.Pengumuman;
import com.example.qayimmasjid.pengumuman.PengumumanViewModel;
import com.example.qayimmasjid.ui.kegiatan.TambahKegiatanMasjidActivity;
import com.google.android.material.button.MaterialButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TambahPengumumanMasjidActivity extends AppCompatActivity {
    private MaterialButton btnCancel, btnTambah;
    private EditText etNamaPengumuman, etDeskripsi, etCP;
    private PengumumanViewModel pengumumanViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pengumuman_masjid);

        pengumumanViewModel = new ViewModelProvider(this).get(PengumumanViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setupUI();
        setupListeners();
    }

    private void setupUI() {
        btnTambah = findViewById(R.id.btn_tambah_pengumuman_masjid);
        btnCancel = findViewById(R.id.btn_cancel_tambah_pengumuman);
        etNamaPengumuman = findViewById(R.id.field_nama_pengumuman);
        etDeskripsi = findViewById(R.id.field_deskripsi_pengumuman);
        etCP = findViewById(R.id.field_cp_pengumuman);
    }

    private void setupListeners() {
        btnCancelListener();
        btnTambahListener();
    }

    private void btnCancelListener() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void btnTambahListener() {
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
                //validateform
            }
        });
    }

    public boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public void validateForm() {
        boolean isValidNamaPengumuman = true, isValidDeskripsiPengumuman = true, isValidCP = true;

        //Validate Nama Pengumuman
        if (isEmpty(etNamaPengumuman)) {
            etNamaPengumuman.setError("nama pengumuman harus diisi!");
            isValidNamaPengumuman = false;
        } else if (etNamaPengumuman.getText().toString().length() > 30) {
            etNamaPengumuman.setError("maksimal 30 digit!");
            isValidNamaPengumuman = false;
        }

        //Validate Deskripsi Pengumuman
        if (isEmpty(etDeskripsi)) {
            etDeskripsi.setError("Deskripsi harus diisi!");
            isValidDeskripsiPengumuman = false;
        } else if (etDeskripsi.getText().toString().length() > 500) {
            etDeskripsi.setError("maksimal 500 digit!");
            isValidDeskripsiPengumuman = false;
        }

        //Validate CP Pengumuman
        Pattern pattern = Pattern.compile("^(\\+62|62|0)8[1-9][0-9]{6,9}$");
        Matcher matcher = pattern.matcher(etCP.getText().toString());
        if (isEmpty(etCP)) {
            etCP.setError("Contact Person harus diisi!");
            isValidCP = false;
        } else if (!matcher.matches()) {
            etCP.setError("Contact Person tidak valid!");
            isValidCP = false;
        }

        //Cek semua data apakah sudah valid atau belum, jika data valid maka tambahkan kegiatan
        if (isValidNamaPengumuman && isValidDeskripsiPengumuman && isValidCP) {
            tambahPengumuman();
        } else {
            Toast.makeText(TambahPengumumanMasjidActivity.this, "data belum valid! mohon cek kembali data anda!", Toast.LENGTH_SHORT).show();
        }
    }

    public String getCurrentDate() {
        Date date = new Date();
        String currDate    = new SimpleDateFormat("yyyy-MM-dd").format(date);

        return currDate;
    }

    public String getCurrentTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("kk:mm:ss");
        // you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+7:00"));

        String localTime = date.format(currentLocalTime);

        return localTime;
    }

    private void tambahPengumuman(){
        Pengumuman pengumuman = new Pengumuman();
        pengumuman.setIdMasjid(SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
        pengumuman.setMnamapengumuman(etNamaPengumuman.getText().toString());
        pengumuman.setDeskripsiPengumuman(etDeskripsi.getText().toString());
        pengumuman.setContactPerson(etCP.getText().toString());
        pengumuman.setTanggalPengumuman(getCurrentDate());
        pengumuman.setWaktuPengumuman(getCurrentTime());
        Log.d("date", "tambahPengumuman: " + getCurrentDate() + " " + getCurrentTime());
        pengumumanViewModel.tambahPengumuman(pengumuman);
        pengumumanViewModel.getData().observe(this, pengumumanList -> {
            if (pengumumanList != null){
                Toast.makeText(getApplicationContext(), "Pengumuman berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                finish();
            } else{
                Toast.makeText(getApplicationContext(), "Pengumuman gagal ditambahkan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}