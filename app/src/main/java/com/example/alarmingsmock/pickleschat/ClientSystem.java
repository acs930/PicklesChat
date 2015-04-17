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
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlarmingSmock on 3/30/15.
 */
public class ClientSystem implements WifiP2pManager.PeerListListener,ChannelListener,ConnectionInfoListener {

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
    private Socket clientSocket;
    public boolean isSearching = false;
    private WifiP2pGroup myGroup = null;
    private WifiP2pDevice owner = null;

    private static final int SERVERPORT = 5000;

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
        if(!isSearching)
        {
            manager.discoverPeers(channel, new ActionListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Peers available!");
                }

                @Override
                public void onFailure(int reason) {

                }
            });
            isSearching = true;
        }
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
                Log.d(TAG, "Requesting info");
                manager.requestConnectionInfo(channel, (ConnectionInfoListener) activity);


            }
        }
    }

    public void clientGetGroupInfo()
    {
        manager.requestGroupInfo(channel, new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup group) {
                myGroup = group;
                owner = group.getOwner();
                System.out.println(owner.toString());
            }
        });
    }


    public void connect(String chosenDevice)
    {
        System.out.println(chosenDevice);
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = chosenDevice;
        config.wps.setup = WpsInfo.PBC;
        //System.out.println("HELLO");


        manager.connect(channel, config, new ActionListener() {
            @Override
            public void onSuccess() {
                //Wifibroadcast notifys
               // Log.d(TAG,);
                System.out.println("Look Pigs are flying");

            }

            @Override
            public void onFailure(int reason) {
                //Log.d(TAG, "Connection failed, output result to screen");
                System.out.println("Connect failed, but it tried");

            }
        } );//*/

    }

    //@Override
    //This should create a thread on the client when a connection to the host is found and chosen
    public void onConnectionInfoAvailable(final WifiP2pInfo info)
    {
        String hostName =  info.groupOwnerAddress.getHostName();

        if(info.groupFormed && info.isGroupOwner)
        {
            //This won't ever run cause I'm a client
            Log.d(TAG, "Why am I running?");
        }
        else if(info.groupFormed) {
            //Create client thread to connect to group owner
            try {
                Log.d(TAG, "Opening socket up to host");
                clientSocket = new Socket(hostName, SERVERPORT);

            } catch (IOException e) {
                e.printStackTrace();
            }

            new Thread((Runnable) clientSocket);
        }

    }

    public class socketMagic extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {

            try {
                clientSocket.connect((new InetSocketAddress(owner.deviceAddress, SERVERPORT))) ;
                System.out.println("Connecting to host");
            } catch (IOException e) {
                e.printStackTrace();
            }
               return null;
        }

    }

    public class sendDataOverSocket extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {

            
            return null;
        }
    }


    public String getWifiStatus()
    {
        if(isWifiP2pEnabled)
            return "true";
        else
            return "false";
    }

    //Need to find the owner intent number of all the devices
    //

    /* Things client needs to do in background

     */



}
