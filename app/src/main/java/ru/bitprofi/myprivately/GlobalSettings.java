package ru.bitprofi.myprivately;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Дмитрий on 27.05.2015.
 */
public class GlobalSettings {
    private static Context m_context;
    private static int m_local_port;
    private static int m_remote_port;
    private static String m_remote_ip;
    private static String m_local_ip;
    private static String m_transport;

    private static String m_user_name;
    private static String m_password;

    private static ArrayList<User> m_users;

    private GlobalSettings() {
        setLocalPort(5080);
        setRemotePort(5060);
        setRemoteIp("5.9.201.234");
        setTransport("udp");
        m_users = new ArrayList<User>();
    }

    private static class SingletonHolder {
        public static final GlobalSettings HOLDER_INSTANCE = new GlobalSettings();
    }

    public static GlobalSettings getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public ArrayList<User> getUsers() {
        return this.m_users;
    }

    public static String getUserName() {
        return m_user_name;
    }

    public Context getContext() {
        return this.m_context;
    }

    public void setContext(Context context) {
        this.m_context = context;
    }

    public static void setSipUserName(String name) {
        m_user_name = name;
    }

    public static String getSipPassword() {
        return m_password;
    }

    public static void setSipPassword(String pwd) {
        m_password = pwd;
    }

    public static String getTransport() {
        return m_transport;
    }

    public  static void setTransport(String tr) {
        m_transport = tr;
    }

    public static String getLocalIp() {
        return m_local_ip;
    }

    public static void setLocalIp(String localIp) {
        m_local_ip = localIp;
    }

    public static int getLocalPort() {
        return m_local_port;
    }

    public static void setLocalPort(int localPort) {
        m_local_port = localPort;
    }

    public static String getLocalEndpoint() {
        return m_local_ip + ":" + m_local_port;
    }

    public static String getRemoteIp() {
        return m_remote_ip;
    }

    public static void setRemoteIp(String remoteIp) {
        m_remote_ip = remoteIp;
    }

    public static int getRemotePort() {
        return m_remote_port;
    }

    public static void setRemotePort(int remotePort) {
        m_remote_port = remotePort;
    }

    public static String getRemoteEndpoint() {
        return m_remote_ip + ":" + m_remote_port;
    }
}
