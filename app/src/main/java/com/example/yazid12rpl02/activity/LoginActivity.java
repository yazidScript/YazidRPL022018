package com.example.yazid12rpl02.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.yazid12rpl02.activity.Admin.AdminDashboardActivity;
import com.example.yazid12rpl02.activity.Admin.AdminUserActivity;
import com.example.yazid12rpl02.activity.Customer.CustomerActivity;
import com.example.yazid12rpl02.activity.Customer.DashboardActivity;
import com.example.yazid12rpl02.helper.Config;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private TextView tv_Signup;
    private EditText et_Username, et_Password;
    private LinearLayout btn_Login;
    private ProgressDialog mProgress;
    private boolean isFormFilled = false;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding();
        tv_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        btn_Login.setOnClickListener(new View.OnClickListener() {
            private void doNothing() {

            }

            @Override
            public void onClick(View view) {
                isFormFilled = true;
                final String username = et_Username.getText().toString();
                final String password = et_Password.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Harap lengkapi isian yang tersedia", Toast.LENGTH_SHORT).show();
                    isFormFilled = false;
                }
                if (isFormFilled) {
                    mProgress.show();
                    HashMap<String, String> body = new HashMap<>();
                    body.put("act", "login");
                    body.put("username", username);
                    body.put("password", password);
                    AndroidNetworking.post(Config.BASE_URL_API + "auth.php")
                            .addBodyParameter(body)
                            .setOkHttpClient(((RS) getApplication()).getOkHttpClient())
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("YZD", "respon : " + response);

                                        String status = response.optString(Config.RESPONSE_STATUS_FIELD);
                                        String message = response.optString(Config.RESPONSE_MESSAGE_FIELD);
                                        if (status.equalsIgnoreCase(Config.RESPONSE_STATUS_VALUE_SUCCESS)) {
                                            JSONObject payload = response.optJSONObject(Config.RESPONSE_PAYLOAD_FIELD);
                                            String U_ID = payload.optString("LOGIN_ID");
                                            String U_NAME = payload.optString("LOGIN_NAME");
                                            String U_LOGIN_TOKEN = payload.optString("U_LOGIN_TOKEN");
                                            String U_GROUP_ROLE = payload.optString("GROUP_ROLE");
                                            String U_EMAIL = payload.optString("LOGIN_EMAIL");

                                            preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                            preferences.edit()
                                                    .putString(Config.LOGIN_ID_SHARED_PREF, U_ID)
                                                    .putString(Config.LOGIN_NAME_SHARED_PREF, U_NAME)
                                                    .putString(Config.LOGIN_TOKEN_SHARED_PREF, U_LOGIN_TOKEN)
                                                    .putString(Config.LOGIN_GROUP_ID_SHARED_PREF, U_GROUP_ROLE)
                                                    .putString(Config.LOGIN_EMAIL_SHARED_PREF, U_EMAIL)
                                                    .apply();

                                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                            if (U_GROUP_ROLE.equalsIgnoreCase("GR_ADMIN"))  intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);

                                            startActivity(intent);
                                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                            finish();
                                            finishAffinity();
                                        }
                                        else {
                                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                        }

                                    mProgress.dismiss();
                                }

                                @Override
                                public void onError(ANError anError) {
                                    mProgress.dismiss();
                                    Toast.makeText(LoginActivity.this, Config.TOAST_AN_ERROR, Toast.LENGTH_SHORT).show();
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
        tv_Signup = findViewById(R.id.tv_signup);
        et_Username = findViewById(R.id.et_Username);
        et_Password = findViewById(R.id.et_password);
        btn_Login = findViewById(R.id.btn_Login);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Login");
        mProgress.setMessage("Mohon tunggu...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
    }
}
