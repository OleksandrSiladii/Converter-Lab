package com.example.myapplication3.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class Organization implements Parcelable {
    private String id;
    private int oldId;
    private int orgType;
    private String title;
    private String regionId;
    private String cityId;
    private String phone;
    private String address;
    private String link;
    private List<Currency> currenciesReal;

    private JsonElement currencies;

    public void setCurrencies(JsonElement currencies) {
        this.currencies = currencies;
    }

    public void deserialize() {
        currenciesReal = new ArrayList<Currency>();
        List<PairedObject> list = CustomDeserializer.getPairedObjectList(currencies);
        for (PairedObject item : list) {
            Currency.CurrencySmall small = new Gson().fromJson(item.getName(), Currency.CurrencySmall.class);
            Currency currency = new Currency();
            currency.setNameCurrency(item.getId());
            currency.setAsk(small.getAsk());
            currency.setBid(small.getBid());
            currenciesReal.add(currency);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOldId() {
        return oldId;
    }

    public void setOldId(int oldId) {
        this.oldId = oldId;
    }

    public int getOrgType() {
        return orgType;
    }

    public void setOrgType(int orgType) {
        this.orgType = orgType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<Currency> getCurrenciesReal() {
        return currenciesReal;
    }

    public void setCurrenciesReal(List<Currency> currencies) {
        currenciesReal = currencies;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.oldId);
        dest.writeInt(this.orgType);
        dest.writeString(this.title);
        dest.writeString(this.regionId);
        dest.writeString(this.cityId);
        dest.writeString(this.phone);
        dest.writeString(this.address);
        dest.writeString(this.link);
        dest.writeTypedList(currenciesReal);
    }

    public Organization() {
    }

    protected Organization(Parcel in) {
        this.id = in.readString();
        this.oldId = in.readInt();
        this.orgType = in.readInt();
        this.title = in.readString();
        this.regionId = in.readString();
        this.cityId = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.link = in.readString();
        this.currenciesReal = in.createTypedArrayList(Currency.CREATOR);
    }

    public static final Parcelable.Creator<Organization> CREATOR = new Parcelable.Creator<Organization>() {
        public Organization createFromParcel(Parcel source) {
            return new Organization(source);
        }

        public Organization[] newArray(int size) {
            return new Organization[size];
        }
    };
}
