package com.example.alarmingsmock.pickleschat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlarmingSmock on 3/30/15.
 */
public class ClientSystem implements WifiP2pManager.PeerListListener,ChannelListener {

    private static final String TAG = ClientSelection.class.getSimpleName();
    private WifiP2pManager manager;
    private final IntentFilter intentFilter;
    private Channel channel;
    private boolean isWifiP2pEnabled;
    private BroadcastReceiver receiver;
    private List<WifiP2pDevice> peers;
    private WifiP2pManager.PeerListListener peerListListener;


    private static ClientSystem ourInstance = new ClientSystem();

    public static ClientSystem getInstance() {
        return ourInstance;
    }

    public ClientSystem() {
        intentFilter = new IntentFilter();
        receiver = null;
        isWifiP2pEnabled = false;
        peers = new ArrayList();
        peerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peerList) {
                peers.clear();
                peers.addAll(peerList.getDeviceList());

                if(peers.size() == 0)
                {
                    Log.d(TAG, "No Devices Found");
                }

            }
        };

    }

    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    public void setManager(WifiP2pManager contextService)
    {
        manager = contextService;

    }

    public void setChannel(Context theContext, Looper theMainLoop)
    {
       channel =  manager.initialize(theContext,theMainLoop, null);
    }

    public boolean Discovery()
    {
        //manager.discoverPeers(channel, new WifiP2pManager.ActionListener())

        return true;
    }

    public void onPeersAvailable(WifiP2pDeviceList connectableDevices)
    {

    }

    public void onChannelDisconnected()
    {

    }

}
