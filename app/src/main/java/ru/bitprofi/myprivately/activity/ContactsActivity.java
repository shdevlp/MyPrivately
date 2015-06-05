package ru.bitprofi.myprivately.activity;

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

import ru.bitprofi.myprivately.R;
import ru.bitprofi.myprivately.User;
import ru.bitprofi.myprivately.adapter.ContactsAdapter;

/**
 * Created by Дмитрий on 05.06.2015.
 */
public class ContactsActivity extends ActionBarActivity {
    private ListView _lvContacts = null;
    // And then find it within the content view:
    private ActionButton _abEditChat = null;

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);

            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.actionbar_title, null);
            ((TextView)v.findViewById(R.id.title_text)).setText("Контакты");
            actionBar.setCustomView(v);

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ab_main_icon_back);

            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainblue)));
        }

    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_contacts);

            showActionBar();

            _abEditChat = (ActionButton) findViewById(R.id.abEditChat);
            _lvContacts = (ListView) findViewById(R.id.lvContacts);

            ArrayList<User> users = new ArrayList<User>();
            users.add(new User(this, "101", R.drawable.cc_no_avatar_big));
            users.add(new User(this, "102", R.drawable.cc_no_avatar_big));
            users.add(new User(this, "103", R.drawable.cc_no_avatar_big));
            users.add(new User(this, "104", R.drawable.cc_no_avatar_big));
            users.add(new User(this, "105", R.drawable.cc_no_avatar_big));
            users.add(new User(this, "106", R.drawable.cc_no_avatar_big));

            ContactsAdapter uAdapter = new ContactsAdapter(this, users);
            _lvContacts.setAdapter(uAdapter);
            _lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                }

            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
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
