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

import java.util.ArrayList;

import ru.bitprofi.myprivately.R;
import ru.bitprofi.myprivately.User;
import ru.bitprofi.myprivately.adapter.ChatsAdapter;


public class ChatsActivity extends ActionBarActivity {
    private ListView _lvChats = null;

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
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_action_navigation_menu);

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

        _lvChats = (ListView) findViewById(R.id.lvChats);

        ArrayList<User> users = new ArrayList<User>();
        users.add(new User(this, "101", R.mipmap.cc_no_avatar_big));
        users.add(new User(this, "102", R.mipmap.cc_no_avatar_big));
        users.add(new User(this, "103", R.mipmap.cc_no_avatar_big));
        users.add(new User(this, "104", R.mipmap.cc_no_avatar_big));
        users.add(new User(this, "105", R.mipmap.cc_no_avatar_big));
        users.add(new User(this, "106", R.mipmap.cc_no_avatar_big));

        ChatsAdapter uAdapter = new ChatsAdapter(this, users);
        _lvChats.setAdapter(uAdapter);
        _lvChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
}
