package com.example.yazid12rpl02.activity.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.yazid12rpl02.R;
import com.example.yazid12rpl02.helper.Config;

public class AdminDashboardActivity extends AppCompatActivity {

    private CardView divSepeda, divUser;
    private ImageView ivLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        ivLogout = findViewById(R.id.iv_logout);
        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        divUser = findViewById(R.id.item_user);
        divUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminDashboardActivity.this,AdminUserActivity.class);
                startActivity(i);
            }
        });
        divSepeda = findViewById(R.id.item_sepeda);
        divSepeda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminDashboardActivity.this,AdminSepedaActivity.class);
                startActivity(i);
            }
        });
    }
    private void logout() {
        new AlertDialog.Builder(AdminDashboardActivity.this)
                .setTitle("Logout")
                .setMessage("Anda yakin akan logout ?")
                .setNegativeButton("Tidak", null)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    private void doNothing() {

                    }

                    public void onClick(DialogInterface arg0, int arg1) {
                        Config.forceLogout(AdminDashboardActivity.this);
                    }
                }).create().show();
    }
}