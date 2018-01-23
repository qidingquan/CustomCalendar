package com.spring.calendardemo;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/22.
 * 日历数据操作类
 */

public class CalendarUtil {
    /**
     * 获取当前日期
     *
     * @return
     */
    public static int[] getTodayDate() {

        int[] today = new int[3];

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        today[0] = calendar.get(Calendar.YEAR);
        today[1] = calendar.get(Calendar.MONTH) + 1;
        today[2] = calendar.get(Calendar.DAY_OF_MONTH);

        return today;
    }

    /**
     * 获取某天星期几
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 星期几
     */
    public static int getWeekOfFirstDayOfMonth(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 计算指定月份的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return -1;
        }
    }

    /**
     * 获取上一个月的 年月
     *
     * @param year  当前年
     * @param month 当前月
     * @return 返回上一个月 的 日期
     */
    public static int[] getPreYearMonth(int year, int month) {
        int[] date = new int[2];
        int preYear = year;
        int preMonth = month;
        if (month == 1) {
            preYear--;
            preMonth = 12;
        } else {
            preMonth--;
        }
        date[0] = preYear;
        date[1] = preMonth;
        return date;
    }

    /**
     * 获取下一个月的 年月
     *
     * @param year  当前年
     * @param month 当前月
     * @return 下一个月的 日期
     */
    public static int[] getBackYearMonth(int year, int month) {
        int[] date = new int[2];
        int backYear = year;
        int backMonth = month;
        if (month == 12) {
            backYear++;
            backMonth = 1;
        } else {
            backMonth++;
        }
        date[0] = backYear;
        date[1] = backMonth;
        return date;
    }

    /**
     * 获取两个日期之间的月份数
     * @param date1 开始日期
     * @param date2 结束日期
     * @return 相差的月数
     */
    public static int getMidMonths(int[] date1, int[] date2) {
        int monthNum = 100;//默认100个月后
        if (date1 == null || date2 == null) {
            return monthNum;
        }
        if (date1[0] < date2[0]) {
            monthNum = (date2[0] - date1[0]) * 12 + (12 - date1[1])+1;
        } else if (date1[0] == date2[0]) {
            if (date2[1] > date1[1]) {
                monthNum = date2[1] - date1[1]+1;
            } else if (date2[1] == date1[1]) {
                monthNum = 1;
            }
        }
        return monthNum;
    }
}
