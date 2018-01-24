package com.spring.calendardemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private CalendarPopwindow calendarPopwindow;
    private boolean isDismiss = false;

    @OnClick({R.id.btn_date})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_date:

                calendarPopwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        isDismiss = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isDismiss = false;
                            }
                        },200);
                    }
                });
                if (calendarPopwindow != null && !isDismiss && !calendarPopwindow.isShowing()) {
                    calendarPopwindow.showAsDropDown(view);
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        calendarPopwindow = new CalendarPopwindow(this);

    }
}
