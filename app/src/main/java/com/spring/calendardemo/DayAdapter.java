package com.spring.calendardemo;

import android.app.job.JobInfo;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/22.
 */

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.VH> {

    private List<DateEntity> dateList;
    private DateEntity dateEntity;
    public DayAdapter(List<DateEntity> dateList) {
        this.dateList = dateList;
    }

    public List<DateEntity> getDateList() {
        return dateList;
    }

    /**
     * 获取当前年月
     *
     * @return
     */
    public DateEntity getYearMonth() {
        return dateEntity;
    }


    public void updateData(List<DateEntity> dateList) {
        this.dateList = dateList;
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        final DateEntity date = dateList.get(position);
        holder.tvSolar.setText(date.getSolarDay() + "");
        switch (date.getType()) {
            case -1://上一月数据
                holder.tvSolar.setTextColor(holder.tvSolar.getContext().getResources().getColor(R.color.light_gray));
                break;
            case 0://本月数据
                dateEntity = date;
                holder.tvSolar.setTextColor(Color.BLACK);
                break;
            case 1://下一月数据
                holder.tvSolar.setTextColor(holder.tvSolar.getContext().getResources().getColor(R.color.light_gray));
                break;
            default:
                break;
        }
        holder.tvSolar.setSelected(date.isSelected());
        if (!date.isSelected() && date.isToday()) {
            holder.tvSolar.setBackgroundResource(R.drawable.bg_today);
        }
        holder.tvSolar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(position, date);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateList != null ? dateList.size() : 0;
    }

    static class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_solar)
        TextView tvSolar;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, DateEntity dateEntity);
    }
}
