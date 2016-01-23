package com.mi.mitestanirban;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mi.mitestanirban.events.GetDeviceListEvent;
import com.mi.mitestanirban.model.Devices;
import com.mi.mitestanirban.model.GetAllDeviceListJob;
import com.mi.mitestanirban.utils.MyDatabase;
import com.mi.mitestanirban.utils.ReusableClass;
import com.mi.mitestanirban.widgets.DeviceListAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.deviceList)
    RecyclerView deviceList;
    @Bind(R.id.progress_container)
    LinearLayout progressContainer;

    private DeviceListAdapter mAdapter;
    private MyDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        db = new MyDatabase(this);
        mAdapter = new DeviceListAdapter(this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        deviceList.setLayoutManager(mLayoutManager);
        deviceList.setAdapter(mAdapter);


        if (ReusableClass.haveNetworkConnection(this)) {
            setListShown(false, true);
            MyApplication.addJobInBackground(new GetAllDeviceListJob());
        } else
            Toast.makeText(this, R.string.error_internet_connection, Toast.LENGTH_LONG).show();
    }

    private void setListShown(boolean shown, boolean animate) {
        ReusableClass.setListShown(deviceList, progressContainer, shown, animate);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(GetDeviceListEvent.Success event) {
        setListShown(true, true);

        db.deleteAllDevice();

        ArrayList<Devices> devices = event.getDevicesArrayList();
        for (int i = 0; i < devices.size(); i++) {
            db.insertDevices(devices.get(i).getId(), devices.get(i).getAndroidId(), devices.get(i).getImageUrl(),
                    devices.get(i).getName(), devices.get(i).getSnippet());
        }

        Cursor cursor = db.getAllDevice();
        ArrayList<Devices> myDevices = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Devices device = new Devices();
                device.setId(cursor.getInt(0));
                device.setAndroidId(cursor.getInt(1));
                device.setImageUrl(cursor.getString(2));
                device.setName(cursor.getString(3));
                device.setSnippet(cursor.getString(4));

                myDevices.add(device);
            }
            cursor.close();
        }


        mAdapter.addAll(myDevices);
    }

    public void onEventMainThread(GetDeviceListEvent.Fail event) {
        setListShown(true, true);
        if (event.getEx() != null) {
            new AlertDialog.Builder(this)
                    .setMessage(event.getEx().getMessage())
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
}
