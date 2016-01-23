package com.mi.mitestanirban.events;

import com.mi.mitestanirban.model.AndroidDeviceWithVersion;

/**
 * Created by AJ on 1/22/16.
 */
public class PassDeviceWithAndroidVersionEvent {
    AndroidDeviceWithVersion androidDeviceWithVersion;

    public PassDeviceWithAndroidVersionEvent(AndroidDeviceWithVersion androidDeviceWithVersion) {
        this.androidDeviceWithVersion = androidDeviceWithVersion;
    }

    public AndroidDeviceWithVersion getAndroidDeviceWithVersion() {
        return androidDeviceWithVersion;
    }
}
