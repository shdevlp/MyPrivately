package ru.bitprofi.myprivately.sip;

import android.gov.nist.javax.sip.SipStackExt;
import android.gov.nist.javax.sip.clientauthutils.AuthenticationHelper;
import android.gov.nist.javax.sip.message.SIPMessage;
import android.javax.sip.ClientTransaction;
import android.javax.sip.Dialog;
import android.javax.sip.DialogState;
import android.javax.sip.DialogTerminatedEvent;
import android.javax.sip.IOExceptionEvent;
import android.javax.sip.InvalidArgumentException;
import android.javax.sip.ListeningPoint;
import android.javax.sip.PeerUnavailableException;
import android.javax.sip.RequestEvent;
import android.javax.sip.ResponseEvent;
import android.javax.sip.ServerTransaction;
import android.javax.sip.SipException;
import android.javax.sip.SipFactory;
import android.javax.sip.SipListener;
import android.javax.sip.SipProvider;
import android.javax.sip.SipStack;
import android.javax.sip.TimeoutEvent;
import android.javax.sip.TransactionDoesNotExistException;
import android.javax.sip.TransactionTerminatedEvent;
import android.javax.sip.TransactionUnavailableException;
import android.javax.sip.address.AddressFactory;
import android.javax.sip.header.CSeqHeader;
import android.javax.sip.header.HeaderFactory;
import android.javax.sip.header.ViaHeader;
import android.javax.sip.message.MessageFactory;
import android.javax.sip.message.Request;
import android.javax.sip.message.Response;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;

import ru.bitprofi.myprivately.iface.ISipEventListener;

/**
 * Created by Дмитрий on 16.06.2015.
 */

public class SipStackAndroid implements SipListener {
    private static SipStackAndroid instance = null;

    public static SipStack       sipStack;
    public static SipProvider    sipProvider;
    public static HeaderFactory  headerFactory;
    public static AddressFactory addressFactory;
    public static MessageFactory messageFactory;
    public static SipFactory     sipFactory;
    public static ListeningPoint udpListeningPoint;

    private Dialog m_dialog;
    private ArrayList<ISipEventListener> m_sip_event_listener_list = new ArrayList<ISipEventListener>();

    protected SipStackAndroid() {
        initialize();
    }

    public static SipStackAndroid getInstance() {
        if (instance == null) {
            instance = new SipStackAndroid();
        }
        return instance;
    }

