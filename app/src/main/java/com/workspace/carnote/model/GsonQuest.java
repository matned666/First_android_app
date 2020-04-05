package com.workspace.carnote.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class GsonQuest {

    public static ArrayList<AutoData> getList(String gsonStr){
        Gson gson = new Gson();
        return gson.fromJson(gsonStr, new TypeToken<ArrayList<AutoData>>() {
        }.getType());
    }

    public static String make(List list){
        Gson gson = new Gson();
        return gson.toJson(list);
    }

}
