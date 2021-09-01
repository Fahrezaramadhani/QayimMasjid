package com.example.qayimmasjid.ui.kegiatan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.qayimmasjid.R;
import com.example.qayimmasjid.RequestHandler;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.kegiatan.KegiatanViewModel;
import com.example.qayimmasjid.model.masjid.Kegiatan;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class TambahKegiatanMasjidActivity extends AppCompatActivity {

    private EditText etNamaKegiatan, etLokasiKegiatan, etPemateriKegiatan, etPJKegiatan;
    private TextView tvTanggalKegiatan, tvWaktuKegiatan;
    private RadioButton rbLuarMasjid, rbDalamMasjid;
    private MaterialButton btnCancel, btnTambah;
    final Calendar calendar = Calendar.getInstance();
    private String tempatKegiatan, tanggalKegiatan, waktuKegiatan;
    private KegiatanViewModel kegiatanViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kegiatan_masjid);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        kegiatanViewModel = new ViewModelProvider(this).get(KegiatanViewModel.class);

        setupUI();
        setupListeners();

    }

    private void setupUI() {
        etNamaKegiatan      = findViewById(R.id.et_nama_kegiatan);
        tvTanggalKegiatan   = findViewById(R.id.tv_tanggal_kegiatan);
        tvWaktuKegiatan     = findViewById(R.id.tv_waktu_kegiatan);
        etLokasiKegiatan    = findViewById(R.id.et_lokasi_kegiatan);
        etPemateriKegiatan  = findViewById(R.id.et_pemateri_kegiatan);
        etPJKegiatan        = findViewById(R.id.et_pj_kegiatan);
        rbLuarMasjid        = findViewById(R.id.luar_masjid);
        rbDalamMasjid       = findViewById(R.id.dalam_masjid);
        btnCancel           = findViewById(R.id.btn_cancel_tambah_kegiatan_masjid);
        btnTambah           = findViewById(R.id.btn_tambah_kegiatan_masjid);
    }

    private void setupListeners() {
        btnCancelListener();
        btnTambahListener();
        tvTanggalKegiatanListener();
        tvWaktuKegiatanListener();
    }

    public boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public void validateField() {
        boolean isValidNamaKegiatan = true, isValidTanggal = true, isValidWaktu = true, isValidLokasi = true,
                isValidPemateri = true, isValidPJ = true;

        //Validate Nama Kegiatan
        if(isEmpty(etNamaKegiatan)){
            etNamaKegiatan.setError("nama kegiatan harus diisi!");
            isValidNamaKegiatan = false;
        } else if(etNamaKegiatan.getText().toString().length() > 30){
            etNamaKegiatan.setError("maksimal 30 digit!");
            isValidNamaKegiatan = false;
        }

        //Validate tanggal kegiatan
        if(tanggalKegiatan == null){
            tvTanggalKegiatan.setError("tanggal kegiatan harus diisi!");
            isValidTanggal = false;
        }

        //Validate waktu kegiatan
        if(waktuKegiatan == null){
            tvWaktuKegiatan.setError("waktu kegiatan harus diisi!");
            isValidWaktu = false;
        }

        //Validate Lokasi Kegiatan
        if(isEmpty(etLokasiKegiatan)){
            etLokasiKegiatan.setError("lokasi kegiatan harus diisi!");
            isValidLokasi = false;
        } else if(etLokasiKegiatan.getText().toString().length() > 30){
            etLokasiKegiatan.setError("maksimal 30 digit!");
            isValidLokasi = false;
        }

        //Validate Pemateri Kegiatan
        if(isEmpty(etPemateriKegiatan)){
            etPemateriKegiatan.setError("isi dengan'-' jika tidak ada pemateri!");
            isValidPemateri = false;
        } else if(etPemateriKegiatan.getText().toString().length() > 30){
            etPemateriKegiatan.setError("maksimal 30 digit!");
            isValidPemateri = false;
        }

        //Validate PJ Kegiatan
        if(isEmpty(etPJKegiatan)){
            etPJKegiatan.setError("penanggungjawab harus diisi!");
            isValidPJ = false;
        } else if(etPJKegiatan.getText().toString().length() > 30){
            etPJKegiatan.setError("maksimal 30 digit!");
            isValidPJ = false;
        }

        //Cek semua data apakah sudah valid atau belum, jika data valid maka tambahkan kegiatan
        if(isValidNamaKegiatan && isValidTanggal && isValidWaktu && isValidLokasi && isValidPemateri && isValidPJ && (tempatKegiatan != null)) {
            tambahKegiatan();
        } else {
            Toast.makeText(TambahKegiatanMasjidActivity.this, "data belum valid! mohon cek kembali data anda!", Toast.LENGTH_SHORT).show();
        }
    }

    public void luarMasjidOnClick(View v) {
        tempatKegiatan = "Luar Masjid";
    }

    public void dalamMasjidOnClick(View v) {
        tempatKegiatan = "Dalam Masjid";
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
                tanggalKegiatan = tvTanggalKegiatan.getText().toString().trim();
            }
        };

        tvTanggalKegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TambahKegiatanMasjidActivity.this, date, calendar
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(TambahKegiatanMasjidActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tvWaktuKegiatan.setText(selectedHour + ":" + selectedMinute + ":00");
                        waktuKegiatan = tvWaktuKegiatan.getText().toString().trim();
                    }
                }, hour, minute, true);//Yes 24 hour time
                timePickerDialog.show();
            }
        });

    }

    public void btnCancelListener() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void btnTambahListener() {
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateField();
            }
        });
    }

    public void tambahKegiatan() {
        Kegiatan kegiatan = new Kegiatan();
        kegiatan.setMidmasjid(SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
        kegiatan.setMnamakegiatan(etNamaKegiatan.getText().toString().trim());
        kegiatan.setTanggalKegiatan(tanggalKegiatan);
        kegiatan.setMwaktukegiatan(waktuKegiatan);
        kegiatan.setMlokasikegiatan(etLokasiKegiatan.getText().toString().trim());
        kegiatan.setMpemateri(etPemateriKegiatan.getText().toString().trim());
        kegiatan.setMpenanggungjawab(etPJKegiatan.getText().toString().trim());
        kegiatan.setTempatKegiatan(tempatKegiatan);
        kegiatanViewModel.tambahKegiatan(kegiatan);
        kegiatanViewModel.getData().observe(this, kegiatanList -> {
            if (kegiatanList != null){
                Toast.makeText(getApplicationContext(), "Kegiatan berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Kegiatan gagal ditambahkan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}