package com.example.yazid12rpl02.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.yazid12rpl02.R;
import com.example.yazid12rpl02.RS;
import com.example.yazid12rpl02.helper.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private TextView tv_Login, back;
    private EditText et_Name, et_Password, et_Email, et_NoHp, et_Address, et_NoKtp;
    private LinearLayout btn_Signup;
    private ProgressDialog mProgress;
    private boolean mIsFormFilled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        binding();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });


        btn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsFormFilled = true;
                final String name = et_Name.getText().toString();
                final String phone = et_NoHp.getText().toString();
                final String address = et_Address.getText().toString();
                final String ktp = et_NoKtp.getText().toString();
                final String password = et_Password.getText().toString();
                final String email = et_Email.getText().toString().trim();

                if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || ktp.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Harap lengkapi isian yang tersedia", Toast.LENGTH_SHORT).show();
                    mIsFormFilled = false;
                }

                if (mIsFormFilled) {
                    HashMap<String, String> body = new HashMap<>();
                    body.put("act", "register_user");

                    mProgress.show();

                    body.put("id", phone);
                    body.put("name", name);
                    body.put("address", address);
                    body.put("noktp", ktp);
                    body.put("password", password);
                    body.put("email", email);

                    AndroidNetworking.post(Config.BASE_URL_API + "auth.php")
                            .addBodyParameter(body)
                            .setPriority(Priority.MEDIUM)
                            .setOkHttpClient(((RS) getApplication()).getOkHttpClient())
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String message = response.getString(Config.RESPONSE_MESSAGE_FIELD);
                                        String status = response.getString(Config.RESPONSE_STATUS_FIELD);

                                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();

                                        if (status.equalsIgnoreCase(Config.RESPONSE_STATUS_VALUE_SUCCESS)) {
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finishAffinity();
                                        }
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d("HBB", "JSONException: " + e.getMessage());
                                    }

                                    mProgress.dismiss();
                                }

                                @Override
                                public void onError(ANError anError) {
                                    mProgress.dismiss();
                                    Toast.makeText(RegisterActivity.this, Config.TOAST_AN_ERROR, Toast.LENGTH_SHORT).show();
                                    Log.d("HBB", "onError: " + anError.getErrorBody());
                                    Log.d("HBB", "onError: " + anError.getLocalizedMessage());
                                    Log.d("HBB", "onError: " + anError.getErrorDetail());
                                    Log.d("HBB", "onError: " + anError.getResponse());
                                    Log.d("HBB", "onError: " + anError.getErrorCode());
                                }
                            });
                }

            }
        });
    }

    private void binding(){
        back = findViewById(R.id.back);
        tv_Login = findViewById(R.id.tv_Login);
        et_Email = findViewById(R.id.et_Email);
        et_Password = findViewById(R.id.et_Password);
        et_NoHp = findViewById(R.id.et_NoHpregister);
        et_NoKtp = findViewById(R.id.et_NoKtp);
        et_Address = findViewById(R.id.et_Address);
        et_Name = findViewById(R.id.et_Name);
        btn_Signup = findViewById(R.id.btn_Signup);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Login");
        mProgress.setMessage("Mohon tunggu...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
    }}
