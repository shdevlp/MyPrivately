package ru.bitprofi.myprivately.sip;

import android.javax.sip.ClientTransaction;
import android.javax.sip.InvalidArgumentException;
import android.javax.sip.SipException;
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
import android.os.AsyncTask;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Дмитрий on 16.06.2015.
 */
public class SipSendMessage extends AsyncTask<Object, Object, Object> {
    private void sendMessage(String to, String message) throws ParseException,
            InvalidArgumentException, SipException {

        SipStackAndroid.getInstance();
        SipURI from = SipStackAndroid.addressFactory.createSipURI(SipStackAndroid.sipUserName,
                SipStackAndroid.localEndpoint);

        SipStackAndroid.getInstance();
        Address fromNameAddress = SipStackAndroid.addressFactory.createAddress(from);

        SipStackAndroid.getInstance();
        FromHeader fromHeader = SipStackAndroid.headerFactory.createFromHeader(fromNameAddress,
                "Tzt0ZEP92");

        SipStackAndroid.getInstance();
        URI toAddress = SipStackAndroid.addressFactory.createURI(to);

        SipStackAndroid.getInstance();
        Address toNameAddress = SipStackAndroid.addressFactory.createAddress(toAddress);

        SipStackAndroid.getInstance();
        ToHeader toHeader = SipStackAndroid.headerFactory.createToHeader(toNameAddress, null);

        SipStackAndroid.getInstance();
        URI requestURI = SipStackAndroid.addressFactory.createURI(to);

        ArrayList<ViaHeader> viaHeaders = SipStackAndroid.createViaHeader();

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

        SipStackAndroid.getInstance();
        SipURI routeUri = SipStackAndroid.addressFactory.createSipURI(null,
                SipStackAndroid.remoteIp);
        routeUri.setTransportParam(SipStackAndroid.transport);
        routeUri.setLrParam();
        routeUri.setPort(SipStackAndroid.remotePort);

        SipStackAndroid.getInstance();
        Address routeAddress = SipStackAndroid.addressFactory.createAddress(routeUri);

        SipStackAndroid.getInstance();
        RouteHeader route = SipStackAndroid.headerFactory.createRouteHeader(routeAddress);
        request.addHeader(route);

        SipStackAndroid.getInstance();
        ContentTypeHeader contentTypeHeader = SipStackAndroid.headerFactory
                .createContentTypeHeader("text", "plain");
        request.setContent(message, contentTypeHeader);

        System.out.println(request);

        SipStackAndroid.getInstance();
        ClientTransaction transaction = SipStackAndroid.sipProvider
                .getNewClientTransaction(request);

        transaction.sendRequest();
    }

    @Override
    protected Object doInBackground(Object... params) {
        final String to = (String) params[0];
        final String message = (String) params[1];

        try {
            sendMessage(to, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
