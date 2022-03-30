package com.yyide.chatim.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.model.WeekTime;
import com.yyide.chatim.utils.TimeUtil;
import com.yyide.chatim.utils.VHUtil;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class TableTimeAdapter extends BaseAdapter {
    public List<TimeUtil.WeekDay> list = TimeUtil.getWeekDay();
    public int position = -1;
    public int today = -1;

    private OnItemClickListener mOnItemClickListener;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public TimeUtil.WeekDay getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.time_item, null, false);
        CheckedTextView day = VHUtil.ViewHolder.get(view, R.id.day);
        CheckedTextView time = VHUtil.ViewHolder.get(view, R.id.time);
        FrameLayout layout = VHUtil.ViewHolder.get(view, R.id.layout);

        layout.setOnClickListener(v -> {
            if (mOnItemClickListener != null){
                mOnItemClickListener.onItemClick(v, position);
            }
        });

        final DateTime today = DateTime.now();
        if (today.toString("yyyy-MM-dd").equals(getItem(position).dataTime)) {
            time.setText("今天");
        }else {
            time.setText(getItem(position).day);
        }

        String week = getItem(position).week;
        String neek = week.replaceAll("星期", "");
        if (!neek.contains("周") && !neek.equals("今天")) {
            day.setText("周" + neek);
        } else {
            day.setText(neek);
        }


        /* if (position == today) {
            day.setText("今天");
        }*/

        if (this.position == position) {
            layout.setBackground(view.getContext().getResources().getDrawable(R.drawable.bg_blue_conners2));
            day.setChecked(true);
            time.setChecked(true);
        } else {
            layout.setBackground(view.getContext().getResources().getDrawable(R.drawable.bg_corners2));
            day.setChecked(false);
            time.setChecked(false);
        }

        return view;
    }

    public void notifyData(List<TimeUtil.WeekDay> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setPosition(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    public void setToday(int today) {
        this.today = today;
        notifyDataSetChanged();
    }

    // 自定义点击事件
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }



}
