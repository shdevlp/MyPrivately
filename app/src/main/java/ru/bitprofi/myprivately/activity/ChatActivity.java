package ru.bitprofi.myprivately.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.software.shell.fab.ActionButton;

import java.util.ArrayList;

import ru.bitprofi.myprivately.OneComment;
import ru.bitprofi.myprivately.R;
import ru.bitprofi.myprivately.User;
import ru.bitprofi.myprivately.adapter.ChatAdapter;
import ru.bitprofi.myprivately.adapter.ChatsAdapter;
import ru.bitprofi.myprivately.adapter.ContactsAdapter;

/**
 * Created by Дмитрий on 05.06.2015.
 */
public class ChatActivity extends ActionBarActivity {
    private ListView _lv_left   = null;
    private ListView _lv_center = null;
    private ListView _lv_right  = null;
    private ImageButton _bt_send = null;
    private EditText _message = null;
    private boolean _left = false;
    private ChatAdapter _adapter = null;

    private void showActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);

            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.actionbar_chat, null);
            ((TextView) v.findViewById(R.id.title_text)).setText(title);
            actionBar.setCustomView(v);

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ab_main_icon_back);

            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainblue)));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        //User usr = (User)intent.getSerializableExtra("User");
        String name = intent.getStringExtra("UserName");
        String status = intent.getStringExtra("UserStatus");
        Integer image = intent.getIntExtra("UserImage", 0);

        showActionBar(name);

        _lv_left = (ListView) findViewById(R.id.lv_left);
        _lv_center = (ListView) findViewById(R.id.lv_center);
        _lv_right = (ListView) findViewById(R.id.lv_right);
        _message = (EditText) findViewById(R.id.et_message);

        _bt_send = (ImageButton) findViewById(R.id.bt_send);
        _bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _left = false;
                _adapter.add(new OneComment(_left, _message.getText().toString()));
                _message.getText().clear();
            }
        });

        _adapter = new ChatAdapter(this);
        _lv_center.setAdapter(_adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
