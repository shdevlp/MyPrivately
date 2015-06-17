package ru.bitprofi.myprivately;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Дмитрий on 27.05.2015.
 */
public class GlobalSettings {
    private static Context m_context;
    private static ArrayList<User> m_users;

    private GlobalSettings() {
        m_users = new ArrayList<User>();
    }

    private static class SingletonHolder {
        public static final GlobalSettings HOLDER_INSTANCE = new GlobalSettings();
    }

    public static GlobalSettings getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public ArrayList<User> getUsers() {
        return this.m_users;
    }

    public Context getContext() {
        return this.m_context;
    }

    public void setContext(Context context) {
        this.m_context = context;
    }
}
