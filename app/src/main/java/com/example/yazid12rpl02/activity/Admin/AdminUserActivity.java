package com.example.yazid12rpl02.activity.Admin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.yazid12rpl02.R;
import com.example.yazid12rpl02.RS;
import com.example.yazid12rpl02.adapter.AdminUserAdapter;
import com.example.yazid12rpl02.helper.AppHelper;
import com.example.yazid12rpl02.helper.Config;
import com.example.yazid12rpl02.model.UserAdminModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminUserActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ImageView iv_more_admin, ivNew, icon_user;

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rv;

    private ArrayList<UserAdminModel> mList = new ArrayList<>();
    private AdminUserAdapter mAdapter;

    private String mLoginToken = "";
    private String mUserId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        mLoginToken = sp.getString(Config.LOGIN_TOKEN_SHARED_PREF,"");
        mUserId = sp.getString(Config.LOGIN_ID_SHARED_PREF, "");

        if(mLoginToken.equalsIgnoreCase("") || mUserId.equalsIgnoreCase("")) {
            finish();
            Config.forceLogout(AdminUserActivity.this);
        }

        setContentView(R.layout.activity_admin_user);
        binding();
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.post(new Runnable() {
            private void doNothing() {

            }

            @Override
            public void run() {
                getUserList();
            }
        });

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

    }

    private void binding() {
        icon_user = findViewById(R.id.icon_user);
        icon_user.setVisibility(View.VISIBLE);
        iv_more_admin = findViewById(R.id.iv_more_admin);
        iv_more_admin.setOnClickListener(new View.OnClickListener() {
            private void doNothing() {

            }

            @Override
            public void onClick(View view) {
                logout();
            }
        });

        rv = findViewById(R.id.rvUserManage);
        swipeRefresh = findViewById(R.id.swipeRefresh);

    }

    @Override
    public void onRefresh() {
        getUserList();
    }


    public void show(){
        mAdapter = new AdminUserAdapter(AdminUserActivity.this, mList, mLoginToken, AdminUserActivity.this);

        rv.setAdapter(mAdapter);
    }


    public void getUserList() {
        swipeRefresh.setRefreshing(true);
        HashMap<String, String> body = new HashMap<>();
        body.put("act", "get_user");
        body.put("loginToken", mLoginToken);
        AndroidNetworking.post(Config.BASE_URL_API + "auth.php")
                .addBodyParameter(body)
                .setPriority(Priority.MEDIUM)
                .setOkHttpClient(((RS) getApplication()).getOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        swipeRefresh.setRefreshing(false);
                        if (mAdapter != null) {
                            mAdapter.clearData();
                            mAdapter.notifyDataSetChanged();
                        }
                        if (mList != null) mList.clear();
                        Log.d("RBA", "res" + response);

                        String status = response.optString(Config.RESPONSE_STATUS_FIELD);
                        String message = response.optString(Config.RESPONSE_MESSAGE_FIELD);
                        if (status.trim().equalsIgnoreCase(Config.RESPONSE_STATUS_VALUE_SUCCESS)) {
                            JSONArray payload = response.optJSONArray(Config.RESPONSE_PAYLOAD_FIELD);

                            if (payload == null) {
                                Toast.makeText(AdminUserActivity.this,"Tidak ada user",Toast.LENGTH_SHORT).show();                                return;
                            }

                            for (int i = 0; i < payload.length(); i++) {
                                JSONObject dataUser = payload.optJSONObject(i);
                                UserAdminModel item = AppHelper.mapUserAdminModel(dataUser);
                                mList.add(item);
                            }
                            show();
                        } else {
                            Toast.makeText(AdminUserActivity.this, message, Toast.LENGTH_SHORT).show();
                            JSONObject payload = response.optJSONObject(Config.RESPONSE_PAYLOAD_FIELD);
                            if (payload != null && payload.optString("API_ACTION").equalsIgnoreCase("LOGOUT"))
                                Config.forceLogout(AdminUserActivity.this);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(AdminUserActivity.this, Config.TOAST_AN_ERROR, Toast.LENGTH_SHORT).show();
                        Log.d("RBA", "onError: " + anError.getErrorBody());
                        Log.d("RBA", "onError: " + anError.getLocalizedMessage());
                        Log.d("RBA", "onError: " + anError.getErrorDetail());
                        Log.d("RBA", "onError: " + anError.getResponse());
                        Log.d("RBA", "onError: " + anError.getErrorCode());
                    }
                });

    }
    private void logout() {
        new AlertDialog.Builder(AdminUserActivity.this)
                .setTitle("Logout")
                .setMessage("Anda yakin akan logout ?")
                .setNegativeButton("Tidak", null)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    private void doNothing() {

                    }

                    public void onClick(DialogInterface arg0, int arg1) {
                        Config.forceLogout(AdminUserActivity.this);
                    }
                }).create().show();
    }

}