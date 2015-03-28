package com.example.alarmingsmock.pickleschat;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;

//ughguhgugh
public class MainScreen extends ActionBarActivity {

    private static final String TAG = MainScreen.class.getSimpleName();
    private WifiP2pManager Charlie;
    private boolean isWifiDirectEnabled = false;
    private final IntentFilter intentFilter = new IntentFilter();
    private Channel channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Charlie = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = Charlie.initialize(this, getMainLooper(), null);
    }

    public void onHostClick(View v)
    {
          //int whichButton = v.getId();
        Log.d(TAG, "ID: " + v.getId() + " HOST");


    }

    public void onClientClick(View v)
    {
        Log.d(TAG, "ID: " + v.getId() + " CLIENT");
        Intent intent = new Intent(getApplicationContext(), ClientSelection.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
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
}
