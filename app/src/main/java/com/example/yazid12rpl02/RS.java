package com.example.yazid12rpl02;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

//import io.realm.Realm;
//import io.realm.RealmConfiguration;

public class RS extends Application {

    private OkHttpClient okHttpClient = null;
    @Override
    public void onCreate() {
        super.onCreate();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if(BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(240, TimeUnit.SECONDS)
                .readTimeout(240, TimeUnit.SECONDS)
                .writeTimeout(240, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptor)
                .build();
        AndroidNetworking.initialize(this,okHttpClient);

//        Realm.init(getApplicationContext());
//        RealmConfiguration config = new RealmConfiguration.Builder()
//                .name("unit.realm")
//                .schemaVersion(2)
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        Realm.setDefaultConfiguration(config);
    }

    public OkHttpClient getOkHttpClient(){
        return okHttpClient;
    }
}
