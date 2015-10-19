package com.example.myapplication3.app.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.JsonElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 9/22/15.
 */
public class GlobalModel  implements Parcelable {
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

    public GlobalModel(String sourceId, String date, List<Organization> organizations,
                       List<PairedObject> orgTypesReal, List<PairedObject> currenciesReal,
                       List<PairedObject> regionsReal, List<PairedObject> citiesReal) {
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



    public static void deserializeAsync(final GlobalModel _model, final DeserializeCallback _callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                    GlobalModel mModel = _model;
                    mModel.orgTypesReal = CustomDeserializer.getPairedObjectList(mModel.orgTypes);
                    mModel.currenciesReal = CustomDeserializer.getPairedObjectList(mModel.currencies);
                    mModel.regionsReal = CustomDeserializer.getPairedObjectList(mModel.regions);
                    mModel.citiesReal = CustomDeserializer.getPairedObjectList(mModel.cities);

                    for (Organization item : mModel.getOrganizations())
                        item.deserialize();
                    _callback.onDeserialized( mModel);
            }
        }).start();

    }

    public interface DeserializeCallback {
        void onDeserialized( GlobalModel model);
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.organizations);

        dest.writeString(this.sourceId);
        dest.writeString(this.date);
        dest.writeList(this.orgTypesReal);
        dest.writeList(this.currenciesReal);
        dest.writeList(this.regionsReal);
        dest.writeList(this.citiesReal);
    }

    protected GlobalModel(Parcel in) {
        this.organizations = new ArrayList<Organization>();
        in.readList(this.organizations, List.class.getClassLoader());


        this.sourceId = in.readString();
        this.date = in.readString();
        this.orgTypesReal = new ArrayList<PairedObject>();
        in.readList(this.orgTypesReal, List.class.getClassLoader());
        this.currenciesReal = new ArrayList<PairedObject>();
        in.readList(this.currenciesReal, List.class.getClassLoader());
        this.regionsReal = new ArrayList<PairedObject>();
        in.readList(this.regionsReal, List.class.getClassLoader());
        this.citiesReal = new ArrayList<PairedObject>();
        in.readList(this.citiesReal, List.class.getClassLoader());
    }

    public static final Creator<GlobalModel> CREATOR = new Creator<GlobalModel>() {
        public GlobalModel createFromParcel(Parcel source) {
            return new GlobalModel(source);
        }

        public GlobalModel[] newArray(int size) {
            return new GlobalModel[size];
        }
    };
}
