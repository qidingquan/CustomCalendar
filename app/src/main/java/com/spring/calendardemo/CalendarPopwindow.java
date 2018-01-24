package com.spring.calendardemo;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/24.
 */

public class CalendarPopwindow extends PopupWindow {

    @BindView(R.id.view_calendar)
    CalendarView viewCalendar;
    @BindView(R.id.tv_select_date)
    TextView tvSelectDate;
    @BindView(R.id.tv_year_month)
    TextView tvYearMonth;
    @BindView(R.id.tv_ok)
    TextView tvOk;

    @OnClick({R.id.tv_ok, R.id.tv_select_date})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.tv_ok:
                dismiss();
                break;
            case R.id.tv_select_date:
                DateEntity dateEntity = viewCalendar.getSelectYearMonthDay();
                if (dateEntity != null) {
                    viewCalendar.setSelectDate(dateEntity.getSolarYear(), dateEntity.getSolarMonth(), dateEntity.getSolarDay());
                }
                break;
        }
    }

    public CalendarPopwindow(Context context) {
        this(context, null);
    }

    public CalendarPopwindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarPopwindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_date_popwindow, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(0x64000000));
        setOutsideTouchable(true);

        int[] startTime = {2018, 1, 1};
        int[] endTime = {2020, 2, 1};
        viewCalendar.setData(startTime, endTime);
        int[] today = CalendarUtil.getTodayDate();
        tvYearMonth.setText(today[0] + "年" + today[1] + "月");
        viewCalendar.setCalendarListener(new CalendarView.CalendarListener() {

            @Override
            public void changeMonth(DateEntity dateEntity) {
                tvYearMonth.setText(dateEntity.getSolarYear() + "年" + dateEntity.getSolarMonth() + "月");
            }

            @Override
            public void selectDate(DateEntity dateEntity) {
                tvSelectDate.setText(dateEntity.getSolarYear() + "年" + dateEntity.getSolarMonth() + "月" + dateEntity.getSolarDay() + "日");
            }
        });
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }
}
