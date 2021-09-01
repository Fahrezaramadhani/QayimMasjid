package com.example.qayimmasjid.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qayimmasjid.R;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.model.masjid.ProfileMasjid;
import com.example.qayimmasjid.profil.ProfilMasjidViewModel;
import com.example.qayimmasjid.ui.profil.EditProfilPengurusMasjidActivity;
import com.google.android.material.button.MaterialButton;

public class ProfilPengurusMasjidFragment extends Fragment {
    private ProfilMasjidViewModel profilMasjidViewModel;
    private MaterialButton btnEditProfilPengurusMasjid, btnDeleteProfilPengurusMasjid;
    private TextView tv_namaPengurus, tv_alamatPengurus, tv_emailPengurus, tv_idPengurus;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil_pengurus_masjid, container, false);

        profilMasjidViewModel = new ViewModelProvider(requireActivity()).get(ProfilMasjidViewModel.class);

        btnEditProfilPengurusMasjid = view.findViewById(R.id.button_edit_profil_pengurus_masjid);
        btnDeleteProfilPengurusMasjid = view.findViewById(R.id.button_hapus_akun_pengurus_masjid);

        tv_idPengurus = view.findViewById(R.id.tv_id_pengurus);
        tv_namaPengurus = view.findViewById(R.id.tv_nama_pengurus);
        tv_alamatPengurus = view.findViewById(R.id.tv_alamat_pengurus);
        tv_emailPengurus = view.findViewById(R.id.tv_email);

        btnEditProfilPengurusMasjid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfilPengurusMasjidActivity.class));
            }
        });

        btnDeleteProfilPengurusMasjid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                deleteAccount(v);
                openDialogHapusAkun();
            }
        });

        setProfilPengurus();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setProfilPengurus();
    }

    public void openDialogHapusAkun() {
        HapusAkunDialogFragment dialog = new HapusAkunDialogFragment(SharedPrefManager.getInstance(getActivity()).getUser().getMidpengurus());
        dialog.show(getFragmentManager(), "Dialog Hapus Akun");
    }

    private void setProfilPengurus(){
        profilMasjidViewModel.getProfilMasjid(SharedPrefManager.getInstance(getContext()).getUser().getMidmasjid());
        profilMasjidViewModel.getData().observe(getViewLifecycleOwner(), profileMasjid -> {
            int index = getIndexPengurus(profileMasjid, SharedPrefManager.getInstance(getContext()).getUser().getMidpengurus());
            Log.d("index", "setProfilPengurus: " + index + SharedPrefManager.getInstance(getContext()).getUser().getMidpengurus() + profileMasjid.getListPengurus().size());
            tv_idPengurus.setText(profileMasjid.getListPengurus().get(index).getMidpengurus());
            tv_namaPengurus.setText(profileMasjid.getListPengurus().get(index).getMnamapengurus());
            tv_alamatPengurus.setText(profileMasjid.getListPengurus().get(index).getAlamatPengurus());
            tv_emailPengurus.setText(profileMasjid.getListPengurus().get(index).getMemailpengurus());
        });
    }

    private int getIndexPengurus(ProfileMasjid profileMasjid, String idPengurus){
        int i;
        for (i = 0; i < profileMasjid.getListPengurus().size(); i++){
            Log.d("cari", "getIndexPengurus: " + profileMasjid.getListPengurus().size());
            if (profileMasjid.getListPengurus().get(i).getMidpengurus().matches(idPengurus)){
                Log.d("cari", "getIndexPengurus: " + profileMasjid.getListPengurus().get(i).getMidpengurus());
                return i;
            }
        }
        return i;
    }

    private void deleteAccount(View view){
        profilMasjidViewModel.deleteAccount(SharedPrefManager.getInstance(getContext()).getUser().getMidpengurus());
        profilMasjidViewModel.getData().observe(getViewLifecycleOwner(), profileMasjid -> {
            if (profileMasjid != null){
                SharedPrefManager.getInstance(getActivity()).logout(view, getActivity());
                Toast.makeText(getContext(), "Pengurus berhasil di hapus", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "Pengurus gagal di hapus", Toast.LENGTH_SHORT).show();
            }
        });
    }
}