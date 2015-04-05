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
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;


public class ClientSelection extends Activity {


    private List<WifiP2pDevice> hostIds;
    private static final String TAG = ClientSelection.class.getSimpleName();
    private ClientSystem deviceClientSystem = new ClientSystem();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        RadioGroup myGroup = ((RadioGroup) findViewById((R.id.radios)));
        Log.d(TAG, "Test: " + myGroup.getCheckedRadioButtonId());

        /*
        Need to make sure that the checked radio button can be used as the id for finding the device from hostIds list
        WifiP2pDevice selectedDevice = hostIds.get(myGroup.getCheckedRadioButtonId());
        deviceClientSystem.connect(selectedDevice);*/

        //Connect to the selected host, get the id of the host button and run the wifi connect fucntion
        Intent intent = new Intent(getApplicationContext(), LobbyMain.class);
        startActivity(intent);

    }

    public void updateHostList()
    {
        RadioGroup myGroup = ((RadioGroup) findViewById((R.id.radios)));

        //myGroup.removeAllViews();

       RadioButton testButton = new RadioButton(this);
        myGroup.addView(testButton);
        testButton.setText("This is a test");


        for(WifiP2pDevice device: hostIds)
        {
            RadioButton newButton = new RadioButton(this);
            newButton.setText((device.deviceName));
            myGroup.addView(newButton);
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


    /* This is going to need to run the discover peers function from the client system class
        to populate the inital list of host connections
        Also gonna need to filter out other client devices and only view those acting as hosts
        refresh will run the refresh thing and see if any other clietns are available
        connect runs the connect to a selected device that was clicked on by the user
     */
}
