package com.mi.mitestanirban;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.mi.mitestanirban.events.GetDeviceEvent;
import com.mi.mitestanirban.events.PassDeviceEvent;
import com.mi.mitestanirban.jobs.SaveDeviceInfoJob;
import com.mi.mitestanirban.model.DeviceAdding;
import com.mi.mitestanirban.utils.MyDatabase;
import com.mi.mitestanirban.utils.ReusableClass;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class AddingDeviceActivity extends AppCompatActivity {

    @Bind(R.id.editTextDeviceName)
    EditText editTextDeviceName;
    @Bind(R.id.inputLayoutDeviceName)
    TextInputLayout inputLayoutDeviceName;
    @Bind(R.id.editTextSnippet)
    EditText editTextSnippet;
    @Bind(R.id.inputLayoutSnippet)
    TextInputLayout inputLayoutSnippet;
    @Bind(R.id.buttonSave)
    CircularProgressButton buttonSave;
    @Bind(R.id.spinnerAndroidVersion)
    Spinner spinnerAndroidVersion;
    List<String> spinnerList = new ArrayList<>();
    List<Integer> spinnerId = new ArrayList<>();


    private MyDatabase db;
    private int selectedAndroidDeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_device);
        ButterKnife.bind(this);

        db = new MyDatabase(this);
        addingVersionIntoSpinner();
    }

    private void addingVersionIntoSpinner() {
        Cursor cursor = db.getAndroidVersionAndId();
        if (cursor.getCount() > 0) {
            spinnerList.add("Select android version");
            while (cursor.moveToNext()) {
                spinnerList.add(cursor.getString(1));
                spinnerId.add(cursor.getInt(0));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(AddingDeviceActivity.this,
                    android.R.layout.simple_spinner_item, spinnerList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAndroidVersion.setAdapter(adapter);
            spinnerAndroidVersion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedAndroidDeviceId = spinnerId.get(position);
                    Log.d("TAG", "id: " + selectedAndroidDeviceId);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    @OnClick(R.id.buttonSave)
    public void savingDevice(View view) {
        String deviceName = editTextDeviceName.getText().toString().trim();
        String deviceDesc = editTextSnippet.getText().toString().trim();

        if (validate(deviceName, deviceDesc)) {
            if (ReusableClass.haveNetworkConnection(this)) {
                buttonSave.setProgress(15);
                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                DeviceAdding deviceAdding = new DeviceAdding();
                deviceAdding.setSnippet(deviceDesc);
                deviceAdding.setAndroidId(selectedAndroidDeviceId);
                deviceAdding.setImageUrl("");
                deviceAdding.setName(deviceName);

                MyApplication.addJobInBackground(new SaveDeviceInfoJob(deviceAdding));
            } else {
                buttonSave.setProgress(0);
                Toast.makeText(this, R.string.error_internet_connection, Toast.LENGTH_LONG).show();
            }
        } else
            buttonSave.setProgress(0);
    }

    private boolean validate(String deviceName, String deviceDesc) {
        if (TextUtils.isEmpty(deviceName)) {
            inputLayoutDeviceName.setError("Device name cannot be empty");
            editTextDeviceName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(deviceDesc)) {
            inputLayoutDeviceName.setError("Device description cannot be empty");
            editTextSnippet.requestFocus();
            return false;
        }
        return true;
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

    public void onEventMainThread(GetDeviceEvent.Success event) {
        buttonSave.setProgress(100);
        EventBus.getDefault().removeStickyEvent(event);

        EventBus.getDefault().postSticky(new PassDeviceEvent.Success(event.getDevice()));
        finish();
    }

    public void onEventMainThread(GetDeviceEvent.Fail event) {
        buttonSave.setProgress(-1);
        if (event.getEx() != null) {
            new AlertDialog.Builder(this)
                    .setMessage(event.getEx().getMessage())
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
}
