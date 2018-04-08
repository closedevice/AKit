package com.closedevice.common.encrypt;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;

/**
 * @author liudongdong-os
 * @date 2018/3/19
 */

public class CodecUtil {
    public static String MD5(String string) {
        if (string == null) {
            return "";
        }
        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(string.getBytes("UTF-8"));
        } catch (Exception e) {
            //  will never happen
        }

        if (digest == null) {
            return "";
        }

        byte[] byteArray = digest.digest();
        StringBuilder builder = new StringBuilder();

        for (byte bt : byteArray) {
            if (Integer.toHexString(0xFF & bt).length() == 1) {
                builder.append("0").append(Integer.toHexString(0xFF & bt));
            } else {
                builder.append(Integer.toHexString(0xFF & bt));
            }
        }
        return builder.toString();
    }

    public static String URLEncode(String s) {
        if (TextUtils.isEmpty(s))
            return "";
        try {
            return URLEncoder.encode(s, "utf-8");
        } catch (UnsupportedEncodingException e) {
            //  will never happen
        }
        return "";
    }

    public static String URLDecode(String s) {
        if (TextUtils.isEmpty(s))
            return "";
        try {
            return URLEncoder.encode(s, "utf-8");
        } catch (UnsupportedEncodingException e) {
            //  will never happen
        }
        return "";
    }

}
