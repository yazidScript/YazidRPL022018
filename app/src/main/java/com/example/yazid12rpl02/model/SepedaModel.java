package com.example.yazid12rpl02.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SepedaModel implements Parcelable {
    private String UNIT_ID;
    private String UNIT_KODE;
    private String UNIT_JENIS;
    private String UNIT_MERK;
    private String UNIT_HARGABARANG;
    private String UNIT_GAMBAR;

    public SepedaModel(Parcel in) {
        UNIT_ID = in.readString();
        UNIT_KODE = in.readString();
        UNIT_JENIS = in.readString();
        UNIT_MERK = in.readString();
        UNIT_HARGABARANG = in.readString();
        UNIT_GAMBAR = in.readString();
    }

    public static final Creator<SepedaModel> CREATOR = new Creator<SepedaModel>() {
        @Override
        public SepedaModel createFromParcel(Parcel in) {
            return new SepedaModel(in);
        }

        @Override
        public SepedaModel[] newArray(int size) {
            return new SepedaModel[size];
        }
    };

    public SepedaModel() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getUNIT_ID() {
        return UNIT_ID;
    }

    public void setUNIT_ID(String UNIT_ID) {
        this.UNIT_ID = UNIT_ID;
    }

    public String getUNIT_KODE() {
        return UNIT_KODE;
    }

    public void setUNIT_KODE(String UNIT_KODE) {
        this.UNIT_KODE = UNIT_KODE;
    }

    public String getUNIT_JENIS() {
        return UNIT_JENIS;
    }

    public void setUNIT_JENIS(String UNIT_JENIS) {
        this.UNIT_JENIS = UNIT_JENIS;
    }

    public String getUNIT_MERK() {
        return UNIT_MERK;
    }

    public void setUNIT_MERK(String UNIT_MERK) {
        this.UNIT_MERK = UNIT_MERK;
    }

    public String getUNIT_HARGABARANG() {
        return UNIT_HARGABARANG;
    }

    public void setUNIT_HARGABARANG(String UNIT_HARGABARANG) {
        this.UNIT_HARGABARANG = UNIT_HARGABARANG;
    }

    public String getUNIT_GAMBAR() {
        return UNIT_GAMBAR;
    }

    public void setUNIT_GAMBAR(String UNIT_GAMBAR) {
        this.UNIT_GAMBAR = UNIT_GAMBAR;
    }

    public static Creator<SepedaModel> getCREATOR() {
        return CREATOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(UNIT_ID);
        dest.writeString(UNIT_KODE);
        dest.writeString(UNIT_JENIS);
        dest.writeString(UNIT_MERK);
        dest.writeString(UNIT_HARGABARANG);
        dest.writeString(UNIT_GAMBAR);
    }
}
