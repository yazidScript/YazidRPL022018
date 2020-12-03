package com.example.yazid12rpl02.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yazid12rpl02.R;
import com.example.yazid12rpl02.activity.Admin.AdminSepedaActivity;
import com.example.yazid12rpl02.activity.Customer.SepedaActivity;
import com.example.yazid12rpl02.helper.AppHelper;
import com.example.yazid12rpl02.helper.Config;
import com.example.yazid12rpl02.model.SepedaModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SepedaAdapter extends RecyclerView.Adapter<SepedaAdapter.ItemViewHolder> {
    private Context context;
    private List<SepedaModel> mList;
    private String mLoginToken = "";
    private SepedaActivity mAdminUserActivity;


    public SepedaAdapter(Context context, List<SepedaModel> mList, String loginToken, Activity AdminUserActivity) {
        this.context = context;
        this.mList = mList;
        this.mLoginToken = loginToken;
        this.mAdminUserActivity = (SepedaActivity) AdminUserActivity;

    }


    @NonNull
    @Override
    public SepedaAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_sepeda_layout, parent, false);
        return new SepedaAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SepedaAdapter.ItemViewHolder holder, int position) {
        final SepedaModel Amodel = mList.get(position);
        holder.bind(Amodel);
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


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private CardView divDetail;
        private ImageView ivSepeda;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSepeda = itemView.findViewById(R.id.ivSepeda);
        }
        private void bind(final SepedaModel Amodel) {
            if(Amodel.getUNIT_GAMBAR().contains(Config.UPLOAD_FOLDER)) {
                Picasso.with(context)
                        .load(Config.BASE_URL + Amodel.getUNIT_GAMBAR())
                        .into(ivSepeda);
            }
            else {
                Picasso.with(context)
                        .load(Config.BASE_URL_UPLOADS + Amodel.getUNIT_GAMBAR())
                        .into(ivSepeda);
            }
            divDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppHelper.goToSepedaDetail(context, Amodel);
                }
            });
        }
    }
}
