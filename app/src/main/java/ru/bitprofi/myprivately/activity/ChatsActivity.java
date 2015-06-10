package ru.bitprofi.myprivately.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.rtp.AudioGroup;
import android.net.rtp.AudioStream;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ru.bitprofi.myprivately.R;
import ru.bitprofi.myprivately.User;
import ru.bitprofi.myprivately.adapter.ChatsAdapter;
import ru.bitprofi.myprivately.adapter.DbAdapter;
import ru.bitprofi.myprivately.iface.INewMessageListener;
import ru.bitprofi.myprivately.iface.ISipEventListener;
import ru.bitprofi.myprivately.sip.SipEvent;
import ru.bitprofi.myprivately.sip.SipRegister;
import ru.bitprofi.myprivately.sip.SipSendMessage;
import ru.bitprofi.myprivately.sip.SipStackAndroid;


public class ChatsActivity extends ActionBarActivity implements ISipEventListener {
    private ListView _lvChats = null;

    public static final String USER_ID = "Privately.USER_ID";

    public static DbAdapter dbHelper;
    public static INewMessageListener msgListener = null;

    String usr;
    String psw;
    String srv;


    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);

            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.actionbar_title, null);
            //((TextView)v.findViewById(R.id.title_text)).setText(title);
            actionBar.setCustomView(v);

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_navigation_menu);

            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainblue)));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        Intent intent = getIntent();
        String login = intent.getStringExtra("login");
        String password = intent.getStringExtra("password");

        showActionBar();

        // Database
        dbHelper = new DbAdapter(this);
        dbHelper.open();

        //PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        refreshCreds();
        doRegister();


        _lvChats = (ListView) findViewById(R.id.lvChats);

        ArrayList<User> users = new ArrayList<User>();
        for (int i = 0; i < 100; i++) {
            users.add(new User(this, "+7901100000" + String.valueOf(i + 1), R.drawable.cc_no_avatar_big));
        }

        ChatsAdapter uAdapter = new ChatsAdapter(this, users);
        _lvChats.setAdapter(uAdapter);
        _lvChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User usr = (User) parent.getItemAtPosition(position);

                Intent myIntent = new Intent(getBaseContext(), ChatActivity.class);
                myIntent.putExtra("UserName", usr.getName());
                myIntent.putExtra("UserStatus", usr.getStatusString());
                myIntent.putExtra("UserImage", usr.getImage());
                startActivity(myIntent);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                Intent intent = new Intent(this, ContactsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSipMessage(SipEvent sipEventObject) {

        if (sipEventObject.getType() == SipEvent.SipEventType.MESSAGE) {
            final String text = sipEventObject.getContent();
            final String to = usr;

            String from = sipEventObject.getFrom();
            from = from.split("@")[0].split(":")[1];
            final String finalFrom = from;

            this.runOnUiThread(new Runnable() {
                public void run() {
                    dbHelper.createMessage(text, finalFrom, to);
                    if (msgListener != null) {
                        msgListener.onMessage(finalFrom);
                    }
                }
            });

        } else if (sipEventObject.getType() == SipEvent.SipEventType.BYE) {
        }
    }

    public void refreshCreds() {
        /*
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        usr  = sharedPref.getString(SettingsActivity.PREF_LOG, "");
        psw  = sharedPref.getString(SettingsActivity.PREF_PSW, "");
        srv  = sharedPref.getString(SettingsActivity.PREF_SRV, "");
        */
    }

    // Main actions
    public void doRegister() {
        SipStackAndroid.getInstance().execute(usr, psw, srv);
        SipStackAndroid.getInstance().addSipListener(this);
        new SipRegister().execute(usr, psw, srv);
    }

    public static void doMessage(String usr, String text) {
        String to = "sip:" + usr + "@5.9.201.234";
        new SipSendMessage().execute(to, text);
    }

}
