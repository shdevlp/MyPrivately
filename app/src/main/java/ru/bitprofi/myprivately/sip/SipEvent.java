package ru.bitprofi.myprivately.sip;

import java.util.EventObject;

public class SipEvent extends EventObject{
    public enum SipEventType {
        MESSAGE,
        BYE,
        CALL
    }

    private SipEventType m_type;
    private String m_content;
    private String m_from;

    public SipEvent(Object source, SipEventType type, String content, String from) {
        super(source);
        this.m_type = type;
        this.m_content = content;
        this.m_from = from;
    }

    public SipEventType getType() {
        return this.m_type;
    }

    public String getContent() {
        return this.m_content;
    }

    public String getFrom() {
        return this.m_from;
    }
}
