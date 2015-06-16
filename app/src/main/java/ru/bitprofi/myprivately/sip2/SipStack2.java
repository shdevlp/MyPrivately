package ru.bitprofi.myprivately.sip2;

import android.gov.nist.javax.sip.SipStackExt;
import android.gov.nist.javax.sip.clientauthutils.AuthenticationHelper;
import android.javax.sip.ClientTransaction;
import android.javax.sip.DialogTerminatedEvent;
import android.javax.sip.IOExceptionEvent;
import android.javax.sip.InvalidArgumentException;
import android.javax.sip.ListeningPoint;
import android.javax.sip.PeerUnavailableException;
import android.javax.sip.RequestEvent;
import android.javax.sip.ResponseEvent;
import android.javax.sip.SipException;
import android.javax.sip.SipFactory;
import android.javax.sip.SipListener;
import android.javax.sip.SipProvider;
import android.javax.sip.SipStack;
import android.javax.sip.TimeoutEvent;
import android.javax.sip.TransactionTerminatedEvent;
import android.javax.sip.address.AddressFactory;
import android.javax.sip.header.HeaderFactory;
import android.javax.sip.header.ViaHeader;
import android.javax.sip.message.MessageFactory;
import android.javax.sip.message.Response;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;

import ru.bitprofi.myprivately.Utils;
import ru.bitprofi.myprivately.sip.SipStackAndroid;

/**
 * Created by Дмитрий on 16.06.2015.
 */

public class SipStack2 implements SipListener {
    private static SipStack2 instance = null;

    public static SipStack       sipStack;
    public static SipProvider    sipProvider;
    public static HeaderFactory  headerFactory;
    public static AddressFactory addressFactory;
    public static MessageFactory messageFactory;
    public static SipFactory     sipFactory;
    public static ListeningPoint udpListeningPoint;

    public static String localIp;
    public static String remoteIp   = "23.23.228.238";
    public static int    localPort  = 5080;
    public static int    remotePort = 5080;
    public static String transport  = "udp";

    public static String registeringAcc = "23_23_228_238";

    public static String localEndpoint  = localIp + ":" + localPort;
    public static String remoteEndpoint = remoteIp + ":" + remotePort;

    public static String sipUserName;
    public String        sipPassword;

    protected SipStack2() {
        initialize();
    }

    public static SipStack2 getInstance() {
        if (instance == null) {
            instance = new SipStack2();
        }
        return instance;
    }

    private static void initialize() {
        localIp = Utils.getIPAddress(true);
        localEndpoint = localIp + ":" + localPort;
        remoteEndpoint = remoteIp + ":" + remotePort;
        sipStack = null;
        sipFactory = SipFactory.getInstance();
        sipFactory.setPathName("android.gov.nist");
        Properties properties = new Properties();
        properties.setProperty("javaxx.sip.OUTBOUND_PROXY", remoteEndpoint + "/"
                + transport);
        properties.setProperty("javaxx.sip.STACK_NAME", "androidSip");
        try {
            sipStack = sipFactory.createSipStack(properties);
            System.out.println("createSipStack " + sipStack);
        } catch (PeerUnavailableException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(0);
        }
        try {
            headerFactory = sipFactory.createHeaderFactory();
            addressFactory = sipFactory.createAddressFactory();
            messageFactory = sipFactory.createMessageFactory();

            udpListeningPoint = sipStack.createListeningPoint(localIp,
                    localPort, transport);

            sipProvider = sipStack.createSipProvider(udpListeningPoint);

            sipProvider.addSipListener(SipStack2.getInstance());
        } catch (PeerUnavailableException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Creating Listener Points");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static ArrayList<ViaHeader> createViaHeader() {
        ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
        ViaHeader myViaHeader;
        try {
            SipStack2.getInstance();
            myViaHeader = SipStack2.headerFactory.createViaHeader(SipStack2.localIp,
                    SipStack2.localPort, SipStack2.transport, null);
            myViaHeader.setRPort();
            viaHeaders.add(myViaHeader);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        return viaHeaders;
    }

    @Override
    public void processRequest(RequestEvent var1) {

    }

    @Override
    public void processResponse(ResponseEvent arg0) {
        Response response = (Response) arg0.getResponse();
        ClientTransaction tid = arg0.getClientTransaction();
        System.out.println(response.getStatusCode());
        if (response.getStatusCode() == Response.PROXY_AUTHENTICATION_REQUIRED
                || response.getStatusCode() == Response.UNAUTHORIZED) {
            AuthenticationHelper authenticationHelper = ((SipStackExt) sipStack)
                    .getAuthenticationHelper(new AccountManager2(),
                            headerFactory);
            try {
                ClientTransaction inviteTid = authenticationHelper
                        .handleChallenge(response, tid, sipProvider, 5);
                inviteTid.sendRequest();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (SipException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void processTimeout(TimeoutEvent var1) {

    }

    @Override
    public void processIOException(IOExceptionEvent var1) {

    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent var1) {

    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent var1) {

    }
}
