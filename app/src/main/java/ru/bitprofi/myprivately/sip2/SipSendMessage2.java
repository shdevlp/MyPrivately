package ru.bitprofi.myprivately.sip2;

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
public class SipSendMessage2 extends AsyncTask<Object, Object, Object> {
    private void sendMessage(String to, String message) throws ParseException,
            InvalidArgumentException, SipException {

        SipStack2.getInstance();
        SipURI from = SipStack2.addressFactory.createSipURI(SipStack2.getInstance().sipUserName,
                SipStack2.getInstance().localEndpoint);

        SipStack2.getInstance();
        Address fromNameAddress = SipStack2.addressFactory.createAddress(from);

        SipStack2.getInstance();
        FromHeader fromHeader = SipStack2.headerFactory.createFromHeader(fromNameAddress,
                "Tzt0ZEP92");

        SipStack2.getInstance();
        URI toAddress = SipStack2.addressFactory.createURI(to);

        SipStack2.getInstance();
        Address toNameAddress = SipStack2.addressFactory.createAddress(toAddress);

        SipStack2.getInstance();
        ToHeader toHeader = SipStack2.headerFactory.createToHeader(toNameAddress, null);

        SipStack2.getInstance();
        URI requestURI = SipStack2.addressFactory.createURI(to);

        ArrayList<ViaHeader> viaHeaders = SipStack2.createViaHeader();

        SipStack2.getInstance();
        CallIdHeader callIdHeader = SipStack2.sipProvider.getNewCallId();

        SipStack2.getInstance();
        CSeqHeader cSeqHeader = SipStack2.headerFactory.createCSeqHeader(50l,
                Request.MESSAGE);

        SipStack2.getInstance();
        MaxForwardsHeader maxForwards = SipStack2.headerFactory
                .createMaxForwardsHeader(70);

        SipStack2.getInstance();
        Request request = SipStack2.messageFactory.createRequest(requestURI,
                Request.MESSAGE, callIdHeader, cSeqHeader, fromHeader,
                toHeader, viaHeaders, maxForwards);

        SipStack2.getInstance();
        SupportedHeader supportedHeader = SipStack2.headerFactory
                .createSupportedHeader("replaces, outbound");
        request.addHeader(supportedHeader);

        SipStack2.getInstance();
        SipURI routeUri = SipStack2.addressFactory.createSipURI(null,
                SipStack2.getInstance().remoteIp);
        routeUri.setTransportParam(SipStack2.transport);
        routeUri.setLrParam();
        routeUri.setPort(SipStack2.remotePort);

        SipStack2.getInstance();
        Address routeAddress = SipStack2.addressFactory.createAddress(routeUri);

        SipStack2.getInstance();
        RouteHeader route = SipStack2.headerFactory.createRouteHeader(routeAddress);
        request.addHeader(route);

        SipStack2.getInstance();
        ContentTypeHeader contentTypeHeader = SipStack2.headerFactory
                .createContentTypeHeader("text", "plain");
        request.setContent(message, contentTypeHeader);

        System.out.println(request);

        SipStack2.getInstance();
        ClientTransaction transaction = SipStack2.sipProvider
                .getNewClientTransaction(request);

        // Send the request statefully,
        // through the client transaction.
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
