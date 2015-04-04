package com.example.alarmingsmock.pickleschat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;


public class ClientSelection extends ActionBarActivity {


    private List<WifiP2pDevice> hostIds;
    private static final String TAG = ClientSelection.class.getSimpleName();
    public ClientSystem deviceClientSystem = new ClientSystem();
    private RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radioGroup = (RadioGroup) findViewById(R.id.radiobuttons);
        setContentView(R.layout.activity_client_selection);
        deviceClientSystem.setManager((WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE));
        deviceClientSystem.setChannel(this, getMainLooper());
        //deviceClientSystem.setActivity((Activity) this.getApplicationContext());
        hostIds = deviceClientSystem.getHosts();
        getTrueHosts();
        updateHostList();
        //Log.d(TAG, )



       //array of crap to show = deviceClientSystem.getPossiblehosts;

        //We need to launch a discovery fuinction and populate the layout with buttons tht corresposnsd to the ids of the host!!!
        //Fpor now this will be a single button


    }


    public void onHostClick(View v)
    {
        //int whichButton = v.getId();
        Log.d(TAG, "ID: " + v.getId() + " ButtonClicked");
        //Connect to the selected host, get the id of the host button and run the wifi connect fucntion
        Intent intent = new Intent(getApplicationContext(), LobbyMain.class);
        startActivity(intent);

    }

    public void updateHostList()
    {
        //radioGroup.clearCheck();
        for(WifiP2pDevice device: hostIds)
        {
            RadioButton newButton = new RadioButton(this);
            newButton.setText((device.deviceName));
            radioGroup.addView(newButton);
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

    public void onDiscoverClick()
    {
        deviceClientSystem.discovery();
        hostIds = deviceClientSystem.getHosts();
        getTrueHosts();
        updateHostList();

    }

    public void getTrueHosts()
    {
        for(WifiP2pDevice cDevice : hostIds)
        {
            if(!cDevice.isGroupOwner())
            {
                hostIds.remove(cDevice);
            }
        }

    }


    public void refresh()
    {
        //This will run the discovery thing
        //add ids to the array
        //and draw them to the screen every time the function is run
    }

    public boolean ConnectToHost()
    {
        return true;
    }

    /* This is going to need to run the discover peers function from the client system class
        to populate the inital list of host connections
        Also gonna need to filter out other client devices and only view those acting as hosts
        refresh will run the refresh thing and see if any other clietns are available
        connect runs the connect to a selected device that was clicked on by the user
     */
}
