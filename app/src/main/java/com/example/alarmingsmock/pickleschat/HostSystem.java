package com.example.alarmingsmock.pickleschat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import android.net.wifi.p2p.WifiP2pManager.Channel;


/**
 * Created by Joe on 4/3/2015.
 */
public class HostSystem extends BroadcastReceiver implements WifiP2pManager.PeerListListener,WifiP2pManager.ChannelListener {

    private WifiP2pManager mManager;
    private Channel mChannel;

    private static HostSystem ourInstance = new HostSystem();

    public static HostSystem getInstance() {
        return ourInstance;
    }

    public HostSystem() {

    }

    public void setManager(WifiP2pManager manager){
        mManager = manager;
    }

    public void setChannel(Context theContext, Looper theLooper){
        mChannel = mManager.initialize(theContext, theLooper, null);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    @Override
    public void onChannelDisconnected() {

    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {

    }
}
