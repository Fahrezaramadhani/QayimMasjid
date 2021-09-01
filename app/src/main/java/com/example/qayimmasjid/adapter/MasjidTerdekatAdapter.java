package com.example.qayimmasjid.adapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.qayimmasjid.R;
import com.example.qayimmasjid.model.masjid.Masjid;
import com.example.qayimmasjid.ui.kegiatan.DetailKegiatanMasjidActivity;
import com.example.qayimmasjid.ui.profil.DetailMasjidActivity;
import com.google.maps.android.SphericalUtil;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MasjidTerdekatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;

    private List<Masjid> mListMasjid;
    private boolean isLoadingAdd;

    public void setData(List<Masjid> list){
        this.mListMasjid = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mListMasjid != null && position == mListMasjid.size() - 1 && isLoadingAdd){
            return  TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_ITEM == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_masjid, parent, false);
            return  new MasjidViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_view, parent, false);
            return  new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM){
            Masjid masjid = mListMasjid.get(position);
            MasjidViewHolder masjidViewHolder = (MasjidViewHolder) holder;
            masjidViewHolder.tvNama.setText(masjid.getMnamamasjid());
            masjidViewHolder.tvAlamat.setText(masjid.getMalamat());
            Log.d("jarak", "onBindViewHolder: " + masjid.getDistance() + "  " + masjid.getMlatidue());
            masjidViewHolder.jarak.setText(String.valueOf(masjid.getDistance()) + " km");
            getImage(masjid.getMid_masjid(), masjidViewHolder.iv_fotoMasjid);

            masjidViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle =new Bundle();
                    bundle.putString("idMasjid", mListMasjid.get(position).getMid_masjid());
                    bundle.putString("nama_masjid", mListMasjid.get(position).getMnamamasjid());
                    bundle.putString("alamat_masjid", mListMasjid.get(position).getMalamat());
                    bundle.putString("tahun_berdiri", mListMasjid.get(position).getTahunBerdiri());
                    bundle.putInt("daya_tampung", mListMasjid.get(position).getDayaTampung());

                    Intent intent = new Intent(v.getContext(), DetailMasjidActivity.class);
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mListMasjid != null){
            return mListMasjid.size();
        }
        return 0;
    }

    public class MasjidViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNama;
        private TextView tvAlamat;
        private ImageView iv_fotoMasjid;
        private TextView jarak;

        public MasjidViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.list_masjid_nama);
            tvAlamat = itemView.findViewById(R.id.list_masjid_alamat);
            iv_fotoMasjid = itemView.findViewById(R.id.list_masjid_image);
            jarak = itemView.findViewById(R.id.jarak);
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
        mListMasjid.add(new Masjid("","", "", "",0,0, 0, image));
    }

    public void removeFooterLoading(){
        isLoadingAdd = false;
        int position = mListMasjid.size() - 1;
        Masjid masjid = mListMasjid.get(position);
        if (masjid != null){
            mListMasjid.remove(position);
            notifyDataSetChanged();
        }
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
