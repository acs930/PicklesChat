package com.example.alarmingsmock.pickleschat;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class LobbyMain extends ActionBarActivity {


    private static final String TAG = ClientSelection.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_main);
    }

    public void onGameClick(View v)
    {
        //int whichButton = v.getId();
        Log.d(TAG, "ID: " + v.getId() + " Game");
        //Birngs to game Selceitjoisn screen nothing sent to host
        Intent intent = new Intent(getApplicationContext(), Game.class);
        startActivity(intent);

    }

    public void onChatClick(View v)
    {
        //int whichButton = v.getId();
        Log.d(TAG, "ID: " + v.getId() + " Chat");
        //Connect to the selected host, get the id of the host button and run the wifi connect fucntion
        Intent intent = new Intent(getApplicationContext(), Chat.class);
        startActivity(intent);

    }

    public void onExitClick(View v)
    {
        Log.d(TAG, "ID: " + v.getId() + " Exit");
        //Go back to the main screen
        //Run the disconnect from host function
        //Erase all stuff that was stored locally
        Intent intent = new Intent(getApplicationContext(), MainScreen.class);
        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lobby_main, menu);
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


//Still need to unbind and rebind service(or at least unbind)