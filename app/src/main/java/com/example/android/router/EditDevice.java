package com.example.android.router;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

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

        nickName = findViewById(R.id.row1_edit_text);
        ipAdd = findViewById(R.id.row2_edit_text);
        macAdd = findViewById(R.id.row3_edit_text);
        deviceName = findViewById(R.id.row4_edit_text);
        saveBtn = findViewById(R.id.edit_device_save_btn);
        dbHandler = new MyDBHandler(this,null,null,1);

        nickName.setText(device.getNickName());
        ipAdd.setText(device.getiPAdd());
        macAdd.setText(device.getmACAdd());
        deviceName.setText(device.getDeviceName());

        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                device.setNickName(String.valueOf(nickName.getText()));
                dbHandler.updateHandler(device);
            }
        });
    }
}
