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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qayimmasjid.MainActivity;
import com.example.qayimmasjid.R;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.model.masjid.Pengurus;
import com.example.qayimmasjid.model.masjid.TransaksiList;
import com.example.qayimmasjid.profil.ProfilMasjidViewModel;
import com.example.qayimmasjid.transaksi.TransaksiViewModel;
import com.example.qayimmasjid.ui.kegiatan.ManageKegiatanMasjidActivity;
import com.example.qayimmasjid.ui.pengumuman.ManagePengumumanMasjidActivity;
import com.example.qayimmasjid.ui.profil.DashboardTambahPengurusActivity;
import com.example.qayimmasjid.ui.transaksi.ManageTransaksiMasjidActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class HomePengurusMasjidFragment extends Fragment {
    private ProfilMasjidViewModel profilMasjidViewModel;
    private TransaksiViewModel transaksiViewModel;
    private ImageView iv_gambarMasjid;
    private TextView tv_namaMasjid, tv_totalPengeluaran, tv_totalPemasukan;
    private ImageButton buttonLogout, buttonKegiatan, buttonPengumuman, buttonTransaksi, buttonTambahPengurus;
    private Pengurus pengurus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_pengurus_masjid, container, false);

        profilMasjidViewModel = new ViewModelProvider(requireActivity()).get(ProfilMasjidViewModel.class);
        transaksiViewModel = new ViewModelProvider(requireActivity()).get(TransaksiViewModel.class);

        buttonLogout = view.findViewById(R.id.button_logout);
        buttonKegiatan = view.findViewById(R.id.button_kegiatan);
        buttonPengumuman = view.findViewById(R.id.button_pengumuman);
        buttonTransaksi = view.findViewById(R.id.button_transaksi);
        buttonTambahPengurus = view.findViewById(R.id.button_tambah_pengurus_masjid);
        iv_gambarMasjid = view.findViewById(R.id.home_pengurus_gambar_masjid);
        tv_namaMasjid = view.findViewById(R.id.namaMasjid);
        tv_totalPemasukan = view.findViewById(R.id.total_pemasukan_masjid);
        tv_totalPengeluaran = view.findViewById(R.id.total_pengeluaran_masjid);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(getActivity()).logout(view, getActivity());
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        buttonKegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ManageKegiatanMasjidActivity.class));
            }
        });

        buttonPengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ManagePengumumanMasjidActivity.class));
            }
        });

        buttonTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ManageTransaksiMasjidActivity.class));
            }
        });

        buttonTambahPengurus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DashboardTambahPengurusActivity.class));
            }
        });

        setHomePengurus(view);
        setDataTransaksi();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setHomePengurus(getView());
        setDataTransaksi();
    }

    private void setHomePengurus(View view){
        pengurus = SharedPrefManager.getInstance(getActivity()).getUser();
        Log.d("profil", "setProfilMasjid: " + pengurus.getMidmasjid());
        profilMasjidViewModel.getProfilMasjid(pengurus.getMidmasjid());
        profilMasjidViewModel.getData().observe(getViewLifecycleOwner(), profileMasjid -> {
            if (profileMasjid != null){
                tv_namaMasjid.setText(profileMasjid.getMasjid().getMnamamasjid());
                getImage(view);
            }
        });
    }

    private void getImage(View view) {
        class GetImage extends AsyncTask<String,Void, Bitmap> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(view.getContext(), "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(Bitmap b) {
                super.onPostExecute(b);
                loading.dismiss();
                iv_gambarMasjid.setImageBitmap(b);
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

    private void setDataTransaksi(){
        transaksiViewModel.getListTransaki(SharedPrefManager.getInstance(getContext()).getUser().getMidmasjid());
        transaksiViewModel.getData().observe(getViewLifecycleOwner(), transaksiList -> {
            if (transaksiList != null){
                String totalPemasukan, totalPengeluaran;
                String[] arrayOfPemasukan, arrayOfPengeluaran;
                Locale id = new Locale("in", "ID");
                NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(id);
                totalPemasukan = dollarFormat.format(totalUang(transaksiList, "pemasukan"));
                totalPengeluaran = dollarFormat.format(totalUang(transaksiList, "pengeluaran"));
                arrayOfPemasukan = totalPemasukan.split(",");
                arrayOfPengeluaran = totalPengeluaran.split(",");
                tv_totalPemasukan.setText(arrayOfPemasukan[0]);
                tv_totalPengeluaran.setText(arrayOfPengeluaran[0]);
            }
        });
    }

    private int totalUang(TransaksiList transaksiList, String type){
        int total = 0;
        for (int i = 0; i < transaksiList.getListTransaksi().size(); i++){
            if (transaksiList.getListTransaksi().get(i).getMjenistransaksi().matches(type)){
                total = total + transaksiList.getListTransaksi().get(i).getMnominal();
            }
        }
        return total;
    }
}