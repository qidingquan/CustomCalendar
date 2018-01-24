package com.spring.calendardemo;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 * 日历控件
 */

public class CalendarView extends ViewPager implements DayAdapter.OnItemClickListener {

    private SparseArray<RecyclerView> viewList;//每个月的控件
    private SparseArray<DayAdapter> dayAdapterList;//每个月数据适配器
    private MonthAdapter pagerAdapter;
    private int[] today;//今天的时间 年月日
    private int row = 6;//显示6行
    private int colnumn = 7;//显示7列
    private int[] selectPosion;//当前选中的日期位置    0 表示月的位置 1 表示月中某天的位置
    private int monthIndex;//当前月索引
    private int monthNum;//显示的月份数
    private int[] startTime;//开始时间
    private int[] endTime;//结束时间

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化
        today = CalendarUtil.getTodayDate();
        viewList = new SparseArray<>();
        dayAdapterList = new SparseArray<>();
        selectPosion = new int[]{-1, -1};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int newWidthSize = widthSize;
        int newHeightSize = 0;

        if (heightMode == MeasureSpec.EXACTLY) {
            newHeightSize = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //根据子控件的大小设置
            int childNum = getChildCount();
            if (childNum > 0) {
                View childView = getChildAt(0);
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                newHeightSize += childView.getMeasuredHeight();
            }
        }
        setMeasuredDimension(newWidthSize, newHeightSize);
    }

    /**
     * 获取当前的年月
     *
     * @return
     */
    private DateEntity getSelectYearMonth() {
        DateEntity dateEntity = new DateEntity();
        if (null != dayAdapterList && dayAdapterList.indexOfKey(monthIndex) != -1) {
            dateEntity = dayAdapterList.get(monthIndex).getYearMonth();
        }
        return dateEntity;
    }

    /**
     * 获取选中的年月日
     *
     * @return
     */
    public DateEntity getSelectYearMonthDay() {
        DateEntity dateEntity = new DateEntity();
        if (selectPosion[0] != -1 && dayAdapterList.indexOfKey(selectPosion[0]) != -1) {
            DayAdapter dayAdapter = dayAdapterList.get(selectPosion[0]);
            if (dayAdapter.getDateList() != null && selectPosion[1] != -1
                    && selectPosion[1] < dayAdapter.getDateList().size()) {
                dateEntity = dayAdapter.getDateList().get(selectPosion[1]);
            }
        }
        return dateEntity;
    }

    /**
     * 设置日历数据
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    public void setData(int[] startTime, int[] endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        //获取对应得年月
        int year = startTime[0];
        //获取结束时间和开始时间相差的月份
        monthNum = CalendarUtil.getMidMonths(startTime, endTime);
        //初始化月的数据
        for (int i = 0; i < monthNum; i++) {
            //初始化月
            RecyclerView recyclerView = new RecyclerView(getContext());
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), colnumn));
            int month = startTime[1] + i;
            year = year + (month > 12 && month % 12 == 1 ? 1 : 0);
            month = month % 12 == 0 ? 12 : month % 12;
            //获取每月显示的数据
            List<DateEntity> dateEntities = getAllDayOfMonth(year, month);
            DayAdapter dayAdapter = new DayAdapter(dateEntities);
            recyclerView.setAdapter(dayAdapter);
            dayAdapter.setItemClickListener(this);
            //将数据保存
            viewList.put(i, recyclerView);
            dayAdapterList.put(i, dayAdapter);
        }

        pagerAdapter = new MonthAdapter(viewList);
        setAdapter(pagerAdapter);
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                monthIndex = position;
                if (calendarListener != null) {
                    DateEntity dateEntity = getSelectYearMonth();
                    calendarListener.changeMonth(dateEntity);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        selectPosion[0] = monthIndex;//设置当前选中日期的月的位置

    }

    /**
     * 设置选中的年月日
     *
     * @param year
     * @param month
     * @param day
     */
    public void setSelectDate(int year, int month, int day) {
        int[] datePosition = getDatePosition(year, month, day);
        setCurrentItem(datePosition[0]);
        changeSelectState(datePosition[1]);
    }

    /**
     * 跳转到今天
     */
    public void toToday() {
        int[] datePosition = getDatePosition(today[0], today[1], today[2]);
        setCurrentItem(datePosition[0]);
    }

    /**
     * 获取某个日期的 月位置和天位置
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    private int[] getDatePosition(int year, int month, int day) {
        int[] datePosition = new int[2];
        outer:
        for (int i = 0; i < dayAdapterList.size(); i++) {
            DayAdapter dayAdapter = dayAdapterList.get(i);
            List<DateEntity> dateList = dayAdapter.getDateList();
            if (dateList != null && dateList.size() > 0) {
                for (int j = 0; j < dateList.size(); j++) {
                    if (year == dateList.get(j).getSolarYear()
                            && month == dateList.get(j).getSolarMonth()
                            && day == dateList.get(j).getSolarDay()
                            && 0 == dateList.get(j).getType()) {
                        //年月日相同 并且为本月数据
                        datePosition[0] = i;
                        datePosition[1] = j;
                        break outer;
                    }
                }
            }
        }
        return datePosition;
    }

    /**
     * 获取一个月的数据
     *
     * @param year  当前年
     * @param month 当前月
     * @return 当前月的数据集合
     */
    private List<DateEntity> getAllDayOfMonth(int year, int month) {
        List<DateEntity> dateEntities = new ArrayList<>();
        //获取本月第一天星期几
        int weekOfFirstDay = CalendarUtil.getWeekOfFirstDayOfMonth(year, month, 1);
        //获取上一个月最大天数
        int[] preYearMonth = CalendarUtil.getPreYearMonth(year, month);
        //获取本月显示的上一个月的数据
        int preMaxDay = CalendarUtil.getMonthDays(preYearMonth[0], preYearMonth[1]);
        for (int i = 1; i <= weekOfFirstDay; i++) {
            dateEntities.add(getOneDayData(preYearMonth[0], preYearMonth[1], preMaxDay - weekOfFirstDay + i, -1));
        }

        //获取本月显示的数据
        int curMaxDay = CalendarUtil.getMonthDays(year, month);
        for (int i = 1; i <= curMaxDay; i++) {
            dateEntities.add(getOneDayData(year, month, i, 0));
        }

        //获取下一个月的数据
        //获取剩下的位置数目
        int backMonthMax = row * colnumn - dateEntities.size();
        //获取下一个月的年月
        int[] backYearMonth = CalendarUtil.getBackYearMonth(year, month);
        for (int i = 1; i <= backMonthMax; i++) {
            dateEntities.add(getOneDayData(backYearMonth[0], backYearMonth[1], i, 1));
        }

        return dateEntities;
    }

    /**
     * 获取某一天的数据
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @param type  -1 表示上一个月的数据 0表示本月的数据 1表示下一个月的数据
     * @return 数据对象
     */
    private DateEntity getOneDayData(int year, int month, int day, int type) {
        DateEntity dateEntity = new DateEntity();
        dateEntity.setSolarYear(year);
        dateEntity.setSolarMonth(month);
        dateEntity.setSolarDay(day);
        dateEntity.setType(type);
        //小于开始时间设置该日期不能操作
        if (startTime != null && startTime.length > 2) {
            if (year < startTime[0]) {
                dateEntity.setDisEnable(true);
            } else if (year == startTime[0]) {
                if (month < startTime[1]) {
                    dateEntity.setDisEnable(true);
                } else if (month == startTime[1]) {
                    if (day < startTime[2]) {
                        dateEntity.setDisEnable(true);
                    }
                }
            }
        }
        //大于结束时间设置该日期不能用
        if (endTime != null && endTime.length > 2) {
            if (year > endTime[0]) {
                dateEntity.setDisEnable(true);
            } else if (year == endTime[0]) {
                if (month > endTime[1]) {
                    dateEntity.setDisEnable(true);
                } else if (month == endTime[1]) {
                    if (day > endTime[2]) {
                        dateEntity.setDisEnable(true);
                    }
                }
            }
        }
        if (year == today[0] && month == today[1] && day == today[2]) {
            dateEntity.setToday(true);
        }
        return dateEntity;
    }

    @Override
    public void onItemClick(int position, DateEntity dateEntity) {
        switch (dateEntity.getType()) {
            case -1://上一月数据
                if (monthIndex > 0) {//还有上一页数据
                    setCurrentItem(--monthIndex);
                    changeSelectState(getDatePosition(monthIndex, dateEntity));
                }
                break;
            case 0://本月数据
                changeSelectState(position);
                break;
            case 1://下一月数据
                if (monthIndex < monthNum - 1) {//还有下一页数据
                    setCurrentItem(++monthIndex);
                    //获取该日期在下一个月的位置 position
                    changeSelectState(getDatePosition(monthIndex, dateEntity));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取某个日期在某月中的位置
     *
     * @param monthIndex 所在月的位置
     * @param dateEntity 日期数据
     * @return 位置
     */
    private int getDatePosition(int monthIndex, DateEntity dateEntity) {
        int position = 0;
        if (dayAdapterList != null && dayAdapterList.indexOfKey(monthIndex) != -1) {
            DayAdapter dayAdapter = dayAdapterList.get(monthIndex);
            List<DateEntity> dateList = dayAdapter.getDateList();
            if (dateList != null && dateList.size() > 0) {
                for (int i = 0; i < dateList.size(); i++) {
                    if (dateList.get(i).getSolarYear() == dateEntity.getSolarYear()
                            && dateList.get(i).getSolarMonth() == dateEntity.getSolarMonth()
                            && dateList.get(i).getSolarDay() == dateEntity.getSolarDay()) {
                        position = i;
                        break;
                    }
                }
            }
        }
        return position;
    }

    /**
     * 切换选中状态
     *
     * @param position 当前某月中天的选中位置
     */
    private void changeSelectState(int position) {
        //将上一个选中的位置设置为不选中
        if (selectPosion[0] != -1 && dayAdapterList.indexOfKey(selectPosion[0]) != -1) {
            DayAdapter dayAdapter = dayAdapterList.get(selectPosion[0]);
            if (dayAdapter.getDateList() != null && selectPosion[1] != -1
                    && selectPosion[1] < dayAdapter.getDateList().size()) {
                DateEntity preDateEntity = dayAdapter.getDateList().get(selectPosion[1]);
                preDateEntity.setSelected(false);
            }
            //刷新上一个被选中的按钮状态
            if (dayAdapter != null) {
                dayAdapter.notifyDataSetChanged();
            }
        }
        selectPosion[0] = monthIndex;//设置当前选中日期的月的位置
        selectPosion[1] = position;//设置当前选中日期在这个月中天的位置
        //将当前位置设置为选中
        if (selectPosion[0] != -1 && dayAdapterList.indexOfKey(selectPosion[0]) != -1) {
            DayAdapter dayAdapter = dayAdapterList.get(selectPosion[0]);
            if (dayAdapter.getDateList() != null && selectPosion[1] != -1
                    && selectPosion[1] < dayAdapter.getDateList().size()) {
                DateEntity preDateEntity = dayAdapter.getDateList().get(selectPosion[1]);
                preDateEntity.setSelected(true);
            }
            //刷新上一个被选中的按钮状态
            if (dayAdapter != null) {
                dayAdapter.notifyDataSetChanged();
            }
        }
        if (calendarListener != null) {
            DateEntity dateEntity = getSelectYearMonthDay();
            calendarListener.selectDate(dateEntity);
        }
    }

    private CalendarListener calendarListener;

    public void setCalendarListener(CalendarListener calendarListener) {
        this.calendarListener = calendarListener;
    }


    public interface CalendarListener {

        void changeMonth(DateEntity dateEntity);

        void selectDate(DateEntity dateEntity);
    }
}
