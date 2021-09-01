package com.example.qayimmasjid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qayimmasjid.R;
import com.example.qayimmasjid.model.masjid.Kegiatan;
import com.example.qayimmasjid.model.masjid.Pengumuman;

import java.util.List;

public class PengumumanPenggunaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pengumuman_detail_masjid, parent, false);
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
            pengumumanViewHolder.tvCP.setText(pengumuman.getContactPerson());
            pengumumanViewHolder.tvDeskripsi.setText(pengumuman.getDeskripsiPengumuman());
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
        private TextView tvNamaPengumuman, tvDeskripsi, tvCP, tvTanggal;

        public PengumumanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaPengumuman = itemView.findViewById(R.id.tv_detail_masjid_nama_pengumuman);
            tvTanggal = itemView.findViewById(R.id.tv_detail_masjid_tanggal_pengumuman);
            tvCP = itemView.findViewById(R.id.tv_detail_masjid_cp_pengumuman);
            tvDeskripsi = itemView.findViewById(R.id.tv_detail_masjid_deskripsi_pengumuman);
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
