package com.example.qayimmasjid.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qayimmasjid.R;
import com.example.qayimmasjid.model.masjid.Kegiatan;

import java.util.List;

public class KegiatanPenggunaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;

    private List<Kegiatan> mListKegiatan;
    private boolean isLoadingAdd;

    public void setData(List<Kegiatan> list){
        this.mListKegiatan = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mListKegiatan != null && position == mListKegiatan.size() - 1 && isLoadingAdd){
            return  TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_ITEM == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_kegiatan_detail_masjid, parent, false);
            return  new KegiatanViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_view, parent, false);
            return  new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM){
            Kegiatan kegiatan = mListKegiatan.get(position);
            KegiatanViewHolder kegiatanViewHolder = (KegiatanViewHolder) holder;
            kegiatanViewHolder.tvNamaKegiatan.setText(kegiatan.getMnamakegiatan());
            kegiatanViewHolder.tvTanggal.setText(kegiatan.getTanggalKegiatan());
            kegiatanViewHolder.tvWaktu.setText(kegiatan.getMwaktukegiatan());
            kegiatanViewHolder.tvLokasi.setText(kegiatan.getMlokasikegiatan());
            kegiatanViewHolder.tvPemateri.setText(kegiatan.getMpemateri());
            kegiatanViewHolder.tvPenanggungJawab.setText(kegiatan.getMpenanggungjawab());
        }
    }

    @Override
    public int getItemCount() {
        if (mListKegiatan != null){
            return mListKegiatan.size();
        }
        return 0;
    }

    public class KegiatanViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNamaKegiatan;
        private TextView tvTanggal;
        private TextView tvWaktu;
        private TextView tvLokasi;
        private TextView tvPemateri;
        private TextView tvPenanggungJawab;


        public KegiatanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaKegiatan = itemView.findViewById(R.id.tv_detail_masjid_nama_kegiatan);
            tvTanggal = itemView.findViewById(R.id.tv_detail_masjid_tanggal_kegiatan);
            tvWaktu = itemView.findViewById(R.id.tv_detail_masjid_waktu_kegiatan);
            tvLokasi = itemView.findViewById(R.id.tv_detail_masjid_lokasi_kegiatan);
            tvPemateri = itemView.findViewById(R.id.tv_detail_masjid_pemateri_kegiatan);
            tvPenanggungJawab = itemView.findViewById(R.id.tv_detail_masjid_pj_kegiatan);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar_loading);
        }
    }

    public void addFooterLoading(){
        isLoadingAdd = true;
        mListKegiatan.add(new Kegiatan("","", "", "","","", "", ""));
    }

    public void removeFooterLoading(){
        isLoadingAdd = false;
        int position = mListKegiatan.size() - 1;
        Kegiatan kegiatan = mListKegiatan.get(position);
        if (kegiatan != null){
            mListKegiatan.remove(position);
            notifyDataSetChanged();
        }
    }
}
