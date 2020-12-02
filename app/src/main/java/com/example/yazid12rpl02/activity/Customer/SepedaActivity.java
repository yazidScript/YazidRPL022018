package com.example.yazid12rpl02.activity.Customer;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.yazid12rpl02.R;
import com.example.yazid12rpl02.RS;
import com.example.yazid12rpl02.activity.Admin.AdminSepedaActivity;
import com.example.yazid12rpl02.activity.Admin.AdminSepedaCreateActivity;
import com.example.yazid12rpl02.adapter.AdminSepedaAdapter;
import com.example.yazid12rpl02.adapter.SepedaAdapter;
import com.example.yazid12rpl02.helper.AppHelper;
import com.example.yazid12rpl02.helper.Config;
import com.example.yazid12rpl02.model.SepedaModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SepedaActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private ImageView ivMore, icon_bike;

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rv;

    private ArrayList<SepedaModel> mList = new ArrayList<>();
    private SepedaAdapter mAdapter;

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
            Config.forceLogout(SepedaActivity.this);
        }
        setContentView(R.layout.activity_sepeda);
        binding();
    }
    private void binding() {
        icon_bike = findViewById(R.id.icon_bike);
        icon_bike.setVisibility(View.VISIBLE);
        ivMore = findViewById(R.id.iv_more_admin);
        ivMore.setOnClickListener(new View.OnClickListener() {
            private void doNothing() {

            }

            @Override
            public void onClick(View v) {
                logout();
            }
        });

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.post(new Runnable() {
            private void doNothing() {

            }

            @Override
            public void run() {
                getSepedaList();
            }
        });

        rv = findViewById(R.id.rvSepeda);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(this,2));

    }

    public void show(){
        mAdapter = new SepedaAdapter(SepedaActivity.this, mList, mLoginToken, SepedaActivity.this);

        rv.setAdapter(mAdapter);
    }

    public void getSepedaList() {
        swipeRefresh.setRefreshing(true);
        HashMap<String, String> body = new HashMap<>();
        body.put("act", "get_unit");
        body.put("loginToken", mLoginToken);
        AndroidNetworking.post(Config.BASE_URL_API + "unit.php")
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
                        Log.d("RBA", "resad" + response);

                        String status = response.optString(Config.RESPONSE_STATUS_FIELD);
                        String message = response.optString(Config.RESPONSE_MESSAGE_FIELD);
                        if (status.trim().equalsIgnoreCase(Config.RESPONSE_STATUS_VALUE_SUCCESS)) {
                            JSONArray payload = response.optJSONArray(Config.RESPONSE_PAYLOAD_FIELD);

                            if (payload == null) {
                                Toast.makeText(SepedaActivity.this,"Tidak ada user",Toast.LENGTH_SHORT).show();                                return;
                            }

                            for (int i = 0; i < payload.length(); i++) {
                                JSONObject dataUser = payload.optJSONObject(i);
                                SepedaModel item = AppHelper.mapSepedaModel(dataUser);
                                mList.add(item);
                            }
                            show();
                        } else {
                            Toast.makeText(SepedaActivity.this, message, Toast.LENGTH_SHORT).show();
                            JSONObject payload = response.optJSONObject(Config.RESPONSE_PAYLOAD_FIELD);
                            if (payload != null && payload.optString("API_ACTION").equalsIgnoreCase("LOGOUT"))
                                Config.forceLogout(SepedaActivity.this);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(SepedaActivity.this, Config.TOAST_AN_ERROR, Toast.LENGTH_SHORT).show();
                        Log.d("RBA", "onError: " + anError.getErrorBody());
                        Log.d("RBA", "onError: " + anError.getLocalizedMessage());
                        Log.d("RBA", "onError: " + anError.getErrorDetail());
                        Log.d("RBA", "onError: " + anError.getResponse());
                        Log.d("RBA", "onError: " + anError.getErrorCode());
                    }
                });

    }

    private void logout() {
        new AlertDialog.Builder(SepedaActivity.this)
                .setTitle("Logout")
                .setMessage("Anda yakin akan logout ?")
                .setNegativeButton("Tidak", null)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    private void doNothing() {

                    }

                    public void onClick(DialogInterface arg0, int arg1) {
                        Config.forceLogout(SepedaActivity.this);
                    }
                }).create().show();
    }

    @Override
    public void onRefresh() {
        getSepedaList();
    }
}