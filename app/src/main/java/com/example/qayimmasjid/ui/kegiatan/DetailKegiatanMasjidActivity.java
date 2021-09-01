package com.example.qayimmasjid.ui.kegiatan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qayimmasjid.R;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.kegiatan.KegiatanViewModel;
import com.example.qayimmasjid.model.masjid.Kegiatan;
import com.example.qayimmasjid.model.masjid.KegiatanList;
import com.example.qayimmasjid.model.masjid.ProfileMasjid;
import com.example.qayimmasjid.ui.profil.EditProfilPengurusMasjidActivity;
import com.google.android.material.button.MaterialButton;

public class DetailKegiatanMasjidActivity extends AppCompatActivity {

    private Dialog dialogHapusKegiatan;
    private Button btnHapus, btnBatal;
    private TextView tv_NamaKegiatan, tv_TanggalKegiatan, tv_WaktuKegiatan, tv_LokasiKegiatan, tv_Pemateri, tv_PJ, tv_tempat;
    private String idKegiatan, namaKegiatan, tanggalKegiatan, waktuKegiatan, lokasiKegiatan, pemateri, pj, tempat;
    private MaterialButton btn_edit_kegiatan, btn_hapus_kegiatan;
    private KegiatanViewModel kegiatanViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kegiatan_masjid);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        
        dialogHapusKegiatan = new Dialog(this);
        kegiatanViewModel = new ViewModelProvider(this).get(KegiatanViewModel.class);

        tv_NamaKegiatan = findViewById(R.id.tv_detail_nama_kegiatan);
        tv_TanggalKegiatan = findViewById(R.id.tv_tanggal_kegiatan);
        tv_WaktuKegiatan = findViewById(R.id.tv_waktu_kegiatan);
        tv_LokasiKegiatan = findViewById(R.id.tv_lokasi_kegiatan);
        tv_Pemateri = findViewById(R.id.tv_pemateri_kegiatan);
        tv_PJ = findViewById(R.id.tv_pj_kegiatan);
        tv_tempat = findViewById(R.id.tv_tempat_masjid);
        btn_edit_kegiatan = findViewById(R.id.button_edit_kegiatan);
        btn_hapus_kegiatan = findViewById(R.id.button_delete_kegiatan);

        getData();
        setData(idKegiatan);

        btn_edit_kegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle =new Bundle();
                bundle.putString("idKegiatan", namaKegiatan);
                bundle.putString("nama", namaKegiatan);
                bundle.putString("tanggal", tanggalKegiatan);
                bundle.putString("waktu", waktuKegiatan);
                bundle.putString("lokasi", lokasiKegiatan);
                bundle.putString("pemateri", pemateri);
                bundle.putString("pj", pj);
                bundle.putString("tempat", tempat);

                Intent intent = new Intent(v.getContext(), EditKegiatanMasjidActivity.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

        btn_hapus_kegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogHapusKegiatan();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setData(idKegiatan);
    }

    public void getData(){

        Bundle bundle = getIntent().getExtras();
        if(getIntent().hasExtra("idKegiatan"))
        {
            idKegiatan = bundle.getString("idKegiatan");
        }
    }

    public void setData(String idKegiatan){
        kegiatanViewModel.getListKegiatan(getApplicationContext(), SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
        kegiatanViewModel.getData().observe(this, kegiatanList -> {
            int index = getIndexKegiatan(kegiatanList, idKegiatan);
            tv_NamaKegiatan.setText(kegiatanList.getkegiatan().get(index).getMnamakegiatan());
            tv_TanggalKegiatan.setText(kegiatanList.getkegiatan().get(index).getTanggalKegiatan());
            tv_WaktuKegiatan.setText(kegiatanList.getkegiatan().get(index).getMwaktukegiatan());
            tv_LokasiKegiatan.setText(kegiatanList.getkegiatan().get(index).getMlokasikegiatan());
            tv_Pemateri.setText(kegiatanList.getkegiatan().get(index).getMpemateri());
            tv_PJ.setText(kegiatanList.getkegiatan().get(index).getMpenanggungjawab());
            tv_tempat.setText(kegiatanList.getkegiatan().get(index).getTempatKegiatan());
            namaKegiatan = kegiatanList.getkegiatan().get(index).getMnamakegiatan();
            tanggalKegiatan = kegiatanList.getkegiatan().get(index).getTanggalKegiatan();
            waktuKegiatan = kegiatanList.getkegiatan().get(index).getMwaktukegiatan();
            lokasiKegiatan = kegiatanList.getkegiatan().get(index).getMlokasikegiatan();
            pemateri = kegiatanList.getkegiatan().get(index).getMpemateri();
            pj = kegiatanList.getkegiatan().get(index).getMpenanggungjawab();
            tempat = kegiatanList.getkegiatan().get(index).getTempatKegiatan();
        });
    }

    private int getIndexKegiatan(KegiatanList kegiatanList, String idKegiatan){
        int i;
        for (i = 0; i < kegiatanList.getkegiatan().size(); i++){
            Log.d("cari", "getIndexPengurus: " + kegiatanList.getkegiatan().size());
            if (kegiatanList.getkegiatan().get(i).getMidkegiatan().matches(idKegiatan)){
                Log.d("cari", "getIndexPengurus: " + kegiatanList.getkegiatan().get(i).getMidkegiatan());
                return i;
            }
        }
        return i;
    }

    public void openDialogHapusKegiatan() {
        Log.d("log", "openDialogHapusAkun: Dialog terbuka!");
        dialogHapusKegiatan.setContentView(R.layout.dialog_hapus_kegiatan);
        btnHapus = (Button) dialogHapusKegiatan.findViewById(R.id.btn_konfirmasi_hapus_kegiatan);
        btnBatal = (Button) dialogHapusKegiatan.findViewById(R.id.btn_cancel_konfirmasi_hapus_kegiatan);
        dialogHapusKegiatan.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogHapusKegiatan.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteKegiatan(idKegiatan);
                Toast.makeText(getApplicationContext(), "Kegiatan berhasil terhapus", Toast.LENGTH_SHORT).show();
                dialogHapusKegiatan.dismiss();
                startActivity(new Intent(DetailKegiatanMasjidActivity.this, ManageKegiatanMasjidActivity.class));
            }
        });
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                dialogHapusKegiatan.dismiss();
            }
        });
        dialogHapusKegiatan.show();
    }
    
    public void deleteKegiatan(String idKegiatan){
        kegiatanViewModel.deleteKegiatan(idKegiatan);
        kegiatanViewModel.getData().observe(this, kegiatanList -> {
            if (kegiatanList != null){
                Toast.makeText(getApplicationContext(), "Kegiatan berhasil di hapus", Toast.LENGTH_LONG).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Kegiatan gagal di hapus", Toast.LENGTH_LONG).show();
            }
        });
    }
}
