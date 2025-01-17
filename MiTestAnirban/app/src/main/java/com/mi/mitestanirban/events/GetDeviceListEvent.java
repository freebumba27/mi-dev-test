package com.mi.mitestanirban.events;

import android.util.Log;

import com.mi.mitestanirban.model.Device;

import java.util.ArrayList;

/**
 * Created by AJ on 1/22/16.
 */
public class GetDeviceListEvent {

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

        ArrayList<Device> deviceArrayList;

        public Success(ArrayList<Device> deviceArrayList) {
            this.deviceArrayList = deviceArrayList;
        }

        public ArrayList<Device> getDeviceArrayList() {
            return deviceArrayList;
        }

    }
}
