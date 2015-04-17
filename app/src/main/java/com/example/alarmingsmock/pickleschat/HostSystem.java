package com.example.alarmingsmock.pickleschat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.text.format.Formatter;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;


/**
 * Created by Joe on 4/3/2015.
 */
public class HostSystem extends BroadcastReceiver implements WifiP2pManager.PeerListListener,WifiP2pManager.ChannelListener {

    private static final String TAG = Host.class.getSimpleName();
    private WifiP2pManager manager;
    private final IntentFilter intentFilter;
    private Channel channel;
    private boolean isWifiP2pEnabled;
    private BroadcastReceiver receiver;
    private WifiP2pManager.PeerListListener peerListListener;
    private Activity activity;
    public static InetAddress hostAddress = null;

    private WifiP2pGroup myGroup = null;
    private WifiP2pDevice myDevice;

    public String deviceName;
    public String deviceAddress;

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
        //isWifiP2pEnabled = false;
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
                //manager.requestConnectionInfo(channel, (WifiP2pManager.ConnectionInfoListener) activity);


            }
        }
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

    public void createGroup()
    {
        WifiP2pManager.ActionListener tempListener = new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Something worked");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Failed");
            }
        };
        manager.requestGroupInfo(channel, new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup group) {
                myGroup = group;
                Log.d(TAG, myGroup.toString());
            }
        });
        /*if(myGroup != null) {
            manager.removeGroup(channel, tempListener);
            Log.d(TAG, "Woop");
        }*/
        //else {
            manager.createGroup(channel, tempListener);
            Log.d(TAG, "Der it is");
        //}
    }

    public void justGetGroup()
    {
        Collection<WifiP2pDevice> allStuff = myGroup.getClientList();
        Object[] array = allStuff.toArray();
        for(int i = 0; i < array.length; i++)
        {
            System.out.println(array[i].toString());
        }
    }

    public void sendToAll()
    {



    }



    public void getGroupInfo()
    {
        manager.requestGroupInfo(channel, new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup group) {
                myGroup = group;
                myDevice = group.getOwner();
                Log.d(TAG, myDevice.toString());
            }
        });
    }

    public void setAddress()
    {
        Log.d(TAG, "IAMHERE");
        try {
            hostSocket = new ServerSocket(SERVERPORT); //Might not wanna do this til I know my IP
            Log.d(TAG, "Server socket made");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setActivity(Activity currentActivity)
    {
        activity = currentActivity;
    }

    public void onConnectionInfoAvailable(final WifiP2pInfo info) {
        //InetAddress groupOwnerAddress = info.groupOwnerAddress.getHostAddress();

        System.out.println("in connection info available");
        if (info.groupFormed && info.isGroupOwner) {
            //This always runs cause I'm a host
            // make a new socket add to list, associate with the client so can always recognize
            // or maybe not and just be able to broadcast out to everyhting
            try {
                clientSockets.add(hostSocket.accept());
                System.out.println("Client Sock got");
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

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
            System.out.println(peers.toString());
    }

    /*things host needs to do in background:

     */

    public void serverSocketThread()
    {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Socket currentClient;
                DataInputStream dataInputStream = null;
                DataOutputStream dataOutputStream = null;

                try {
                    while(true) {
                        currentClient = hostSocket.accept();


                        dataInputStream = new DataInputStream(
                                currentClient.getInputStream());

                        String message;

                        message = dataInputStream.readUTF();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getAllClientsConnected()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket currentClient = null;

                while (true)
                {
                    for(int i =0; i< clientSockets.size(); i++)
                    {
                        System.out.println("Ohohohohoho");
                    }
                }

            }
        }).start();

        //This is where we decided to jjust to a demo


    }



    public class socketMagic extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            try {
                clientSockets.add(hostSocket.accept());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    public class updateChatAll extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            for (int i = 0 ; i < clientSockets.size(); i++)
            {
               // hostSocket.  clientSockets.get(i);
            }
            return null;
        }

    }

    private class HostAsyncTasks extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String... params) {
            //theServer = new HostSystem();//HostSystem.getInstance();
            //theServer.setAddress();
            //theServer.setManager((WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE));
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(TAG, "UGHHGHGHGHGHG");
            super.onPostExecute(s);
        }

        protected void onProgressUpdate()
        {}

    }

    @Override
    public void onChannelDisconnected() {

    }
}


