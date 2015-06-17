package ru.bitprofi.myprivately.sip;

import android.gov.nist.javax.sip.clientauthutils.AccountManager;
import android.gov.nist.javax.sip.clientauthutils.UserCredentials;
import android.javax.sip.ClientTransaction;

/**
 * Created by Дмитрий on 16.06.2015.
 */
public class AccountManagerImpl implements AccountManager {
    public UserCredentials getCredentials(ClientTransaction challengedTransaction, String realm) {
        return new UserCredentialsImpl(SipStackAndroid.sipUserName, SipStackAndroid.remoteIp,
                SipStackAndroid.sipPassword);
    }
}
