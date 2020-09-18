package com.example.yazid12rpl02.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserAdminModel implements Parcelable {
    private String U_ID;
    private String U_NAME;
    private String U_EMAIL;
    private String U_GROUP_ROLE;
    private String U_PHONE;
    private String U_AUTHORITY_ID_1;
    private String U_ADDRESS;

    public UserAdminModel(Parcel in) {
        U_ID = in.readString();
        U_NAME = in.readString();
        U_EMAIL = in.readString();
        U_GROUP_ROLE = in.readString();
        U_PHONE = in.readString();
        U_AUTHORITY_ID_1 = in.readString();
        U_ADDRESS = in.readString();
    }

    public UserAdminModel() {}


    public static Creator<UserAdminModel> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<UserAdminModel> CREATOR = new Creator<UserAdminModel>() {
        @Override
        public UserAdminModel createFromParcel(Parcel in) {
            return new UserAdminModel(in);
        }

        @Override
        public UserAdminModel[] newArray(int size) {
            return new UserAdminModel[size];
        }
    };

    public String getU_ID() {
        return U_ID;
    }

    public void setU_ID(String u_ID) {
        U_ID = u_ID;
    }

    public String getU_NAME() {
        return U_NAME;
    }

    public void setU_NAME(String u_NAME) {
        U_NAME = u_NAME;
    }

    public String getU_EMAIL() {
        return U_EMAIL;
    }

    public void setU_EMAIL(String u_EMAIL) {
        U_EMAIL = u_EMAIL;
    }

    public String getU_GROUP_ROLE() {
        return U_GROUP_ROLE;
    }

    public void setU_GROUP_ROLE(String u_GROUP_ROLE) {
        U_GROUP_ROLE = u_GROUP_ROLE;
    }

    public String getU_PHONE() {
        return U_PHONE;
    }

    public void setU_PHONE(String u_PHONE) {
        U_PHONE = u_PHONE;
    }

    public String getU_AUTHORITY_ID_1() {
        return U_AUTHORITY_ID_1;
    }

    public void setU_AUTHORITY_ID_1(String u_AUTHORITY_ID_1) {
        U_AUTHORITY_ID_1 = u_AUTHORITY_ID_1;
    }

    public String getU_ADDRESS() {
        return U_ADDRESS;
    }

    public void setU_ADDRESS(String u_ADDRESS) {
        U_ADDRESS = u_ADDRESS;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(U_ID);
        dest.writeString(U_NAME);
        dest.writeString(U_EMAIL);
        dest.writeString(U_GROUP_ROLE);
        dest.writeString(U_PHONE);
        dest.writeString(U_AUTHORITY_ID_1);
        dest.writeString(U_ADDRESS);
    }
}
