package com.example.qayimmasjid.ui.kegiatan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.qayimmasjid.R;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.kegiatan.KegiatanViewModel;
import com.example.qayimmasjid.model.masjid.Kegiatan;
import com.example.qayimmasjid.model.masjid.Pengurus;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditKegiatanMasjidActivity extends AppCompatActivity {
    private EditText etNamaKegiatan, etLokasi, etPemateri, etPJ, etTempat;
    private TextView tvTanggalKegiatan, tvWaktuKegiatan;
    private MaterialButton btnCancel, btnSimpan;
    private RadioButton rbLuarMasjid, rbDalamMasjid;
    private String idKegiatan, namaKegiatan, tanggalKegiatan, waktuKegiatan, lokasiKegiatan, pemateri, pj, tempat;
    private String tempatKegiatan;
    final Calendar calendar = Calendar.getInstance();
    private KegiatanViewModel kegiatanViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kegiatan_masjid);

        kegiatanViewModel = new ViewModelProvider(this).get(KegiatanViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setupUI();
        setupListeners();
        getData();
        setData();
    }

    public void setupUI() {
        etNamaKegiatan          = findViewById(R.id.et_edit_nama_kegiatan);
        tvTanggalKegiatan       = findViewById(R.id.tv_edit_tanggal_kegiatan);
        tvWaktuKegiatan         = findViewById(R.id.tv_edit_waktu_kegiatan);
        etLokasi                = findViewById(R.id.et_edit_lokasi_kegiatan);
        etPemateri              = findViewById(R.id.et_edit_pemateri_kegiatan);
        etPJ                    = findViewById(R.id.et_edit_pj_kegiatan);
        btnCancel               = findViewById(R.id.btn_cancel_edit_kegiatan_masjid);
        btnSimpan               = findViewById(R.id.btn_Simpan_edit_kegiatan_masjid);
        rbLuarMasjid            = findViewById(R.id.luar_masjid_edit);
        rbDalamMasjid           = findViewById(R.id.dalam_masjid_edit);
    }

    public void setupListeners() {
        //atMasjidListener();
        mbSimpanListener();
        mbCancelListener();
        tvTanggalKegiatanListener();
        tvWaktuKegiatanListener();
    }

    public void tvTanggalKegiatanListener() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                tvTanggalKegiatan.setText(sdf.format(calendar.getTime()));
            }
        };

        tvTanggalKegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditKegiatanMasjidActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void tvWaktuKegiatanListener() {

        tvWaktuKegiatan.setOnClickListener(new View.OnClickListener() {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditKegiatanMasjidActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tvWaktuKegiatan.setText(selectedHour + ":" + selectedMinute + ":00");
                    }
                }, hour, minute, true);//Yes 24 hour time
                timePickerDialog.show();
            }
        });

    }

    public void mbCancelListener(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void mbSimpanListener() {
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEditKegiatan();
            }
        });
    }

    public void validateEditKegiatan(){
        Bundle bundle = getIntent().getExtras();
        String namaKegiatan     = etNamaKegiatan.getText().toString().trim();
        String tanggalKegiatan  = tvTanggalKegiatan.getText().toString().trim();
        String waktuKegiatan    = tvWaktuKegiatan.getText().toString().trim();
        String lokasiKegiatan   = etLokasi.getText().toString().trim();
        String pemateriKegiatan = etPemateri.getText().toString().trim();
        String pjKegiatan       = etPJ.getText().toString().trim();
        boolean isValidNamaKegiatan = true, isValidTanggal = true, isValidWaktu = true, isValidLokasi = true, isValidPemateri = true, isValidPJKegiatan = true;

        //Validate name
        if(TextUtils.isEmpty(namaKegiatan)){
            etNamaKegiatan.setError("Nama kegiatan tidak boleh kosong!");
            isValidNamaKegiatan = false;
        } else if(namaKegiatan.length() > 30){
            etNamaKegiatan.setError("Nama kegiatan tidak boleh lebih dari 30 huruf!");
            isValidNamaKegiatan = false;
        }

        //Validate email
        if (TextUtils.isEmpty(tanggalKegiatan)){
            tvTanggalKegiatan.setError("Email tidak boleh kosong!");
            isValidTanggal = false;
        }

        //Validate password
        if (TextUtils.isEmpty(waktuKegiatan)) {
            tvWaktuKegiatan.setError("Password tidak boleh kosong!");
            isValidWaktu = false;
        }

        //Validate alamat
        if (TextUtils.isEmpty(lokasiKegiatan)) {
            etLokasi.setError("Lokasi kegiatan tidak boleh kosong!");
            isValidLokasi = false;
        } else if (lokasiKegiatan.length() > 30){
            etLokasi.setError("Lokasi kegiatan tidak boleh lebih dari 30 huruf!");
            isValidLokasi = false;
        }

        //Validate pemateri
        if (TextUtils.isEmpty(pemateriKegiatan)) {
            etPemateri.setError("Pemateri tidak boleh kosong!");
            isValidPemateri = false;
        } else if (pemateriKegiatan.length() > 30){
            etLokasi.setError("Pemateri tidak boleh lebih dari 30 huruf!");
            isValidPemateri = false;
        }

        //Validate
        if (TextUtils.isEmpty(pjKegiatan)) {
            etPJ.setError("PJ kegiatan tidak boleh kosong!");
            isValidPJKegiatan = false;
        } else if (pjKegiatan.length() > 30){
            etPJ.setError("PJ kegiatan tidak boleh lebih dari 30 huruf!");
            isValidPJKegiatan = false;
        }

        //Check that all the field is Valid
        if(isValidNamaKegiatan && isValidTanggal && isValidWaktu && isValidLokasi && isValidPemateri && isValidPJKegiatan && (tempatKegiatan != null)) {
            //API tambah pengurus masjid
            Kegiatan kegiatan = new Kegiatan();
            kegiatan.setMidkegiatan(bundle.getString("idKegiatan"));
            kegiatan.setMnamakegiatan(namaKegiatan);
            Log.d("nama kegiatan", "validateEditKegiatan: " + kegiatan.getMnamakegiatan());
            kegiatan.setTanggalKegiatan(tanggalKegiatan);
            kegiatan.setMwaktukegiatan(waktuKegiatan);
            kegiatan.setMlokasikegiatan(lokasiKegiatan);
            kegiatan.setMpemateri(pemateriKegiatan);
            kegiatan.setMpenanggungjawab(pjKegiatan);
            kegiatan.setTempatKegiatan(tempatKegiatan);
            kegiatanViewModel.editKegiatan(kegiatan);
            kegiatanViewModel.getData().observe(this, kegiatanList -> {
                if (kegiatanList != null){
                    Toast.makeText(getApplicationContext(), "kegiatan berhasil di update", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Kegiatan gagal di update", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Mohon untuk mengisi data dengan benar", Toast.LENGTH_SHORT).show();
        }
    }

    public void luarMasjidOnClick(View v) {
        tempatKegiatan = "Luar Masjid";
    }

    public void dalamMasjidOnClick(View v) {
        tempatKegiatan = "Dalam Masjid";
    }

    public void getData(){

        Bundle bundle = getIntent().getExtras();
        if(getIntent().hasExtra("idKegiatan")
                && getIntent().hasExtra("nama")
                && getIntent().hasExtra("tanggal")
                && getIntent().hasExtra("waktu")
                && getIntent().hasExtra("lokasi")
                && getIntent().hasExtra("pemateri")
                && getIntent().hasExtra("pj")
                && getIntent().hasExtra("tempat"))
        {
            idKegiatan = bundle.getString("idKegiatan");
            namaKegiatan = bundle.getString("nama");
            tanggalKegiatan = bundle.getString("tanggal");
            waktuKegiatan = bundle.getString("waktu");
            lokasiKegiatan = bundle.getString("lokasi");
            pemateri = bundle.getString("pemateri");
            pj = bundle.getString("pj");
            tempat = bundle.getString("tempat");
        }
    }

    public void setData(){
        etNamaKegiatan.setText(namaKegiatan);
        tvTanggalKegiatan.setText(tanggalKegiatan);
        tvWaktuKegiatan.setText(waktuKegiatan);
        etLokasi.setText(lokasiKegiatan);
        etPemateri.setText(pemateri);
        etPJ.setText(pj);
        if (tempat.matches("Luar Masjid")){
            rbLuarMasjid.performClick();
        } else{
            rbDalamMasjid.performClick();
        }
    }
}