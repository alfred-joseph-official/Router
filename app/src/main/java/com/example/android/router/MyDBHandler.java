package com.example.android.router;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DecimalFormat;
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

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory,int version) {
        super(context,DATABASE_NAME,factory,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_MAC_ADD + " TEXT, " +
                COLUMN_DEVICE_NAME + " TEXT, " + COLUMN_NICK_NAME + " TEXT, " + COLUMN_IP_ADD + " TEXT, " +
                COLUMN_LAN + " INTEGER, "+ COLUMN_IMAGE + " INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int i,int i1) { }

//    public List<Devices> loadHandler() {
//        List<Devices> devicesList= new ArrayList<>();
//        Devices device;
//        String query = "SELECT * FROM " + TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(query,null);
//        while (cursor.moveToNext()) {
//            device = new Devices(cursor.getString(0),cursor.getString(1),cursor.getString(2),
//                    cursor.getString(3),cursor.getInt(4));
//            devicesList.add(device);
//        }
//        cursor.close();
//        db.close();
//        return devicesList;
//    }

    public String loadHandler() {
        String result = "";
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()) {
            String result_0 = cursor.getString(0);
            String result_1 = cursor.getString(1);
            String result_2 = cursor.getString(2);
            String result_3 = cursor.getString(3);
            result += String.valueOf(result_0 +" "+result_1+" " + result_2 + " " + result_3 + "\n\n");
        }
        cursor.close();
        db.close();
        return result;
    }

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
            result = cursor.getString(2);
        } else result = null;

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
        db.update(TABLE_NAME,args,COLUMN_MAC_ADD + " = '" + device.getmACAdd()+"'",null);
        db.close();
    }

//    public List<String> loadHandler() {
//        List<String> result = new ArrayList<>();
//        String query = "SELECT * FROM " + TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(query,null);
//        while (cursor.moveToNext()) {
//            result.add(cursor.getString(0));
//            result.add(cursor.getString(1));
//            result.add(cursor.getString(2));
//            result.add(cursor.getString(3));
//        }
//        cursor.close();
//        db.close();
//        return result;
//    }

//    public void addHandler(Devices device) {
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_DEVICE_NAME,device.getDeviceName());
//        values.put(COLUMN_IP_ADD,device.getiPAdd());
//        values.put(COLUMN_MAC_ADD,device.getmACAdd());
//        values.put(COLUMN_LAN,device.getLan());
//        SQLiteDatabase db = this.getReadableDatabase();
//        db.insert(TABLE_NAME,null,values);
//        db.close();
//    }

//    public void deleteAllHelper() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_NAME,null,null);
//        db.execSQL("vacuum");
//        db.close();
//    }



}
