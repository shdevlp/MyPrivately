package ru.bitprofi.myprivately.sip;

import java.util.EventObject;

public class SipEvent extends EventObject{
    public enum SipEventType {
        MESSAGE,
        BYE,
        CALL
    }
    public SipEventType _type;

    public String _content;
    public String _from;

    public SipEvent(Object source, SipEventType type, String content, String from) {
        super(source);
        this._type = type;
        this._content = content;
        this._from = from;
    }
}
