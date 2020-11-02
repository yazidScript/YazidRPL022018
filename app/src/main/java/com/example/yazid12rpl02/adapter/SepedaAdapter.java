package com.example.yazid12rpl02.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yazid12rpl02.R;
import com.example.yazid12rpl02.activity.Admin.AdminSepedaActivity;
import com.example.yazid12rpl02.activity.Customer.SepedaActivity;
import com.example.yazid12rpl02.model.SepedaModel;

import java.util.List;

public class SepedaAdapter extends RecyclerView.Adapter<SepedaAdapter.SepedaViewHolder> {
    private Context context;
    private List<SepedaModel> mList;
    private String mLoginToken = "";
    private boolean mBusy = false;
    private ProgressDialog mProgressDialog;
    private SepedaActivity mAdminUserActivity;


    public SepedaAdapter(Context context, List<SepedaModel> mList, String loginToken, Activity SepedaActivity) {
        this.context = context;
        this.mList = mList;
        this.mLoginToken = loginToken;
        this.mAdminUserActivity = (SepedaActivity) SepedaActivity;

    }

    @NonNull
    @Override
    public SepedaAdapter.SepedaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_sepeda_layout, parent, false);
        return new SepedaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SepedaAdapter.SepedaViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void clearData() {
        int size = this.mList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.mList.remove(0);
            }
        }
    }

    public class SepedaViewHolder extends RecyclerView.ViewHolder {
        public SepedaViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
