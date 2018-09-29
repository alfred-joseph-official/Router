package com.example.android.router;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DevicesDB.db";
    private static final String TABLE_NAME = "Devices";
    private static final String COLUMN_MAC_ADD = "MAC_Address";
    private static final String COLUMN_DEVICE_NAME = "Device_Name";
    private static final String COLUMN_NICK_NAME = "Nick_Name";
    private static final String COLUMN_IP_ADD = "Ip_Address";
    private static final String COLUMN_LAN = "Wired_Wireless";
    private static final String COLUMN_IMAGE = "Image";
    private static final String COLUMN_UP_SPEED = "Up_Speed";
    private static final String COLUMN_DOWN_SPEED = "Down_Speed";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory,int version) {
        super(context,DATABASE_NAME,factory,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                COLUMN_MAC_ADD + " TEXT, " +
                COLUMN_DEVICE_NAME + " TEXT, " +
                COLUMN_NICK_NAME + " TEXT, " +
                COLUMN_IP_ADD + " TEXT, " +
                COLUMN_LAN + " INTEGER, " +
                COLUMN_IMAGE + " INTEGER, " +
                COLUMN_UP_SPEED + " INTEGER, " +
                COLUMN_DOWN_SPEED + " INTEGER " +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int i,int i1) { }

    /**
     *
     *  Use loadHandler to display all the data from the db.
     */

//    public String loadHandler() {
//        String result = "";
//        String query = "SELECT * FROM " + TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(query,null);
//        while (cursor.moveToNext()) {
//            String result_0 = cursor.getString(0);
//            String result_1 = cursor.getString(1);
//            String result_2 = cursor.getString(2);
//            String result_3 = cursor.getString(3);
//            result += String.valueOf(result_0 +" "+result_1+" " + result_2 + " " + result_3 + "\n\n");
//        }
//        cursor.close();
//        db.close();
//        return result;
//    }

    public void addHandler(Devices device) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_MAC_ADD + " FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_MAC_ADD + " = '" + device.getmACAdd() + "'",null);
        if (!cursor.moveToFirst())
        {
            ContentValues value = new ContentValues();
            value.put(COLUMN_MAC_ADD,device.getmACAdd());
            value.put(COLUMN_DEVICE_NAME,device.getDeviceName());
            value.put(COLUMN_NICK_NAME,device.getNickName());
            value.put(COLUMN_IP_ADD,device.getiPAdd());
            value.put(COLUMN_LAN,device.getLan());
            value.put(COLUMN_IMAGE,device.getImg());
            value.put(COLUMN_UP_SPEED,device.getUpSpeed());
            value.put(COLUMN_DOWN_SPEED,device.getDownSpeed());

            db.insert(TABLE_NAME,null,value);
            cursor.close();
            db.close();
        } else {
            cursor.close();
            db.close();
        }
    }

    public String getNickNameHandler(Devices device) {
        String result;
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_MAC_ADD + " = " + "'" +
                device.getmACAdd() + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            result = cursor.getString(2); // 2 is the index of the column that has nick name saved.
        } else result = null;

        cursor.close();
        db.close();
        return result;
    }

    public List<Integer> getUploadBandwidthAllottedHandler(String iPAdd) { //Get BandwidthAllocated to the device
        List<Integer> result = new ArrayList<>(2);
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_IP_ADD + " = " + "'" +
                iPAdd + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            result.add(cursor.getInt(6));
            result.add(cursor.getInt(7)); //6th and 7th index up and down speed respectively
        } else { result = null; }

        cursor.close();
        db.close();
        return result;
    }

    public void updateHandler(Devices device) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();

        args.put(COLUMN_MAC_ADD,device.getmACAdd());
        args.put(COLUMN_DEVICE_NAME,device.getDeviceName());
        args.put(COLUMN_NICK_NAME,device.getNickName());
        args.put(COLUMN_IP_ADD,device.getiPAdd());
        args.put(COLUMN_LAN,device.getLan());
        args.put(COLUMN_IMAGE,device.getImg());
        args.put(COLUMN_UP_SPEED,device.getUpSpeed());
        args.put(COLUMN_DOWN_SPEED,device.getDownSpeed());

        db.update(TABLE_NAME,args,COLUMN_MAC_ADD + " = '" + device.getmACAdd()+"'",null);
        db.close();
    }

    public void updateSpeedHandler(String ip,int up, int down) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_IP_ADD + " = " + "'" +
                ip + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            String macAdd = cursor.getString(0);
            args.put(COLUMN_MAC_ADD,macAdd);
            args.put(COLUMN_DEVICE_NAME,cursor.getString(1));
            args.put(COLUMN_NICK_NAME,cursor.getString(2));
            args.put(COLUMN_IP_ADD,cursor.getString(3));
            args.put(COLUMN_LAN,cursor.getInt(4));
            args.put(COLUMN_IMAGE,cursor.getInt(5));
            args.put(COLUMN_UP_SPEED,up);
            args.put(COLUMN_DOWN_SPEED,down);
            db.update(TABLE_NAME,args,COLUMN_MAC_ADD + " = '" + macAdd +"'",null);
        }
        cursor.close();
        db.close();
    }

//    public void deleteAllHelper() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_NAME,null,null);
//        db.execSQL("vacuum");
//        db.close();
//    }



}
