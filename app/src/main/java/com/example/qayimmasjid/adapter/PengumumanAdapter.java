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
import com.example.qayimmasjid.model.masjid.Pengumuman;
import com.example.qayimmasjid.ui.kegiatan.DetailKegiatanMasjidActivity;
import com.example.qayimmasjid.ui.pengumuman.DetailPengumumanMasjidActivity;

import java.util.List;

public class PengumumanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;

    private List<Pengumuman> listPengumuman;
    private boolean isLoadingAdd;

    public void setData(List<Pengumuman> list){
        this.listPengumuman = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (listPengumuman != null && position == listPengumuman.size() - 1 && isLoadingAdd){
            return  TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_ITEM == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pengumuman, parent, false);
            return  new PengumumanViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_view, parent, false);
            return  new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM){
            Pengumuman pengumuman = listPengumuman.get(position);
            PengumumanViewHolder pengumumanViewHolder = (PengumumanViewHolder) holder;
            pengumumanViewHolder.tvNamaPengumuman.setText(pengumuman.getMnamapengumuman());
            pengumumanViewHolder.tvTanggal.setText(pengumuman.getTanggalPengumuman());

            pengumumanViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle =new Bundle();
                    bundle.putString("idPengumuman", listPengumuman.get(position).getMidpengumuman());

                    Intent intent = new Intent(v.getContext(), DetailPengumumanMasjidActivity.class);
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (listPengumuman != null){
            return listPengumuman.size();
        }
        return 0;
    }

    public class PengumumanViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNamaPengumuman, tvTanggal;

        public PengumumanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaPengumuman = itemView.findViewById(R.id.tv_list_pengumuman_masjid);
            tvTanggal = itemView.findViewById(R.id.tv_list_tanggal_pengumuman_masjid);
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
        listPengumuman.add(new Pengumuman("","", "", "","","", ""));
    }

    public void removeFooterLoading(){
        isLoadingAdd = false;
        int position = listPengumuman.size() - 1;
        Pengumuman pengumuman = listPengumuman.get(position);
        if (pengumuman != null){
            listPengumuman.remove(position);
            notifyDataSetChanged();
        }
    }
}
