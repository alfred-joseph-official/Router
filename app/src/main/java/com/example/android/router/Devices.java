package com.example.android.router;

import java.io.Serializable;

/**
 * https://www.techjini.com/blog/passing-objects-via-intent-in-android/
 *
 * Parcelable is faster and uses less memory but it requires bit coding and time.
 *      Whereas Serialization needs less coding and quick to implement but it
 *                      is slower and consumes more memory.
 *
 */

public class Devices implements Serializable { //implement Parcel if you want to use it.

    private String deviceName, iPAdd, mACAdd,nickName;
    private int lan = 0;

    private int img;

    public Devices(String deviceNAme,String nickName, String iPAdd, String mACAdd, int lan, int img) {
        this.mACAdd = mACAdd;
        this.deviceName = deviceNAme;
        this.nickName = nickName;
        this.iPAdd = iPAdd;
        this.lan = lan;
        this.img = img;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceNAme) {
        this.deviceName = deviceNAme;
    }

    public String getiPAdd() {
        return iPAdd;
    }

    public void setiPAdd(String iPAdd) {
        this.iPAdd = iPAdd;
    }

    public String getmACAdd() {
        return mACAdd;
    }

    public void setmACAdd(String mACAdd) {
        this.mACAdd = mACAdd;
    }

    public int getLan() {
        return lan;
    }

    public void setLan(int lan) {
        this.lan = lan;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {

        this.img = img;
    }

    /**
     * Parcel Code
     */
//    public Devices(Parcel in) {
//        mACAdd = in.readString();
//        deviceName = in.readString();
//        nickName = in.readString();
//        iPAdd = in.readString();
//        lan = in.readInt();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest,int flags){
//        dest.writeString(mACAdd);
//        dest.writeString(deviceName);
//        dest.writeString(nickName);
//        dest.writeString(iPAdd);
//        dest.writeInt(lan);
//    }
//
//    public static final Parcelable.Creator<Devices> CREATOR = new Parcelable.Creator<Devices>() {
//        public Devices createFromParcel(Parcel in) {
//            return new Devices(in);
//        }
//
//        public Devices[] newArray(int size) {
//            return new Devices[size];
//    }
//    };


}
