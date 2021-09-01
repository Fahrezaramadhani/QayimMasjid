package com.example.qayimmasjid.ui.transaksi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.qayimmasjid.PaginationScrollListener;
import com.example.qayimmasjid.R;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.adapter.PengumumanAdapter;
import com.example.qayimmasjid.adapter.TransaksiAdapter;
import com.example.qayimmasjid.kegiatan.KegiatanViewModel;
import com.example.qayimmasjid.model.masjid.Kegiatan;
import com.example.qayimmasjid.model.masjid.Pengumuman;
import com.example.qayimmasjid.model.masjid.Transaksi;
import com.example.qayimmasjid.pengumuman.PengumumanViewModel;
import com.example.qayimmasjid.transaksi.TransaksiViewModel;
import com.example.qayimmasjid.ui.pengumuman.TambahPengumumanMasjidActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ManageTransaksiMasjidActivity extends AppCompatActivity {
    private TransaksiViewModel transaksiViewModel;
    private KegiatanViewModel kegiatanViewModel;
    private FloatingActionButton btn_tambah_transaksi;
    private RecyclerView recyclerView;
    private TransaksiAdapter transaksiAdapter;
    private List<Transaksi> listTransaksi;
    private List<String> listNamaKegiatan = new ArrayList<>();
    private ArrayList<Kegiatan> listKegiatan;
    private EditText et_search;
    private String keyword = "";

    public boolean isLoading;
    public boolean isLastPage;
    private int currentPage = 1;
    private int totalPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_transaksi_masjid);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        transaksiViewModel = new ViewModelProvider(this).get(TransaksiViewModel.class);
        kegiatanViewModel = new ViewModelProvider(this).get(KegiatanViewModel.class);

        btn_tambah_transaksi = findViewById(R.id.btn_tambah_transaksi);
        et_search = findViewById(R.id.et_search_transaksi);

        recyclerView = findViewById(R.id.recyclerView_transaksi);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        transaksiAdapter = new TransaksiAdapter();

        recyclerView.setAdapter(transaksiAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);


        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoading = true;
                currentPage += 1;
//                getListTransaksi(2);
                searchTransaksi(2, keyword);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });

//        getListTransaksi(1);
        searchTransaksi(1, keyword);

        btn_tambah_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TambahTransaksiMasjidActivity.class));
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = s.toString();
                searchTransaksi(1, s.toString());
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        currentPage = 1;
        totalPage = 1;
        isLoading = false;
        isLastPage = false;
