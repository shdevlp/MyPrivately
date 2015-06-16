package ru.bitprofi.myprivately.sip2;

import android.gov.nist.javax.sip.clientauthutils.UserCredentials;

public class UserCredentials2 implements UserCredentials{
    private String m_user_name;
    private String m_sip_domain;
    private String m_password;

    public UserCredentials2(String userName, String sipDomain, String password) {
        this.m_user_name = userName;
        this.m_sip_domain = sipDomain;
        this.m_password = password;
    }

    public String getPassword() {
        return m_password;
    }

    public String getSipDomain() {
        return m_sip_domain;
    }

    public String getUserName() {
        return m_user_name;
    }
}
