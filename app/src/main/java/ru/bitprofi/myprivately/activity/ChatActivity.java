package ru.bitprofi.myprivately.activity;

import ru.bitprofi.myprivately.adapter.DbAdapter;
import ru.bitprofi.myprivately.iface.INewMessageListener;
import ru.bitprofi.myprivately.sip.*;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
public class ChatActivity extends ActionBarActivity implements INewMessageListener {
    private ListView m_lv_left   = null;
    private ListView m_lv_center = null;
    private ListView m_lv_right  = null;

    private ImageButton m_bt_send = null;
    private ChatAdapter m_adapter = null;
    private EditText m_message = null;
    private TextView m_title_view = null;
    private TextView m_status_view = null;
    private boolean m_left = false;

    private String m_current_user = null;
    private String m_target_user = null;
    private String m_current_user_status = null;
    private Integer m_current_user_image = 0;

    private void showActionBar(String title, String status) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);

            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.actionbar_chat, null);
            m_title_view = ((TextView) v.findViewById(R.id.title_text));
            m_title_view.setText(title);
            m_status_view = ((TextView) v.findViewById(R.id.status_text));
            m_status_view.setText(status);
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

        m_current_user = intent.getStringExtra("CurrentUserName");
        m_target_user =  intent.getStringExtra("TargetUserName");

        m_current_user_status = intent.getStringExtra("UserStatus");
        m_current_user_image = intent.getIntExtra("UserImage", 0);

        showActionBar(m_target_user, m_current_user_status);

        m_lv_left = (ListView) findViewById(R.id.lv_left);
        m_lv_center = (ListView) findViewById(R.id.lv_center);
        m_lv_right = (ListView) findViewById(R.id.lv_right);
        m_message = (EditText) findViewById(R.id.et_message);

        m_bt_send = (ImageButton) findViewById(R.id.bt_send);
        m_bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        m_adapter = new ChatAdapter(this);
        m_lv_center.setAdapter(m_adapter);

        displayList();
    }

    private void hideKeyboard() {
        InputMethodManager inputManager =
                (InputMethodManager) this.
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
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

    /**
     * Отправка сообщения
     */
    private void sendMessage() {
        String text = m_message.getText().toString();
        if (text.length() > 0) {
            m_message.getText().clear();
            m_left = false;
            ChatsActivity.dbHelper.createMessage(text, m_current_user, m_target_user);
            ChatsActivity.doMessage(m_target_user, text);
            hideKeyboard();
            displayList();
            if (m_adapter.getCount()==0) {
                ChatsActivity.dbHelper.createMessage(text, m_current_user, m_target_user);
                ChatsActivity.doMessage(m_target_user, text);
                displayList();
            }

        }
    }

    /**
     * Получили сообщение от когото
     * @param from
     */
    @Override
    public void onMessage(String from) {
        displayList();
    }

    /**
     * Отобразить все сообщения, подгрузка из базы данных
     */
    private void displayList() {
        Cursor cursor = ChatsActivity.dbHelper.fetchMesagesByCreds(m_target_user, m_current_user);

        if (m_adapter != null) {
            m_adapter.clear();
            m_adapter = null;
        }
        m_adapter = new ChatAdapter(this);
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                final String keyFrom = cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_FROM));
                final String keyText = cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_TEXT));
                final boolean isLeft = keyFrom.equalsIgnoreCase(m_current_user);
                final String message = keyFrom + "\n" + keyText;

                m_adapter.add(new OneComment(isLeft, message));
            }
        }

        m_lv_center.setAdapter(m_adapter);
    }
}
