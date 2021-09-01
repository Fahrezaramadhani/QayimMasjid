package com.example.qayimmasjid.ui.transaksi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qayimmasjid.R;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.kegiatan.KegiatanViewModel;
import com.example.qayimmasjid.model.masjid.Kegiatan;
import com.example.qayimmasjid.model.masjid.PengumumanList;
import com.example.qayimmasjid.model.masjid.TransaksiList;
import com.example.qayimmasjid.pengumuman.PengumumanViewModel;
import com.example.qayimmasjid.profil.ProfilMasjidViewModel;
import com.example.qayimmasjid.transaksi.TransaksiViewModel;
import com.example.qayimmasjid.ui.pengumuman.EditPengumumanMasjidActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class DetailTransaksiMasjidActivity extends AppCompatActivity {
    private TextView tv_idtransaksi, tv_namaTransaksi, tv_namaKegiatan, tv_nominal, tv_tanggalTransaksi, tv_jenisTransaksi;
    private ImageView iv_iconTransaksi;
    private String idTransaksi, namakegiatan, nominal, tanggal, jenis, namaTransaksi;
    private MaterialButton btn_edit_transaksi, btn_hapus_transaksi;
    private TransaksiViewModel transaksiViewModel;
    private KegiatanViewModel kegiatanViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi_masjid);

        transaksiViewModel = new ViewModelProvider(this).get(TransaksiViewModel.class);
        kegiatanViewModel = new ViewModelProvider(this).get(KegiatanViewModel.class);

        tv_idtransaksi = findViewById(R.id.tv_detail_id_transaksi);
        tv_namaTransaksi = findViewById(R.id.nama_transaksi_detail);
        tv_namaKegiatan = findViewById(R.id.tv_detail_nama_kegiatan_transaksi);
        tv_nominal = findViewById(R.id.tv_detail_nominal_transaksi);
        tv_tanggalTransaksi = findViewById(R.id.tv_detail_tanggal_transaksi);
        tv_jenisTransaksi = findViewById(R.id.tv_detail_judul_jenis_transaksi);
        iv_iconTransaksi = findViewById(R.id.icon_transaksi);
        btn_edit_transaksi = findViewById(R.id.button_edit_transaksi);
        btn_hapus_transaksi = findViewById(R.id.button_delete_transaksi);

        getData();
        setData(idTransaksi);

        btn_edit_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle =new Bundle();
                bundle.putString("idTransaksi", idTransaksi);
                bundle.putString("namaKegiatan", namakegiatan);
                bundle.putString("nama_transaksi", namaTransaksi);
                bundle.putString("nominal_transaksi", nominal);
                bundle.putString("tanggal_transaksi", tanggal);
                bundle.putString("jenis_transaksi", jenis);

                Intent intent = new Intent(v.getContext(), EditTransaksiMasjidActivity.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

        btn_hapus_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTransaksi(idTransaksi);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setData(idTransaksi);
    }

    public void getData(){

        Bundle bundle = getIntent().getExtras();
        if(getIntent().hasExtra("idTransaksi"))
        {
            idTransaksi = bundle.getString("idTransaksi");
            Log.d("id", "getData: " + idTransaksi);
        }
    }

    public void setData(String idTransaksi){
        transaksiViewModel.getListTransaki(SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
        transaksiViewModel.getData().observe(this, transaksiList -> {
            int index = getIndexTransaksi(transaksiList, idTransaksi);
            Log.d("index", "setData: " + index);
            tv_idtransaksi.setText(idTransaksi);
            tv_nominal.setText(String.valueOf(transaksiList.getListTransaksi().get(index).getMnominal()));
            tv_tanggalTransaksi.setText(transaksiList.getListTransaksi().get(index).getMtanggal());
            tv_namaTransaksi.setText(transaksiList.getListTransaksi().get(index).getMnamatransaksi());
            tv_jenisTransaksi.setText(transaksiList.getListTransaksi().get(index).getMjenistransaksi());
            namaTransaksi = transaksiList.getListTransaksi().get(index).getMnamatransaksi();
            nominal = String.valueOf(transaksiList.getListTransaksi().get(index).getMnominal());
            tanggal = transaksiList.getListTransaksi().get(index).getMtanggal();
            jenis = transaksiList.getListTransaksi().get(index).getMjenistransaksi();
            if (transaksiList.getListTransaksi().get(index).getMjenistransaksi().matches("pengeluaran")){
                iv_iconTransaksi.setImageResource(R.drawable.ic_pengeluaran);
            }
            kegiatanViewModel.getListKegiatan(getApplicationContext(), SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
            kegiatanViewModel.getData().observe(this, kegiatanList -> {
                tv_namaKegiatan.setText(getNamaKegiatan(kegiatanList.getkegiatan(), transaksiList.getListTransaksi().get(index).getMidkegiatan()));
                namakegiatan = getNamaKegiatan(kegiatanList.getkegiatan(), transaksiList.getListTransaksi().get(index).getMidkegiatan());
            });
        });
    }

    private int getIndexTransaksi(TransaksiList transaksiList, String idTransaksi){
        int i;
        for (i = 0; i < transaksiList.getListTransaksi().size(); i++){
            Log.d("cari", "getIndexPengurus: " + transaksiList.getListTransaksi().size());
            if (transaksiList.getListTransaksi().get(i).getMidtransaksi().matches(idTransaksi)){
                Log.d("cari", "getIndexPengurus: " + transaksiList.getListTransaksi().get(i).getMidtransaksi());
                return i;
            }
        }
        return i;
    }

    public void deleteTransaksi(String idTransaksi){
        transaksiViewModel.deletePengumuman(idTransaksi);
        transaksiViewModel.getData().observe(this, transaksiList -> {
            if (transaksiList != null){
                Toast.makeText(getApplicationContext(), "Transaksi berhasil di hapus", Toast.LENGTH_LONG).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Transaksi gagal di hapus", Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getNamaKegiatan(ArrayList<Kegiatan> listKegiatan, String idKegiatan){
        for (int i = 0; i < listKegiatan.size(); i++) {
            if (listKegiatan.get(i).getMidkegiatan().matches(idKegiatan)){
                return listKegiatan.get(i).getMnamakegiatan();
            }
        }
        return "";
    }
}