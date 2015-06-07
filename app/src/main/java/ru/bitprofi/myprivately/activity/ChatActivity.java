package ru.bitprofi.myprivately.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private ListView _lvLeft = null;
    private ListView _lvCenter = null;
    private ListView _lvRight = null;

    private void showActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);

            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.actionbar_title, null);
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
        User usr = (User)intent.getSerializableExtra("User");

        showActionBar(usr.getName());

        _lvLeft = (ListView) findViewById(R.id.lvLeft);
        _lvCenter = (ListView) findViewById(R.id.lvCenter);
        _lvRight = (ListView) findViewById(R.id.lvRight);

        ChatAdapter uAdapter = new ChatAdapter(this, R.drawable.cc_no_avatar_big);
        for (int i = 0; i < 40; i++) {
            uAdapter.add(new OneComment(false, null, true));
        }
        _lvCenter.setAdapter(uAdapter);
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
