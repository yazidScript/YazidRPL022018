package com.example.yazid12rpl02.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.yazid12rpl02.R;
import com.example.yazid12rpl02.activity.Admin.AdminSepedaActivity;
import com.example.yazid12rpl02.helper.AppHelper;
import com.example.yazid12rpl02.helper.Config;
import com.example.yazid12rpl02.model.SepedaModel;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

public class AdminSepedaAdapter extends RecyclerView.Adapter<AdminSepedaAdapter.ItemViewHolder> {
    private Context context;
    private List<SepedaModel> mList;
    private String mLoginToken = "";
    private boolean mBusy = false;
    private ProgressDialog mProgressDialog;
    private AdminSepedaActivity mAdminUserActivity;


    public AdminSepedaAdapter(Context context, List<SepedaModel> mList, String loginToken, Activity AdminUserActivity) {
        this.context = context;
        this.mList = mList;
        this.mLoginToken = loginToken;
        this.mAdminUserActivity = (AdminSepedaActivity) AdminUserActivity;

    }

    @NonNull
    @Override
    public AdminSepedaAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_admin_sepeda_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminSepedaAdapter.ItemViewHolder holder, int position) {
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
        private LinearLayout divDetail;
        private TextView tvKodesepeda, tvMerkSepeda;
        private ImageView divDelete;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKodesepeda = itemView.findViewById(R.id.tvKodesepeda);
            tvMerkSepeda = itemView.findViewById(R.id.tvMerksepeda);
            divDetail = itemView.findViewById(R.id.item_sepeda);
            divDelete = itemView.findViewById(R.id.ivDelete);
        }
        private void bind(final SepedaModel Amodel) {
            tvKodesepeda.setText(Amodel.getUNIT_KODE());
            tvMerkSepeda.setText(Amodel.getUNIT_MERK());
            divDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppHelper.goToSepedaAdminDetail(context, Amodel);
                }
            });

            divDelete.setOnClickListener(new View.OnClickListener() {
                private void doNothing() {

                }

                @Override
                public void onClick(View view) {
                    if(mBusy) {
                        Toast.makeText(context, "Harap tunggu proses sebelumnya selesai...", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("Hapus data image ?");
                    alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        private void doNothing() {

                        }

                        public void onClick(DialogInterface arg0, int arg1) {
                            deleteData(String.valueOf(Amodel.getUNIT_ID()));
                        }
                    });
                    alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        private void doNothing() {

                        }

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                        }
                    });

                    //Showing the alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
        }

        private void deleteData(String id) {
            if(mBusy) {
                Toast.makeText(context, "Harap tunggu proses sebelumnya selesai...", Toast.LENGTH_SHORT).show();
                return;
            }

            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setMessage("Proses ...");
            mProgressDialog.setCancelable(true);
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Batal", new DialogInterface.OnClickListener() {
                private void doNothing() {

                }

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            if(!mProgressDialog.isShowing())    mProgressDialog.show();

            Log.d("HBB", "act:delete_unit\n" +
                    "loginToken:" + mLoginToken + "\n" +
                    "imgId:" + id);

            AndroidNetworking.post(Config.BASE_URL_API + "unit.php")
                    .addBodyParameter("act", "delete_unit")
                    .addBodyParameter("loginToken", mLoginToken)
                    .addBodyParameter("unitId", id)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject jsonResponse) {
                            mBusy = false;
                            if(mProgressDialog != null) mProgressDialog.dismiss();

                            String message = jsonResponse.optString(Config.RESPONSE_MESSAGE_FIELD);
                            String status = jsonResponse.optString(Config.RESPONSE_STATUS_FIELD);

                            if(status != null && status.equalsIgnoreCase(Config.RESPONSE_STATUS_VALUE_SUCCESS)) {
                                mAdminUserActivity.getSepedaList();
                            }
                            else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            mBusy = false;
                            if(mProgressDialog != null) mProgressDialog.dismiss();

                            Toast.makeText(context, Config.TOAST_AN_ERROR, Toast.LENGTH_SHORT).show();
                            Log.d("HBB", "onError: " + anError.getErrorBody());
                            Log.d("HBB", "onError: " + anError.getLocalizedMessage());
                            Log.d("HBB", "onError: " + anError.getErrorDetail());
                            Log.d("HBB", "onError: " + anError.getResponse());
                            Log.d("HBB", "onError: " + anError.getErrorCode());
                        }
                    });
        }
    }
}
