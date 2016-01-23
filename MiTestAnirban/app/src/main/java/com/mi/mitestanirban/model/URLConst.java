package com.mi.mitestanirban.model;

import android.net.Uri;

/**
 * Created by AJ on 1/22/16.
 */
public class URLConst {
    public static final String BASE_URL = "http://mobilesandboxdev.azurewebsites.net";
    private static final String ANDROID_VERSION_API = BASE_URL + "/android";
    private static final String DEVICES_API = BASE_URL + "/devices";

    public static String getDevice() {
        return Uri.parse(DEVICES_API).buildUpon()
                .toString();
    }

    public static String getAndroidVersion() {
        return Uri.parse(ANDROID_VERSION_API).buildUpon()
                .toString();
    }
}
