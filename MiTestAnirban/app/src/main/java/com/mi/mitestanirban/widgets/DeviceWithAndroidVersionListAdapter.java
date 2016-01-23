package com.mi.mitestanirban.widgets;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mi.mitestanirban.DeviceDetailActivity;
import com.mi.mitestanirban.R;
import com.mi.mitestanirban.events.PassDeviceWithAndroidVersionEvent;
import com.mi.mitestanirban.model.AndroidDeviceWithVersion;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by AJ on 1/22/16.
 */
public class DeviceWithAndroidVersionListAdapter extends RecyclerView.Adapter<DeviceWithAndroidVersionListAdapter.ViewHolder> {

    ArrayList<AndroidDeviceWithVersion> androidDeviceWithVersionArrayList;
    Context context;

    public DeviceWithAndroidVersionListAdapter(Context context) {
        this.context = context;
        androidDeviceWithVersionArrayList = new ArrayList<>();
    }

    @Override
    public DeviceWithAndroidVersionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_list_item_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DeviceWithAndroidVersionListAdapter.ViewHolder viewHolder, int position) {
        final AndroidDeviceWithVersion androidDeviceWithVersion = androidDeviceWithVersionArrayList.get(position);
        viewHolder.device_name.setText(androidDeviceWithVersion.getDeviceName());
        viewHolder.device_version.setText(androidDeviceWithVersion.getAndroidVersionName() + " (" + androidDeviceWithVersion.getVersion() + ")");

        if (androidDeviceWithVersion.getImageUrl() != null)
            Glide.with(context).load(androidDeviceWithVersion.getImageUrl())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .crossFade()
                    .into(viewHolder.device_image);
        else
            viewHolder.device_image.setImageResource(R.drawable.placeholder);

        viewHolder.linearLayoutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new PassDeviceWithAndroidVersionEvent(androidDeviceWithVersion));

                Intent i = new Intent(context, DeviceDetailActivity.class);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return androidDeviceWithVersionArrayList.size();
    }

    public void addAll(List<AndroidDeviceWithVersion> androidDeviceWithVersions) {
        this.androidDeviceWithVersionArrayList.clear();
        this.androidDeviceWithVersionArrayList.addAll(androidDeviceWithVersions);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView device_name;
        private TextView device_version;
        private ImageView device_image;
        private LinearLayout linearLayoutRow;

        public ViewHolder(View itemView) {
            super(itemView);
            device_name = (TextView) itemView.findViewById(R.id.device_name);
            device_version = (TextView) itemView.findViewById(R.id.device_version);
            device_image = (ImageView) itemView.findViewById(R.id.device_image);
            linearLayoutRow = (LinearLayout) itemView.findViewById(R.id.linearLayoutRow);
        }
    }
}
