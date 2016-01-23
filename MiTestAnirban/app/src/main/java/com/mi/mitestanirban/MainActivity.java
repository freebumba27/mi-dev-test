package com.mi.mitestanirban;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mi.mitestanirban.events.GetAndroidVersionListEvent;
import com.mi.mitestanirban.events.GetDeviceListEvent;
import com.mi.mitestanirban.events.PassDeviceEvent;
import com.mi.mitestanirban.jobs.GetAllAndroidVersionListJob;
import com.mi.mitestanirban.jobs.GetAllDeviceListJob;
import com.mi.mitestanirban.model.AndroidDeviceWithVersion;
import com.mi.mitestanirban.model.AndroidVersion;
import com.mi.mitestanirban.model.Device;
import com.mi.mitestanirban.utils.MyDatabase;
import com.mi.mitestanirban.utils.ReusableClass;
import com.mi.mitestanirban.widgets.DeviceWithAndroidVersionListAdapter;

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
    @Bind(R.id.textViewNoData)
    TextView textViewNoData;

    private DeviceWithAndroidVersionListAdapter mAdapter;
    private MyDatabase db;
    private boolean noData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddingDeviceActivity.class);
                startActivity(i);
            }
        });

        db = new MyDatabase(this);
        mAdapter = new DeviceWithAndroidVersionListAdapter(this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        deviceList.setLayoutManager(mLayoutManager);
        deviceList.setAdapter(mAdapter);

        Cursor cursor = db.getAllDeviceWithAndroidVersion();
        if (cursor.getCount() > 0) {
            noData = false;
            addingDataToList(cursor);
        } else
            noData = true;

        if (ReusableClass.haveNetworkConnection(this)) {
            if (noData)
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
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(GetDeviceListEvent.Success event) {
        setListShown(true, true);

        if (event.getDeviceArrayList() != null) {
            db.deleteAllDevice();

            ArrayList<Device> devices = event.getDeviceArrayList();
            for (int i = 0; i < devices.size(); i++) {
                db.insertDevices(devices.get(i).getId(), devices.get(i).getAndroidId(), devices.get(i).getImageUrl(),
                        devices.get(i).getName(), devices.get(i).getSnippet());
            }

            if (ReusableClass.haveNetworkConnection(this)) {
                if (noData)
                    setListShown(false, true);
                MyApplication.addJobInBackground(new GetAllAndroidVersionListJob());
            } else
                Toast.makeText(this, R.string.error_internet_connection, Toast.LENGTH_LONG).show();
        }
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

    public void onEventMainThread(GetAndroidVersionListEvent.Success event) {
        setListShown(true, true);

        if (event.getandroidVersionArrayList() != null) {
            db.deleteAllAndroidVersion();

            ArrayList<AndroidVersion> androidVersions = event.getandroidVersionArrayList();
            for (int i = 0; i < androidVersions.size(); i++) {
                db.insertAndroidVersions(androidVersions.get(i).getId(), androidVersions.get(i).getName(), androidVersions.get(i).getVersion(),
                        androidVersions.get(i).getCodename(), androidVersions.get(i).getTarget(), androidVersions.get(i).getDistribution());
            }

            addingDataToList();
        }
    }

    private void addingDataToList() {
        Cursor cursor = db.getAllDeviceWithAndroidVersion();
        ArrayList<AndroidDeviceWithVersion> deviceWithVersionsList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                AndroidDeviceWithVersion androidDeviceWithVersion = new AndroidDeviceWithVersion();
                androidDeviceWithVersion.setDeviceId(cursor.getInt(0));
                androidDeviceWithVersion.setImageUrl(cursor.getString(1));
                androidDeviceWithVersion.setDeviceName(cursor.getString(2));
                androidDeviceWithVersion.setSnippet(cursor.getString(3));
                androidDeviceWithVersion.setAndroidVersionId(cursor.getInt(4));
                androidDeviceWithVersion.setAndroidVersionName(cursor.getString(5));
                androidDeviceWithVersion.setVersion(cursor.getString(6));

                deviceWithVersionsList.add(androidDeviceWithVersion);
            }
            cursor.close();
        } else {
            textViewNoData.setVisibility(View.VISIBLE);
        }
        mAdapter.addAll(deviceWithVersionsList);
    }

    private void addingDataToList(Cursor cursor) {
        ArrayList<AndroidDeviceWithVersion> deviceWithVersionsList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                AndroidDeviceWithVersion androidDeviceWithVersion = new AndroidDeviceWithVersion();
                androidDeviceWithVersion.setDeviceId(cursor.getInt(0));
                androidDeviceWithVersion.setImageUrl(cursor.getString(1));
                androidDeviceWithVersion.setDeviceName(cursor.getString(2));
                androidDeviceWithVersion.setSnippet(cursor.getString(3));
                androidDeviceWithVersion.setAndroidVersionId(cursor.getInt(4));
                androidDeviceWithVersion.setAndroidVersionName(cursor.getString(5));
                androidDeviceWithVersion.setVersion(cursor.getString(6));

                deviceWithVersionsList.add(androidDeviceWithVersion);
            }
            cursor.close();
        } else {
            textViewNoData.setVisibility(View.VISIBLE);
        }
        mAdapter.addAll(deviceWithVersionsList);
    }

    public void onEventMainThread(GetAndroidVersionListEvent.Fail event) {
        setListShown(true, true);
        if (event.getEx() != null) {
            new AlertDialog.Builder(this)
                    .setMessage(event.getEx().getMessage())
                    .setPositiveButton("OK", null)
                    .show();
        }
    }


    public void onEventMainThread(PassDeviceEvent.Success event) {

        if (ReusableClass.haveNetworkConnection(this)) {
            setListShown(false, true);
            MyApplication.addJobInBackground(new GetAllDeviceListJob());
        } else
            Toast.makeText(this, R.string.error_internet_connection, Toast.LENGTH_LONG).show();
        EventBus.getDefault().removeStickyEvent(event);
    }
}
