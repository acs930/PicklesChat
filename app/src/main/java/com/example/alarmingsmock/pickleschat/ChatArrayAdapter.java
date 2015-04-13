package com.example.alarmingsmock.pickleschat;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by xblin_000 on 4/13/2015.
 */
public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    public ChatArrayAdapter(Context context, int textViewResourceId, ChatMessage[] objects){
        super(context, textViewResourceId, objects);

    }

}
