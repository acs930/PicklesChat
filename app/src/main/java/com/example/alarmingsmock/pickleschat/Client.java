package com.example.alarmingsmock.pickleschat;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
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

    private final IBinder clientBinder = new LocalBinder();


    public Client() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return clientBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d(TAG, "TEST");
        //new clientAsync().execute();

        thisIntent = intent;
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        theClient = new ClientSystem();
        theClient.setManager((WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE));
        theClient.setChannel(this, getMainLooper());

    }

    @Override
    public void onDestroy()
    {
        //Won't get destroyed
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
        String value = null;
        if (thisIntent !=null && thisIntent.getExtras()!=null) {
            value = thisIntent.getExtras().getString(address);
        }

        new clientConnect().execute(value);
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
            String deviceAddress = Params[0];
            Log.d(TAG, deviceAddress);
            theClient.connect(deviceAddress);
            return null;
        }
    }
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
