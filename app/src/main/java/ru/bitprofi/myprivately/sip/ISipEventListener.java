package ru.bitprofi.myprivately.sip;

import java.util.EventListener;

public interface ISipEventListener extends EventListener {
    public void onSipMessage(SipEvent sipEvent);
}
