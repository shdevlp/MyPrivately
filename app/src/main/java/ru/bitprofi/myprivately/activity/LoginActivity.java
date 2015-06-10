package ru.bitprofi.myprivately.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import ru.bitprofi.myprivately.GlobalSettings;
import ru.bitprofi.myprivately.R;
import ru.bitprofi.myprivately.User;


public class LoginActivity extends Activity {
    private Button   m_bt_login = null;
    private EditText m_et_login = null;
    private EditText m_et_password = null;
    private EditText m_et_password_retype = null;

    private ArrayList<User> m_users = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadUsers();

        m_et_login = (EditText)findViewById(R.id.et_login);
        m_et_password = (EditText)findViewById(R.id.et_password);
        m_et_password_retype = (EditText)findViewById(R.id.et_password_retype);

        m_bt_login = (Button)findViewById(R.id.bt_login);
        m_bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String login = m_et_login.getText().toString();
                final String password = m_et_password.getText().toString();
                final String password_retype = m_et_password_retype.getText().toString();

                if (password != password_retype) {
                    Toast.makeText(getApplicationContext(), "Пароли не равны!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (checkAutofication(login, password)) {
                    Intent myIntent = new Intent(getBaseContext(), ChatsActivity.class);
                    myIntent.putExtra("login", login);
                    finish();
                    startActivity(myIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Имя пользователя или пароль введен не верно",
                            Toast.LENGTH_SHORT).show();
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
            if ((usr.getName() == user) &&
                    (usr.getPassword() == password)) {
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
