package com.spring.calendardemo;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2018/1/24.
 * 宽高相等的TextView
 */

public class SquareTextView extends android.support.v7.widget.AppCompatTextView {

    public SquareTextView(Context context) {
        this(context, null);
    }

    public SquareTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(heightSize,heightSize);
    }
}
