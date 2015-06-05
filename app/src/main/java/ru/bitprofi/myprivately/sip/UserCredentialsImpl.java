package ru.bitprofi.myprivately.sip;

import android.gov.nist.javax.sip.clientauthutils.UserCredentials;

public class UserCredentialsImpl implements UserCredentials{
    private String _userName;
    private String _sipDomain;
    private String _password;

    public UserCredentialsImpl(String userName, String sipDomain, String password) {
        this._userName = userName;
        this._sipDomain = sipDomain;
        this._password = password;
    }

    public String getPassword() {
        return _password;
    }

    public String getSipDomain() {
        return _sipDomain;
    }

    public String getUserName() {
        return _userName;
    }

}
