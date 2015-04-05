package com.example.alarmingsmock.pickleschat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.os.Looper;
import android.util.Log;
import java.net.InetAddress;
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
    //private WifiP2pManager.ConnectionInfoListener connectionListener;
    private Activity activity;  //This is the activity the object is currently in, should be set each time the context is changed if possible

    private static ClientSystem ourInstance = new ClientSystem();

    public static ClientSystem getInstance() {
        return ourInstance;
    }

    public ClientSystem() {
        intentFilter = new IntentFilter();
        receiver = null;
        isWifiP2pEnabled = false;
        peers = new ArrayList();
        peerListListener = new WifiP2pManager.PeerListListener(){
            @Override
            public void onPeersAvailable(WifiP2pDeviceList connectableDevices) //when do I run, everytime a callback for peerlist change is found?
            {
                peers.clear();
                peers.addAll(connectableDevices.getDeviceList());

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
    public void setActivity(Activity currentActivity)
    {
        activity = currentActivity;
    }


    public void setChannel(Context theContext, Looper theMainLoop)
    {
       channel =  manager.initialize(theContext,theMainLoop, null);
    }

    //This manually calls the request the current peer list
    public void discovery()
    {
        manager.requestPeers(channel, peerListListener);
    }
    public List getHosts()
    {
        return peers;
    }


    public void onPeersAvailable(WifiP2pDeviceList connectableDevices)
    {
        Log.d(TAG, "AM I RUNNING THIS, I shoudln't be!!");
    }

    public void onChannelDisconnected()
    {

    }
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();

        if(manager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            //check if wifi p2p is on
            int state = intent.getIntExtra(manager.EXTRA_WIFI_STATE, -1);
            if (state == manager.WIFI_P2P_STATE_ENABLED) {
                this.setIsWifiP2pEnabled(false);
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
                manager.requestConnectionInfo(channel, (ConnectionInfoListener) activity);


            }
        }
    }

    public void connect(WifiP2pDevice chosenDevice)
    {

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = chosenDevice.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        manager.connect(channel, config, new ActionListener() {
            @Override
            public void onSuccess() {
                //Wifibroadcast notifys
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Connection failed, output result to screen");
            }
        } );

    }

    //@Override
    public void onConnectionInfoAvailable(final WifiP2pInfo info)
    {
        //String groupOwnerAddress =  info.groupOwnerAddress.getHostAddress();
        if(info.groupFormed && info.isGroupOwner)
        {
            //This won't ever run cause I'm a client
        }
        else if(info.groupFormed) {
            //Create client thread to connect to group owner
        }

    }

    //Need to find the owner intent number of all the devices
    //


}
