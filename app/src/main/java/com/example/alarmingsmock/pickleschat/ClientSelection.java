package com.example.alarmingsmock.pickleschat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class ClientSelection extends Activity {


    private List<WifiP2pDevice> hostIds;
    private static final String TAG = ClientSelection.class.getSimpleName();
    Client clientService;
    boolean mBound = false;
    Intent clientIntent;
    String selectedDeviceAddress;

    //WifiP2pDevice selectedDevice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_selection);

    }

    protected void onStart(){
        super.onStart();
        clientIntent = new Intent(this, Client.class);
        bindService(clientIntent, mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onDiscoverClick(View v)
    {
        hostIds = clientService.Discover();
        getTrueHosts();
        updateHostList();

    }

    public void onHostClick(View v)
    {
        final Intent connectIntent = new Intent(this, Client.class);
        final String deviceAddress = getSelectedDevice();//.deviceAddress;
        final CountDownLatch latch = new CountDownLatch(1);
        connectIntent.putExtra("deviceAddress", deviceAddress);
        new Thread(new Runnable() {
            @Override
            public void run() {
                startService(connectIntent);
                Log.d(TAG, "OLAH");
                latch.countDown();
                //Log.d(TAG, "UGHGHGHGGH");
            }
        }).start();

        final CountDownLatch newLatch = new CountDownLatch(1);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                clientService.Connect();
                latch.countDown();

            }
        }).start();
        //stopService(new Intent(this, Client.class));
        //Log.d(TAG, deviceAddress);
        //bindService(connectIntent, mConnection, Context.BIND_AUTO_CREATE);


        //Intent intent = new Intent(getApplicationContext(), LobbyMain.class);
        //startActivity(intent);
    }

    public void updateHostList()
    {
        RadioGroup myGroup = ((RadioGroup) findViewById((R.id.radios)));

        myGroup.removeAllViews();

        /*RadioButton testButton = new RadioButton(this);
        myGroup.addView(testButton);
        testButton.setText("This is a test"); //get rid of in final copy*/



        for(WifiP2pDevice device: hostIds)
        {
            RadioButton newButton = new RadioButton(this);
            newButton.setText((device.deviceName));
            myGroup.addView(newButton);
        }
    }

    public String getSelectedDevice()
    {
        RadioGroup myGroup = ((RadioGroup) findViewById((R.id.radios)));

        int id = myGroup.getCheckedRadioButtonId();
        Log.d(TAG, id + " this is the id");

        WifiP2pDevice selectedDevice = null;
        View radioButton = myGroup.findViewById(id);
        //int radioId = myGroup.indexOfChild(radioButton);
        RadioButton btn = (RadioButton) findViewById( myGroup.getCheckedRadioButtonId());
        String selection = (String) btn.getText();
        myGroup.getCheckedRadioButtonId();
        System.out.println(selection);

        for(int i = 0; i < hostIds.size(); i++)
        {
            //Log.d(TAG,hostIds.get(i).deviceName);
            System.out.println("LOLO " + hostIds.get(i).deviceName);
            if(hostIds.get(i).deviceName.equals(selection))
                selectedDevice = hostIds.get(i);
        }

        System.out.println(selectedDevice.toString());
        //Log.d(TAG, selectedDevice.toString());
        //Log.d(TAG, selection);


        //This is where I am  needs to check what in the if statement to chekc
        //After this pass it throught he bundle and then connect to the address
        //Then things should work
        //Then work on getting a thing to be sent

        selectedDeviceAddress = selectedDevice.deviceAddress;
        return selectedDeviceAddress;

    }

    public void getTrueHosts()
    {
        /*for(WifiP2pDevice cDevice : hostIds)
        {
            if(!cDevice.isGroupOwner())
            {
                hostIds.remove(cDevice);
            }
        }*/

    }


    //This sets up the binding so I can call functions
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Client.LocalBinder binder = (Client.LocalBinder) service;
            clientService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}


/*
SOOOOOOOOO
I'm at the point where I should establish a connection between the clietn and the host
we'll see how this turns out
After that we need to send data back and forth from the host to client and vice versa
maybe that bit won't be tooooooo crazy
I have tomorrow (kinda) to work on it
But mostly Thursday (gona be a late nighter)

 */
