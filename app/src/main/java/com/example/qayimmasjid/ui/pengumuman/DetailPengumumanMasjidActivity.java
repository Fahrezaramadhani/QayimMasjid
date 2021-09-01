package com.example.qayimmasjid.ui.pengumuman;

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
import com.example.qayimmasjid.model.masjid.KegiatanList;
import com.example.qayimmasjid.model.masjid.PengumumanList;
import com.example.qayimmasjid.model.masjid.ProfileMasjid;
import com.example.qayimmasjid.pengumuman.PengumumanViewModel;
import com.example.qayimmasjid.profil.ProfilMasjidViewModel;
import com.example.qayimmasjid.ui.kegiatan.EditKegiatanMasjidActivity;
import com.google.android.material.button.MaterialButton;

public class DetailPengumumanMasjidActivity extends AppCompatActivity {
    private TextView tv_NamaPengumuman, tv_Deskripsi, tv_CP, tv_NamaMasjid;
    private String idPengumuman, idMasjid, namaPengumuman, deskripsi, cp;
    private MaterialButton btn_edit_pengumuman, btn_hapus_pengumuman;
    private PengumumanViewModel pengumumanViewModel;
    private ProfilMasjidViewModel profilMasjidViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pengumuman_masjid);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        pengumumanViewModel = new ViewModelProvider(this).get(PengumumanViewModel.class);
        profilMasjidViewModel = new ViewModelProvider(this).get(ProfilMasjidViewModel.class);

        tv_NamaPengumuman = findViewById(R.id.tv_detail_nama_pengumuman);
        tv_Deskripsi = findViewById(R.id.tv_detail_deskripsi_pengumuman);
        tv_CP = findViewById(R.id.tv_detail_cp_pengumuman);
        tv_NamaMasjid = findViewById(R.id.tv_nama_masjid);
        btn_edit_pengumuman = findViewById(R.id.button_edit_pengumuman);
        btn_hapus_pengumuman = findViewById(R.id.button_delete_pengumuman);

        getData();
        setData(idPengumuman);

        btn_edit_pengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle =new Bundle();
                bundle.putString("idPengumuman", idPengumuman);
                bundle.putString("nama_pengumuman", namaPengumuman);
                bundle.putString("deskripsi_pengumuman", deskripsi);
                bundle.putString("contact_person", cp);

                Intent intent = new Intent(v.getContext(), EditPengumumanMasjidActivity.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

        btn_hapus_pengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePengumuman(idPengumuman);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setData(idPengumuman);
    }

    public void getData(){

        Bundle bundle = getIntent().getExtras();
        if(getIntent().hasExtra("idPengumuman"))
        {
            idPengumuman = bundle.getString("idPengumuman");
        }
    }

    public void setData(String idPengumuman){
        pengumumanViewModel.getListPengumuman(SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
        pengumumanViewModel.getData().observe(this, pengumumanList -> {
            int index = getIndexPengumuman(pengumumanList, idPengumuman);
            tv_NamaPengumuman.setText(pengumumanList.getpengumuman().get(index).getMnamapengumuman());
            tv_Deskripsi.setText(pengumumanList.getpengumuman().get(index).getDeskripsiPengumuman());
            tv_CP.setText(pengumumanList.getpengumuman().get(index).getContactPerson());
            namaPengumuman = pengumumanList.getpengumuman().get(index).getMnamapengumuman();
            deskripsi = pengumumanList.getpengumuman().get(index).getDeskripsiPengumuman();
            cp = pengumumanList.getpengumuman().get(index).getContactPerson();
        });
        profilMasjidViewModel.getProfilMasjid(SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
        profilMasjidViewModel.getData().observe(this, profileMasjid -> {
            tv_NamaMasjid.setText(profileMasjid.getMasjid().getMnamamasjid());
        });
    }

    private int getIndexPengumuman(PengumumanList pengumumanList, String idPengumuman){
        int i;
        for (i = 0; i < pengumumanList.getpengumuman().size(); i++){
            Log.d("cari", "getIndexPengurus: " + pengumumanList.getpengumuman().size());
            if (pengumumanList.getpengumuman().get(i).getMidpengumuman().matches(idPengumuman)){
                Log.d("cari", "getIndexPengurus: " + pengumumanList.getpengumuman().get(i).getMidpengumuman());
                return i;
            }
        }
        return i;
    }

    public void deletePengumuman(String idPengumuman){
        pengumumanViewModel.deletePengumuman(idPengumuman);
        pengumumanViewModel.getData().observe(this, pengumumanList -> {
            if (pengumumanList != null){
                Toast.makeText(getApplicationContext(), "Pengumuman berhasil di hapus", Toast.LENGTH_LONG).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Pengumuman gagal di hapus", Toast.LENGTH_LONG).show();
            }
        });
    }
}