package com.closedevice.common;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author liudongdong
 * @date 2018/1/12
 */

public class NetworkUtil {

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static Boolean isNetworkConnected(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        return !(networkinfo == null || !networkinfo.isAvailable());
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    public static final String NET_TYPE_NULL = "NULL";
    public static final String NET_TYPE_WIFI = "WIFI";
    public static final String NET_TYPE_2G = "2G";
    public static final String NET_TYPE_3G = "3G";
    public static final String NET_TYPE_4G = "4G";
    public static final String NET_TYPE_UNKNOWN = "UNKNOWN";

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static String getNetworkType(Context context) {
        try {
            boolean netConn = isNetworkConnected(context);
            if (!netConn) {
                return NET_TYPE_NULL;
            }

            if (isWifiConnected(context)) {
                return NET_TYPE_WIFI;
            }

            TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            int networkType = mTelephonyManager.getNetworkType();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return NET_TYPE_2G;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return NET_TYPE_3G;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return NET_TYPE_4G;
                default:
                    return NET_TYPE_UNKNOWN;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NET_TYPE_UNKNOWN;
    }

    private static String sLastKnownIp;
    private static long sLastUpdateTime;
    public static final long UPDATE_INTERVAL = 1000 * 60 * 5;

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE})
    public static String getIpLazy(Context context) {
        if (!TextUtils.isEmpty(sLastKnownIp) && (System.currentTimeMillis() - sLastUpdateTime) < UPDATE_INTERVAL) {
            return sLastKnownIp;
        }
        String ip = getIp(context);
        if (!TextUtils.isEmpty(ip)) {
            sLastKnownIp = ip;
            sLastUpdateTime = System.currentTimeMillis();
        }
        return sLastKnownIp;
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE})
    public static String getIp(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // 3/4g
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface networkInterface = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    //ignore
                }
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //  wifi
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = toIpString(wifiInfo.getIpAddress());
                return ipAddress;
            } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                // local
                return getLocalIp();
            }
        }
        return null;
    }

    private static String toIpString(int ip) {
        StringBuilder str = new StringBuilder()
                .append((ip & 0xFF)).append(".")
                .append(((ip >> 8) & 0xFF)).append(".")
                .append(((ip >> 16) & 0xFF)).append(".")
                .append((ip >> 24 & 0xFF));
        return str.toString();
    }

    public static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            //ignore here
        }
        return "0.0.0.0";
    }

}
