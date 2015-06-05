package ru.bitprofi.myprivately.sip;

import android.javax.sip.ClientTransaction;
import android.javax.sip.InvalidArgumentException;
import android.javax.sip.address.Address;
import android.javax.sip.address.SipURI;
import android.javax.sip.address.URI;
import android.javax.sip.header.CSeqHeader;
import android.javax.sip.header.CallIdHeader;
import android.javax.sip.header.ContentTypeHeader;
import android.javax.sip.header.FromHeader;
import android.javax.sip.header.MaxForwardsHeader;
import android.javax.sip.header.RouteHeader;
import android.javax.sip.header.SupportedHeader;
import android.javax.sip.header.ToHeader;
import android.javax.sip.header.ViaHeader;
import android.javax.sip.message.Request;
import android.net.sip.SipException;
import android.os.AsyncTask;

import java.text.ParseException;
import java.util.ArrayList;

import ru.bitprofi.myprivately.GlobalSettings;

public class SipSendMessage extends AsyncTask<Object, Object, Object> {

    private GlobalSettings _gs = GlobalSettings.getInstance();

    private void sendMessage(String to, String message) throws ParseException,
            InvalidArgumentException, SipException, android.javax.sip.SipException {

        SipStackAndroid.getInstance();
        SipURI from = SipStackAndroid.addressFactory.createSipURI(_gs.getSipUserName(), _gs.getRemoteIp());

        SipStackAndroid.getInstance();
        Address fromNameAddress = SipStackAndroid.addressFactory.createAddress(from);
        SipStackAndroid.getInstance();
        // fromNameAddress.setDisplayName(sipUsername);
        FromHeader fromHeader = SipStackAndroid.headerFactory.createFromHeader(fromNameAddress,
                "Tzt0ZEP92");

        SipStackAndroid.getInstance();
        URI toAddress = SipStackAndroid.addressFactory.createURI(to);
        SipStackAndroid.getInstance();
        Address toNameAddress = SipStackAndroid.addressFactory.createAddress(toAddress);
        SipStackAndroid.getInstance();
        // toNameAddress.setDisplayName(username);
        ToHeader toHeader = SipStackAndroid.headerFactory.createToHeader(toNameAddress, null);

        SipStackAndroid.getInstance();
        URI requestURI = SipStackAndroid.addressFactory.createURI(to);
        // requestURI.setTransportParam("udp");

        ArrayList<ViaHeader> viaHeaders = createViaHeader();

        SipStackAndroid.getInstance();
        CallIdHeader callIdHeader = SipStackAndroid.sipProvider.getNewCallId();

        SipStackAndroid.getInstance();
        CSeqHeader cSeqHeader = SipStackAndroid.headerFactory.createCSeqHeader(50l,
                Request.MESSAGE);

        SipStackAndroid.getInstance();
        MaxForwardsHeader maxForwards = SipStackAndroid.headerFactory
                .createMaxForwardsHeader(70);

        SipStackAndroid.getInstance();
        Request request = SipStackAndroid.messageFactory.createRequest(requestURI,
                Request.MESSAGE, callIdHeader, cSeqHeader, fromHeader,
                toHeader, viaHeaders, maxForwards);
        SipStackAndroid.getInstance();
        SupportedHeader supportedHeader = SipStackAndroid.headerFactory
                .createSupportedHeader("replaces, outbound");
        request.addHeader(supportedHeader);

        SipURI routeUri = SipStackAndroid.addressFactory.createSipURI(null, _gs.getRemoteIp());
        routeUri.setTransportParam(_gs.getTransport());
        routeUri.setLrParam();
        routeUri.setPort(_gs.getRemotePort());

        SipStackAndroid.getInstance();
        Address routeAddress = SipStackAndroid.addressFactory.createAddress(routeUri);
        RouteHeader route =SipStackAndroid.headerFactory.createRouteHeader(routeAddress);
        request.addHeader(route);
        ContentTypeHeader contentTypeHeader = SipStackAndroid.headerFactory
                .createContentTypeHeader("text", "plain");
        request.setContent(message, contentTypeHeader);
        System.out.println(request);
        ClientTransaction transaction = SipStackAndroid.sipProvider
                .getNewClientTransaction(request);
        // Send the request statefully, through the client transaction.
        transaction.sendRequest();
    }

    private ArrayList<ViaHeader> createViaHeader() {
        ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
        ViaHeader myViaHeader;
        try {
            myViaHeader = SipStackAndroid.headerFactory.createViaHeader(
                    _gs.getLocalIp(),
                    _gs.getLocalPort(),
                    _gs.getTransport(), null);
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
    protected Object doInBackground(Object... params) {
        try {
            String to = (String) params[0];
            String message = (String) params[1];

            sendMessage(to, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
