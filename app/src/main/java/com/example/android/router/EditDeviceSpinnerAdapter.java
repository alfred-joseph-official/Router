package com.example.android.router;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class EditDeviceSpinnerAdapter extends BaseAdapter {
    Context eDSACTX;
    List<Integer> images;
    List<String> deviceTypes;
    LayoutInflater inflater;

    public EditDeviceSpinnerAdapter(Context eDSACTX, List<Integer> images, List<String> deviceTypes) {
        this.eDSACTX = eDSACTX;
        this.images = images;
        this.deviceTypes = deviceTypes;
        inflater = (LayoutInflater.from(eDSACTX));
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.spinner_items,null);
        ImageView icon = view.findViewById(R.id.spinner1_imageview);
//        TextView deviceType = view.findViewById(R.id.spinner1_textview);
        icon.setImageResource(images.get(i));
//        deviceType.setText(deviceTypes.get(i));
        return view;
    }
}
