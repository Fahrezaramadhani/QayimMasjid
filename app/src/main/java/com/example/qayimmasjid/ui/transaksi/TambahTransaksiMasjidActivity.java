package com.example.qayimmasjid.ui.transaksi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qayimmasjid.MoneyTextWatcher;
import com.example.qayimmasjid.R;
import com.example.qayimmasjid.RequestHandler;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.kegiatan.KegiatanViewModel;
import com.example.qayimmasjid.model.masjid.Kegiatan;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class TambahTransaksiMasjidActivity extends AppCompatActivity {

    private AutoCompleteTextView actvListMasjid;
    private KegiatanViewModel kegiatanViewModel;
    private TextView tvTanggalTransaksi;
    private EditText etNominalTransaksi, etNamaTransaksi;
    private MaterialButton btnTambahTransaksi, btnCancelTransaksi;
    private String tanggalTransaksi, jenisTransaksi, idKegiatan, idMasjid;
    private final Calendar calendar = Calendar.getInstance();
    private ArrayList<String> listNamaKegiatan = new ArrayList<>();
    private ArrayList<String> listIdKegiatan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_transaksi_masjid);

        kegiatanViewModel = new ViewModelProvider(this).get(KegiatanViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setupUI();
        setupListeners();
        setListKegiatan();

        etNominalTransaksi.addTextChangedListener(new MoneyTextWatcher(etNominalTransaksi));
    }

    private void setupUI() {
        actvListMasjid = findViewById(R.id.field_filter_masjid_transaksi);
        tvTanggalTransaksi = findViewById(R.id.tv_tanggal_transaksi);
        etNominalTransaksi = findViewById(R.id.et_nominal_transaksi);
        etNamaTransaksi    = findViewById(R.id.et_nama_transaksi);
        btnCancelTransaksi = findViewById(R.id.btn_cancel_tambah_transaksi_masjid);
        btnTambahTransaksi = findViewById(R.id.btn_tambah_transaksi_masjid);
    }

    private void setupListeners() {
//        actvListMasjidListener();
        tvTanggalTransaksiListener();
        btnTambahTransaksiListener();
        btnCancelTransaksiListener();
    }

    public void pengeluaranOnclick(View v) {
        jenisTransaksi = "pengeluaran";
    }

    public void pemasukanOnclick(View v) {
        jenisTransaksi = "pemasukan";
    }

    private void btnCancelTransaksiListener() {
        btnCancelTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                new DatePickerDialog(TambahTransaksiMasjidActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void btnTambahTransaksiListener() {
        btnTambahTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });
    }


    private void validateForm() {
        int nominal = (int) MoneyTextWatcher.parseCurrencyValue(etNominalTransaksi.getText().toString());
        Log.d("nominal", "validateForm: " + nominal);
        boolean isValidNamaTransaksi = true, isValidTanggalTransaksi = true, isValidMasjid = true, isValidNominalTransaksi = true, isValidJenisTransaksi = true;

        //Validate Masjid
        if(actvListMasjid.getText().toString().trim().matches("pilih kegiatan")) {
            Toast.makeText(getApplicationContext(), "Kegiatan harus dipilih", Toast.LENGTH_SHORT).show();
            isValidMasjid = false;
        }

        //Validate Nama Transaksi
        if(TextUtils.isEmpty(etNamaTransaksi.getText().toString())) {
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
        if(TextUtils.isEmpty(String.valueOf(nominal))) {
            etNominalTransaksi.setError("Nominal transaksi harus diisi!");
            isValidNominalTransaksi = false;
        } else if(!String.valueOf(nominal).matches("[0-9]+")){
            etNominalTransaksi.setError("Nominal transaksi hanya diisi oleh angka");
        } else if(String.valueOf(nominal).length() > 11) {
            etNominalTransaksi.setError("Nominal transaksi maksimal 11 digit!");
            isValidNominalTransaksi = false;
        }
        //Validate Jenis Transaksi
        if (jenisTransaksi == null) {
            isValidJenisTransaksi = false;
        }

        //Validate all
        if(isValidMasjid && isValidNamaTransaksi && isValidTanggalTransaksi && isValidNominalTransaksi && isValidJenisTransaksi) {
//            tambahTransaksi();
        }
    }

    public void setListKegiatan(){
        kegiatanViewModel.getListKegiatan(getApplicationContext(), SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
        kegiatanViewModel.getData().observe(this, kegiatanList -> {
            Log.d("masjidList", "setListMasjid: " + kegiatanList);
            for (int i = 0; i < kegiatanList.getkegiatan().size(); i++){
                listNamaKegiatan.add(kegiatanList.getkegiatan().get(i).getMnamakegiatan());
                listIdKegiatan.add(kegiatanList.getkegiatan().get(i).getMidkegiatan());
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, listNamaKegiatan);
            actvListMasjid.setAdapter(arrayAdapter);
        });
    }

    public String getIdkegiatan(String namaKegiatan){
        for (int i = 0; i < listNamaKegiatan.size(); i++){
            if (listNamaKegiatan.get(i).matches(namaKegiatan)){
                return listIdKegiatan.get(i);
            }
        }
        return null;
    }

    public void tambahTransaksi() {
        //id kegiatan, tanggal transaksi dan jenis transaksi sudah berupa string nilainya.
        //yang dikirim id kegiatan, id masjid, nama transaksi, tanggal transaksi, nominal transaksi, jenis transaksi
        String namaTransaksi    = etNamaTransaksi.getText().toString().trim();
        String nominalTransaksi = etNominalTransaksi.getText().toString().trim();


        class Tambah extends AsyncTask<Void, Void, String> {
            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();

                params.put("id_masjid", SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid()); //bisa pake sharedpreferences
                params.put("id_kegiatan", getIdkegiatan(actvListMasjid.getText().toString().trim()));
                params.put("nama_transaksi", namaTransaksi);
                params.put("tanggal_transaksi", tanggalTransaksi);
                params.put("nominal_transaksi", nominalTransaksi);
                params.put("jenis_transaksi", jenisTransaksi);

                //returing the response
                return requestHandler.sendPostRequest("https://qayimmasjid.com/API/qayimmasjid/tambahTransaksi.php", params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) findViewById(R.id.progress_bar_tambah_transaksi);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);

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