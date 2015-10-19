package com.example.myapplication3.app.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by omar on 9/22/15.
 */
public class PairedObject implements Parcelable{
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
    }

    public PairedObject() {
    }

    protected PairedObject(Parcel in) {
        this.name = in.readString();
        this.id = in.readString();
    }

    public static final Creator<PairedObject> CREATOR = new Creator<PairedObject>() {
        public PairedObject createFromParcel(Parcel source) {
            return new PairedObject(source);
        }

        public PairedObject[] newArray(int size) {
            return new PairedObject[size];
        }
    };
}
