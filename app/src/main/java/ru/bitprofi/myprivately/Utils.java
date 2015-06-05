package ru.bitprofi.myprivately;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.widget.TabHost;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Дмитрий on 27.05.2015.
 */
public class Utils {
    private Random _random = null;

    private Utils() {
        _random = new Random();
    }

    private static class SingletonHolder {
        public static final Utils HOLDER_INSTANCE = new Utils();
    }

    public static Utils getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections
                    .list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf
                        .getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port
                                // suffix
                                return delim < 0 ? sAddr : sAddr.substring(0,
                                        delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    public int getRandomInt(int n) {
        return _random.nextInt(n);
    }

    public boolean getRandomBoolean() {
        return _random.nextBoolean();
    }

    public void setHeaderActionBar(Activity activity, String heading, boolean backBtn) {
        ActionBar actionBar = (ActionBar)activity.getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        //actionBar.setHomeButtonEnabled(true);
        ///actionBar.setDisplayHomeAsUpEnabled(backBtn);
        //actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.mainblue)));
        //actionBar.setIcon(R.mipmap.ic_launcher);
        //actionBar.setTitle(heading);
        //actionBar.show();
    }
}
