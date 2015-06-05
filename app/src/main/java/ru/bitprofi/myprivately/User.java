package ru.bitprofi.myprivately;

import android.content.Context;

import java.util.Random;

/**
 * Created by Дмитрий on 15.05.2015.
 */
public class User {
    private Context _context;

    private String     _name;
    private int        _image;
    private UserStatus _status;
    private String     _lastMessage;
    private String     _lastTime;
    private boolean    _lock;
    private int        _unreadMessageCount;

    public enum UserStatus {
        online,
        offline
    };

    public User(Context context, String name, int image) {
        this._context = context;
        this._name = name;
        this._image = image;
        _status = UserStatus.offline;
        _lastMessage = null;
        _lastTime = "21:42";
        _lock = Utils.getInstance().getRandomBoolean();
        _unreadMessageCount = Utils.getInstance().getRandomInt(99);
    }

    public String getName() {
        return _name;
    }

    public int getImage() {
        return _image;
    }

    public UserStatus getStatus() {
        return _status;
    }

    public String getStatusString() {
        if (_status == UserStatus.online) {
            return _context.getString(R.string.user_status_online);
        }
        if (_status == UserStatus.offline) {
            return _context.getString(R.string.user_status_offline);
        }
        return null;
    }

    public void setStatus(UserStatus status) {
        _status = status;
    }

    public void setLastMessage(String message) {
        _lastMessage = message;
    }

    public String getLastMessage() {
        return _lastMessage;
    }

    public void setLastTime(String time) {
        _lastTime = time;
    }

    public String getLastTime() {
        return _lastTime;
    }

    public void setLock(boolean lock) {
        _lock = lock;
    }

    public boolean getLock() {
        return _lock;
    }

    public void setUndeadMessageCount(int count) {
        _unreadMessageCount = count;
    }

    public int getUnreadMessageCount() {
        return _unreadMessageCount;
    }
}
