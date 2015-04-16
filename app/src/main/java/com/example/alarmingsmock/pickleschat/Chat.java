package com.example.alarmingsmock.pickleschat;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class Chat extends ActionBarActivity {


    private ChatArrayAdapter adp;
    private ListView list;
    private EditText chatText;
    private Button sendButton;
    Intent in;
    private boolean side = false;

    private static final String TAG = ClientSelection.class.getSimpleName();

    public Chat() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent i = getIntent();


        sendButton=(Button)findViewById(R.id.sendButton);

        list = (ListView)findViewById(R.id.listView);

        adp = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat);

        chatText =(EditText)findViewById(R.id.chat);

    }

    public void onLeaveClick(View v)
    {
        //int whichButton = v.getId();
        Log.d(TAG, "ID: " + v.getId() + " ButtonClicked");
        //Send a message to the server saying that the client has left the room
        //Delete all stuff related to game that was stored locally
        Intent intent = new Intent(getApplicationContext(), LobbyMain.class);
        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
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
