package com.example.qayimmasjid.profil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qayimmasjid.model.masjid.Masjid;
import com.example.qayimmasjid.model.masjid.Pengurus;
import com.example.qayimmasjid.model.masjid.ProfileMasjid;

public class ProfilMasjidViewModel extends ViewModel {
    private MutableLiveData<ProfileMasjid> mutableLiveData;
    private ProfilMasjidRepository profilMasjidRepository;

    public void getProfilMasjid(String idMasjid){
        profilMasjidRepository = new ProfilMasjidRepository();
        mutableLiveData = profilMasjidRepository.getProfilMasjid(idMasjid);
    }

    public void updateProfilMasjid(Masjid masjid){
        profilMasjidRepository = new ProfilMasjidRepository();
        mutableLiveData = profilMasjidRepository.updateProfilMasjid(masjid);
    }

    public void uploadImage(String idMasjid, String image){
        profilMasjidRepository = new ProfilMasjidRepository();
        mutableLiveData = profilMasjidRepository.uploadImage(idMasjid, image);
    }

    public void tambahMasjid(Masjid masjid){
        profilMasjidRepository = new ProfilMasjidRepository();
        mutableLiveData = profilMasjidRepository.tambahMasjid(masjid);
    }

    public void deleteAccount(String idPengurus){
        profilMasjidRepository = new ProfilMasjidRepository();
        mutableLiveData = profilMasjidRepository.deleteAccount(idPengurus);
    }

    public void updateAccount(Pengurus pengurus){
        profilMasjidRepository = new ProfilMasjidRepository();
        mutableLiveData = profilMasjidRepository.updateProfilPengurus(pengurus);
    }

    public LiveData<ProfileMasjid> getData() {
        return mutableLiveData;
    }
}
