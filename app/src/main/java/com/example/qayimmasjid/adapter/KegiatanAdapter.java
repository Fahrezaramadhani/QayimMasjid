package com.example.qayimmasjid.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Script;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qayimmasjid.R;
import com.example.qayimmasjid.model.masjid.Kegiatan;
import com.example.qayimmasjid.model.masjid.Masjid;
import com.example.qayimmasjid.model.masjid.Pengurus;
import com.example.qayimmasjid.ui.kegiatan.DetailKegiatanMasjidActivity;

import java.util.List;

public class KegiatanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_kegiatan, parent, false);
            return  new KegiatanAdapter.KegiatanViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_view, parent, false);
            return  new KegiatanAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM){
            Kegiatan kegiatan = mListKegiatan.get(position);
            KegiatanViewHolder masjidViewHolder = (KegiatanViewHolder) holder;
            masjidViewHolder.tvNamaKegiatan.setText(kegiatan.getMnamakegiatan());
            masjidViewHolder.tvTanggal.setText(kegiatan.getMwaktukegiatan());
            masjidViewHolder.tvPenanggungJawab.setText(kegiatan.getMpenanggungjawab());

            masjidViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle =new Bundle();
                    bundle.putString("idKegiatan", mListKegiatan.get(position).getMidkegiatan());

                    Intent intent = new Intent(v.getContext(), DetailKegiatanMasjidActivity.class);
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });
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
        private TextView tvPenanggungJawab;


        public KegiatanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaKegiatan = itemView.findViewById(R.id.tv_list_kegiatan_masjid);
            tvTanggal = itemView.findViewById(R.id.tv_list_masjid_tanggal);
            tvPenanggungJawab = itemView.findViewById(R.id.ketua_pelaksana);
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
        String image = null;
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
