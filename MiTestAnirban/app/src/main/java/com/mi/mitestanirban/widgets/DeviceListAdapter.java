package com.mi.mitestanirban.widgets;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mi.mitestanirban.R;
import com.mi.mitestanirban.model.Devices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AJ on 1/22/16.
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {

    ArrayList<Devices> devicesArrayList;
    Context context;

    public DeviceListAdapter(Context context) {
        this.context = context;
        devicesArrayList = new ArrayList<>();
    }

    @Override
    public DeviceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_list_item_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DeviceListAdapter.ViewHolder viewHolder, int position) {

        viewHolder.device_name.setText(devicesArrayList.get(position).getName());
        viewHolder.device_version.setText("Version. " + devicesArrayList.get(position).getAndroidId());
        if (devicesArrayList.get(position).getImageUrl() != null)
            Glide.with(context).load(devicesArrayList.get(position).getImageUrl()).into(viewHolder.device_image);
        else
            viewHolder.device_image.setImageResource(R.drawable.placeholder);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Do your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return devicesArrayList.size();
    }

    public void addAll(List<Devices> devices) {
        this.devicesArrayList.clear();
        this.devicesArrayList.addAll(devices);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView device_name;
        private TextView device_version;
        private ImageView device_image;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            device_name = (TextView) itemView.findViewById(R.id.device_name);
            device_version = (TextView) itemView.findViewById(R.id.device_version);
            device_image = (ImageView) itemView.findViewById(R.id.device_image);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}
