package com.mi.mitestanirban.jobs;

import android.content.Context;
import android.util.Log;

import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.mi.mitestanirban.MyApplication;
import com.mi.mitestanirban.events.GetDeviceListEvent;
import com.mi.mitestanirban.model.Device;
import com.mi.mitestanirban.model.URLConst;
import com.mi.mitestanirban.utils.Priority;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import de.greenrobot.event.EventBus;

/**
 * Created by AJ on 1/22/16.
 */
public class GetAllDeviceListJob extends Job {

    private static final AtomicInteger jobCounter = new AtomicInteger(0);
    private final int id;


    public GetAllDeviceListJob() {
        super(new Params(Priority.HIGH).requireNetwork());
        id = jobCounter.incrementAndGet();
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
                .load("GET", url)
                .addHeader("Content-Type", "application/json")
                .setStringBody("")
                .asString()
                .withResponse()
                .get();

        String json = response.getResult();
        Log.d("TAG", "response = " + json);

        if (response.getHeaders().code() != HttpURLConnection.HTTP_OK) {
            EventBus.getDefault().post(new GetDeviceListEvent.Fail(new Exception(json)));
            return;
        }

        ArrayList<Device> deviceArrayList = Device.toList(json);
        EventBus.getDefault().post(new GetDeviceListEvent.Success(deviceArrayList));
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        EventBus.getDefault().post(new GetDeviceListEvent.Fail(new Exception(throwable.getMessage())));
        return false;
    }
}
