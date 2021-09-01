package com.example.qayimmasjid.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qayimmasjid.R;
import com.example.qayimmasjid.model.masjid.Kegiatan;
import com.example.qayimmasjid.model.masjid.Pengumuman;
import com.example.qayimmasjid.model.masjid.Transaksi;
import com.example.qayimmasjid.ui.pengumuman.DetailPengumumanMasjidActivity;
import com.example.qayimmasjid.ui.transaksi.DetailTransaksiMasjidActivity;

import java.util.List;

public class TransaksiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;

    private List<Transaksi> listTransaksi;
    private List<String> listNamaKegiatan;
    private boolean isLoadingAdd;

    public void setData(List<Transaksi> list, List<String> listNamaKegiatan){
        this.listTransaksi = list;
        this.listNamaKegiatan = listNamaKegiatan;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (listTransaksi != null && position == listTransaksi.size() - 1 && isLoadingAdd){
            return  TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_ITEM == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_transaksi, parent, false);
            return  new TransaksiViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_view, parent, false);
            return  new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM){
            Transaksi transaksi = listTransaksi.get(position);
            TransaksiViewHolder transaksiViewHolder = (TransaksiViewHolder) holder;
            transaksiViewHolder.tvNamaTransaksi.setText(transaksi.getMnamatransaksi());
            transaksiViewHolder.tvTanggalTransaksi.setText(transaksi.getMtanggal());
            transaksiViewHolder.tvNamaKegiatan.setText(listNamaKegiatan.get(position));
            if (!transaksi.getMjenistransaksi().matches("pemasukan")) {
                transaksiViewHolder.iv_tipeTransaksi.setImageResource(R.drawable.ic_pengeluaran);
            }

            transaksiViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle =new Bundle();
                    bundle.putString("idTransaksi", listTransaksi.get(position).getMidtransaksi());

                    Intent intent = new Intent(v.getContext(), DetailTransaksiMasjidActivity.class);
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (listTransaksi != null){
            return listTransaksi.size();
        }
        return 0;
    }

    public class TransaksiViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNamaTransaksi, tvTanggalTransaksi, tvNamaKegiatan;
        private ImageView iv_tipeTransaksi;

        public TransaksiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaTransaksi = itemView.findViewById(R.id.tv_list_nama_transaksi);
            tvNamaKegiatan= itemView.findViewById(R.id.tv_list_kegiatan_transaksi);
            tvTanggalTransaksi = itemView.findViewById(R.id.tv_list_tanggal_transaksi);
            iv_tipeTransaksi = itemView.findViewById(R.id.list_icon_jenis_transaksi);
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
        listTransaksi.add(new Transaksi("","", "", "","",0, ""));
    }

    public void removeFooterLoading(){
        isLoadingAdd = false;
        int position = listTransaksi.size() - 1;
        Transaksi transaksi = listTransaksi.get(position);
        if (transaksi != null){
            listTransaksi.remove(position);
            notifyDataSetChanged();
        }
    }
}
