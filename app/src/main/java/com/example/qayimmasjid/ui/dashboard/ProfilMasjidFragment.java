package com.example.qayimmasjid.ui.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qayimmasjid.R;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.model.masjid.Pengurus;
import com.example.qayimmasjid.profil.ProfilMasjidViewModel;
import com.example.qayimmasjid.ui.kegiatan.EditKegiatanMasjidActivity;
import com.example.qayimmasjid.ui.profil.EditProfilMasjidActivity;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ProfilMasjidFragment extends Fragment {
    private ProfilMasjidViewModel profilMasjidViewModel;
    private MaterialButton btnEditProfilMasjid;
    private TextView tv_namaMasjid;
    private TextView tv_alamatMasjid;
    private TextView tv_tahunBerdiri;
    private TextView tv_dayaTampung;
    private TextView tv_pengurus;
    private ImageView iv_foto;
    private Pengurus pengurus;
    private String namaMasjid, alamatMasjid, tahunBerdiri, dayaTampung;

    @Override
    public void onResume() {
        super.onResume();
        setProfilMasjid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil_masjid, container, false);

        profilMasjidViewModel = new ViewModelProvider(getActivity()).get(ProfilMasjidViewModel.class);

        btnEditProfilMasjid = view.findViewById(R.id.button_edit_profil_masjid);
        tv_namaMasjid = view.findViewById(R.id.tv_nama_masjid);
        tv_alamatMasjid = view.findViewById(R.id.tv_alamat_masjid);
        tv_tahunBerdiri = view.findViewById(R.id.tv_tahun_berdiri);
        tv_dayaTampung = view.findViewById(R.id.tv_daya_tampung);
        tv_pengurus = view.findViewById(R.id.tv_pengurus);
        iv_foto = view.findViewById(R.id.foto_masjid);

        btnEditProfilMasjid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle =new Bundle();
                bundle.putString("nama_masjid", namaMasjid);
                bundle.putString("alamat_masjid", alamatMasjid);
                bundle.putString("tahun_berdiri", tahunBerdiri);
                bundle.putString("daya_tampung", dayaTampung);

                Intent intent = new Intent(v.getContext(), EditProfilMasjidActivity.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

        setProfilMasjid();

        return view;
    }

    private void setProfilMasjid(){
        pengurus = SharedPrefManager.getInstance(getActivity()).getUser();
        Log.d("profil", "setProfilMasjid: " + pengurus.getMidmasjid());
        profilMasjidViewModel.getProfilMasjid(pengurus.getMidmasjid());
        profilMasjidViewModel.getData().observe(getViewLifecycleOwner(), profileMasjid -> {
            if (profileMasjid != null){
                tv_namaMasjid.setText(profileMasjid.getMasjid().getMnamamasjid());
                Log.d("profil", "setProfilMasjid: " + profileMasjid.getMasjid().getMnamamasjid());
                tv_alamatMasjid.setText(profileMasjid.getMasjid().getMalamat());
                tv_tahunBerdiri.setText(profileMasjid.getMasjid().getTahunBerdiri());
                tv_dayaTampung.setText(String.valueOf(profileMasjid.getMasjid().getDayaTampung()));
                tv_pengurus.setText(getPengurus(profileMasjid.getListPengurus()));
                getImage();
                namaMasjid = profileMasjid.getMasjid().getMnamamasjid();
                alamatMasjid = profileMasjid.getMasjid().getMalamat();
                dayaTampung = String.valueOf(profileMasjid.getMasjid().getDayaTampung());
                tahunBerdiri = profileMasjid.getMasjid().getTahunBerdiri();
            }
        });
    }

    private String getPengurus(List<Pengurus> list){
        String listPengurus = "";
        for (int i = 0; i < list.size(); i++){
            listPengurus = listPengurus + list.get(i).getMnamapengurus();
            if (list.size()-1!= i){
                listPengurus = listPengurus + ", ";
            }
        }
        return listPengurus;
    }

    private void getImage() {
        class GetImage extends AsyncTask<String,Void, Bitmap> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Bitmap b) {
                super.onPostExecute(b);
                iv_foto.setImageBitmap(b);
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                String id = pengurus.getMidmasjid();
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