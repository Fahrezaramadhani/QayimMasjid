package com.example.qayimmasjid.ui.transaksi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qayimmasjid.R;
import com.example.qayimmasjid.model.masjid.Transaksi;
import com.example.qayimmasjid.transaksi.TransaksiViewModel;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditTransaksiMasjidActivity extends AppCompatActivity {
    private EditText etNominalTransaksi, etNamaTransaksi;
    private AutoCompleteTextView actvListKegiatan;
    private TextView tvTanggalTransaksi;
    private String tanggalTransaksi, jenisTransaksi, idTransaksi, namaKegiatan, namaTransaksi, nominal;
    private MaterialButton btnCancel, btnSimpan;
    final Calendar calendar = Calendar.getInstance();
    private TransaksiViewModel transaksiViewModel;
    private RadioButton rb_pemasukan, rb_pengeluaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaksi_masjid);

        transaksiViewModel = new ViewModelProvider(this).get(TransaksiViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setupUI();
        getData();
        setData();
        setupListeners();
    }

    public void setupUI() {
        actvListKegiatan            = findViewById(R.id.field_filter_transaksi);
        etNamaTransaksi           = findViewById(R.id.et_edit_nama_transaksi);
        etNominalTransaksi        = findViewById(R.id.et_edit_nominal_transaksi);
        tvTanggalTransaksi        = findViewById(R.id.tv_edit_tanggal_transaksi);
        btnCancel                 = findViewById(R.id.btn_cancel_edit_transaksi_masjid);
        btnSimpan                 = findViewById(R.id.btn_edit_transaksi_masjid);
        rb_pemasukan              = findViewById(R.id.edit_pemasukan);
        rb_pengeluaran            = findViewById(R.id.edit_pengeluaran);
    }

    public void setupListeners() {
        //atMasjidListener();
        mbSimpanListener();
        mbCancelListener();
        tvTanggalTransaksiListener();
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

    private void tvTanggalTransaksiListener() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                tvTanggalTransaksi.setText(sdf.format(calendar.getTime()));
                tanggalTransaksi = tvTanggalTransaksi.getText().toString().trim();
            }
        };
        //create listener for create date picker
        tvTanggalTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditTransaksiMasjidActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void editPengeluaranOnClick(View v) {
        jenisTransaksi = "pengeluaran";
    }

    public void editPemasukanOnClick(View v) {
        jenisTransaksi = "pemasukan";
    }

    public void validateEditKegiatan(){
        String namaTransaksi    = etNamaTransaksi.getText().toString().trim();
        String nominalTransaksi = etNominalTransaksi.getText().toString().trim();

        boolean isValidNamaTransaksi = true, isValidTanggalTransaksi = true, isValidMasjid = true, isValidNominalTransaksi = true, isValidJenisTransaksi = true;

        //Validate Masjid
        if(actvListKegiatan.getText().toString().trim().matches("pilih kegiatan")) {
            actvListKegiatan.setError("Masjid harus dipilih!");
            isValidMasjid = false;
        }

        //Validate Nama Transaksi
        if(TextUtils.isEmpty(namaTransaksi)) {
            etNamaTransaksi.setError("Nama transaksi harus diisi!");
            isValidNamaTransaksi = false;
        } else if(etNamaTransaksi.getText().toString().length() > 30) {
            etNamaTransaksi.setError("Nama transaksi maksimal 30 digit!");
            isValidNamaTransaksi = false;
        }

        //Validate Tanggal Transaksi
        if(tanggalTransaksi == null) {
            tvTanggalTransaksi.setError("Tanggal transaksi harus diisi!");
            isValidTanggalTransaksi = false;
        }

        //Validate Nominal Transaksi
        if(TextUtils.isEmpty(nominalTransaksi)) {
            etNominalTransaksi.setError("Nominal transaksi harus diisi!");
            isValidNominalTransaksi = false;
        } else if(!etNominalTransaksi.getText().toString().matches("[0-9]+")){
            etNominalTransaksi.setError("Nominal transaksi hanya diisi oleh angka");
        } else if(etNominalTransaksi.getText().toString().length() > 11) {
            etNominalTransaksi.setError("Nominal transaksi maksimal 11 digit!");
            isValidNominalTransaksi = false;
        }
        //Validate Jenis Transaksi
        if (jenisTransaksi == null) {
            isValidJenisTransaksi = false;
        }

        //Check that all the field is Valid
        if(isValidMasjid && isValidNamaTransaksi && isValidTanggalTransaksi && isValidNominalTransaksi && isValidJenisTransaksi) {
            //API tambah pengurus masjid
            Transaksi transaksi = new Transaksi();
            transaksi.setMidtransaksi(idTransaksi);
            transaksi.setMnamatransaksi(etNamaTransaksi.getText().toString());
            transaksi.setMnominal(Integer.valueOf(etNominalTransaksi.getText().toString()));
            transaksi.setMtanggal(tanggalTransaksi);
            transaksi.setMjenistransaksi(jenisTransaksi);
            transaksiViewModel.editTransaksi(transaksi);
            transaksiViewModel.getData().observe(this, transaksiList -> {
                if (transaksiList != null){
                    Toast.makeText(getApplicationContext(), "Transaksi berhasil di edit", Toast.LENGTH_SHORT).show();
                    finish();
                } else{
                    Toast.makeText(getApplicationContext(), "Transaksi gagal di edit", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Mohon untuk mengisi data dengan benar", Toast.LENGTH_SHORT).show();
        }
    }

    public void getData(){

        Bundle bundle = getIntent().getExtras();
        if(getIntent().hasExtra("idTransaksi")
                && getIntent().hasExtra("namaKegiatan")
                && getIntent().hasExtra("nama_transaksi")
                && getIntent().hasExtra("nominal_transaksi")
                && getIntent().hasExtra("tanggal_transaksi")
                && getIntent().hasExtra("jenis_transaksi"))
        {
            idTransaksi = bundle.getString("idTransaksi");
            namaKegiatan = bundle.getString("namaKegiatan");
            namaTransaksi = bundle.getString("nama_transaksi");
            nominal = bundle.getString("nominal_transaksi");
            tanggalTransaksi = bundle.getString("tanggal_transaksi");
            jenisTransaksi = bundle.getString("jenis_transaksi");
        }
    }

    public void setData(){
        actvListKegiatan.setText(namaKegiatan);
        etNamaTransaksi.setText(namaTransaksi);
        tvTanggalTransaksi.setText(tanggalTransaksi);
        etNominalTransaksi.setText(nominal);
        if (jenisTransaksi.matches("pemasukan") || jenisTransaksi.matches("Pemasukan")){
            rb_pemasukan.performClick();
        } else{
            rb_pengeluaran.performClick();
        }
    }
}