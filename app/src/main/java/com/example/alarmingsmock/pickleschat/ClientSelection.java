package com.example.alarmingsmock.pickleschat;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class ClientSelection extends ActionBarActivity {


    public int[] hostIds;
    private static final String TAG = ClientSelection.class.getSimpleName();
    public ClientSystem deviceClientSystem = new ClientSystem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_selection);
        deviceClientSystem.setManager((WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE));
        deviceClientSystem.setChannel(this, getMainLooper());

      //  new WifiP2pManager.ActionListener();

        //deviceClientSystem.Discovery();

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

    public void DiscoverHosts()
    {
        //populate the host array with different host ids
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
}
