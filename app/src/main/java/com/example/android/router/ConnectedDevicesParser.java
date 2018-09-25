package com.example.android.router;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ConnectedDevicesParser extends AppCompatActivity {

    int lan,img;
    List<Devices> deviceList;
    RecyclerView recyclerView;
    ConnectedDevicesParserAdapter adapter;
    MyDBHandler dbHandler;
    TextView dbView;
    Button loadBtn;

//    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connected_devices);

//        To Create Custom Toolbar

//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        deviceList = new ArrayList<>();
        lan = 0;

        recyclerView = findViewById(R.id.ACDP_recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ConnectedDevicesParserAdapter(this);

        dbHandler = new MyDBHandler(this,null,null,1);

//        dbView = findViewById(R.id.dbView);
//
//        loadBtn = findViewById(R.id.load);
//
//        loadBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                dbView.setText(dbHandler.loadHandler());
//            }
//        });

        parseIt();
    }

    public void parseIt() {
        String siteUrl = "http://192.168.0.1/dhcptbl.htm";
        (new ParseURL() ).execute(new String[]{siteUrl});
    }

    private class ParseURL extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuffer buffer = new StringBuffer();
            try {
                Log.d("JSwa", "Connecting to ["+strings[0]+"]");
                Document doc = Jsoup.connect(strings[0]).get();
                Log.d("JSwa", "Connected to ["+strings[0]+"]");

                Element body = doc.select("table#body_header").get(0);
                Elements tables = body.select("table.formlisting"); //table array size 2
                for(Element temp : tables) {
                    Elements tr = temp.select("tr"); //tr array size 2
                    for (Element temp2 : tr) {
                        Elements td = temp2.select("td"); //td array size 3
                        String name = td.get(0).text(); //is getting the right values!
                        if(!name.equals("Name")) {
                            String Ip = td.get(1).text(); //is getting the right values!
                            String MAC = td.get(2).text(); //is getting the right values!

                            if (lan == 1) img = R.drawable.laptop_icon_black_bubble;
                            else img = R.drawable.mobile_icon_black_bubble;

                            Devices device = new Devices(name,"", Ip, MAC, lan,img);
                            String string = dbHandler.getNickNameHandler(device);
                            if(string != null && !string.equals(device.getDeviceName())) device.setNickName(string);
                            else {
                                device.setNickName(device.getDeviceName());
                                if (string == null) {
                                    dbHandler.addHandler(device);
                                }
                            }
                            deviceList.add(device);
                        } else {
                            lan ^= 1;
                        }
                    }
                }
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            return buffer.toString();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter.setDevicesList(deviceList);
            recyclerView.setAdapter(adapter);
        }
    }
}