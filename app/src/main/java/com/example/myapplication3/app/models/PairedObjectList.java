package com.example.myapplication3.app.models;

import java.util.List;

public class PairedObjectList   {
    private List<PairedObject> list;

    public PairedObjectList(List<PairedObject> list) {
        this.list = list;
    }

    public List<PairedObject> getList() {
        return list;
    }

    public void setList(List<PairedObject> list) {
        this.list = list;
    }


}