//        getListTransaksi(1);
        searchTransaksi(1, keyword);
    }

    private void getListTransaksi(int type){
        kegiatanViewModel.getListKegiatan(getApplicationContext(), SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
        kegiatanViewModel.getData().observe(this, kegiatanList -> {
            if (kegiatanList != null){
                transaksiViewModel.getListTransaki(SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
                transaksiViewModel.getData().observe(this, transaksiList -> {
                    if (transaksiList != null){
                        totalPage = transaksiList.getListTransaksi().size()/10 + 1;
                        if (type == 1){
                            List<Transaksi> list = new ArrayList<>();
                            if (transaksiList.getListTransaksi().size() > 10){
                                for (int i = 0; i < 10; i++){
                                    listNamaKegiatan.add(getNamaKegiatan(kegiatanList.getkegiatan(), transaksiList.getListTransaksi().get(i).getMidkegiatan()));
                                    list.add(transaksiList.getListTransaksi().get(i));
                                }
                                listTransaksi = list;
                                transaksiAdapter.setData(listTransaksi, listNamaKegiatan);
                                Log.d("List", "setFirstData21321: " + listTransaksi);
                                recyclerView.setAdapter(transaksiAdapter);

                                if (currentPage < totalPage) {
                                    transaksiAdapter.addFooterLoading();
                                } else {
                                    isLastPage = true;
                                }
                            }else {
                                for (int i = 0; i < transaksiList.getListTransaksi().size(); i++){
                                    listNamaKegiatan.add(getNamaKegiatan(kegiatanList.getkegiatan(), transaksiList.getListTransaksi().get(i).getMidkegiatan()));
                                    list.add(transaksiList.getListTransaksi().get(i));
                                }
                                listTransaksi = list;
                                transaksiAdapter.setData(listTransaksi, listNamaKegiatan);
                                Log.d("List", "setFirstData21321: " + listTransaksi);
                                recyclerView.setAdapter(transaksiAdapter);

                                if (currentPage < totalPage) {
                                    transaksiAdapter.addFooterLoading();
                                } else {
                                    isLastPage = true;
                                }
                            }
                        }else{
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    List<Transaksi> list1 = transaksiList.getListTransaksi();
                                    List<Transaksi> list2 = new ArrayList<>();
                                    for (int i = 1; i < list1.size(); i++){
                                        if (!findPengumuman(list1.get(i), listTransaksi)){
                                            if (list2.size() != 10){
                                                listNamaKegiatan.add(getNamaKegiatan(kegiatanList.getkegiatan(), transaksiList.getListTransaksi().get(i).getMidkegiatan()));
                                                list2.add(list1.get(i));
                                            }
                                        }
                                    }

                                    transaksiAdapter.removeFooterLoading();
                                    listTransaksi.addAll(list2);
                                    transaksiAdapter.notifyDataSetChanged();

                                    isLoading = false;
                                    if (currentPage < totalPage) {
                                        transaksiAdapter.addFooterLoading();
                                    } else {
                                        isLastPage = true;
                                    }
                                }
                            }, 2000);
                        }
                    }
                });
            }
        });
    }

    private void searchTransaksi(int type, String keyword){
        kegiatanViewModel.getListKegiatan(getApplicationContext(), SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
        kegiatanViewModel.getData().observe(this, kegiatanList -> {
            if (kegiatanList != null){
                transaksiViewModel.searchTransaksi(keyword, SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
                transaksiViewModel.getData().observe(this, transaksiList -> {
                    if (transaksiList != null){
                        totalPage = transaksiList.getListTransaksi().size()/10 + 1;
                        if (type == 1){
                            List<Transaksi> list = new ArrayList<>();
                            if (transaksiList.getListTransaksi().size() > 10){
                                for (int i = 0; i < 10; i++){
                                    listNamaKegiatan.add(getNamaKegiatan(kegiatanList.getkegiatan(), transaksiList.getListTransaksi().get(i).getMidkegiatan()));
                                    list.add(transaksiList.getListTransaksi().get(i));
                                }
                                listTransaksi = list;
                                transaksiAdapter.setData(listTransaksi, listNamaKegiatan);
                                Log.d("List", "setFirstData21321: " + listTransaksi);
                                recyclerView.setAdapter(transaksiAdapter);

                                if (currentPage < totalPage) {
                                    transaksiAdapter.addFooterLoading();
                                } else {
                                    isLastPage = true;
                                }
                            }else {
                                for (int i = 0; i < transaksiList.getListTransaksi().size(); i++){
                                    listNamaKegiatan.add(getNamaKegiatan(kegiatanList.getkegiatan(), transaksiList.getListTransaksi().get(i).getMidkegiatan()));
                                    list.add(transaksiList.getListTransaksi().get(i));
                                }
                                listTransaksi = list;
                                transaksiAdapter.setData(listTransaksi, listNamaKegiatan);
                                Log.d("List", "setFirstData21321: " + listTransaksi);
                                recyclerView.setAdapter(transaksiAdapter);

                                if (currentPage < totalPage) {
                                    transaksiAdapter.addFooterLoading();
                                } else {
                                    isLastPage = true;
                                }
                            }
                        }else{
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    List<Transaksi> list1 = transaksiList.getListTransaksi();
                                    List<Transaksi> list2 = new ArrayList<>();
                                    for (int i = 1; i < list1.size(); i++){
                                        if (!findPengumuman(list1.get(i), listTransaksi)){
                                            if (list2.size() != 10){
                                                listNamaKegiatan.add(getNamaKegiatan(kegiatanList.getkegiatan(), transaksiList.getListTransaksi().get(i).getMidkegiatan()));
                                                list2.add(list1.get(i));
                                            }
                                        }
                                    }

                                    transaksiAdapter.removeFooterLoading();
                                    listTransaksi.addAll(list2);
                                    transaksiAdapter.notifyDataSetChanged();

                                    isLoading = false;
                                    if (currentPage < totalPage) {
                                        transaksiAdapter.addFooterLoading();
                                    } else {
                                        isLastPage = true;
                                    }
                                }
                            }, 2000);
                        }
                    }
                });
            }
        });
    }


    private boolean findPengumuman(Transaksi transaksi, List<Transaksi> list) {
        for (int i = 0; i < list.size(); i++) {
            if (transaksi.getMidtransaksi().matches(list.get(i).getMidtransaksi())){
                return true;
            }
        }
        return false;
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