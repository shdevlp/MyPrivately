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
import android.widget.Toast;

import java.util.ArrayList;

import ru.bitprofi.myprivately.GlobalSettings;
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
    private ListView m_lv_chats = null;

    public static DbAdapter m_db_helper;
    public static INewMessageListener m_msg_listener = null;

    private String m_current_user;
    private String m_current_user_password;

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);

            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.actionbar_title, null);
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
        m_current_user = intent.getStringExtra("login");
        m_current_user_password = intent.getStringExtra("password");

        showActionBar();

        // Database
        m_db_helper = new DbAdapter(this);
        m_db_helper.open();


        m_lv_chats = (ListView) findViewById(R.id.lvChats);

        ArrayList<User> users = GlobalSettings.getInstance().getUsers();
        //Убираем пользователя который зарегистрировался
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getName().equals(m_current_user)) {
                users.remove(i);
                break;
            }
        }
        ChatsAdapter uAdapter = new ChatsAdapter(this, users);
        m_lv_chats.setAdapter(uAdapter);
        m_lv_chats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User target_user = (User) parent.getItemAtPosition(position);

                Intent myIntent = new Intent(getBaseContext(), ChatActivity.class);
                myIntent.putExtra("CurrentUserName", m_current_user);
                myIntent.putExtra("TargetUserName", target_user.getName());

                myIntent.putExtra("UserStatus", target_user.getStatusString());
                myIntent.putExtra("UserImage", target_user.getImage());
                startActivity(myIntent);
            }
        });

        doRegister();
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
                intent.putExtra("CurrentUserName", m_current_user);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSipMessage(SipEvent sipEventObject) {
        if (sipEventObject.getType() == SipEvent.SipEventType.MESSAGE) {
            final String text = sipEventObject.getContent();
            final String to = m_current_user;

            String from = sipEventObject.getFrom();
            from = from.split("@")[0].split(":")[1];
            final String finalFrom = from;

            Toast.makeText(getApplicationContext(), "From:" + from + " to:"+ to + " Message:" + text,
                    Toast.LENGTH_SHORT).show();

            this.runOnUiThread(new Runnable() {
                public void run() {
                    m_db_helper.createMessage(text, finalFrom, to);
                    if (m_msg_listener != null) {
                        m_msg_listener.onMessage(finalFrom);
                    }
                }
            });

        } else if (sipEventObject.getType() == SipEvent.SipEventType.BYE) {
        }
    }

    private void doRegister() {
        SipStackAndroid.getInstance().addSipListener(this);
        SipStackAndroid.getInstance().execute(m_current_user, "12345", "5.9.201.234");
        new SipRegister().execute(m_current_user, "12345", "5.9.201.234");
    }

    public static void doMessage(String usr, String text) {
        String to = "sip:" + usr + "@5.9.201.234";
        new SipSendMessage().execute(to, text);
    }
}