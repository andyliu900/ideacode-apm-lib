package com.ideacode.apm.library.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by Administrator on 2017/6/28.
 * 时间相关的工具类
 */

public class TimeUtils {

    public static final String DATETIMESECOND = "yyyyMMddHHmmss";
    public static final String DATETIMESECOND_SPLIT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIMEMILLIS_SPLIT = "yyyy-MM-dd HH:mm:ss:SSS";

    /**
     * 将秒数转换成 00:30 的格式
     */
    public static String formatTime(int time) {
        int sec = time % 60;
        int min = time / 60;
        return (min < 10 ? ("0" + min) : min) + ":" + (sec < 10 ? ("0" + sec) : sec);
    }

    /**
     * 毫秒转时分秒
     *
     * @param time
     * @return
     */
    public static String millsToTime(long time) {
        if (time <= 0) {
            return "0毫秒";
        }

        if (time > 0 && time < 1000) {
            return time + "毫秒";
        }

        int second = (int)time / 1000;
        long mills = time % 1000;
        if (second >= 1 && second < 60) {
            return second + "秒" + mills + "毫秒";
        }

        int minute = second / 60;
        second = (int)(time - minute * 60 * 1000) / 60;
        mills = time - minute * 60 * 1000 - second * 1000;
        if (minute >= 1 && minute < 60) {
            return minute + "分" + second + "秒" + mills + "毫秒";
        }

        int hour = minute / 60;
        minute = (int)(time - hour * 60 * 60 * 1000) / 60;
        second = (int)(time - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 60;
        mills = time - hour * 60 * 60 * 1000 - minute * 60 * 1000 - second * 1000;
        if (hour > 99) {
            return "99小时59分钟59秒";
        } else {
            return hour + "小时" + minute + "分" + second + "秒" + mills + "毫秒";
        }
    }

    /**
     * 秒转时分
     *
     * @param time
     * @return
     */
    public static String secToOvenTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
//        int second = 0;
        if (time <= 0) {
            return "0分";
        } else {
            minute = time / 60;
            if (minute < 60) {
//                second = time % 60;
                timeStr = unitFormat(minute) + "分";
            } else {
                hour = minute / 60;
                if (hour > 99) {
                    return "99小时59分";
                }
                minute = minute % 60;
//                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + "小时" + unitFormat(minute) + "分";
            }
        }
        return timeStr;
    }


    public static String minToTime(int min) {
        int hour = 0;
        String timeStr = null;
        if (min <= 0) {
            return "0分钟";
        } else {
            if (min < 60) {
                timeStr = min + "分钟";
            } else {
                hour = min / 60;
                int minute = min % 60;
                if (minute != 0) {
                    timeStr = unitFormat(hour) + "小时" + unitFormat(minute) + "分钟";
                } else {
                    timeStr = unitFormat(hour) + "小时";
                }
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

    public static String stringForTime(int timeMs) {
        StringBuilder mFormatBuilder;
        Formatter mFormatter;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static String getFormatTime(long time, String pormatPattern) {
        SimpleDateFormat df = new SimpleDateFormat(pormatPattern);
        return df.format(time);
    }

    public static String getFormatWeek(String dtTime){
        String strRet = "";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        Date date;
        if(TextUtils.isEmpty(dtTime)){
            calendar.setTime(new Date(System.currentTimeMillis()));
        } else {
            try {
                date = df.parse(dtTime);
            } catch (ParseException e) {
                date = null;
                e.printStackTrace();
            }
            if(date != null){
                calendar.setTime(new Date(date.getTime()));
            }
        }
        int nRet = calendar.get(Calendar.DAY_OF_WEEK);
        switch (nRet){
            case 1:
                strRet = "星期日";
                break;
            case 2:
                strRet = "星期一";
                break;
            case 3:
                strRet = "星期二";
                break;
            case 4:
                strRet = "星期三";
                break;
            case 5:
                strRet = "星期四";
                break;
            case 6:
                strRet = "星期五";
                break;
            case 7:
                strRet = "星期六";
                break;
        }
        return  strRet;
    }

    public static Date long2Date(long time) {
        Date date = new Date();
        date.setTime(time);

        return date;
    }

}
