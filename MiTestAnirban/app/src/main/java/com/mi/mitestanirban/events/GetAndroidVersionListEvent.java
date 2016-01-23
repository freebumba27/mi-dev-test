package com.mi.mitestanirban.events;

import android.util.Log;

import com.mi.mitestanirban.model.AndroidVersion;

import java.util.ArrayList;

/**
 * Created by AJ on 1/22/16.
 */
public class GetAndroidVersionListEvent {

    public static class Fail {
        Exception ex;

        public Fail(Exception ex) {
            this.ex = ex;
        }

        public Exception getEx() {
            Log.d("TAG", "Error");

            return ex;
        }
    }

    public static class Success {

        ArrayList<AndroidVersion> androidVersions;

        public Success(ArrayList<AndroidVersion> androidVersions) {
            this.androidVersions = androidVersions;
        }

        public ArrayList<AndroidVersion> getandroidVersionArrayList() {
            return androidVersions;
        }

    }
}
