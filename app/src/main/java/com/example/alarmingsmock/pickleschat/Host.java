package com.example.alarmingsmock.pickleschat;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class Host extends Service {

    private static final String TAG = ClientSelection.class.getSimpleName();
    private HostSystem theHost;
    private final IBinder hostBinder = new LocalBinder();

    public class LocalBinder extends Binder
    {
        //This returns the Host so methods can be called
        Host getService()
        {
            return Host.this;
        }
    }
    public Host() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return hostBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d(TAG, "TEST");

        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        theHost = new HostSystem();
        theHost.setManager((WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE));
        theHost.setChannel(this, getMainLooper());

    }

    @Override
    public void onDestroy()
    {}

    public void runSetHost()
    {
        new setHostAddress().execute();
    }

    public void runCreateGroup()
    {
        new createWifiGroup().execute();
    }

    public void runGetGroupInfo()
    {
        new getGroupInfo().execute();
    }

    public class setHostAddress extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "MADE it to async");
            theHost.setAddress();
            return null;
        }
    }

    public class createWifiGroup extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params){
            theHost.createGroup();
            return null;
        }
    }

    public class getGroupInfo extends  AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params){
            theHost.getGroupInfo();
            return null;
        }
    }




}
