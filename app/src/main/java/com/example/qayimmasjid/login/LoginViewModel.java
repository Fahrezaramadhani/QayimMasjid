package com.example.qayimmasjid.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.qayimmasjid.model.masjid.Login;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<Login> mutableLiveData;
    private LoginRepository loginRepository;

    public void getData(String email, String password){
        loginRepository = new LoginRepository();
        mutableLiveData = loginRepository.getLoginData(email, password);
    }

    public LiveData<Login> getLoginData() {
        return mutableLiveData;
    }
}
