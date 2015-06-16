package ru.bitprofi.myprivately.sip2;

import android.javax.sip.ClientTransaction;
import android.javax.sip.InvalidArgumentException;
import android.javax.sip.SipProvider;
import android.javax.sip.address.Address;
import android.javax.sip.address.AddressFactory;
import android.javax.sip.address.URI;
import android.javax.sip.header.ExpiresHeader;
import android.javax.sip.header.HeaderFactory;
import android.javax.sip.header.ViaHeader;
import android.javax.sip.message.MessageFactory;
import android.javax.sip.message.Request;
import android.os.AsyncTask;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Дмитрий on 16.06.2015.
 */
public class SipRegister2 extends AsyncTask<Object, Object, Object> {
    private void sendRegister() {
        try {
            System.out.println();

            SipStack2.getInstance();
            AddressFactory addressFactory = SipStack2.addressFactory;

            SipStack2.getInstance();
            SipProvider sipProvider = SipStack2.sipProvider;

            SipStack2.getInstance();
            MessageFactory messageFactory = SipStack2.messageFactory;

            SipStack2.getInstance();
            HeaderFactory headerFactory = SipStack2.headerFactory;

            // Create addresses and via header for the request
            Address fromAddress = addressFactory.createAddress("sip:"
                    + SipStack2.sipUserName + "@" + SipStack2.remoteIp);
            fromAddress.setDisplayName(SipStack2.sipUserName);

            Address toAddress = addressFactory.createAddress("sip:"
                    + SipStack2.sipUserName + "@" + SipStack2.remoteIp);
            toAddress.setDisplayName(SipStack2.sipUserName);

            Address contactAddress = createContactAddress();
            ArrayList<ViaHeader> viaHeaders = SipStack2.createViaHeader();
            URI requestURI = addressFactory.createAddress(
                    "sip:" + SipStack2.remoteEndpoint).getURI();

            // Build the request
            final Request request = messageFactory.createRequest(requestURI,
                    Request.REGISTER, sipProvider.getNewCallId(),
                    headerFactory.createCSeqHeader(1l, Request.REGISTER),
                    headerFactory.createFromHeader(fromAddress, "c3ff411e"),
                    headerFactory.createToHeader(toAddress, null), viaHeaders,
                    headerFactory.createMaxForwardsHeader(70));

            // Add the contact header
            request.addHeader(headerFactory.createContactHeader(contactAddress));
            ExpiresHeader eh = headerFactory.createExpiresHeader(300);
            request.addHeader(eh);

            // Print the request
            System.out.println(request.toString());
            // Send the request --- triggers an IOException
            // sipProvider.sendRequest(request);

            ClientTransaction transaction = sipProvider
                    .getNewClientTransaction(request);

            // Send the request statefully, through the client transaction.
            transaction.sendRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Address createContactAddress() {
        try {
            SipStack2.getInstance();
            return SipStack2.addressFactory.createAddress("sip:" + SipStack2.sipUserName + "@"
                    + SipStack2.localEndpoint + ";transport=udp"
                    + ";registering_acc="+SipStack2.registeringAcc);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    protected Object doInBackground(Object... params) {
        try {
            sendRegister();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
