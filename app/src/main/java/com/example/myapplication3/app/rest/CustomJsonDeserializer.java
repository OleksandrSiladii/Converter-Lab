package com.example.myapplication3.app.rest;



import com.example.myapplication3.app.models.Currency;
import com.example.myapplication3.app.models.GlobalModel;
import com.example.myapplication3.app.models.Organization;
import com.example.myapplication3.app.models.PairedObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sasha on 21.10.2015.
 */
public class CustomJsonDeserializer implements JsonDeserializer<GlobalModel> {
    int q = 0;

    @Override
    public GlobalModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        GlobalModel globalModel = new GlobalModel();
        JsonObject jsonObject = json.getAsJsonObject();
        if (!(jsonObject == null)) {
            globalModel.setSourceId(jsonObject.get("sourceId").getAsString());
            globalModel.setDate(jsonObject.get("date").getAsString());
            List<Organization> organizations = new ArrayList<Organization>();
            JsonArray jsonOrganizationArray = jsonObject.get("organizations").getAsJsonArray();
            for (JsonElement jsonO : jsonOrganizationArray) {
                try {
                    JsonObject jsonOrganization = jsonO.getAsJsonObject();
                    Organization organization = new Organization();
                    organization.setId(jsonOrganization.get("id").getAsString());
                    organization.setOldId(jsonOrganization.get("oldId").getAsInt());
                    organization.setOrgType(jsonOrganization.get("orgType").getAsInt());
                    organization.setTitle(jsonOrganization.get("title").getAsString());
                    organization.setRegionId(jsonOrganization.get("regionId").getAsString());
                    organization.setCityId(jsonOrganization.get("cityId").getAsString());
                    organization.setPhone(jsonOrganization.get("phone").getAsString());
                    organization.setAddress(jsonOrganization.get("address").getAsString());
                    organization.setLink(jsonOrganization.get("link").getAsString());

                    JsonObject jsonCurrencies = jsonOrganization.get("currencies").getAsJsonObject();

                    List<Currency> currencies = new ArrayList<Currency>();

                    Iterable<Map.Entry<String, JsonElement>> entries = jsonCurrencies.entrySet();

                    for (Map.Entry<String, JsonElement> currencyMap : entries) {
                        JsonObject jsonCurrency = currencyMap.getValue().getAsJsonObject();

                        Currency currency = new Currency();
                        currency.setAsk(jsonCurrency.get("ask").getAsString());
                        currency.setBid(jsonCurrency.get("bid").getAsString());
                        currency.setNameCurrency(currencyMap.getKey());
                        currencies.add(currency);
                    }
                    organization.setCurrenciesReal(currencies);
                    organizations.add(organization);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            globalModel.setOrganizations(organizations);
            globalModel.setOrgTypesReal(getListPairedObjects(jsonObject, "orgTypes"));
            globalModel.setCurrenciesReal(getListPairedObjects(jsonObject, "currencies"));
            globalModel.setRegionsReal(getListPairedObjects(jsonObject, "regions"));
            globalModel.setCitiesReal(getListPairedObjects(jsonObject, "cities"));
        }
        return globalModel;
    }

    private List<PairedObject> getListPairedObjects(JsonObject jsonObject1, String key) {
        List<PairedObject> PairedObjects = new ArrayList<PairedObject>();
        JsonObject jsonOrgTypes = jsonObject1.get(key).getAsJsonObject();
        Iterable<Map.Entry<String, JsonElement>> entries = jsonOrgTypes.entrySet();

        for (Map.Entry<String, JsonElement> orgTypesMap : entries) {
            PairedObject pairedObject = new PairedObject();
            pairedObject.setId(orgTypesMap.getKey());
            pairedObject.setName(String.valueOf(orgTypesMap.getValue()));
            PairedObjects.add(pairedObject);
        }
        return PairedObjects;
    }
}
