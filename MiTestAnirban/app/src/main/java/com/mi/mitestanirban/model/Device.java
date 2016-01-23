package com.mi.mitestanirban.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Dream on 22-Jan-16.
 */
public class Device {

    private int Id;
    private int androidId;
    private String carrier;
    private String imageUrl;
    private String name;
    private String snippet;

    public static ArrayList<Device> toList(String json) {
        return new Gson().fromJson(json, new TypeToken<ArrayList<Device>>() {
        }.getType());
    }

    public static Device fromJson(String json) {
        return new Gson().fromJson(json, Device.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getAndroidId() {
        return androidId;
    }

    public void setAndroidId(int androidId) {
        this.androidId = androidId;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
