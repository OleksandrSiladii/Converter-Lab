package com.example.myapplication3.app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Currency implements Parcelable {

    private String ask;
    private String bid;
    private String nameCurrency;
    private String id;
    private String previousAck = "0";
    private String previousBid = "100";

    public String getPreviousAck() {
        return previousAck;
    }

    public void setPreviousAck(String previousAck) {
        this.previousAck = previousAck;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPreviousBid() {
        return previousBid;
    }

    public void setPreviousBid(String previousBid) {
        this.previousBid = previousBid;
    }

    public String getNameCurrency() {
        return nameCurrency;
    }

    public void setNameCurrency(String name) {
        this.nameCurrency = name;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public class CurrencySmall {
        private String ask;
        private String bid;

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getAsk() {
            return ask;
        }

        public void setAsk(String ask) {
            this.ask = ask;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ask);
        dest.writeString(this.bid);
        dest.writeString(this.nameCurrency);
        dest.writeString(this.id);
        dest.writeString(this.previousAck);
        dest.writeString(this.previousBid);
    }

    public Currency() {
    }

    protected Currency(Parcel in) {
        this.ask = in.readString();
        this.bid = in.readString();
        this.nameCurrency = in.readString();
        this.id = in.readString();
        this.previousAck = in.readString();
        this.previousBid = in.readString();
    }

    public static final Parcelable.Creator<Currency> CREATOR = new Parcelable.Creator<Currency>() {
        public Currency createFromParcel(Parcel source) {
            return new Currency(source);
        }

        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };
}
