package com.example.alarmingsmock.pickleschat;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Switch;

import java.util.List;

public class Client extends Service {
    private static final String TAG = ClientSelection.class.getSimpleName();
    public ClientSystem theClient;

    private Intent thisIntent;
    public String selectedHost;

    private final IBinder clientBinder = new LocalBinder();

   /* BroadcastReceiver hostReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.d(TAG, intent.toString());
            Log.d(TAG, "BROADCAST GOT");
            selectedHost = intent.getStringExtra("deviceAddress");

        }
    };*/


    public Client() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return clientBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //Log.d(TAG, "TEST");
        //new clientAsync().execute();

       Log.d(TAG, intent.getStringExtra("deviceAddress").toString());
        selectedHost = intent.getStringExtra("deviceAddress").toString();
        System.out.println("Gah: " + selectedHost);

        thisIntent = intent;
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        theClient = new ClientSystem();
        theClient.setManager((WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE));
        theClient.setChannel(this, getMainLooper());
        IntentFilter filter = new IntentFilter();
        //filter.addAction(Intent.ACTION_SCREEN_ON);
        //registerReceiver(hostReceiver, filter);

    }

    @Override
    public void onDestroy()
    {
        //Won't get destroyed
        //unregisterReceiver(hostReceiver);

    }

    //Use this to search for people to connect to from activities
    public List Discover()
    {
        List<WifiP2pDevice> hostIds;
        theClient.discovery();
        hostIds = theClient.getHosts();

        return hostIds;
    }

    public void Connect()
    {
        String address = "deviceAddress";

        //Log.d(TAG, selectedHost);

        System.out.println("Huh: " + selectedHost);
        new clientConnect().execute(selectedHost);
    }


    public class LocalBinder extends Binder
    {
        Client getService()  //This returns the Client so methods can be called
        {
            return Client.this;
        }
    }


    //A bunch of async tasks to do each thing I need

    public class clientConnect extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... Params)
        {
            //Log.d(TAG, "Ugh");
            for(int i= 0; i < Params.length; i ++)
            {
                System.out.println(i + " " + Params[i]);
            }
            String deviceAddress = Params[0];
            //Log.d(TAG, selectedHost);
            System.out.println("host: " + deviceAddress);
            theClient.connect(selectedHost);
            return null;
        }
    }

    //The device addreess getting is messed up
    //Then connect will be messed up als
    // Might need to get the address instead of name(currently gets device name form the list)
    
    public class ClientConnectTask extends AsyncTask<String, Integer, Void>
    {

        @Override
        protected Void doInBackground(String... Action) {

            String actionCheck = Action[0];
            switch(actionCheck)
            {
                case "Ugh":
                    break;
                case "Connect":
                    break;
                case "Send":
                    break;
                case "Receive":
                    break;
                case "Other":
                    break;
            }

            return null;
        }


        protected void onPostExecute(String s) {
            Log.d(TAG, "UGHHGHGHGHGHG");
            //super.onPostExecute(s);
        }

        protected void onProgressUpdate()
        {}

    }
}
