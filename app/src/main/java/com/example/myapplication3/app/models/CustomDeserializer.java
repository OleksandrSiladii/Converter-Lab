package com.example.myapplication3.app.models;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by omar on 9/22/15.
 */
public class CustomDeserializer implements JsonDeserializer<PairedObjectList> {


    @Override
    public PairedObjectList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        List<PairedObject> list = new ArrayList<PairedObject>();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            PairedObject pairedObject = new PairedObject();
            pairedObject.setId(entry.getKey());
            pairedObject.setName(String.valueOf(entry.getValue()));
            list.add(pairedObject);
        }
        return new PairedObjectList(list);
    }


    public static  List<PairedObject> getPairedObjectList(JsonElement jsonElement)
    {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(PairedObjectList.class, new CustomDeserializer());
        Gson gson = builder.create();
        PairedObjectList list =  gson.fromJson(jsonElement, PairedObjectList.class);
        return list.getList();
    }
}

