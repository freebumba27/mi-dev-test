package com.mi.mitestanirban.events;

import android.util.Log;

import com.mi.mitestanirban.model.Device;

/**
 * Created by AJ on 1/22/16.
 */
public class PassDeviceEvent {

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

        Device device;

        public Success(Device device) {
            this.device = device;
        }

        public Device getDevice() {
            return device;
        }

    }
}
