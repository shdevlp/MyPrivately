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

import ru.bitprofi.myprivately.GlobalSettings;
import ru.bitprofi.myprivately.R;
import ru.bitprofi.myprivately.User;
import ru.bitprofi.myprivately.adapter.ChatsAdapter;
import ru.bitprofi.myprivately.adapter.ContactsAdapter;

/**
 * Created by Дмитрий on 05.06.2015.
 */
public class ContactsActivity extends ActionBarActivity {
    private ListView _lvContacts = null;
    private ActionButton _abEditChat = null;
    private String m_current_user;

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

        Intent intent = getIntent();
        m_current_user = intent.getStringExtra("CurrentUserName");

        ArrayList<User> users = GlobalSettings.getInstance().getUsers();

        //Убираем пользователя который зарегистрировался
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getName().equals(m_current_user)) {
                users.remove(i);
                break;
            }
        }

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
