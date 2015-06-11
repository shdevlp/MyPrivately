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
import android.javax.sip.message.MessageFactory;
import android.javax.sip.message.Request;
import android.javax.sip.message.Response;
import android.os.AsyncTask;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;

import ru.bitprofi.myprivately.GlobalSettings;
import ru.bitprofi.myprivately.Utils;
import ru.bitprofi.myprivately.iface.ISipEventListener;

public class SipStackAndroid extends AsyncTask<Object, Object, Object>
        implements SipListener {

    private static GlobalSettings _gs = GlobalSettings.getInstance();
    private static Utils _utils = Utils.getInstance();

    public static SipStack sipStack;
    public static SipProvider sipProvider;
    public static HeaderFactory headerFactory;
    public static AddressFactory addressFactory;
    public static MessageFactory messageFactory;
    public static SipFactory sipFactory;

    public static ListeningPoint udpListeningPoint;

    private Dialog dialog;
    private Request ackRequest;
    private boolean ackReceived;

    private ArrayList<ISipEventListener> sipEventListenerList = new ArrayList<ISipEventListener>();

    protected SipStackAndroid() {
    }

    private static class SingletonHolder {
        public static final SipStackAndroid HOLDER_INSTANCE = new SipStackAndroid();
    }

    public static SipStackAndroid getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public synchronized void addSipListener(ISipEventListener listener) {
        if (!sipEventListenerList.contains(listener)) {
            sipEventListenerList.add(listener);
        }
    }

    private void dispatchSipEvent(SipEvent sipEvent) {
        ArrayList<ISipEventListener> tmpSipListenerList;

        synchronized (this) {
            if (sipEventListenerList.size() == 0)
                return;
            tmpSipListenerList = (ArrayList<ISipEventListener>) sipEventListenerList.clone();
        }

        for (ISipEventListener listener : tmpSipListenerList) {
            listener.onSipMessage(sipEvent);
        }
    }

    private static void initialize() {
        String localIp = _utils.getIPAddress(true);
        _gs.setLocalIp(localIp);

        sipStack = null;
        sipFactory = SipFactory.getInstance();
        sipFactory.setPathName("android.gov.nist");

        Properties properties = new Properties();
        properties.setProperty("android.javax.sip.OUTBOUND_PROXY", _gs.getRemoteEndpoint() + "/"
                + _gs.getTransport());
        properties.setProperty("android.javax.sip.STACK_NAME", "androidSip");

        try {
            // Create SipStack object
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
            udpListeningPoint = sipStack.createListeningPoint(_gs.getLocalIp(), _gs.getLocalPort(),
                    _gs.getTransport());
            sipProvider = sipStack.createSipProvider(udpListeningPoint);
            sipProvider.addSipListener(SipStackAndroid.getInstance());
            // this.send_register();
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

    @Override
    protected Object doInBackground(Object... params) {
        String sipUsername = (String) params[0];
        String sipPassword = (String) params[1];
        String sipDomain = (String) params[2];

        _gs.setSipUserName(sipUsername);
        _gs.setSipPassword(sipPassword);

        _gs.setRemoteIp(sipDomain);
        initialize();
        return null;
    }

    @Override
    public void processRequest(RequestEvent arg0) {
        Request request = (Request) arg0.getRequest();
        ServerTransaction serverTransactionId = arg0
                .getServerTransaction();
        SIPMessage sp = (SIPMessage)request;
        System.out.println(request.getMethod());
        if(request.getMethod().equals("MESSAGE")){
            sendOk(arg0);
            try {
                String message = sp.getMessageContent();
                dispatchSipEvent(new SipEvent(this, SipEvent.SipEventType.MESSAGE,
                        message, sp.getFrom().getAddress().toString()));

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else if (request.getMethod().equals(Request.BYE)) {
            processBye(request, serverTransactionId);
            dispatchSipEvent(new SipEvent(this, SipEvent.SipEventType.BYE,"",sp.getFrom().getAddress().toString() ));

        }
    }

    public void sendOk(RequestEvent requestEvt){
        Response response;
        try {
            response = messageFactory.createResponse(
                    200, requestEvt.getRequest());
            ServerTransaction serverTransaction = requestEvt.getServerTransaction();
            if (serverTransaction == null) {
                serverTransaction = sipProvider.getNewServerTransaction(requestEvt.getRequest());
            }
            serverTransaction.sendResponse(response);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SipException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void processIOException(IOExceptionEvent exceptionEvent) {
        System.out.println("IOException happened for "
                + exceptionEvent.getHost() + " port = "
                + exceptionEvent.getPort());

    }

    public void processTransactionTerminated(
            TransactionTerminatedEvent transactionTerminatedEvent) {
        System.out.println("Transaction terminated event recieved");
    }

    public void processDialogTerminated(
            DialogTerminatedEvent dialogTerminatedEvent) {
        System.out.println("dialogTerminatedEvent");
    }

    public void processTimeout(TimeoutEvent timeoutEvent) {
        System.out.println("Transaction Time out");
    }

    @Override
    public void processResponse(ResponseEvent arg0) {
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
                        .handleChallenge(response, tid, sipProvider, 5,true);
                inviteTid.sendRequest();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (SipException e) {
                e.printStackTrace();
            }

        }
        else if (response.getStatusCode() == Response.OK){
            if (cseq.getMethod().equals(Request.INVITE)) {

                System.out.println("Dialog after 200 OK  " + dialog);
                try {
                    Request ackRequest = responseDialog.createAck(cseq.getSeqNumber());
                    System.out.println("Sending ACK");
                    responseDialog.sendAck(ackRequest);
                } catch (InvalidArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SipException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (cseq.getMethod().equals(Request.CANCEL)) {
                if (dialog.getState() == DialogState.CONFIRMED) {
                    // oops cancel went in too late. Need to hang up the
                    // dialog.
                    System.out
                            .println("Sending BYE -- cancel went in too late !!");
                    Request byeRequest = null;
                    try {
                        byeRequest = dialog.createRequest(Request.BYE);
                    } catch (SipException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    ClientTransaction ct = null;
                    try {
                        ct = sipProvider
                                .getNewClientTransaction(byeRequest);
                    } catch (TransactionUnavailableException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    try {
                        dialog.sendRequest(ct);
                    } catch (TransactionDoesNotExistException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (SipException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public void processBye(Request request,
                           ServerTransaction serverTransactionId) {
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
}