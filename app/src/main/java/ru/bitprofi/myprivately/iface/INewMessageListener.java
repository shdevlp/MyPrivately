package ru.bitprofi.myprivately.iface;

import java.util.EventListener;

public interface INewMessageListener extends EventListener {
    void onMessage(String from);
}
