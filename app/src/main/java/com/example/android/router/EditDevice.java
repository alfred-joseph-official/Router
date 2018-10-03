package com.example.android.router;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditDevice extends AppCompatActivity {

    Devices device;
    int position;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_device);

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        //making sure there are extras. Otherwise app will crash!
        if(getIntent().hasExtra("clicked_device")) {
//          device = getIntent().getParcelableExtra("clicked_device"); //If data was parceled
            device = (Devices) getIntent().getSerializableExtra("clicked_device");
            loadData();
        }

        if(getIntent().hasExtra("position")) {
            position = getIntent().getIntExtra("position",position);
//            Log.d("position : " ,"" + position);
        }
    }

    private void loadData() {
        final EditText nickName,ipAdd,macAdd,deviceName;
        Button saveBtn;
        final Spinner spinner;
        final List<String> deviceTypes = new ArrayList<>();
        Collections.addAll(deviceTypes, "Laptop","Mobile","Desktop","Printer","Router");
        final List<Integer> images = new ArrayList<>();
        images.add(R.drawable.laptop_icon_black_bubble);
        images.add(R.drawable.mobile_icon_black_bubble);
        images.add(R.drawable.laptop_icon_black_bubble);
        images.add(R.drawable.laptop_icon_black_bubble);
        images.add(R.drawable.mobile_icon_black_bubble);

        nickName = findViewById(R.id.row1_edit_text);
        ipAdd = findViewById(R.id.row2_edit_text);
        macAdd = findViewById(R.id.row3_edit_text);
        deviceName = findViewById(R.id.row4_edit_text);
        saveBtn = findViewById(R.id.edit_device_save_btn);
        spinner = findViewById(R.id.edit_device_spinner);
        dbHandler = new MyDBHandler(this,null,null,1);

        nickName.setText(device.getNickName());
        ipAdd.setText(device.getiPAdd());
        macAdd.setText(device.getmACAdd());
        deviceName.setText(device.getDeviceName());

        EditDeviceSpinnerAdapter editDeviceSpinnerAdapter = new EditDeviceSpinnerAdapter(getApplicationContext(),images,deviceTypes);
        spinner.setAdapter(editDeviceSpinnerAdapter);
        final int position = images.indexOf(device.getImg());
        spinner.post(new Runnable() { // setting the saved image from the database for the device
            @Override
            public void run() {
                spinner.setSelection(position);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                device.setImg(images.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                device.setNickName(String.valueOf(nickName.getText()));
                dbHandler.updateHandler(device);
            }
        });

    }
}
