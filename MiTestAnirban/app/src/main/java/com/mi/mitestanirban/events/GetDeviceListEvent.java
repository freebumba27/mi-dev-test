package com.mi.mitestanirban.events;

import android.util.Log;

import com.mi.mitestanirban.model.Devices;

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

        ArrayList<Devices> devicesArrayList;

        public Success(ArrayList<Devices> devicesArrayList) {
            this.devicesArrayList = devicesArrayList;
        }

        public ArrayList<Devices> getDevicesArrayList() {
            return devicesArrayList;
        }

    }
}
