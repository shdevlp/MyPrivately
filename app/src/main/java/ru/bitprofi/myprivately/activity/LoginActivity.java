package ru.bitprofi.myprivately.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.bitprofi.myprivately.GlobalSettings;
import ru.bitprofi.myprivately.R;
import ru.bitprofi.myprivately.User;


public class LoginActivity extends ActionBarActivity {
    private ImageButton m_bt_use_password = null;
    private EditText m_et_login = null;
    private EditText m_et_password = null;
    private EditText m_et_password_retype = null;
    private boolean m_use_password = false;

    private ArrayList<User> m_users = null;

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);

            LayoutInflater inflator = LayoutInflater.from(this);
            View v = inflator.inflate(R.layout.actionbar_title, null);
            TextView title = ((TextView)v.findViewById(R.id.title_text));
            title.setText("Регистрация");
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) title.getLayoutParams();
            mlp.setMargins(100, 0, 0, 0);
            actionBar.setCustomView(v);

            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mainblue)));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GlobalSettings.getInstance().setContext(getApplicationContext());
        showActionBar();
        loadUsers();

        m_et_login = (EditText)findViewById(R.id.et_login);
        m_et_login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (m_et_login.getText().toString().equalsIgnoreCase("Номер телефона")) {
                        m_et_login.getText().clear();
                        m_et_login.setText("7901100000");
                    }
                }
                return false;
            }
        });

        m_et_password = (EditText)findViewById(R.id.et_password);
        m_et_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    m_et_password.getText().clear();
                }
                return false;
            }
        });

        m_et_password_retype = (EditText)findViewById(R.id.et_password_retype);
        m_et_password_retype.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    m_et_password_retype.getText().clear();
                }
                return false;
            }
        });

        m_bt_use_password = (ImageButton)findViewById(R.id.bt_use_password);
        m_bt_use_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View parent) {
                m_use_password = !m_use_password;
                if (m_use_password) {
                    m_bt_use_password.setImageResource(R.drawable.r_switch_button_on);
                    m_et_password.setEnabled(true);
                    m_et_password_retype.setEnabled(true);
                } else {
                    m_bt_use_password.setImageResource(R.drawable.r_switch_button_off);
                    m_et_password.setEnabled(false);
                    m_et_password_retype.setEnabled(false);
                }
            }
        });

    }

    /**
     * Проверка Имя пользователя и Пароля
     * @param user
     * @param password
     * @return
     */
    private boolean checkAutofication(String user, String password) {
        for (User usr: m_users) {
            if ((usr.getName().equals(user)) &&
                    (usr.getPassword().equals(password))) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     */
    private void loadUsers() {
        GlobalSettings gs = GlobalSettings.getInstance();
        m_users = gs.getUsers();
        m_users.add(new User("79011000001", "12345"));
        m_users.add(new User("79011000002", "12345"));
        m_users.add(new User("79011000003", "12345"));
        m_users.add(new User("79011000004", "12345"));
        m_users.add(new User("79011000005", "12345"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_reg: {
                final String login = m_et_login.getText().toString();
                final String password = m_et_password.getText().toString();
                final String password_retype = m_et_password_retype.getText().toString();

                if (!password.equals(password_retype)) {
                    Toast.makeText(getApplicationContext(), "Пароли не равны!", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (checkAutofication(login, password)) {
                    Intent myIntent = new Intent(getBaseContext(), ChatsActivity.class);
                    myIntent.putExtra("login", login);
                    myIntent.putExtra("password", password);
                    finish();
                    startActivity(myIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Имя пользователя или пароль введен не верно",
                            Toast.LENGTH_SHORT).show();
                }
            }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
