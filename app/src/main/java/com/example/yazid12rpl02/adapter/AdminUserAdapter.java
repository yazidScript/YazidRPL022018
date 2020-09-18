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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.yazid12rpl02.R;
import com.example.yazid12rpl02.activity.Admin.AdminUserActivity;
import com.example.yazid12rpl02.helper.Config;
import com.example.yazid12rpl02.model.UserAdminModel;

import org.json.JSONObject;

import java.util.List;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.ItemViewHolder> {
    private Context context;
    private List<UserAdminModel> mList;
    private String mLoginToken = "";
    private boolean mBusy = false;
    private ProgressDialog mProgressDialog;
    private AdminUserActivity mAdminUserActivity;


    public AdminUserAdapter(Context context, List<UserAdminModel> mList, String loginToken, Activity AdminUserActivity) {
        this.context = context;
        this.mList = mList;
        this.mLoginToken = loginToken;
        this.mAdminUserActivity = (AdminUserActivity) AdminUserActivity;

    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_user_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminUserAdapter.ItemViewHolder itemViewHolder, int i) {
        final UserAdminModel Amodel = mList.get(i);
        itemViewHolder.bind(Amodel);
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
        private TextView tv_name, tv_phone;
        private ImageView divDelete;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            divDelete = itemView.findViewById(R.id.ivDelete);
            tv_name = itemView.findViewById(R.id.tvNama);
            tv_phone = itemView.findViewById(R.id.tvPhone);
        }

        private void bind(final UserAdminModel Amodel) {
            tv_name.setText(Amodel.getU_NAME());
            tv_phone.setText(Amodel.getU_PHONE());

            divDelete.setOnClickListener(new View.OnClickListener() {
                private void doNothing() {

                }

                @Override
                public void onClick(View view) {
                    if (mBusy) {
                        Toast.makeText(context, "Harap tunggu proses sebelumnya selesai...", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("Hapus data produk ?");
                    alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        private void doNothing() {

                        }

                        public void onClick(DialogInterface arg0, int arg1) {
                            deleteData(String.valueOf(Amodel.getU_ID()));
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
            if (mBusy) {
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
            if (!mProgressDialog.isShowing()) mProgressDialog.show();

            Log.d("YZD", "act:delete_user\n" +
                    "loginToken:" + mLoginToken + "\n" +
                    "uId:" + id);

            AndroidNetworking.post(Config.BASE_URL_API + "auth.php")
                    .addBodyParameter("act", "delete_user")
                    .addBodyParameter("loginToken", mLoginToken)
                    .addBodyParameter("uId", id)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject jsonResponse) {
                            mBusy = false;
                            if (mProgressDialog != null) mProgressDialog.dismiss();

                            String message = jsonResponse.optString(Config.RESPONSE_MESSAGE_FIELD);
                            String status = jsonResponse.optString(Config.RESPONSE_STATUS_FIELD);

                            if (status != null && status.equalsIgnoreCase(Config.RESPONSE_STATUS_VALUE_SUCCESS)) {
                                mAdminUserActivity.getUserList();
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            mBusy = false;
                            if (mProgressDialog != null) mProgressDialog.dismiss();

                            Toast.makeText(context, Config.TOAST_AN_ERROR, Toast.LENGTH_SHORT).show();
                            Log.d("RBA", "onError: " + anError.getErrorBody());
                            Log.d("RBA", "onError: " + anError.getLocalizedMessage());
                            Log.d("RBA", "onError: " + anError.getErrorDetail());
                            Log.d("RBA", "onError: " + anError.getResponse());
                            Log.d("RBA", "onError: " + anError.getErrorCode());
                        }
                    });

        }
    }
}