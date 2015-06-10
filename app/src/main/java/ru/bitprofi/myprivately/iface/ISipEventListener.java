package ru.bitprofi.myprivately.iface;

import java.util.EventListener;

import ru.bitprofi.myprivately.sip.SipEvent;

public interface ISipEventListener extends EventListener {
    public void onSipMessage(SipEvent sipEvent);
}
