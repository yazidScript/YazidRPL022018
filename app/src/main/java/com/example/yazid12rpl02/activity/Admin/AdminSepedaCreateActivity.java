package com.example.yazid12rpl02.activity.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bumptech.glide.Glide;
import com.example.yazid12rpl02.R;
import com.example.yazid12rpl02.RS;
import com.example.yazid12rpl02.helper.Config;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class AdminSepedaCreateActivity extends AppCompatActivity implements IPickResult {
    private EditText et_kodeSepeda, et_warnaSepeda, et_merkSepeda, et_HargaSepeda;
    private ImageView iv_sepeda;
    private LinearLayout btn_save;
    private ProgressBar PbSave;

    Button btPick, btSend;
    private Bitmap mSelectedImage;
    private String mSelectedImagePath ;
    private File uploadFile;
    TextView tvLinkFoto;
    ImageView ivFotoDone;

    private boolean mBusy = false;
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
            Config.forceLogout(this);
        }
        setContentView(R.layout.activity_admin_sepeda_create);
        binding();
    }

    private void binding(){
        et_kodeSepeda = findViewById(R.id.et_KodeSepeda);
        et_merkSepeda = findViewById(R.id.et_merkSepeda);
        et_warnaSepeda = findViewById(R.id.et_warnaSepeda);
        et_HargaSepeda = findViewById(R.id.et_harga);
        iv_sepeda = findViewById(R.id.iv_sepeda);
        iv_sepeda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup()).show(getSupportFragmentManager());
            }
        });
        btn_save = findViewById(R.id.btnSave);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        PbSave = findViewById(R.id.pbSave);
        PbSave.setVisibility(View.GONE);

    }

    private void saveData() {

        if (et_kodeSepeda.getText().toString().trim().equalsIgnoreCase("") || TextUtils.isEmpty(et_kodeSepeda.getText().toString().trim())) {
            Toast.makeText(this, "Harap isikan kode sepeda", Toast.LENGTH_SHORT).show();
            return;
        }

        if (et_merkSepeda.getText().toString().trim().equalsIgnoreCase("") || TextUtils.isEmpty(et_merkSepeda.getText().toString().trim())) {
            Toast.makeText(this, "Harap isikan merk sepeda", Toast.LENGTH_SHORT).show();
            return;
        }

        if (et_warnaSepeda.getText().toString().trim().equalsIgnoreCase("") || TextUtils.isEmpty(et_warnaSepeda.getText().toString().trim())) {
            Toast.makeText(this, "Harap isikan warna sepeda", Toast.LENGTH_SHORT).show();
            return;
        }

        if (et_HargaSepeda.getText().toString().trim().equalsIgnoreCase("") || TextUtils.isEmpty(et_HargaSepeda.getText().toString().trim())) {
            Toast.makeText(this, "Harap isikan harga sepeda", Toast.LENGTH_SHORT).show();
            return;
        }

        btn_save.setVisibility(View.GONE);
        PbSave.setVisibility(View.VISIBLE);

        HashMap<String, String> body = new HashMap<>();
        body.put("act", "create_unit");
        body.put("unitMerk", et_merkSepeda.getText().toString().trim());
        body.put("unitHargasewa", et_HargaSepeda.getText().toString().trim());
        body.put("unitWarna", et_warnaSepeda.getText().toString().trim().toUpperCase());
        body.put("unitKode", et_kodeSepeda.getText().toString().trim().toUpperCase());
        body.put("loginToken", mLoginToken);

        HashMap<String, File> file = new HashMap<>();
        if (mSelectedImagePath != null){
            file.put("uploadFile", uploadFile);
        }

//        AndroidNetworking.upload(Config.BASE_URL_API + "unit.php")
//                .addMultipartFile(file)
//                .addMultipartParameter(body)
//                .setPriority(Priority.HIGH)
//                .setOkHttpClient(((RS) this.getApplication()).getOkHttpClient())
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    private void doNothing() {
//
//                    }
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        PbSave.setVisibility(View.GONE);
//                        btn_save.setVisibility(View.VISIBLE);
//
//                        String message = response.optString(Config.RESPONSE_MESSAGE_FIELD);
//                        String status = response.optString(Config.RESPONSE_STATUS_FIELD);
//
//                        Toast.makeText(AdminSepedaCreateActivity.this, message, Toast.LENGTH_LONG).show();
//
//                        if (status.equalsIgnoreCase(Config.RESPONSE_STATUS_VALUE_SUCCESS)) {
//                            PbSave.setVisibility(View.GONE);
//                            btn_save.setVisibility(View.GONE);
//                            SharedPreferences done = getSharedPreferences(Config.SHARED_PREF_NAME_BIAYA_SINKRON, Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editorBiayaDone = done.edit();
//                            editorBiayaDone.putBoolean(Config.SHARED_PREF_NAME_BIAYA_SINKRON, false);
//                            editorBiayaDone.putString(Config.BS_KODE, "");
//                            editorBiayaDone.putString(Config.BS_ORDER_ID_SPINNER, "");
//                            editorBiayaDone.putString(Config.BS_NOMINAL, "");
//                            editorBiayaDone.putString(Config.BS_INFO, "");
//                            editorBiayaDone.putString(Config.BS_IMG_PATH_BIAYA, "");
//                            editorBiayaDone.commit();
//                            finish();
//                        } else {
//                            Toast.makeText(AdminSepedaCreateActivity.this, "Proses daftar biaya baru gagal\nData perlu disinkronisasi", Toast.LENGTH_LONG).show();
//                            SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME_BIAYA_SINKRON, Context.MODE_PRIVATE);
//                            preferences.edit()
//                                    .putString(Config.BS_KODE, oeCode)
//                                    .putString(Config.BS_ORDER_ID_SPINNER, mSelectedOrderId)
//                                    .putString(Config.BS_NOMINAL, oeAmount)
//                                    .putString(Config.BS_INFO, oeInfo)
//                                    .putString(Config.BS_IMG_PATH_BIAYA, mSelectedImagePathBiaya)
//                                    .apply();
//
//                            PbSave.setVisibility(View.GONE);
//                            btn_save.setVisibility(View.VISIBLE);
//
//                            finish();
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        btn_save.setVisibility(View.VISIBLE);
//                        PbSave.setVisibility(View.GONE);
//                        Toast.makeText(AdminSepedaCreateActivity.this, "Proses daftar biaya baru gagal\nData perl di sinkronisasi", Toast.LENGTH_LONG).show();
//                        Log.d("RBA", "onError: " + anError.getErrorBody());
//                        Log.d("RBA", "onError: " + anError.getLocalizedMessage());
//                        Log.d("RBA", "onError: " + anError.getErrorDetail());
//                        Log.d("RBA", "onError: " + anError.getResponse());
//                        Log.d("RBA", "onError: " + anError.getErrorCode());
//                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME_BIAYA_SINKRON, Context.MODE_PRIVATE);
//                        preferences.edit()
//                                .putString(Config.BS_KODE, oeCode)
//                                .putString(Config.BS_ORDER_ID_SPINNER, mSelectedOrderId)
//                                .putString(Config.BS_NOMINAL, oeAmount)
//                                .putString(Config.BS_INFO, oeInfo)
//                                .putString(Config.BS_IMG_PATH_BIAYA , mSelectedImagePathBiaya)
//                                .apply();
//                        finish();
//                    }
//                });

    }


    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            try {
                File fileku = new Compressor(this)
                        .setQuality(50)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .compressToFile(new File(r.getPath()));

                mSelectedImagePath = fileku.getAbsolutePath();
                uploadFile = new File(mSelectedImagePath);

                Log.d("RBA", "onPickResult: "+ mSelectedImagePath);

                mSelectedImage = r.getBitmap();
                iv_sepeda.setImageBitmap(mSelectedImage);
                //selectedImageFile = new File(r.getPath());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
