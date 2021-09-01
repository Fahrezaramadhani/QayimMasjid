package com.example.qayimmasjid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LupaPasswordActivity extends AppCompatActivity {

    private ImageButton backButton;
    private EditText etEmail;
    private MaterialButton kirimButton;
    private StringRequest stringRequest;
    private final String URL = "https://qayimmasjid.com/API/qayimmasjid/forgotpassword.php";
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setupUI();
        setupListeners();

    }

    public void setupUI() {
        backButton  = findViewById(R.id.back_button_lupa_password);
        etEmail       = findViewById(R.id.field_email_lupa_password);
        kirimButton = findViewById(R.id.button_kirim_lupa_password);
    }

    public void setupListeners(){
        backButtonListener();
        kirimButtonListener();
    }

    public void backButtonListener() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LupaPasswordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void kirimButtonListener() {
        kirimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();

                if(email.isEmpty()) {
                    Toast.makeText(LupaPasswordActivity.this, "Mohon isi email akun anda terlebih dahulu!", Toast.LENGTH_LONG).show();
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.d("Response", "onResponse: " + response);
                                        JSONObject object = new JSONObject(response);
                                        String mail = object.getString("error");

                                        if (mail.equals("false")) {
                                            Toast.makeText(LupaPasswordActivity.this, "Email terkirim ke akun email anda", Toast.LENGTH_LONG).show();
                                        } else  {
                                            Toast.makeText(LupaPasswordActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LupaPasswordActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        protected Map<String,String> getParams() throws AuthFailureError {
                            HashMap<String, String> forgotparams = new HashMap<>();
                            forgotparams.put("email", email);
                            return forgotparams;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(LupaPasswordActivity.this);
                    requestQueue.add(stringRequest);
                }
            }
        });
    }
}