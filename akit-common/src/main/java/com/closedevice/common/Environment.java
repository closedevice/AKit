package com.closedevice.common;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

public class Environment {
    public static String getConfiguration(Resources res) {
        Configuration config = res.getConfiguration();
        String c = "\n {" +
                "\n   fontScale= " + config.fontScale +
                ", \n   locale= " + config.locale +
                ", \n   mcc= " + config.mcc +
                ", \n   screenHeightDp= " + config.screenHeightDp +
                ", \n   screenWidthDp= " + config.screenWidthDp +
                ", \n   touchscreen= " + config.touchscreen +
                ", \n   keyboard= " + config.keyboard +
                ", \n   navigation= " + config.navigation;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            c = c + ",\n   densityDpi= " + config.densityDpi;
        }
        c = c + "\n }";
        return c;
    }

    public static String getBuild() {

        String c = "\n {" +
                "\n   MODEL= " + Build.MODEL +
                ", \n   BOARD= " + Build.BOARD +
                ", \n   BOOTLOADER= " + Build.BOOTLOADER +
                ", \n   BRAND= " + Build.BRAND +
                ", \n   DEVICE= " + Build.DEVICE +
                ", \n   DISPLAY= " + Build.DISPLAY +
                ", \n   FINGERPRINT= " + Build.FINGERPRINT +
                ", \n   HARDWARE= " + Build.HARDWARE +
                ", \n   HOST= " + Build.HOST;
        c = c + "\n }";
        return c;
    }

    public static String getDisplayMetrics(Resources res) {
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        String c = "\n {" +
                "\n   density= " + displayMetrics.density +
                ", \n   densityDpi= " + displayMetrics.densityDpi +
                ", \n   heightPixels= " + displayMetrics.heightPixels +
                ", \n   widthPixels= " + displayMetrics.widthPixels +
                ", \n   scaledDensity= " + displayMetrics.scaledDensity +
                ", \n   xdpi= " + displayMetrics.xdpi +
                ", \n   ydpi= " + displayMetrics.ydpi;
        c = c + "\n }";
        return c;
    }
}
