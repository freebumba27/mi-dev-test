package com.mi.mitestanirban.model;

/**
 * Created by Dream on 23-Jan-16.
 */
public class AndroidDeviceWithVersion {
    private int deviceId;
    private int AndroidVersionId;
    private String androidVersionName;
    private String version;
    private String imageUrl;
    private String deviceName;
    private String snippet;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getAndroidVersionId() {
        return AndroidVersionId;
    }

    public void setAndroidVersionId(int androidVersionId) {
        AndroidVersionId = androidVersionId;
    }

    public String getAndroidVersionName() {
        return androidVersionName;
    }

    public void setAndroidVersionName(String androidVersionName) {
        this.androidVersionName = androidVersionName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
}
