package ru.bitprofi.myprivately;

import android.content.Context;
import java.io.Serializable;

/**
 * Created by Дмитрий on 15.05.2015.
 */

@SuppressWarnings("serial")
public class User implements Serializable {
    private String     m_name;
    private String     m_password;
    private int        m_image;
    private UserStatus m_status;
    private String     m_last_message;
    private String     m_last_time;
    private int        m_unread_messages;

    public enum UserStatus {
        online,
        offline
    };

    public User(String name, int image) {
        this.m_name = name;
        this.m_image = image;
        this.m_status = UserStatus.offline;
        this.m_last_message = null;
        this.m_last_time = "0:00";
        this.m_unread_messages = 0;
        this.m_password = null;
    }

    public User(String name, String password) {
        this.m_name = name;
        this.m_password = password;
        this.m_image = 0;
        this.m_unread_messages = 0;
        this.m_status = UserStatus.offline;
        this.m_last_message = null;
        this.m_last_time = "0:00";
    }

    public String getPassword() {
        return this.m_password;
    }

    public void setPassword(String password) {
        this.m_password = password;
    }

    public String getName() {
        return this.m_name;
    }

    public int getImage() {
        return this.m_image;
    }

    public UserStatus getStatus() {
        return this.m_status;
    }

    public String getStatusString() {
        Context ctx = GlobalSettings.getInstance().getContext();
        if (m_status == UserStatus.online) {
            return ctx.getString(R.string.user_status_online);
        }
        if (m_status == UserStatus.offline) {
            return ctx.getString(R.string.user_status_offline);
        }
        return null;
    }

    public void setStatus(UserStatus status) {
        this.m_status = status;
    }

    public void setLastMessage(String message) {
        this.m_last_message = message;
    }

    public String getLastMessage() {
        return this.m_last_message;
    }

    public void setLastTime(String time) {
        this.m_last_time = time;
    }

    public String getLastTime() {
        return this.m_last_time;
    }

    public void setUndeadMessages(int count) {
        this.m_unread_messages = count;
    }

    public int getUnreadMessages() {
        return this.m_unread_messages;
    }
}
