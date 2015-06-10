package ru.bitprofi.myprivately.iface;

import java.util.EventListener;

public interface INewMessageListener extends EventListener {
    public void onMessage(String from);
}
