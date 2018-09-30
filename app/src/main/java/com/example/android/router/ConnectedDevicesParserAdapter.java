package com.example.android.router;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ConnectedDevicesParserAdapter extends RecyclerView.Adapter<ConnectedDevicesParserAdapter.ConnectedDevicesParserHolder>{
    private Context cDPCtx;
    private List<Devices> devicesList;

    public ConnectedDevicesParserAdapter(Context cDPCtx) {
        this.cDPCtx = cDPCtx;
    }

    public void setDevicesList(List<Devices> devicesList) {
        this.devicesList = devicesList;
    }

    @Override
    public ConnectedDevicesParserHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        LayoutInflater inflater = LayoutInflater.from(cDPCtx);
        View view = inflater.inflate(R.layout.connected_devices_card_template,null);
        return new ConnectedDevicesParserHolder(view);
    }

    @Override
    public void onBindViewHolder(final ConnectedDevicesParserHolder holder, final int position) {
        final Devices device = devicesList.get(position);

        holder.deviceNameTV.setText(device.getNickName());
        holder.ipAddTV.setText(device.getiPAdd());
        holder.macAddTV.setText(device.getmACAdd());
        holder.imageView.setImageDrawable(cDPCtx.getResources().getDrawable(device.getImg()));

        if(device.getUpSpeed() == -1 || device.getDownSpeed() == -1) {
            holder.speedIndicatorLL.setVisibility(View.GONE);
            holder.unLimitedSpeedIV.setVisibility(View.VISIBLE);
        } else {
            holder.unLimitedSpeedIV.setVisibility(View.GONE);
            holder.speedIndicatorLL.setVisibility(View.VISIBLE);
            holder.upSpeedTV.setText(String.valueOf(device.getUpSpeed()));
            holder.downSpeedTV.setText(String.valueOf(device.getDownSpeed()));
            holder.upTUTV.setText(device.getUpTUString());
            holder.downTUTV.setText(device.getDownTUString());
        }

//        holder.imageView.setImageDrawable(cDPCtx.getResources().getDrawable(R.mipmap.ic_launcher_round));

        holder.cardRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(cDPCtx,"You Clicked " + device.getNickName(),Toast.LENGTH_LONG).show(); //works!
                Intent intent = new Intent(cDPCtx,EditDevice.class);
                intent.putExtra("position",position);
                intent.putExtra("clicked_device",device);
                cDPCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return devicesList.size();
    }

    class ConnectedDevicesParserHolder extends RecyclerView.ViewHolder {

        ImageView imageView,unLimitedSpeedIV;
        TextView deviceNameTV, ipAddTV, macAddTV, upSpeedTV, downSpeedTV, upTUTV, downTUTV;
        LinearLayout cardRelativeLayout, speedIndicatorLL;

        public ConnectedDevicesParserHolder(View itemView) {
            super(itemView);

            cardRelativeLayout = itemView.findViewById(R.id.card_relative_layout);

            imageView = itemView.findViewById(R.id.card_image_view);
            deviceNameTV = itemView.findViewById(R.id.card_text_view_name);
            ipAddTV = itemView.findViewById(R.id.card_text_view_ip);
            macAddTV = itemView.findViewById(R.id.card_text_view_mac);

            speedIndicatorLL = itemView.findViewById(R.id.card_ll_speed_indicator);
            unLimitedSpeedIV = itemView.findViewById(R.id.card_when_unlimited_speed);
            upSpeedTV = itemView.findViewById(R.id.card_ll_rl_ll_up_speed_text_view);
            downSpeedTV = itemView.findViewById(R.id.card_ll_rl_ll_down_speed_text_view);
            upTUTV = itemView.findViewById(R.id.card_ll_rl_ll_up_speed_transfer_units);
            downTUTV = itemView.findViewById(R.id.card_ll_rl_ll_down_speed_transfer_units);
        }
    }
}
