package com.spring.calendardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_calendar)
    CalendarView viewCalendar;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_month)
    TextView tvMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        int[] startTime = {2018, 1, 22};
        int[] endTime = {2018, 3, 22};
        viewCalendar.setData(startTime, endTime);
        viewCalendar.setCalendarListener(new CalendarView.CalendarListener() {

            @Override
            public void changeMonth(DateEntity dateEntity) {
                tvMonth.setText(dateEntity.getSolarYear() + "年" + dateEntity.getSolarMonth() + "月");
            }

            @Override
            public void selectDate(DateEntity dateEntity) {
                tvDate.setText(dateEntity.getSolarYear() + "年" + dateEntity.getSolarMonth() + "月" + dateEntity.getSolarDay() + "日");
            }
        });
    }
}
