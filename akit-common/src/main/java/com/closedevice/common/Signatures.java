package com.closedevice.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.closedevice.common.encrypt.EncryptUtils;

import java.security.MessageDigest;
import java.util.Collections;
import java.util.HashSet;

public class Signatures {

    @SuppressLint("PackageManagerGetSignatures")
    public static Signature[] getSystemSignatures(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo androidPackageInfo = pm.getPackageInfo("android", PackageManager.GET_SIGNATURES);

            return androidPackageInfo.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            //never happen
        }
        return null;
    }

    @SuppressLint("PackageManagerGetSignatures")
    public static Signature[] getAppSignatures(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo appPackageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            return appPackageInfo.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            //never happen
        }
        return null;
    }


    /**
     * 获取应用签名的的SHA1值
     * <p>可据此判断高德，百度地图key是否正确</p>
     *
     * @param context 签名
     * @return 应用签名的SHA1字符串, 比如：53:FD:54:DC:19:0F:11:AC:B5:22:9E:F1:1A:68:88:1B:8B:E8:54:42
     */
    public static String getAppSignatureSHA1(Context context) {
        Signature[] signature = getAppSignatures(context);
        if (signature == null) return null;
        return EncryptUtils.encryptSHA1ToString(signature[0].toByteArray()).
                replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
    }


    public interface LogConsumer {
        void accept(String log);
    }

    public static void dumpSignatures(String name, Signature[] signatures) {
        dumpSignatures(name, signatures, null);
    }

    public static void dumpSignatures(String name, Signature[] signatures, LogConsumer handler) {
        if (signatures == null || signatures.length == 0) {
            return;
        }
        if (handler == null) {
            System.out.println("start dump " + name + " signatures: ");
        } else {
            handler.accept("start dump " + name + " signatures: ");
        }
        for (int i = 0; i < signatures.length; i++) {
            if (handler == null) {
                System.out.println("signature@" + i + ": " + MD5(signatures[i].toByteArray()).toUpperCase());
            } else {
                handler.accept("signature@" + i + ": " + MD5(signatures[i].toByteArray()).toUpperCase());
            }
        }
        if (handler == null) {
            System.out.println("stop");
        } else {
            handler.accept("stop");
        }
    }

    public static final int SIGNATURE_MATCH = 0;
    public static final int SIGNATURE_NEITHER_SIGNED = 1;
    public static final int SIGNATURE_FIRST_NOT_SIGNED = -1;
    public static final int SIGNATURE_SECOND_NOT_SIGNED = -2;
    public static final int SIGNATURE_NO_MATCH = -3;

    public static int compareSignatures(Signature[] s1, Signature[] s2) {
        if (s1 == null) {
            return s2 == null
                    ? SIGNATURE_NEITHER_SIGNED
                    : SIGNATURE_FIRST_NOT_SIGNED;
        }

        if (s2 == null) {
            return SIGNATURE_SECOND_NOT_SIGNED;
        }

        if (s1.length != s2.length) {
            return SIGNATURE_NO_MATCH;
        }

        // Since both signature sets are of size 1, we can compare without HashSets.
        if (s1.length == 1) {
            return s1[0].equals(s2[0]) ?
                    SIGNATURE_MATCH :
                    SIGNATURE_NO_MATCH;
        }

        HashSet<Signature> set1 = new HashSet<>();
        Collections.addAll(set1, s1);
        HashSet<Signature> set2 = new HashSet<>();
        Collections.addAll(set2, s2);

        // Make sure s2 contains all signatures in s1.
        if (set1.equals(set2)) {
            return SIGNATURE_MATCH;
        }
        return SIGNATURE_NO_MATCH;
    }


    private static String MD5(byte[] bytes) {
        MessageDigest digest;
        StringBuilder builder = new StringBuilder();
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(bytes);
            byte[] byteArray = digest.digest();
            for (byte b : byteArray) {
                if (Integer.toHexString(0xFF & b).length() == 1) {
                    builder.append("0").append(Integer.toHexString(0xFF & b));
                } else {
                    builder.append(Integer.toHexString(0xFF & b));
                }
            }
        } catch (Exception e) {
            //ignore
        }
        return builder.toString();
    }
}