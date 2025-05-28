package com.example.rabu21mei;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Mahasiswa implements Parcelable {
    private String nim, nama, path;
    private int umur;

    public Mahasiswa(String nim, String nama, String path, int umur) {
        this.nim = nim;
        this.nama = nama;
        this.path = path;
        this.umur = umur;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getUmur() {
        return umur;
    }

    public void setUmur(int umur) {
        this.umur = umur;
    }

    protected Mahasiswa(Parcel in) {
        nim = in.readString();
        nama = in.readString();
        path = in.readString();
        umur = in.readInt();
    }

    public static final Creator<Mahasiswa> CREATOR = new Creator<Mahasiswa>() {
        @Override
        public Mahasiswa createFromParcel(Parcel in) {
            return new Mahasiswa(in);
        }

        @Override
        public Mahasiswa[] newArray(int size) {
            return new Mahasiswa[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(nim);
        dest.writeString(nama);
        dest.writeString(path);
        dest.writeInt(umur);
    }
}
