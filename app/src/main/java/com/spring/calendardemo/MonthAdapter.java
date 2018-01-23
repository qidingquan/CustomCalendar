package com.spring.calendardemo;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2018/1/22.
 */

public class MonthAdapter extends PagerAdapter{

    private SparseArray<RecyclerView> viewList;

    public MonthAdapter(SparseArray<RecyclerView> viewList) {
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return viewList!=null?viewList.size():0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }
}
