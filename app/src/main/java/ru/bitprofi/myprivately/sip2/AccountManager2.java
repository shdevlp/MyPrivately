package ru.bitprofi.myprivately.sip2;

import android.gov.nist.javax.sip.clientauthutils.AccountManager;
import android.javax.sip.ClientTransaction;

/**
 * Created by Дмитрий on 16.06.2015.
 */
public class AccountManager2 implements AccountManager {
    public UserCredentials2 getCredentials(ClientTransaction challengedTransaction, String realm) {
        return new UserCredentials2(SipStack2.sipUserName, SipStack2.remoteIp,
                SipStack2.getInstance().sipPassword);
    }
}
