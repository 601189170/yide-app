package com.yyide.chatim.adapter.sitetable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;

import com.yyide.chatim.R;
import com.yyide.chatim.utils.TimeUtil;
import com.yyide.chatim.utils.VHUtil;

import java.util.List;

/**
 * 场地课表头周列表
 * @author lt
 * @date 2021/12/09
 */

public class SiteTableTimeAdapter extends BaseAdapter {
    public List<TimeUtil.WeekDay> list;
    public int position = -1;
    public int today = -1;

    private OnItemClickListener mOnItemClickListener;

    public SiteTableTimeAdapter(List<TimeUtil.WeekDay> list) {
        this.list = list;
    }

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

        time.setText(getItem(position).day);
        String week = getItem(position).week;
        day.setText(week);
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

    public void notifyData(List<TimeUtil.WeekDay> dataList) {
        this.list.clear();
        this.list.addAll(dataList);
        notifyDataSetChanged();
    }

    public void setPosition(int position) {
        this.position = position;
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
