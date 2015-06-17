package ru.bitprofi.myprivately.sip;

import ru.bitprofi.myprivately.Utils;

/**
 * Created by Дмитрий on 17.06.2015.
 */
public class SipStackSettings {
    public static String userName;
    public static String password = "12345";

    public static String transport      = "udp";
    public static String registeringAcc = "188_40_152_149";
    public static String remoteIp       = "188.40.152.149";

    public static int    remotePort     = 5060;
    public static String remoteEndpoint = remoteIp + ":" + remotePort;

    public static String localIp = Utils.getIPAddress(true);
    public static int    localPort = 5080;
    public static String localEndpoint = SipStackSettings.localIp + ":" + SipStackSettings.localPort;
}