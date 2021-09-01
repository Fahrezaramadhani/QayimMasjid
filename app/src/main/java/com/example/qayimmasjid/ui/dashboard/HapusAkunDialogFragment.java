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
import android.widget.Toast;

import com.example.qayimmasjid.MainActivity;
import com.example.qayimmasjid.R;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.profil.ProfilMasjidViewModel;
import com.example.qayimmasjid.ui.SplashScreen;
import com.example.qayimmasjid.ui.kegiatan.ManageKegiatanMasjidActivity;

public class HapusAkunDialogFragment extends DialogFragment {
    private ProfilMasjidViewModel profilMasjidViewModel;
    private String idPengurus;

    public HapusAkunDialogFragment (String idPengurus){
        this.idPengurus = idPengurus;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hapus_akun_dialog, container, false);

        profilMasjidViewModel = new ViewModelProvider(this).get(ProfilMasjidViewModel.class);

        Button okay = view.findViewById(R.id.btn_okay);
        Button cancel = view.findViewById(R.id.btn_cancel);

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount(v);
                Toast.makeText(getContext(), "Okay", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
                startActivity(new Intent(getActivity(), SplashScreen.class));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        });
        return view;
    }

    private void deleteAccount(View view){
        profilMasjidViewModel.deleteAccount(idPengurus);
        profilMasjidViewModel.getData().observe(getViewLifecycleOwner(), profileMasjid -> {
            if (profileMasjid != null){
                Log.d("response", "deleteAccount: berhasil" );
                SharedPrefManager.getInstance(getContext()).logout(view, getActivity());
            }else{
                Toast.makeText(getContext(), "Pengurus gagal di hapus", Toast.LENGTH_SHORT).show();
            }
        });
    }
}