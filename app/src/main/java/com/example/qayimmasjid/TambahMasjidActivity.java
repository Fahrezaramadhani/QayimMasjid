package com.example.qayimmasjid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qayimmasjid.model.masjid.Masjid;
import com.example.qayimmasjid.profil.ProfilMasjidViewModel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TambahMasjidActivity extends AppCompatActivity {
    private EditText etNamaMasjid, etTahunBerdiri, etDayaTampung;
    private TextView tv_alamat, tv_fotoMasjid;
    private MaterialButton btnCancel, btnTambah;
    private static int PICK_IMAGE_REQUEST = 2;
    private static int PICK_ADDRESS = 1;
    private Uri filePath;
    private Bitmap bitmap;
    private ProfilMasjidViewModel profilMasjidViewModel;
    private double longitude = 0, latitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_masjid);

        profilMasjidViewModel = new ViewModelProvider(this).get(ProfilMasjidViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setupUI();
        setupListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_ADDRESS){
            if (resultCode == RESULT_OK && data != null){
//                Bundle bundle = data.getExtras();
                Place place = PlacePicker.getPlace(this, data);
                Log.d("Place", "onActivityResult: " + place);
//                masjid.setMlatidue(place.getLatLng().latitude);
//                masjid.setMlongitude(place.getLatLng().longitude);
                longitude = place.getLatLng().longitude;
                latitude = place.getLatLng().latitude;

                setAddress(place.getLatLng().latitude, place.getLatLng().longitude);
            }else if (resultCode == PlacePicker.RESULT_ERROR) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(TambahMasjidActivity.this), PICK_ADDRESS);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                String fileName = "";
                if (filePath.getScheme().equals("file")) {
                    fileName = filePath.getLastPathSegment();
                } else {
                    Cursor cursor = null;
                    try {
                        cursor = getContentResolver().query(filePath, new String[]{
                                MediaStore.Images.ImageColumns.DISPLAY_NAME
                        }, null, null, null);

                        if (cursor != null && cursor.moveToFirst()) {
                            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
                            Log.d("File name", "name is " + fileName);
                        }
                    } finally {

                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                }
                tv_fotoMasjid.setText(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public boolean isEmpty(TextView text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    // Module for validation check login account
    public boolean validateAccount (Boolean isValid){

        if(isEmpty(etNamaMasjid)) {
            etNamaMasjid.setError("Nama masjid tidak boleh kosong!");
            isValid = false;
        }else {
            if (etNamaMasjid.getText().toString().length() > 30) {
                etNamaMasjid.setError("Nama masjid tidak boleh lebih dari 30 huruf!");
                isValid = false;
            }
        }

        if(isEmpty(etTahunBerdiri)) {
            etTahunBerdiri.setError("Tahun berdiri tidak boleh kosong!");
            isValid = false;
        } else {
            if (etTahunBerdiri.getText().toString().length() > 4) {
                etTahunBerdiri.setError("Tahun berdiri tidak boleh lebih dari 4 huruf!");
                isValid = false;
            }
        }

        if(isEmpty(etDayaTampung)) {
            etDayaTampung.setError("Daya tampung tidak boleh kosong!");
            isValid = false;
        }

        if(isEmpty(tv_alamat)) {
            tv_alamat.setError("Alamat tidak boleh kosong!");
            isValid = false;
        } else {
            if (tv_alamat.getText().toString().length() > 100) {
                tv_alamat.setError("Alamat tidak boleh lebih dari 100 huruf!");
                isValid = false;
            }
        }

        if(isEmpty(tv_fotoMasjid)) {
            tv_fotoMasjid.setError("Foto masjid tidak boleh kosong!");
            isValid = false;
        }

        return isValid;
    }

    public void setupUI() {
        btnCancel = findViewById(R.id.btn_cancel_tambah_masjid);
        btnTambah = findViewById(R.id.btn_tambah_masjid);
        etNamaMasjid = findViewById(R.id.field_nama_masjid);
        tv_alamat = findViewById(R.id.field_alamat_masjid);
        etTahunBerdiri = findViewById(R.id.field_tahun_berdiri_masjid);
        etDayaTampung = findViewById(R.id.field_daya_tampung_masjid);
        tv_fotoMasjid = findViewById(R.id.field_foto_masjid);
    }

    public void setupListeners() {
        btnCancelListener();
        btnTambahListener();
        etAlamatListener();
        etFotoMasjid();
    }

    public void btnCancelListener(){
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
                //USE API SIMPAN DATA
                boolean isValid = true;
                isValid = validateAccount(isValid);
//                loginAccount(view, isValid);
                //back to main screen pengurus masjid
                tambahMasjid(isValid);
            }
        });
    }

    public void etAlamatListener(){
        tv_alamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(TambahMasjidActivity.this), PICK_ADDRESS);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void etFotoMasjid(){
        tv_fotoMasjid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void setAddress(double latitude, double longitude){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> address = geocoder.getFromLocation(latitude, longitude, 1);
            tv_alamat.setText(address.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tambahMasjid (boolean isValid) {
        if (isValid) {
            Masjid masjid = new Masjid();
            masjid.setMid_masjid(SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
            masjid.setMnamamasjid(etNamaMasjid.getText().toString());
            masjid.setMalamat(tv_alamat.getText().toString());
            masjid.setTahunBerdiri(etTahunBerdiri.getText().toString());
            masjid.setDayaTampung(Integer.valueOf(etDayaTampung.getText().toString()));
            masjid.setMlongitude(longitude);
            masjid.setMlatidue(latitude);
            masjid.setFoto_masjid(getStringImage(bitmap));
            profilMasjidViewModel.tambahMasjid(masjid);
            profilMasjidViewModel.getData().observe(this, profileMasjid -> {
                if (profileMasjid != null){
                    Log.d("profile", "tambahMasjid: " + profileMasjid);
                    Toast.makeText(getApplicationContext(), "Masjid berhasil di tambahkan", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Masjid gagal di tambahkan", Toast.LENGTH_LONG).show();
                }
            });
        } else{
            Toast.makeText(getApplicationContext(), "Terdapat field yang tidak valid", Toast.LENGTH_LONG).show();
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}