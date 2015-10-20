package com.example.myapplication3.app.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.JsonElement;

import java.util.List;

/**
 * Created by omar on 9/22/15.
 */
public class GlobalModel implements Parcelable {
    private List<Organization> organizations;
    private JsonElement orgTypes;
    private JsonElement currencies;
    private JsonElement regions;
    private JsonElement cities;
    private String sourceId;
    private String date;

    private List<PairedObject> orgTypesReal;
    private List<PairedObject> currenciesReal;
    private List<PairedObject> regionsReal;
    private List<PairedObject> citiesReal;


    public GlobalModel() {
    }

    public GlobalModel(String sourceId, String date, List<Organization> organizations, List<PairedObject> orgTypesReal, List<PairedObject> currenciesReal, List<PairedObject> regionsReal, List<PairedObject> citiesReal) {
        this.sourceId = sourceId;
        this.date = date;
        this.organizations = organizations;
        this.orgTypesReal = orgTypesReal;
        this.currenciesReal = currenciesReal;
        this.regionsReal = regionsReal;
        this.citiesReal = citiesReal;
    }


    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }


    public void setOrgTypes(JsonElement orgTypes) {
        this.orgTypes = orgTypes;
    }

    public void setCurrencies(JsonElement currencies) {
        this.currencies = currencies;
    }

    public void setRegions(JsonElement regionsJSon) {
        this.regions = regionsJSon;
    }

    public void setCities(JsonElement citiesJSon) {
        this.cities = citiesJSon;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public List<PairedObject> getOrgTypes() {
        return orgTypesReal;
    }

    public void setOrgTypesReal(List<PairedObject> orgTypesReal) {
        this.orgTypesReal = orgTypesReal;
    }

    public List<PairedObject> getCurrenciesReal() {
        return currenciesReal;
    }

    public void setCurrenciesReal(List<PairedObject> currenciesReal) {
        this.currenciesReal = currenciesReal;
    }

    public List<PairedObject> getRegionsReal() {
        return regionsReal;
    }

    public void setRegionsReal(List<PairedObject> regionsReal) {
        this.regionsReal = regionsReal;
    }

    public List<PairedObject> getCitiesReal() {
        return citiesReal;
    }

    public void setCitiesReal(List<PairedObject> citiesReal) {
        this.citiesReal = citiesReal;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public void deserialize() {
        this.orgTypesReal = CustomDeserializer.getPairedObjectList(orgTypes);
        this.currenciesReal = CustomDeserializer.getPairedObjectList(currencies);
        this.regionsReal = CustomDeserializer.getPairedObjectList(regions);
        this.citiesReal = CustomDeserializer.getPairedObjectList(cities);

        for (Organization item : organizations)
            item.deserialize();
    }

    public interface DeserializeCallback {
        void onDeserialized(GlobalModel model);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.sourceId);
        dest.writeString(this.date);
        dest.writeTypedList(organizations);
        dest.writeTypedList(orgTypesReal);
        dest.writeTypedList(currenciesReal);
        dest.writeTypedList(regionsReal);
        dest.writeTypedList(citiesReal);
    }

    protected GlobalModel(Parcel in) {

        this.sourceId = in.readString();
        this.date = in.readString();
        this.organizations = in.createTypedArrayList(Organization.CREATOR);
        this.orgTypesReal = in.createTypedArrayList(PairedObject.CREATOR);
        this.currenciesReal = in.createTypedArrayList(PairedObject.CREATOR);
        this.regionsReal = in.createTypedArrayList(PairedObject.CREATOR);
        this.citiesReal = in.createTypedArrayList(PairedObject.CREATOR);
    }

    public static final Parcelable.Creator<GlobalModel> CREATOR = new Parcelable.Creator<GlobalModel>() {
        public GlobalModel createFromParcel(Parcel source) {
            return new GlobalModel(source);
        }

        public GlobalModel[] newArray(int size) {
            return new GlobalModel[size];
        }
    };
}
