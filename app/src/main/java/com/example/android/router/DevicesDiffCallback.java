package com.example.android.router;

import android.support.v7.util.DiffUtil;

import java.util.List;

public class DevicesDiffCallback extends DiffUtil.Callback {
    private List<Devices> newList;
    private List<Devices> oldList;

    public DevicesDiffCallback(List<Devices> newList, List<Devices> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    /**
     *
     * checks if ipAdd are the same.
     *
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getiPAdd().equals(newList.get(newItemPosition).getiPAdd());
    }

    /**
     *
     * if areItemsTheSame returns true then it will check if the contents are same.
     *
     */

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
