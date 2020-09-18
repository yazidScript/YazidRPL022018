package com.example.yazid12rpl02.activity.Customer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.yazid12rpl02.R;
import com.example.yazid12rpl02.helper.Config;
import com.example.yazid12rpl02.iface.ProfileActionListener;
import com.example.yazid12rpl02.model.UserAdminModel;

import org.json.JSONObject;

import java.util.HashMap;

public class ProfilActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private Context mContext;

    private String mLoginToken = "";
    private String mUserId = "";
    private UserAdminModel mUseradminModel;

    private ProfileActionListener mProfileActionListener;

    private LinearLayout divUpdate, divLogout, divClose;
    private EditText etName, etPhone;
    private TextView tvUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        binding(mUseradminModel);

    }
    private void binding(final UserAdminModel model){

        etName = findViewById(R.id.etName);
        etName.setText(model.getU_NAME());

        etPhone = findViewById(R.id.etPhone);
        etPhone.setText(model.getU_PHONE());

        tvUserID = findViewById(R.id.tvUserID);
        tvUserID.setText(model.getU_ID());


        divUpdate = findViewById(R.id.divUpdate);
        divUpdate.setOnClickListener(new View.OnClickListener() {
            private void doNothing() {

            }

            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        divLogout = findViewById(R.id.divLogout);
        divLogout.setOnClickListener(new View.OnClickListener() {
            private void doNothing() {

            }

            @Override
            public void onClick(View v) {
                logout();
            }
        });
        divClose = findViewById(R.id.divClose);
        divClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mProgressDialog = new ProgressDialog(this.mContext);
        mProgressDialog.setTitle("Proses");
        mProgressDialog.setMessage("Mohon tunggu...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
    }
    private void updateProfile() {
        if(etName.getText().toString().isEmpty()) {
            Toast.makeText(ProfilActivity.this,"Harap isikan nama",Toast.LENGTH_SHORT);
            return;
        }


        Log.d("HBB", Config.BASE_URL_API + "auth.php");
        Log.d("HBB", "act:update_profil\n" +
                "loginToken:" + mLoginToken + "\n" +
                "uId:" + mUserId + "\n" +
                "uName:" + etName.getText().toString().trim().toUpperCase());

        mProgressDialog.show();

        HashMap<String, String> body = new HashMap<>();
        body.put("act", "update_profil");
        body.put("loginToken", mLoginToken);
        body.put("uId", mUserId);
        body.put("uName", etName.getText().toString().trim());

        AndroidNetworking.post(Config.BASE_URL_API + "auth.php")
                .addBodyParameter(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RBA", "" + response );
                        mProgressDialog.dismiss();

                        String status = response.optString(Config.RESPONSE_STATUS_FIELD);
                        String message = response.optString(Config.RESPONSE_MESSAGE_FIELD);

                        Toast.makeText(ProfilActivity.this,message,Toast.LENGTH_SHORT);

                        if (status.trim().equalsIgnoreCase(Config.RESPONSE_STATUS_VALUE_SUCCESS)) {
                            Toast.makeText(ProfilActivity.this,"Profil berhasil di update",Toast.LENGTH_SHORT);
                            mProfileActionListener.onProfileUpdated(etName.getText().toString().trim().toUpperCase());
                        }
                        else{
                            mProfileActionListener.onProfileUpdateFailed("");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        mProgressDialog.dismiss();
                        mProfileActionListener.onProfileUpdateFailed("");
                        Toast.makeText(mContext, Config.TOAST_AN_ERROR, Toast.LENGTH_SHORT).show();
                        Log.d("RBA", "onError: " + anError.getErrorBody());
                        Log.d("RBA", "onError: " + anError.getLocalizedMessage());
                        Log.d("RBA", "onError: " + anError.getErrorDetail());
                        Log.d("RBA", "onError: " + anError.getResponse());
                        Log.d("RBA", "onError: " + anError.getErrorCode());
                    }
                });
    }

    private void logout() {
        new AlertDialog.Builder(mContext)
                .setTitle("Logout")
                .setMessage("Anda yakin akan logout ?")
                .setNegativeButton("Tidak", null)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    private void doNothing() {

                    }

                    public void onClick(DialogInterface arg0, int arg1) {
                        Config.forceLogout(mContext);
                        mProfileActionListener.onLogout();
                    }
                }).create().show();
    }
}