    private void initialize() {
        sipStack = null;
        sipFactory = SipFactory.getInstance();
        sipFactory.setPathName("android.gov.nist");

        Properties properties = new Properties();
        properties.setProperty("javaxx.sip.OUTBOUND_PROXY",
                SipStackSettings.remoteEndpoint + "/" + SipStackSettings.transport);
        properties.setProperty("android.javax.sip.STACK_NAME", "androidSip");

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

            udpListeningPoint = sipStack.createListeningPoint(SipStackSettings.localIp,
                    SipStackSettings.localPort, SipStackSettings.transport);
            sipProvider = sipStack.createSipProvider(udpListeningPoint);
            sipProvider.addSipListener(this);
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
            SipStackAndroid.getInstance();
            myViaHeader = SipStackAndroid.headerFactory.createViaHeader(SipStackSettings.localIp,
                    SipStackSettings.localPort, SipStackSettings.transport, null);
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
    public void processResponse(ResponseEvent arg0) {
        Response response = (Response) arg0.getResponse();
        ClientTransaction tid = arg0.getClientTransaction();
        System.out.println(response.getStatusCode());
        if (response.getStatusCode() == Response.PROXY_AUTHENTICATION_REQUIRED
                || response.getStatusCode() == Response.UNAUTHORIZED) {
            AuthenticationHelper authenticationHelper = ((SipStackExt) sipStack)
                    .getAuthenticationHelper(new AccountManagerImpl(),
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

            /*
        Response response = (Response) arg0.getResponse();
        Dialog responseDialog = null;

        ClientTransaction tid = arg0.getClientTransaction();
        if(tid != null) {
            responseDialog = tid.getDialog();
        } else {
            responseDialog = arg0.getDialog();
        }

        CSeqHeader cseq = (CSeqHeader) response.getHeader(CSeqHeader.NAME);
        System.out.println(response.getStatusCode());

        if (response.getStatusCode() == Response.PROXY_AUTHENTICATION_REQUIRED
                || response.getStatusCode() == Response.UNAUTHORIZED) {
            AuthenticationHelper authenticationHelper = ((SipStackExt) sipStack)
                    .getAuthenticationHelper(new AccountManagerImpl(),
                            headerFactory);
            try {
                ClientTransaction inviteTid = authenticationHelper
                        .handleChallenge(response, tid, sipProvider, 5, true);
                inviteTid.sendRequest();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (SipException e) {
                e.printStackTrace();
            }
        } else if (response.getStatusCode() == Response.OK){

            if (cseq.getMethod().equals(Request.INVITE)) {
                System.out.println("Dialog after 200 OK  " + m_dialog);
                try {
                    Request ackRequest = responseDialog.createAck(cseq.getSeqNumber());
                    responseDialog.sendAck(ackRequest);
                } catch (InvalidArgumentException e) {
                    e.printStackTrace();
                } catch (SipException e) {
                    e.printStackTrace();
                }
            } else if (cseq.getMethod().equals(Request.CANCEL)) {
                if (m_dialog.getState() == DialogState.CONFIRMED) {
                    Request byeRequest = null;
                    try {
                        byeRequest = m_dialog.createRequest(Request.BYE);
                    } catch (SipException e) {
                        e.printStackTrace();
                    }
                    ClientTransaction ct = null;
                    try {
                        ct = sipProvider.getNewClientTransaction(byeRequest);
                    } catch (TransactionUnavailableException e) {
                        e.printStackTrace();
                    }
                    try {
                        m_dialog.sendRequest(ct);
                    } catch (TransactionDoesNotExistException e) {
                        e.printStackTrace();
                    } catch (SipException e) {
                        e.printStackTrace();
                    }
                }
            }
*/

    }

    @Override
    public void processRequest(RequestEvent var1) {
         Request request = (Request) var1.getRequest();
        ServerTransaction serverTransactionId = var1
                .getServerTransaction();
        SIPMessage sp = (SIPMessage)request;
        System.out.println(request.getMethod());

        if(request.getMethod().equals("MESSAGE")){
            sendOk(var1);
            try {
                String message = sp.getMessageContent();
                dispatchSipEvent(new SipEvent(this, SipEvent.SipEventType.MESSAGE,
                        message, sp.getFrom().getAddress().toString()));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else if (request.getMethod().equals(Request.BYE)) {
            processBye(request, serverTransactionId);
            dispatchSipEvent(new SipEvent(this, SipEvent.SipEventType.BYE,"",
                    sp.getFrom().getAddress().toString() ));
        }
    }

    /**
     * Добавить слушателя
     * @param listener
     */
    public synchronized void addSipListener(ISipEventListener listener) {
        if (!m_sip_event_listener_list.contains(listener)) {
            m_sip_event_listener_list.add(listener);
        }
    }

    /**
     *
     * @param sipEvent
     */
    private void dispatchSipEvent(SipEvent sipEvent) {
        ArrayList<ISipEventListener> tmpSipListenerList;

        synchronized (this) {
            if (m_sip_event_listener_list.size() == 0) {
                return;
            }
            tmpSipListenerList = (ArrayList<ISipEventListener>)
                    m_sip_event_listener_list.clone();
        }

        for (ISipEventListener listener : tmpSipListenerList) {
            listener.onSipMessage(sipEvent);
        }
    }

    /**
     *
     * @param requestEvt
     */
    private void sendOk(RequestEvent requestEvt){
        Response response;
        try {
            response = messageFactory.createResponse(
                    200, requestEvt.getRequest());
            ServerTransaction serverTransaction = requestEvt.
                    getServerTransaction();
            if (serverTransaction == null) {
                serverTransaction = sipProvider.
                        getNewServerTransaction(requestEvt.getRequest());
            }
            serverTransaction.sendResponse(response);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SipException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param request
     * @param serverTransactionId
     */
    private void processBye(Request request, ServerTransaction serverTransactionId) {
        try {
            System.out.println("shootist:  got a bye .");
            if (serverTransactionId == null) {
                System.out.println("shootist:  null TID.");
                return;
            }
            Dialog dialog = serverTransactionId.getDialog();
            System.out.println("Dialog State = " + dialog.getState());
            Response response = messageFactory.createResponse(200, request);
            serverTransactionId.sendResponse(response);
            System.out.println("shootist:  Sending OK.");
            System.out.println("Dialog State = " + dialog.getState());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void processTimeout(TimeoutEvent event) {
        System.out.println("TimeoutEvent:" + event.toString());
    }

    @Override
    public void processIOException(IOExceptionEvent exceptionEvent) {
        System.out.println("IOException happened for "
                + exceptionEvent.getHost() + " port = "
                + exceptionEvent.getPort());
    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent event) {
        String message = event.toString();
        System.out.println("TransactionTerminatedEvent:" + message);
    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent event) {
        System.out.println("DialogTerminatedEvent:" + event.toString());
    }
}
