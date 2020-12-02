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
    private EditText etKodeSepeda, etWarnaSepeda, etMerkSepeda, etHargaSepeda, etJenisSepeda;
    private ImageView ivSepeda;
    private LinearLayout btnSave;
    private ProgressBar pbSave;

    private Bitmap mSelectedImage;
    private String mSelectedImagePath ;
    private File uploadFile;

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
        etKodeSepeda = findViewById(R.id.et_KodeSepeda);
        etMerkSepeda = findViewById(R.id.et_merkSepeda);
        etWarnaSepeda = findViewById(R.id.et_warnaSepeda);
        etJenisSepeda = findViewById(R.id.et_jenisSepeda);
        etHargaSepeda = findViewById(R.id.et_harga);
        ivSepeda = findViewById(R.id.iv_sepeda);
        ivSepeda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup()).show(AdminSepedaCreateActivity.this);
                new PickSetup().setCameraButtonText("Gallery");
            }
        });
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        pbSave = findViewById(R.id.pbSave);
        pbSave.setVisibility(View.GONE);

    }

    private void saveData() {

        if (etKodeSepeda.getText().toString().trim().equalsIgnoreCase("") || TextUtils.isEmpty(etKodeSepeda.getText().toString().trim())) {
            Toast.makeText(this, "Harap isikan kode sepeda", Toast.LENGTH_SHORT).show();
            return;
        }

        if (etMerkSepeda.getText().toString().trim().equalsIgnoreCase("") || TextUtils.isEmpty(etMerkSepeda.getText().toString().trim())) {
            Toast.makeText(this, "Harap isikan merk sepeda", Toast.LENGTH_SHORT).show();
            return;
        }

        if (etWarnaSepeda.getText().toString().trim().equalsIgnoreCase("") || TextUtils.isEmpty(etWarnaSepeda.getText().toString().trim())) {
            Toast.makeText(this, "Harap isikan warna sepeda", Toast.LENGTH_SHORT).show();
            return;
        }

        if (etJenisSepeda.getText().toString().trim().equalsIgnoreCase("") || TextUtils.isEmpty(etJenisSepeda.getText().toString().trim())) {
            Toast.makeText(this, "Harap isikan jenis sepeda", Toast.LENGTH_SHORT).show();
            return;
        }

        if (etHargaSepeda.getText().toString().trim().equalsIgnoreCase("") || TextUtils.isEmpty(etHargaSepeda.getText().toString().trim())) {
            Toast.makeText(this, "Harap isikan harga sepeda", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSave.setVisibility(View.GONE);
        pbSave.setVisibility(View.VISIBLE);

        HashMap<String, String> body = new HashMap<>();
        body.put("act", "create_unit");
        body.put("unitMerk", etMerkSepeda.getText().toString().trim());
        body.put("unitHargasewa", etHargaSepeda.getText().toString().trim());
        body.put("unitWarna", etWarnaSepeda.getText().toString().trim().toUpperCase());
        body.put("unitJenis", etJenisSepeda.getText().toString().trim().toUpperCase());
        body.put("unitKode", etKodeSepeda.getText().toString().trim().toUpperCase());
        body.put("loginToken", mLoginToken);

        AndroidNetworking.upload(Config.BASE_URL_API + "unit.php")
                .addMultipartFile("uploadFile",uploadFile)
                .addMultipartParameter(body)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(((RS) this.getApplication()).getOkHttpClient())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    private void doNothing() {

                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        pbSave.setVisibility(View.GONE);
                        btnSave.setVisibility(View.VISIBLE);

                        String message = response.optString(Config.RESPONSE_MESSAGE_FIELD);
                        String status = response.optString(Config.RESPONSE_STATUS_FIELD);

                        Toast.makeText(AdminSepedaCreateActivity.this, message, Toast.LENGTH_LONG).show();

                        if (status.equalsIgnoreCase(Config.RESPONSE_STATUS_VALUE_SUCCESS)) {
                            pbSave.setVisibility(View.GONE);
                            btnSave.setVisibility(View.GONE);
                            Intent i = new Intent(getApplicationContext(),AdminSepedaActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(AdminSepedaCreateActivity.this, "Proses daftar sepeda baru gagal\nCoba ulangi lagi", Toast.LENGTH_LONG).show();
                            pbSave.setVisibility(View.GONE);
                            btnSave.setVisibility(View.VISIBLE);
                            finish();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        btnSave.setVisibility(View.VISIBLE);
                        pbSave.setVisibility(View.GONE);
                        Toast.makeText(AdminSepedaCreateActivity.this, "Proses daftar sepeda baru gagal\nCoba ulangi lagi", Toast.LENGTH_LONG).show();
                        Log.d("RBA", "onError: " + anError.getErrorBody());
                        Log.d("RBA", "onError: " + anError.getLocalizedMessage());
                        Log.d("RBA", "onError: " + anError.getErrorDetail());
                        Log.d("RBA", "onError: " + anError.getResponse());
                        Log.d("RBA", "onError: " + anError.getErrorCode());
                        finish();
                    }
                });

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
                ivSepeda.setImageBitmap(mSelectedImage);
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
