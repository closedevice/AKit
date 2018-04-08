package com.closedevice.common;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 */
@SuppressWarnings("unused")
public class TimeUtil {

    public static final long UNIT_MILLI_SECOND = 1000;
    public static final long UNIT_MILLI_MINUTE = UNIT_MILLI_SECOND * 60;
    public static final long UNIT_MILLI_HOUR = UNIT_MILLI_MINUTE * 60;
    public static final long UNIT_MILLI_HALF_DAY = UNIT_MILLI_HOUR * 12;
    public static final long UNIT_MILLI_DAY = UNIT_MILLI_HOUR * 24;

    /**
     * 获取当前时间简单字符串
     *
     * @return
     */
    public static String getSimpleTime() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return fmt.format(new Date());
    }

    public static String getSimpleDate() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        return fmt.format(new Date());
    }

    public static long getTimeDurFrom(long time) {
        return System.currentTimeMillis() - time;
    }

    public static long now() {
        return System.currentTimeMillis();
    }

    public static long getUnixTime() {
        return System.currentTimeMillis() / 1000;
    }



    /*  *                     yyyy-MM-dd 1969-12-31
     *               yyyy-MM-dd HH:mm 1969-12-31 16:00
     *              yyyy-MM-dd HH:mmZ 1969-12-31 16:00-0800
     *              yyyy-MM-dd HH:mmZ 1970-01-01 00:00+0000
     *       yyyy-MM-dd HH:mm:ss.SSSZ 1969-12-31 16:00:00.000-0800
     *       yyyy-MM-dd HH:mm:ss.SSSZ 1970-01-01 00:00:00.000+0000
     *     yyyy-MM-dd'T'HH:mm:ss.SSSZ 1969-12-31T16:00:00.000-0800
     *     yyyy-MM-dd'T'HH:mm:ss.SSSZ 1970-01-01T00:00:00.000+0000*/

    public static final String DATA_FORMAT_TEMPLATE_1 = "yyyy-MM-dd HH:mm";
    public static final String DATA_FORMAT_TEMPLATE_2 = "yyyy-MM-dd HH:mm:ss";

    public static long format(String time, String format) {
        SimpleDateFormat fmt = new SimpleDateFormat(format, Locale.getDefault());
        try {
            Date date = fmt.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            Log.e("TimeUtil", "parse " + time + " with format :" + format + " error, msg:" + e.getMessage());
            return 0;
        }
    }

    public static String timeUtilNow(long from) {
        if (!TextUtils.equals(Locale.getDefault().getLanguage(), Locale.SIMPLIFIED_CHINESE.getLanguage())) {
            return "";
        }
        long now = System.currentTimeMillis();
        if (from == 0) {
            from = now;
        }
        long result = Math.abs(now - from);

        String t = "";
        if (result < 60000) {// 一分钟内
            t = "刚刚";
        } else if (result >= 60000 && result < 3600000) {// 一小时内
            long seconds = result / 60000;
            t = seconds + "分钟前";
        } else if (result >= 3600000 && result < 86400000) {// 一天内
            long seconds = result / 3600000;
            t = seconds + "小时前";
        } else if (result >= 86400000 && result < 1702967296) {// 三十天内
            long seconds = result / 86400000;
            t = seconds + "天前";
        }
        return t;
    }
}
