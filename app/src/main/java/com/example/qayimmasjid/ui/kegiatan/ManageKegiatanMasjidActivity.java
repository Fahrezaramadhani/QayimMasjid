package com.example.qayimmasjid.ui.kegiatan;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qayimmasjid.PaginationScrollListener;
import com.example.qayimmasjid.R;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.adapter.KegiatanAdapter;
import com.example.qayimmasjid.adapter.MasjidTerdekatAdapter;
import com.example.qayimmasjid.kegiatan.KegiatanViewModel;
import com.example.qayimmasjid.masjid.MasjidViewModel;
import com.example.qayimmasjid.model.masjid.Kegiatan;
import com.example.qayimmasjid.model.masjid.Masjid;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class ManageKegiatanMasjidActivity extends AppCompatActivity {
    private KegiatanAdapter kegiatanAdapter;
    private KegiatanViewModel kegiatanViewModel;
    private RecyclerView recyclerView;
    private List<Kegiatan> listKegiatan;
    private EditText et_search;
    private String keyword = "";

    public boolean isLoading;
    public boolean isLastPage;
    private int currentPage = 1;
    private int totalPage;

    private FloatingActionButton btn_Tambah_Kegiatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_kegiatan_masjid);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        kegiatanViewModel = new ViewModelProvider(this).get(KegiatanViewModel.class);

        btn_Tambah_Kegiatan = findViewById(R.id.btn_tambah_kegiatan);
        et_search = findViewById(R.id.et_search_kegiatan);
        recyclerView = findViewById(R.id.recyclerView_kegiatan);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        kegiatanAdapter = new KegiatanAdapter();

        recyclerView.setAdapter(kegiatanAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);


        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoading = true;
                currentPage += 1;
//                getListKegiatan(2);
                searchKegiatan(2, keyword);
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

//        getListKegiatan(1);
        searchKegiatan(1, keyword);

        btn_Tambah_Kegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TambahKegiatanMasjidActivity.class));
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
                searchKegiatan(1, s.toString());
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getListKegiatan(1);
//    }
//


    @Override
    protected void onRestart() {
        super.onRestart();
        currentPage = 1;
        totalPage = 1;
        isLoading = false;
        isLastPage = false;
        String keyword = "";
//        getListKegiatan(1);
        searchKegiatan(1, keyword);
    }

    private void getListKegiatan(int type){
        Toast.makeText(getApplicationContext(), "current page : " + currentPage, Toast.LENGTH_SHORT).show();
        kegiatanViewModel.getListKegiatan(getApplicationContext(), SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
        kegiatanViewModel.getData().observe(this, kegiatanList -> {
            if (kegiatanList != null){
                totalPage = kegiatanList.getkegiatan().size()/10 + 1;
                if (type == 1){
                    List<Kegiatan> list = new ArrayList<>();
                    if (kegiatanList.getkegiatan().size() > 10){
                        for (int i = 0; i < 10; i++){
                            list.add(kegiatanList.getkegiatan().get(i));
                        }
                        listKegiatan = list;
                        kegiatanAdapter.setData(listKegiatan);
                        Log.d("List", "setFirstData21321: " + listKegiatan);
                        recyclerView.setAdapter(kegiatanAdapter);

                        if (currentPage < totalPage) {
                            kegiatanAdapter.addFooterLoading();
                        } else {
                            isLastPage = true;
                        }
                    }else {
                        for (int i = 0; i < kegiatanList.getkegiatan().size(); i++){
                            list.add(kegiatanList.getkegiatan().get(i));
                        }
                        listKegiatan= list;
                        kegiatanAdapter.setData(listKegiatan);
                        Log.d("List", "setFirstData21321: " + listKegiatan);
                        recyclerView.setAdapter(kegiatanAdapter);

                        if (currentPage < totalPage) {
                            kegiatanAdapter.addFooterLoading();
                        } else {
                            isLastPage = true;
                        }
                    }
                }else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<Kegiatan> list1 = kegiatanList.getkegiatan();
                            List<Kegiatan> list2 = new ArrayList<>();
                            for (int i = 1; i < list1.size(); i++){
                                if (!findMasjid(list1.get(i), listKegiatan)){
                                    if (list2.size() != 10){
                                        list2.add(list1.get(i));
                                    }
                                }
                            }

                            kegiatanAdapter.removeFooterLoading();
                            listKegiatan.addAll(list2);
                            kegiatanAdapter.notifyDataSetChanged();

                            isLoading = false;
                            if (currentPage < totalPage) {
                                kegiatanAdapter.addFooterLoading();
                            } else {
                                isLastPage = true;
                            }
                        }
                    }, 2000);
                }
            }
        });
    }

    private void searchKegiatan(int type, String keyword){
        Toast.makeText(getApplicationContext(), "current page : " + currentPage, Toast.LENGTH_SHORT).show();
        kegiatanViewModel.searchKegiatan(keyword, SharedPrefManager.getInstance(getApplicationContext()).getUser().getMidmasjid());
        kegiatanViewModel.getData().observe(this, kegiatanList -> {
            if (kegiatanList != null){
                totalPage = kegiatanList.getkegiatan().size()/10 + 1;
                if (type == 1){
                    List<Kegiatan> list = new ArrayList<>();
                    if (kegiatanList.getkegiatan().size() > 10){
                        for (int i = 0; i < 10; i++){
                            list.add(kegiatanList.getkegiatan().get(i));
                        }
                        listKegiatan = list;
                        kegiatanAdapter.setData(listKegiatan);
                        Log.d("List", "setFirstData21321: " + listKegiatan);
                        recyclerView.setAdapter(kegiatanAdapter);

                        if (currentPage < totalPage) {
                            kegiatanAdapter.addFooterLoading();
                        } else {
                            isLastPage = true;
                        }
                    }else {
                        for (int i = 0; i < kegiatanList.getkegiatan().size(); i++){
                            list.add(kegiatanList.getkegiatan().get(i));
                        }
                        listKegiatan= list;
                        kegiatanAdapter.setData(listKegiatan);
                        Log.d("List", "setFirstData21321: " + listKegiatan);
                        recyclerView.setAdapter(kegiatanAdapter);

                        if (currentPage < totalPage) {
                            kegiatanAdapter.addFooterLoading();
                        } else {
                            isLastPage = true;
                        }
                    }
                }else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<Kegiatan> list1 = kegiatanList.getkegiatan();
                            List<Kegiatan> list2 = new ArrayList<>();
                            for (int i = 1; i < list1.size(); i++){
                                if (!findMasjid(list1.get(i), listKegiatan)){
                                    if (list2.size() != 10){
                                        list2.add(list1.get(i));
                                    }
                                }
                            }

                            kegiatanAdapter.removeFooterLoading();
                            listKegiatan.addAll(list2);
                            kegiatanAdapter.notifyDataSetChanged();

                            isLoading = false;
                            if (currentPage < totalPage) {
                                kegiatanAdapter.addFooterLoading();
                            } else {
                                isLastPage = true;
                            }
                        }
                    }, 2000);
                }
            }
        });
    }


    private boolean findMasjid(Kegiatan kegiatan, List<Kegiatan> list) {
        for (int i = 0; i < list.size(); i++) {
            if (kegiatan.getMidkegiatan().matches(list.get(i).getMidkegiatan())){
                return true;
            }
        }
        return false;
    }
}