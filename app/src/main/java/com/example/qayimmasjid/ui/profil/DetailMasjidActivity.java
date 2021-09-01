package com.example.qayimmasjid.ui.profil;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qayimmasjid.PaginationScrollListener;
import com.example.qayimmasjid.R;
import com.example.qayimmasjid.adapter.KegiatanPenggunaAdapter;
import com.example.qayimmasjid.adapter.PengumumanPenggunaAdapter;
import com.example.qayimmasjid.kegiatan.KegiatanViewModel;
import com.example.qayimmasjid.model.masjid.Kegiatan;
import com.example.qayimmasjid.model.masjid.Pengumuman;
import com.example.qayimmasjid.pengumuman.PengumumanViewModel;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailMasjidActivity extends AppCompatActivity {
    private KegiatanViewModel kegiatanViewModel;
    private PengumumanViewModel pengumumanViewModel;
    private RecyclerView rvKegiataList, rvPengumumanList;
    private KegiatanPenggunaAdapter kegiatanPenggunaAdapter;
    private PengumumanPenggunaAdapter pengumumanPenggunaAdapter;
    private boolean isLoadingKegiatan, isLoadingPengumuman;
    private boolean isLastPageKegiatan, isLastPagePengumuman;
    private int currentPageKegiatan = 1, currentPagePengumuman = 1, totalPagekegiatan, totalPagePengumuman;
    private List<Kegiatan> listKegiatan;
    private List<Pengumuman> listPengumuman;
    private Bundle bundle;
    private String idMasjid, namaMasjid, alamatMasjid, tahunBerdiri;
    private TextView tv_namaMasjid, tv_alamatMasjid, tv_tahunBerdiri, tv_dayaTampung;
    private int dayaTampung;
    private ImageView btn_dropdown_kegiatan, btn_dropdown_pengumuman, ivFotoMasjid;
    private boolean statusButtonKegiatan = true, statusButtonPengumuman = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_masjid);

        kegiatanViewModel = new ViewModelProvider(this).get(KegiatanViewModel.class);
        pengumumanViewModel = new ViewModelProvider(this).get(PengumumanViewModel.class);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        getData();

        btn_dropdown_kegiatan = findViewById(R.id.button_dropdown_kegiatan);
        btn_dropdown_kegiatan.setRotation(180);
        btn_dropdown_kegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusButtonKegiatan){
                    if (listKegiatan != null){
                        ViewGroup.LayoutParams params = rvKegiataList.getLayoutParams();
                        params.height = 550;
                        rvKegiataList.setLayoutParams(params);
                    }
                    statusButtonKegiatan = false;
                    btn_dropdown_kegiatan.setRotation(360);
                }else{
                    statusButtonKegiatan = true;
                    btn_dropdown_kegiatan.setRotation(180);
                    ViewGroup.LayoutParams params = rvKegiataList.getLayoutParams();
                    params.height = 1;
                    rvKegiataList.setLayoutParams(params);
                }
            }
        });

        btn_dropdown_pengumuman = findViewById(R.id.button_dropdown_pengumuman);
        btn_dropdown_pengumuman.setRotation(180);
        btn_dropdown_pengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusButtonPengumuman){
                    if (listPengumuman != null){
                        ViewGroup.LayoutParams params = rvPengumumanList.getLayoutParams();
                        params.height = 550;
                        rvPengumumanList.setLayoutParams(params);
                    }
                    statusButtonPengumuman = false;
                    btn_dropdown_pengumuman.setRotation(360);
                }else{
                    statusButtonPengumuman = true;
                    btn_dropdown_pengumuman.setRotation(180);
                    ViewGroup.LayoutParams params = rvPengumumanList.getLayoutParams();
                    params.height = 1;
                    rvPengumumanList.setLayoutParams(params);
                }
            }
        });

        //RecyclerView List Kegiatan
        rvKegiataList = findViewById(R.id.rv_kegiatan_masjid);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvKegiataList.setLayoutManager(linearLayoutManager);

        ViewGroup.LayoutParams params = rvKegiataList.getLayoutParams();
        params.height = 1;
        rvKegiataList.setLayoutParams(params);

        kegiatanPenggunaAdapter = new KegiatanPenggunaAdapter();

        rvKegiataList.setAdapter(kegiatanPenggunaAdapter);
        rvKegiataList.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        rvKegiataList.addItemDecoration(itemDecoration);

        getListKegiatan(1);

        rvKegiataList.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoadingKegiatan = true;
                currentPageKegiatan += 1;
                getListKegiatan(2);
            }

            @Override
            public boolean isLoading() {
                return isLoadingKegiatan;
            }

            @Override
            public boolean isLastPage() {
                return isLastPageKegiatan;
            }
        });
        //---------------------------------------------------------------

        //RecyclerView list pengumuman
        rvPengumumanList = findViewById(R.id.rv_pengumuman_masjid);
        LinearLayoutManager linearLayoutManagerPengumuman = new LinearLayoutManager(this);
        rvPengumumanList.setLayoutManager(linearLayoutManagerPengumuman);

        ViewGroup.LayoutParams paramsPengumuman = rvPengumumanList.getLayoutParams();
        paramsPengumuman.height = 1;
        rvPengumumanList.setLayoutParams(paramsPengumuman);

        pengumumanPenggunaAdapter = new PengumumanPenggunaAdapter();

        rvPengumumanList.setAdapter(pengumumanPenggunaAdapter);
        rvPengumumanList.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration itemDecorationPengumuman = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        rvPengumumanList.addItemDecoration(itemDecorationPengumuman);

        getListPengumuman(1);


        rvPengumumanList.addOnScrollListener(new PaginationScrollListener(linearLayoutManagerPengumuman) {
            @Override
            public void loadMoreItem() {
                isLoadingPengumuman = true;
                currentPagePengumuman += 1;
                getListPengumuman(2);
            }

            @Override
            public boolean isLoading() {
                return isLoadingPengumuman;
            }

            @Override
            public boolean isLastPage() {
                return isLastPagePengumuman;
            }
        });

        setupUIDataMasjid();
        setDataMasjid();
    }

    private void getListKegiatan(int type){
        Log.d("masjid", "getListKegiatan: " + idMasjid);
        kegiatanViewModel.getListKegiatan(getApplicationContext(), idMasjid);
        kegiatanViewModel.getData().observe(this, kegiatanList -> {
            if (kegiatanList != null){
                totalPagekegiatan = kegiatanList.getkegiatan().size()/4 + 1;
                if (type == 1){
                    List<Kegiatan> list = new ArrayList<>();
                    if (kegiatanList.getkegiatan().size() > 4){
                        for (int i = 0; i < 4; i++){
                            list.add(kegiatanList.getkegiatan().get(i));
                        }
                        listKegiatan = list;
                        kegiatanPenggunaAdapter.setData(listKegiatan);
                        Log.d("List", "setFirstData21321: " + listKegiatan);
                        rvKegiataList.setAdapter(kegiatanPenggunaAdapter);

                        if (currentPageKegiatan < totalPagekegiatan) {
                            kegiatanPenggunaAdapter.addFooterLoading();
                        } else {
                            isLastPageKegiatan = true;
                        }
                    }else {
                        for (int i = 0; i < kegiatanList.getkegiatan().size(); i++){
                            list.add(kegiatanList.getkegiatan().get(i));
                        }
                        listKegiatan= list;
                        kegiatanPenggunaAdapter.setData(listKegiatan);
                        Log.d("List", "setFirstData21321: " + listKegiatan);
                        rvKegiataList.setAdapter(kegiatanPenggunaAdapter);

                        if (currentPageKegiatan < totalPagekegiatan) {
                            kegiatanPenggunaAdapter.addFooterLoading();
                        } else {
                            isLastPageKegiatan = true;
                        }
                    }
                }else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<Kegiatan> list1 = kegiatanList.getkegiatan();
                            List<Kegiatan> list2 = new ArrayList<>();
                            for (int i = 1; i < list1.size(); i++){
                                if (!findKegiatan(list1.get(i), listKegiatan)){
                                    if (list2.size() != 4){
                                        list2.add(list1.get(i));
                                    }
                                }
                            }

                            kegiatanPenggunaAdapter.removeFooterLoading();
                            listKegiatan.addAll(list2);
                            kegiatanPenggunaAdapter.notifyDataSetChanged();

                            isLoadingKegiatan = false;
                            if (currentPageKegiatan < totalPagekegiatan) {
                                kegiatanPenggunaAdapter.addFooterLoading();
                            } else {
                                isLastPageKegiatan = true;
                            }
                        }
                    }, 2000);
                }
            }
        });
    }


    private boolean findKegiatan(Kegiatan kegiatan, List<Kegiatan> list) {
        for (int i = 0; i < list.size(); i++) {
            if (kegiatan.getMidkegiatan().matches(list.get(i).getMidkegiatan())){
                return true;
            }
        }
        return false;
    }

    private void getListPengumuman(int type){
        pengumumanViewModel.getListPengumuman(idMasjid);
        pengumumanViewModel.getData().observe(this, pengumumanList -> {
            if (pengumumanList != null){
                totalPagePengumuman = pengumumanList.getpengumuman().size()/4 + 1;
                if (type == 1){
                    List<Pengumuman> list = new ArrayList<>();
                    if (pengumumanList.getpengumuman().size() > 4){
                        for (int i = 0; i < 4; i++){
                            list.add(pengumumanList.getpengumuman().get(i));
                        }
                        listPengumuman = list;
                        pengumumanPenggunaAdapter.setData(listPengumuman);
                        Log.d("List", "setFirstData21321: " + listPengumuman);
                        rvPengumumanList.setAdapter(pengumumanPenggunaAdapter);

                        if (currentPagePengumuman < totalPagePengumuman) {
                            pengumumanPenggunaAdapter.addFooterLoading();
                        } else {
                            isLastPagePengumuman = true;
                        }
                    }else {
                        for (int i = 0; i < pengumumanList.getpengumuman().size(); i++){
                            list.add(pengumumanList.getpengumuman().get(i));
                        }
                        listPengumuman = list;
                        pengumumanPenggunaAdapter.setData(listPengumuman);
                        Log.d("List", "setFirstData21321: " + listPengumuman);
                        rvPengumumanList.setAdapter(pengumumanPenggunaAdapter);

                        if (currentPagePengumuman < totalPagePengumuman) {
                            pengumumanPenggunaAdapter.addFooterLoading();
                        } else {
                            isLastPagePengumuman = true;
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
                                    if (list2.size() != 4){
                                        list2.add(list1.get(i));
                                    }
                                }
                            }

                            pengumumanPenggunaAdapter.removeFooterLoading();
                            listPengumuman.addAll(list2);
                            pengumumanPenggunaAdapter.notifyDataSetChanged();

                            isLoadingPengumuman = false;
                            if (currentPagePengumuman < totalPagePengumuman) {
                                pengumumanPenggunaAdapter.addFooterLoading();
                            } else {
                                isLastPagePengumuman = true;
                            }
                        }
                    }, 2000);
                }
            }
        });
    }


    private boolean findPengumuman(Pengumuman pengumuman, List<Pengumuman> list) {
        for (int i = 0; i < list.size(); i++) {
            if (pengumuman.getMidpengumuman() == list.get(i).getMidpengumuman()){
                return true;
            }
        }
        return false;
    }

    public void getData(){

        Bundle bundle = getIntent().getExtras();
        if(getIntent().hasExtra("idMasjid")
                && getIntent().hasExtra("nama_masjid")
                && getIntent().hasExtra("alamat_masjid")
                && getIntent().hasExtra("tahun_berdiri")
                && getIntent().hasExtra("daya_tampung"))
        {
            idMasjid = bundle.getString("idMasjid");
            namaMasjid = bundle.getString("nama_masjid");
            alamatMasjid = bundle.getString("alamat_masjid");
            tahunBerdiri = bundle.getString("tahun_berdiri");
            dayaTampung = bundle.getInt("daya_tampung");
        }
    }

    public void setupUIDataMasjid(){
        tv_namaMasjid = findViewById(R.id.tv_detail_nama_masjid);
        tv_alamatMasjid = findViewById(R.id.tv_detail_alamat_masjid);
        tv_tahunBerdiri = findViewById(R.id.tv_detail_tahun_berdiri_masjid);
        tv_dayaTampung = findViewById(R.id.tv_detail_daya_tampung_masjid);
        ivFotoMasjid = findViewById(R.id.iv_foto_detail_masjid);
    }

    public void setDataMasjid(){
        tv_namaMasjid.setText(namaMasjid);
        tv_alamatMasjid.setText(alamatMasjid);
        tv_tahunBerdiri.setText(tahunBerdiri);
        tv_dayaTampung.setText(String.valueOf(dayaTampung));
        getImage(idMasjid, ivFotoMasjid);
    }

    private void getImage(String idMasjid, ImageView ivFotoImage) {
        class GetImage extends AsyncTask<String,Void, Bitmap> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Bitmap b) {
                super.onPostExecute(b);
                ivFotoImage.setImageBitmap(b);
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                String id = idMasjid;
                String add = "https://qayimmasjid.com/API/qayimmasjid/getImage.php?id_masjid="+id;
                URL url = null;
                Bitmap image = null;
                try {
                    url = new URL(add);
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image;
            }
        }

        GetImage gi = new GetImage();
        gi.execute();
    }
}