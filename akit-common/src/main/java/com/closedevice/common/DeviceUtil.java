package com.closedevice.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author liudongdong
 * @date 2018/1/12
 */

public class DeviceUtil {

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getVendor() {
        return Build.MANUFACTURER;
    }

    public static String getBuildSerial() {
        String id = Build.SERIAL;
        return id;
    }

    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static String getDeviceID(Context context) {
        //    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return null;
        }
        try {
            String id = tm.getDeviceId();
            return id;
        } catch (Exception e) {
            //ignore
        }
        return null;
    }

    @Nullable
    public static String getSimSerialNumber(Context context) {
        //    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return null;
        }
        try {
            String num = tm.getSimSerialNumber();
            return num;
        } catch (Exception e) {
            //ignore
        }
        return null;
    }

    public static String getMacAddress() {
        String address = "00:00:00:00:00:00";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface netWork = interfaces.nextElement();
                byte[] by = netWork.getHardwareAddress();
                if (by == null || by.length == 0) {
                    continue;
                }
                StringBuilder builder = new StringBuilder();
                for (byte b : by) {
                    builder.append(String.format("%02X:", b));
                }
                if (builder.length() > 0) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                String mac = builder.toString();
                if (netWork.getName().equals("wlan0")) {
                    address = mac;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return address;
    }

    public static String getIMEI(Context context) {
        String imei = getDeviceID(context);
        return imei == null ? "" : imei;
    }

    public static String getMcc(Context context) {
        TelephonyManager mTelMan = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            String operator = mTelMan.getNetworkOperator();
            if (TextUtils.isEmpty(operator) || operator.equals("00000")) {
                return "";
            }
            return operator.substring(0, 3);
        } catch (Exception e) {
            //
        }
        return "";
    }

    public static String getMnc(Context context) {
        TelephonyManager mTelMan = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            String operator = mTelMan.getNetworkOperator();
            CellLocation location = mTelMan.getCellLocation();
            String mnc = "";
            if (location instanceof GsmCellLocation) {
                mnc = operator.substring(3);
            } else if (location instanceof CdmaCellLocation) {
                mnc = String.valueOf(((CdmaCellLocation) location).getSystemId());
            }
            return mnc;
        } catch (Exception e) {
            //
        }
        return "";
    }

    public static String getLac(Context context) {
        TelephonyManager mTelMan = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            String operator = mTelMan.getNetworkOperator();
            CellLocation location = mTelMan.getCellLocation();
            String lac = "";
            if (location instanceof GsmCellLocation) {
                lac = ((GsmCellLocation) location).getLac() + "";
            } else if (location instanceof CdmaCellLocation) {
                lac = ((CdmaCellLocation) location).getNetworkId() + "";
            }
            return lac;
        } catch (Exception e) {
            //
        }
        return "";
    }

    public static String getCellId(Context context) {
        TelephonyManager mTelMan = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            String operator = mTelMan.getNetworkOperator();
            CellLocation location = mTelMan.getCellLocation();
            String id = "";
            if (location instanceof GsmCellLocation) {
                id = ((GsmCellLocation) location).getCid() + "";
            } else if (location instanceof CdmaCellLocation) {
                id = ((CdmaCellLocation) location).getBaseStationId() + "";
            }
            return id;
        } catch (Exception e) {
            //
        }
        return "";
    }

    private static String getDeviceSerial() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }

    public static String getOperator(Context context) {
        TelephonyManager mTelMan = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return mTelMan.getSimOperator();
        } catch (Exception e) {
            //ignore
        }
        return "";
    }

    public static String getOperatorName(Context context) {
        TelephonyManager mTelMan = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return mTelMan.getNetworkOperatorName();
        } catch (Exception e) {
            //ignore
        }
        return "";
    }

    public static boolean hasTouchScreen(Context context) {
        boolean feature = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN);
        boolean res = Resources.getSystem().getConfiguration().touchscreen > 1;
        return feature && res;
    }

    public static boolean isTabletDevice(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean hasHardKeyNavigation() {
        return Resources.getSystem().getConfiguration().navigation >= 2;
    }

    public static int getDeviceWidth() {
        DisplayMetrics size = Resources.getSystem().getDisplayMetrics();
        return size.widthPixels;
    }

    public static int getDeviceHeight() {
        DisplayMetrics size = Resources.getSystem().getDisplayMetrics();
        return size.heightPixels;
    }

    public static float getDeviceDensity() {
        DisplayMetrics size = Resources.getSystem().getDisplayMetrics();
        return size.density;
    }

    public static String getUserAgent() {
        String userAgent = System.getProperty("http.agent");
        return userAgent;
    }

    public static int CHINA_MOBILE = 1;
    public static int CHINA_UNICOM = 3;
    public static int CHINA_TELECOM = 2;
    public static int CHINA_UNKNOWN = 0;
    public static int CHINA_OTHER = 99;

    public static int getMobileType(Context context) {
        int type = 0;
        TelephonyManager iPhoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String iNumeric = iPhoneManager.getSimOperator();
        if (iNumeric.length() > 0) {
            if (iNumeric.equals("46000") || iNumeric.equals("46002")) {
                type = CHINA_MOBILE;
            } else if (iNumeric.equals("46001")) {
                type = CHINA_UNICOM;
            } else if (iNumeric.equals("46003")) {
                type = CHINA_TELECOM;
            } else {
                type = CHINA_OTHER;
            }
        } else {
            type = CHINA_UNKNOWN;
        }
        return type;
    }
}
