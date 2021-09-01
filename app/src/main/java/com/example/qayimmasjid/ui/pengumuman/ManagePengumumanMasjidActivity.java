package com.example.qayimmasjid.ui.pengumuman;

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
import com.example.qayimmasjid.adapter.KegiatanAdapter;
import com.example.qayimmasjid.adapter.PengumumanAdapter;
import com.example.qayimmasjid.kegiatan.KegiatanViewModel;
import com.example.qayimmasjid.model.masjid.Pengumuman;
import com.example.qayimmasjid.pengumuman.PengumumanViewModel;
import com.example.qayimmasjid.ui.kegiatan.TambahKegiatanMasjidActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shadow.ShadowRenderer;

import java.util.ArrayList;
import java.util.List;

public class ManagePengumumanMasjidActivity extends AppCompatActivity {
    private PengumumanViewModel pengumumanViewModel;
    private FloatingActionButton btn_tambah_pengumuman;
    private RecyclerView recyclerView;
    private PengumumanAdapter pengumumanAdapter;
    private List<Pengumuman> listPengumuman;
    private EditText et_search;
    private String keyword = "";

    public boolean isLoading;
    public boolean isLastPage;
    private int currentPage = 1;
    private int totalPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_pengumuman_masjid);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        pengumumanViewModel = new ViewModelProvider(this).get(PengumumanViewModel.class);
//        pengumumanViewModel.getListPengumuman(SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());

        btn_tambah_pengumuman = findViewById(R.id.btn_tambah_pengumuman);
        et_search = findViewById(R.id.et_search_pengumuman);

        recyclerView = findViewById(R.id.recyclerView_pengumuman);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        pengumumanAdapter = new PengumumanAdapter();

        recyclerView.setAdapter(pengumumanAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);


        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoading = true;
                currentPage += 1;
//                getListPengumuman(2);
                searchPengumuman(2, keyword);
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

//        getListPengumuman(1);
        searchPengumuman(1, keyword);

        btn_tambah_pengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TambahPengumumanMasjidActivity.class));
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
                searchPengumuman(1, s.toString());
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        currentPage = 1;
        totalPage = 1;
        String keyword = "";
        isLoading = false;
        isLastPage = false;
        searchPengumuman(1, keyword);
//        getListPengumuman(1);
    }

    private void getListPengumuman(int type){
        pengumumanViewModel.getListPengumuman(SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
        pengumumanViewModel.getData().observe(this, pengumumanList -> {
            if (pengumumanList != null){
                totalPage = pengumumanList.getpengumuman().size()/10 + 1;
                if (type == 1){
                    List<Pengumuman> list = new ArrayList<>();
                    if (pengumumanList.getpengumuman().size() > 10){
                        for (int i = 0; i < 10; i++){
                            list.add(pengumumanList.getpengumuman().get(i));
                        }
                        listPengumuman = list;
                        pengumumanAdapter.setData(listPengumuman);
                        Log.d("List", "setFirstData21321: " + listPengumuman);
                        recyclerView.setAdapter(pengumumanAdapter);

                        if (currentPage < totalPage) {
                            pengumumanAdapter.addFooterLoading();
                        } else {
                            isLastPage = true;
                        }
                    }else {
                        for (int i = 0; i < pengumumanList.getpengumuman().size(); i++){
                            list.add(pengumumanList.getpengumuman().get(i));
                        }
                        listPengumuman = list;
                        pengumumanAdapter.setData(listPengumuman);
                        Log.d("List", "setFirstData21321: " + listPengumuman);
                        recyclerView.setAdapter(pengumumanAdapter);

                        if (currentPage < totalPage) {
                            pengumumanAdapter.addFooterLoading();
                        } else {
                            isLastPage = true;
                        }
                    }
                }else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<Pengumuman> list1 = pengumumanList.getpengumuman();
                            List<Pengumuman> list2 = new ArrayList<>();
                            for (int i = 1; i < list1.size(); i++){
                                if (!findPengumuman(list1.get(i), listPengumuman)){
                                    if (list2.size() != 10){
                                        list2.add(list1.get(i));
                                    }
                                }
                            }

                            pengumumanAdapter.removeFooterLoading();
                            listPengumuman.addAll(list2);
                            pengumumanAdapter.notifyDataSetChanged();

                            isLoading = false;
                            if (currentPage < totalPage) {
                                pengumumanAdapter.addFooterLoading();
                            } else {
                                isLastPage = true;
                            }
                        }
                    }, 2000);
                }
            }
        });
    }

    private void searchPengumuman(int type, String keyword){
        pengumumanViewModel.searchPengumuman(keyword, SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
        pengumumanViewModel.getData().observe(this, pengumumanList -> {
            if (pengumumanList != null){
                totalPage = pengumumanList.getpengumuman().size()/10 + 1;
                if (type == 1){
                    List<Pengumuman> list = new ArrayList<>();
                    if (pengumumanList.getpengumuman().size() > 10){
                        for (int i = 0; i < 10; i++){
                            list.add(pengumumanList.getpengumuman().get(i));
                        }
                        listPengumuman = list;
                        pengumumanAdapter.setData(listPengumuman);
                        Log.d("List", "setFirstData21321: " + listPengumuman);
                        recyclerView.setAdapter(pengumumanAdapter);

                        if (currentPage < totalPage) {
                            pengumumanAdapter.addFooterLoading();
                        } else {
                            isLastPage = true;
                        }
                    }else {
                        for (int i = 0; i < pengumumanList.getpengumuman().size(); i++){
                            list.add(pengumumanList.getpengumuman().get(i));
                        }
                        listPengumuman = list;
                        pengumumanAdapter.setData(listPengumuman);
                        Log.d("List", "setFirstData21321: " + listPengumuman);
                        recyclerView.setAdapter(pengumumanAdapter);

                        if (currentPage < totalPage) {
                            pengumumanAdapter.addFooterLoading();
                        } else {
                            isLastPage = true;
                        }
                    }
                }else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<Pengumuman> list1 = pengumumanList.getpengumuman();
                            List<Pengumuman> list2 = new ArrayList<>();
                            for (int i = 1; i < list1.size(); i++){
                                if (!findPengumuman(list1.get(i), listPengumuman)){
                                    if (list2.size() != 10){
                                        list2.add(list1.get(i));
                                    }
                                }
                            }

                            pengumumanAdapter.removeFooterLoading();
                            listPengumuman.addAll(list2);
                            pengumumanAdapter.notifyDataSetChanged();

                            isLoading = false;
                            if (currentPage < totalPage) {
                                pengumumanAdapter.addFooterLoading();
                            } else {
                                isLastPage = true;
                            }
                        }
                    }, 2000);
                }
            }
        });
    }


    private boolean findPengumuman(Pengumuman pengumuman, List<Pengumuman> list) {
        for (int i = 0; i < list.size(); i++) {
            if (pengumuman.getMidpengumuman().matches(list.get(i).getMidpengumuman())){
                return true;
            }
        }
        return false;
    }
}