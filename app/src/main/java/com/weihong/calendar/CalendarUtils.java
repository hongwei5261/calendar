package com.weihong.calendar;

import android.icu.util.Calendar;

/**
 * Created by wei.hong on 2017/9/6.
 */

public class CalendarUtils {
    // 此日历从1900年1月1日开始计算
    public static final int START_YEAR = 1900;
    // 此日历显示多少年
    public static int SHOW_YEAR = 200;
    // 一年有多少个月份
    public static int MONTH_COUNT = 12;

    static Calendar clickCalendar = Calendar.getInstance();

    public static Calendar getClickCalendar() {
        return clickCalendar;
    }

    public static void setClickCalendar(Calendar calendar) {
        clickCalendar = (Calendar) calendar.clone();
    }

    public static boolean isEquals(Calendar c1, Calendar c2) {
        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH));
    }

    public static boolean isEqualsYM(Calendar c1, Calendar c2) {
        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH));
    }

    public static Calendar coordinateToCalendar(Calendar calendar, int x, int y) {
        if (calendar == null) {
            return null;
        }
        calendar.set(Calendar.DAY_OF_WEEK, x);
        calendar.set(Calendar.WEEK_OF_MONTH, y - 1);
        return calendar;
    }

    public static int[] calendarToCoordinate(Calendar calendar) {
        int[] coordinate = new int[2];
        coordinate[0] = calendar.get(Calendar.DAY_OF_WEEK);
        coordinate[1] = calendar.get(Calendar.WEEK_OF_MONTH) + 1;
        return coordinate;
    }

    public static int[] calendarToYMD(Calendar calendar) {
        int[] ymd = new int[3];
        ymd[0] = calendar.get(Calendar.YEAR);
        ymd[1] = calendar.get(Calendar.MONTH) + 1;
        ymd[2] = calendar.get(Calendar.DAY_OF_MONTH);
        return ymd;
    }

    /**
     * 获取最小年日历
     *
     * @return 日历
     */
    public static Calendar getMinCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(START_YEAR, 1, 1);
        return calendar;
    }


    public static int getDaysInMonth(int year, int month) {
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return 31;
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return 30;
            case Calendar.FEBRUARY:
                return (year % 4 == 0) ? 29 : 28;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }
}
