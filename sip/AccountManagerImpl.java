package ru.bitprofi.myprivately.sip;

import android.gov.nist.javax.sip.clientauthutils.AccountManager;
import android.gov.nist.javax.sip.clientauthutils.UserCredentials;
import android.javax.sip.ClientTransaction;

import ru.bitprofi.myprivately.GlobalSettings;
import ru.bitprofi.myprivately.sip2.UserCredentialsImpl;

public class AccountManagerImpl implements AccountManager {
    public UserCredentials getCredentials(ClientTransaction challengedTransaction, String realm) {
        GlobalSettings _gs = GlobalSettings.getInstance();
        return new UserCredentialsImpl(_gs.getSipUserName(), _gs.getRemoteIp(), _gs.getSipPassword());
    }

}
