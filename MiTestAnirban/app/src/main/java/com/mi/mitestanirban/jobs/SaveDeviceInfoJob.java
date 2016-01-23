package com.mi.mitestanirban.jobs;

import android.content.Context;
import android.util.Log;

import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.mi.mitestanirban.MyApplication;
import com.mi.mitestanirban.events.GetDeviceEvent;
import com.mi.mitestanirban.model.Device;
import com.mi.mitestanirban.model.DeviceAdding;
import com.mi.mitestanirban.model.URLConst;
import com.mi.mitestanirban.utils.Priority;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.net.HttpURLConnection;
import java.util.concurrent.atomic.AtomicInteger;

import de.greenrobot.event.EventBus;

/**
 * Created by AJ on 1/22/16.
 */
public class SaveDeviceInfoJob extends Job {

    private static final AtomicInteger jobCounter = new AtomicInteger(0);
    private final int id;
    private DeviceAdding deviceAdding;

    public SaveDeviceInfoJob(DeviceAdding deviceAdding) {
        super(new Params(Priority.HIGH).requireNetwork());
        id = jobCounter.incrementAndGet();
        this.deviceAdding = deviceAdding;
    }


    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {

        if (id != jobCounter.get()) {
            // looks like other fetch jobs has been added after me. no reason to
            // keep fetching many times, cancel me, let the other one fetch
            return;
        }

        Context context = MyApplication.getInstance();

        String url = URLConst.device();
        Log.d("TAG", "url = " + url);

        Response<String> response = Ion.with(context)
                .load("POST", url)
                .addHeader("Content-Type", "application/json")
                .setStringBody(deviceAdding.toString())
                .asString()
                .withResponse()
                .get();

        String json = response.getResult();
        Log.d("TAG", "response = " + json);

        if (response.getHeaders().code() != HttpURLConnection.HTTP_CREATED) {
            EventBus.getDefault().postSticky(new GetDeviceEvent.Fail(new Exception(json)));
            return;
        }

        Device device = Device.fromJson(json);
        EventBus.getDefault().post(new GetDeviceEvent.Success(device));
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        EventBus.getDefault().post(new GetDeviceEvent.Fail(new Exception(throwable.getMessage())));
        return false;
    }
}
