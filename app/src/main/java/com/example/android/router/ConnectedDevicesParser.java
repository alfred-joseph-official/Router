package com.example.android.router;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ConnectedDevicesParser extends AppCompatActivity {

    private static final String activeClientTableURL = "http://192.168.0.1/dhcptbl.htm";
    private static final String trafficControlURL = "http://192.168.0.1/ipqostc_gen_ap.htm";
    int lan,img;
    private List<Devices> deviceList;
    ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ConnectedDevicesParserAdapter adapter;
    private MyDBHandler dbHandler;
//    TextView dbView;
//    Button loadBtn;

//    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connected_devices);
        progressBar = findViewById(R.id.ACDP_progress_bar);

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
        beginParsing();
    }

    private void beginParsing() {
        new activeClientTableParse().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,activeClientTableURL);
        new trafficControlParse().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,trafficControlURL);

    }

    @SuppressLint("StaticFieldLeak")
    private class activeClientTableParse extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.i("AsyncTask ", "activeClientTableParse Started");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
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

                            Devices device = new Devices(name,"", Ip, MAC, lan, img);
                            String string = dbHandler.getNickNameHandler(device);
                            List<Integer> speeds = dbHandler.getUploadBandwidthAllottedHandler(Ip);
                            if (speeds != null) {
                                device.setUpSpeed(speeds.get(0));
                                device.setDownSpeed(speeds.get(1));
                            }
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
            Log.d("JSwa", " Finished " +activeClientTableURL); // The 2nd asyncTask starts right after this doInBackground Ends.
            return "";
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("AsyncTask ", "activeClientTableParse Complete");
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class trafficControlParse extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.i("AsyncTask ", "TrafficControlParse Started");
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Log.d("JSwa", "Connecting to ["+strings[0]+"]");
                Document doc = Jsoup.connect(strings[0]).get();
                Log.d("JSwa", "Connected to ["+strings[0]+"]");

                Element table = doc.select("table#qosTable").get(0);
                Elements tr = table.select("tr"); //table array size 2
                int skip2 =0;
                for(Element temp : tr) {
                    if(skip2 > 1) {
                        Elements td = temp.select("td");
                        int skip=0;
                        String ip = null, upSpeed = null, downSpeed = null;
                        for (Element temp2 : td) {
                            if(skip == 4 ) { //ip
                                Elements b = temp2.select("b");
                                ip = b.get(0).text();
                                ip = ip.substring(0,ip.indexOf('/'));
                                Log.d("Traffic : ip ", ip);
                                //write code to find this on the database
                            }

                            if (skip == 8) { //upspeed max
                                Elements b = temp2.select("b");
                                upSpeed = b.get(0).text();
                                Log.d("Traffic : upspeed ",upSpeed);
                                //write code to add upspeed of this ip to db
                            }

                            if (skip == 9) { //downspeed max
                                Elements b = temp2.select("b");
                                downSpeed = b.get(0).text();
                                Log.d("Traffic : downspeed",downSpeed);
                                //write code to add downspeed of this ip to db
                            }
                            skip++;
                        }
                        Log.d("Traffic : ","Ip = " + ip +"\nUP = " + upSpeed + "\nDown = " + downSpeed);
                        if (ip != null && upSpeed != null && downSpeed != null) {
                            dbHandler.updateSpeedHandler(ip,Integer.parseInt(upSpeed),Integer.parseInt(downSpeed));
                        }
                    }
                    else skip2++;
                }
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            Log.d("JSwa", " Finished " + trafficControlURL);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("AsyncTask ", "TrafficControlParse Complete");
            adapter.setDevicesList(deviceList);
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
        }
    }
}