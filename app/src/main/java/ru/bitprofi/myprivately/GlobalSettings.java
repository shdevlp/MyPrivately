package ru.bitprofi.myprivately;

/**
 * Created by Дмитрий on 27.05.2015.
 */
public class GlobalSettings {
    private static int _localPort;
    private static int _remotePort;
    private static String _remoteIp;
    private static String _localIp;
    private static String _transport;
    private static String _sipUserName;
    private static String _sipPassword;

    private GlobalSettings() {
        setLocalPort(5080);
        setRemotePort(5060);
        setRemoteIp("5.9.201.234");
        setTransport("udp");
    }

    private static class SingletonHolder {
        public static final GlobalSettings HOLDER_INSTANCE = new GlobalSettings();
    }

    public static GlobalSettings getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public static String getSipUserName() {
        return _sipUserName;
    }

    public static void setSipUserName(String name) {
        _sipUserName = name;
    }

    public static String getSipPassword() {
        return _sipPassword;
    }

    public static void setSipPassword(String pwd) {
        _sipPassword = pwd;
    }

    public static String getTransport() {
        return _transport;
    }

    public  static void setTransport(String tr) {
        _transport = tr;
    }

    public static String getLocalIp() {
        return _localIp;
    }

    public static void setLocalIp(String localIp) {
        _localIp = localIp;
    }

    public static int getLocalPort() {
        return _localPort;
    }

    public static void setLocalPort(int localPort) {
        _localPort = localPort;
    }

    public static String getLocalEndpoint() {
        return _localIp + ":" + _localPort;
    }

    public static String getRemoteIp() {
        return _remoteIp;
    }

    public static void setRemoteIp(String remoteIp) {
        _remoteIp = remoteIp;
    }

    public static int getRemotePort() {
        return _remotePort;
    }

    public static void setRemotePort(int remotePort) {
        _remotePort = remotePort;
    }

    public static String getRemoteEndpoint() {
        return _remoteIp + ":" + _remotePort;
    }
}