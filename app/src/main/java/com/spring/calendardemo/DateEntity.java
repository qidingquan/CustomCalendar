package com.spring.calendardemo;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/22.
 * 时间数据类
 */

public class DateEntity implements Serializable {

    private int solarYear;//阳历年
    private int solarMonth;//阳历月
    private int solarDay;//阳历天
    private String lunarYear;//阴历年
    private String lunarMonth;//阴历月
    private String lunarDay;//阴历天
    private int type;//-1表示前一个月的数据 0 表示上一个月的数据 1表示下一个月的数据
    private boolean isSelected;//是否当前选中日期
    private boolean isToday;//是否是今天

    public int getSolarYear() {
        return solarYear;
    }

    public void setSolarYear(int solarYear) {
        this.solarYear = solarYear;
    }

    public int getSolarMonth() {
        return solarMonth;
    }

    public void setSolarMonth(int solarMonth) {
        this.solarMonth = solarMonth;
    }

    public int getSolarDay() {
        return solarDay;
    }

    public void setSolarDay(int solarDay) {
        this.solarDay = solarDay;
    }

    public String getLunarDay() {
        return lunarDay;
    }

    public void setLunarDay(String lunarDay) {
        this.lunarDay = lunarDay;
    }

    public String getLunarYear() {
        return lunarYear;
    }

    public void setLunarYear(String lunarYear) {
        this.lunarYear = lunarYear;
    }

    public String getLunarMonth() {
        return lunarMonth;
    }

    public void setLunarMonth(String lunarMonth) {
        this.lunarMonth = lunarMonth;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }
}
