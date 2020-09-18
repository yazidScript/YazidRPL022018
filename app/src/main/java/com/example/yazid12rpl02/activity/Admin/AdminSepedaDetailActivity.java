package com.example.yazid12rpl02.activity.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yazid12rpl02.R;
import com.example.yazid12rpl02.helper.Config;
import com.example.yazid12rpl02.model.SepedaModel;

import java.io.File;

public class AdminSepedaDetailActivity extends AppCompatActivity {
    private ImageView ivSepeda;
    private ProgressDialog mProgress;

    private String mLoginToken = "";
    private String mUserId = "";

    private Bitmap mSelectedImage;
    private String mSelectedImagePath = "";
    private File mFileImage;

    private LinearLayout divSave,divDelete;

    private SepedaModel mBiayaData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        mLoginToken = sp.getString(Config.LOGIN_TOKEN_SHARED_PREF,"");
        mUserId = sp.getString(Config.LOGIN_ID_SHARED_PREF, "");

        if(mLoginToken.equalsIgnoreCase("") || mUserId.equalsIgnoreCase("")) {
            finish();
            Config.forceLogout(AdminSepedaDetailActivity.this);
        }

        setContentView(R.layout.activity_admin_sepeda_detail);
    }
}