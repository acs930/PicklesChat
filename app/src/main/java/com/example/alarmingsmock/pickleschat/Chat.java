package com.example.alarmingsmock.pickleschat;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View.OnKeyListener;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.database.DataSetObserver;

import java.util.concurrent.CountDownLatch;
import java.util.logging.LogRecord;

public class Chat extends ActionBarActivity {


    private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    int count = 0;

    Intent intent;
    private boolean side = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        setContentView(R.layout.activity_chat);

        buttonSend = (Button) findViewById(R.id.sendButton);

        listView = (ListView) findViewById(R.id.listView);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_message);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.chat);
        chatText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }

    private boolean sendChatMessage(){
        chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
        chatText.setText("");
        side = !side;
        return true;
    }

    private boolean sendChatMessage(String theMessage){
        chatArrayAdapter.add(new ChatMessage(side, theMessage));
        chatText.setText("");
        side = !side;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
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

    public void runDemo(View v)
    {
        String[] words = new String[5];
        words[0] = "Hello";
        words[1] = "Testing, test";
        words[2] = "Oh well look there";
        words[3] = "~(*_*)~";
        words[4] = "LOLOLOLOLOLOL";

        if(count >= 4)
            count = 0;

        sendChatMessage(words[count]);

        count++;


        // }
    }

    public void runWait()
    {

        new runWait().execute();
    }


    public class runWait extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params){
            sendChatMessage("LOLOLOLOLOLOL");
            return null;
        }
    }
}
