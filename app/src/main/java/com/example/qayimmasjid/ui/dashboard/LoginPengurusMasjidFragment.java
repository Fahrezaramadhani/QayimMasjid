package com.example.qayimmasjid.ui.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qayimmasjid.LupaPasswordActivity;
import com.example.qayimmasjid.MainActivityPengurusMasjid;
import com.example.qayimmasjid.R;
import com.example.qayimmasjid.SharedPrefManager;
import com.example.qayimmasjid.TambahPengurusMasjidActivity;
import com.example.qayimmasjid.login.LoginViewModel;
import com.google.android.material.button.MaterialButton;

public class LoginPengurusMasjidFragment extends Fragment {

    private MaterialButton buttonLogin;
    private EditText email, password;
    private TextView register, forgotPassword;
    private LoginViewModel loginViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_pengurus_masjid, container, false);

        //Initialize view model
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        setupUI(view);
        setupListeners(view);

        return view;
    }

    public void setupUI(View view){
        email          = view.findViewById(R.id.field_login_email);
        password       = view.findViewById(R.id.field_login_password);
        buttonLogin    = view.findViewById(R.id.button_login);
        register       = view.findViewById(R.id.tv_daftar_akun_baru);
        forgotPassword = view.findViewById(R.id.tv_lupa_password);
    }

    public void setupListeners(View view){
        loginListener(view);
        registerListener(view);
        forgotPasswordListener();
    }

    public void loginListener(View view) {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;
                isValid = validateAccount();
                loginAccount(view, isValid);
            }
        });
    }

    public void registerListener(View view) {
        // If User need to register account
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TambahPengurusMasjidActivity.class));
            }
        });
    }

    public void forgotPasswordListener() {
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LupaPasswordActivity.class));
            }
        });
    }

    public boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    // Module for validation check login account
    public boolean validateAccount (){
        boolean isValid = true;

        if(isEmpty(email)) {
            email.setError("Email tidak boleh kosong!");
            isValid = false;
        } else if(!isEmail(email)){
            email.setError("Email tidak valid!");
            isValid = false;
        }

        if(isEmpty(password)) {
            password.setError("Password tidak boleh kosong!");
            isValid = false;
        } else {
            if (password.getText().toString().length() < 8) {
                password.setError("Password harus lebih dari 8 digit!");
                isValid = false;
            }
        }
        return isValid;
    }

    public void loginAccount (View view, boolean isValid) {
        if (isValid) {
            String emailValue = email.getText().toString();
            String passwordValue = password.getText().toString();
            loginViewModel.getData(emailValue, passwordValue);
            loginViewModel.getLoginData().observe(getViewLifecycleOwner(), login -> {
                if (login.getPengurus() != null && login.getMasjid() != null){
                    SharedPrefManager.getInstance(getContext()).userLogin(login.getPengurus(), login.getMasjid());
                    Log.d("login", "loginAccount: " + login.getPengurus().getMnamapengurus());
                    Toast.makeText(getActivity(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), MainActivityPengurusMasjid.class);
                    view.getContext().startActivity(intent);
                    Activity activity = (Activity) getActivity();
                    activity.finish();
                }
                else{
                    Toast.makeText(view.getContext(), "Email atau Password salah!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}