package com.example.qayimmasjid.ui.pengumuman;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.example.qayimmasjid.model.masjid.Pengumuman;
import com.example.qayimmasjid.pengumuman.PengumumanViewModel;
import com.example.qayimmasjid.ui.kegiatan.EditKegiatanMasjidActivity;
import com.google.android.material.button.MaterialButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditPengumumanMasjidActivity extends AppCompatActivity {
    private EditText etNamaPengumuman, etDeskripsi, etCP;
    private MaterialButton btnCancel, btnSimpan;
    final Calendar calendar = Calendar.getInstance();
    private PengumumanViewModel pengumumanViewModel;
    private String idPengumuman, namaPengumuman, deskripsi, cp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pengumuman_masjid);

        pengumumanViewModel = new ViewModelProvider(this).get(PengumumanViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setupUI();
        getData();
        setData();
        setupListeners();
    }

    public void setupUI() {
        etNamaPengumuman          = findViewById(R.id.edit_field_nama_pengumuan);
        etDeskripsi               = findViewById(R.id.edit_field_deskripsi_pengumuman);
        etCP                      = findViewById(R.id.edit_field_cp_pengumuman);
        btnCancel                 = findViewById(R.id.btn_cancel_edit_pengumuman);
        btnSimpan                 = findViewById(R.id.btn_simpan_edit_pengumuman_masjid);
    }

    public void setupListeners() {
        //atMasjidListener();
        mbSimpanListener();
        mbCancelListener();
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

    public void validateEditKegiatan(){
        Bundle bundle = getIntent().getExtras();
        String namaPengumuman     = etNamaPengumuman.getText().toString().trim();
        String deskripsiPengumuman  = etDeskripsi.getText().toString().trim();
        String contactPerson    = etCP.getText().toString().trim();
        boolean isValidNamaPengumuman = true, isValidDeskripsiPengumuman = true, isValidCP = true;

        //Validate name
        if(TextUtils.isEmpty(namaPengumuman)){
            etNamaPengumuman.setError("Nama pengumuman tidak boleh kosong!");
            isValidNamaPengumuman = false;
        } else if(namaPengumuman.length() > 30){
            etNamaPengumuman.setError("Nama pengumuman tidak boleh lebih dari 30 huruf!");
            isValidNamaPengumuman = false;
        }

        //Validate email
        if (TextUtils.isEmpty(deskripsiPengumuman)){
            etDeskripsi.setError("Deskripsi tidak boleh kosong!");
            isValidDeskripsiPengumuman = false;
        } else if (deskripsiPengumuman.length() > 500){
            etDeskripsi.setError("maksimal 500 digit!");
            isValidDeskripsiPengumuman = false;
        }

        //Validate password
        Pattern pattern = Pattern.compile("^(\\+62|62|0)8[1-9][0-9]{6,9}$");
        Matcher matcher = pattern.matcher(etCP.getText().toString());
        if (TextUtils.isEmpty(contactPerson)) {
            etCP.setError("Contact Person harus diisi!");
            isValidCP = false;
        } else if (!matcher.matches()) {
            etCP.setError("Contact Person tidak valid!");
            isValidCP = false;
        }

        //Check that all the field is Valid
        if(isValidNamaPengumuman && isValidDeskripsiPengumuman && isValidCP) {
            //API tambah pengurus masjid
            Pengumuman pengumuman = new Pengumuman();
            pengumuman.setMidpengumuman(bundle.getString("idPengumuman"));
            pengumuman.setMnamapengumuman(etNamaPengumuman.getText().toString());
            pengumuman.setDeskripsiPengumuman(etDeskripsi.getText().toString());
            pengumuman.setContactPerson(etCP.getText().toString());
            pengumuman.setTanggalPengumuman(getCurrentDate());
            pengumuman.setWaktuPengumuman(getCurrentTime());
            Log.d("date", "tambahPengumuman: " + getCurrentDate() + " " + getCurrentTime());
            pengumumanViewModel.editPengumuman(pengumuman);
            pengumumanViewModel.getData().observe(this, pengumumanList -> {
                if (pengumumanList != null){
                    Toast.makeText(getApplicationContext(), "Pengumuman berhasil di edit", Toast.LENGTH_SHORT).show();
                    finish();
                } else{
                    Toast.makeText(getApplicationContext(), "Pengumuman gagal di edit", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Mohon untuk mengisi data dengan benar", Toast.LENGTH_SHORT).show();
        }
    }

    public void getData(){

        Bundle bundle = getIntent().getExtras();
        if(getIntent().hasExtra("idPengumuman")
                && getIntent().hasExtra("nama_pengumuman")
                && getIntent().hasExtra("deskripsi_pengumuman")
                && getIntent().hasExtra("contact_person"))
        {
            idPengumuman = bundle.getString("idPengumuman");
            namaPengumuman = bundle.getString("nama_pengumuman");
            deskripsi = bundle.getString("deskripsi_pengumuman");
            cp = bundle.getString("contact_person");
        }
    }

    public void setData(){
        etNamaPengumuman.setText(namaPengumuman);
        etDeskripsi.setText(deskripsi);
        etCP.setText(cp);
    }
}