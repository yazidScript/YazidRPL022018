package com.example.yazid12rpl02.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yazid12rpl02.R;
import com.example.yazid12rpl02.activity.Admin.AdminDashboardActivity;
import com.example.yazid12rpl02.activity.Admin.AdminUserActivity;
import com.example.yazid12rpl02.activity.Customer.CustomerActivity;
import com.example.yazid12rpl02.activity.Customer.DashboardActivity;
import com.example.yazid12rpl02.helper.Config;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);


        //hide keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            private void doNothing() {

            }

            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String userId = sp.getString(Config.LOGIN_ID_SHARED_PREF,"");
                String role = sp.getString(Config.LOGIN_GROUP_ID_SHARED_PREF,"");

                if (userId.equalsIgnoreCase("")) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    if(role.equalsIgnoreCase("GR_ADMIN")){
                        Intent intent1 = new Intent(getApplicationContext(), AdminDashboardActivity.class);
                        startActivity(intent1);
                        finish();
                    }
                    else {
                        Intent intent1 = new Intent(getApplicationContext(), DashboardActivity.class);
                        startActivity(intent1);
                        finish();
                    }

                }
            }
        }, 2000);

    }
}
