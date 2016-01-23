package com.mi.mitestanirban;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mi.mitestanirban.events.PassDeviceWithAndroidVersionEvent;
import com.mi.mitestanirban.model.AndroidDeviceWithVersion;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class DeviceDetailActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.textViewDeviceName)
    TextView textViewDeviceName;
    @Bind(R.id.textViewVersionName)
    TextView textViewVersionName;
    @Bind(R.id.textViewVersionCode)
    TextView textViewVersionCode;
    @Bind(R.id.textViewDescription)
    TextView textViewDescription;
    @Bind(R.id.imageViewDeviceImage)
    ImageView imageViewDeviceImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

    public void onEventMainThread(PassDeviceWithAndroidVersionEvent event) {

        AndroidDeviceWithVersion androidDeviceWithVersion = event.getAndroidDeviceWithVersion();
        if (androidDeviceWithVersion != null) {
            if (androidDeviceWithVersion.getImageUrl() != null)
                Glide.with(this).load(androidDeviceWithVersion.getImageUrl())
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .crossFade()
                        .into(imageViewDeviceImage);
            else
                imageViewDeviceImage.setImageResource(R.drawable.placeholder);

            textViewDeviceName.setText(androidDeviceWithVersion.getDeviceName());
            textViewVersionName.setText(androidDeviceWithVersion.getAndroidVersionName());
            textViewVersionCode.setText("Version: " + androidDeviceWithVersion.getVersion());
            textViewDescription.setText(androidDeviceWithVersion.getSnippet());
        }
        EventBus.getDefault().removeStickyEvent(event);
    }
}
