package com.example.alarmingsmock.pickleschat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;


/**
 * Created by Joe on 4/3/2015.
 */
public class HostSystem extends BroadcastReceiver implements WifiP2pManager.PeerListListener,WifiP2pManager.ChannelListener {

    private static final String TAG = ClientSelection.class.getSimpleName();
    private WifiP2pManager manager;
    private final IntentFilter intentFilter;
    private Channel channel;
    private boolean isWifiP2pEnabled;
    private BroadcastReceiver receiver;
    private WifiP2pManager.PeerListListener peerListListener;
    private Activity activity;
    public static InetAddress hostAddress = null;

    Thread serverThread;
    private ServerSocket hostSocket;
    private List<Socket> clientSockets;

    private static final int SERVERPORT = 5000;

    private static HostSystem ourInstance = new HostSystem();

    public static HostSystem getInstance() {
        return ourInstance;
    }

    public HostSystem() {
        intentFilter = new IntentFilter();
        receiver = null;
        isWifiP2pEnabled = false;
    }

    public void setAddress()
    {
        try {
            hostAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            hostSocket = new ServerSocket(6000, 0, hostAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();

        if(manager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            //check if wifi p2p is on
            int state = intent.getIntExtra(manager.EXTRA_WIFI_STATE, -1);
            if (state == manager.WIFI_P2P_STATE_ENABLED) {
                this.setIsWifiP2pEnabled(true);
            } else {
                this.setIsWifiP2pEnabled(false);
            }
        }
        else if(manager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
            //Peer list changed
            if (manager != null) {
                manager.requestPeers(channel, peerListListener);
            }
            Log.d(TAG, "P2P peers changed");


        }
        else if (manager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            if(manager == null)
            {
                return;
            }
            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if(networkInfo.isConnected())
            {
                //Connected to other device
                //request connection info to get group owner IP
                manager.requestConnectionInfo(channel, (WifiP2pManager.ConnectionInfoListener) activity);


            }
        }
    }

    @Override
    public void onChannelDisconnected() {

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

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {

    }

    public void setActivity(Activity currentActivity)
    {
        activity = currentActivity;
    }

    public void onConnectionInfoAvailable(final WifiP2pInfo info) {
        //InetAddress groupOwnerAddress = info.groupOwnerAddress.getHostAddress();


        if (info.groupFormed && info.isGroupOwner) {
            //This always runs cause I'm a host
            // make a new socket add to list, associate with the client so can always recognize
            // or maybe not and just be able to broadcast out to everyhting
            try {
                clientSockets.add(hostSocket.accept());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String getWifiStatus()
    {
        if(isWifiP2pEnabled)
            return "true";
        else
            return "false";
    }
}
