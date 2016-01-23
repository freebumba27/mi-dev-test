package com.mi.mitestanirban.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Dream on 22-Jan-16.
 */
public class AndroidVersion {

    private int id;
    private String name;
    private String version;
    private String codename;
    private String target;
    private String distribution;

    public static ArrayList<AndroidVersion> toList(String json) {
        return new Gson().fromJson(json, new TypeToken<ArrayList<AndroidVersion>>() {
        }.getType());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }
}
