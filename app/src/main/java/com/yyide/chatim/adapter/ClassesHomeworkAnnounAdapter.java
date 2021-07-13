package com.yyide.chatim.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.yyide.chatim.R;
import com.yyide.chatim.model.SelectSchByTeaidRsp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2017/11/29.
 */

public class ClassesHomeworkAnnounAdapter extends LoopPagerAdapter {

    public List<SelectSchByTeaidRsp.DataBean> list = new ArrayList<>();
    Activity context;

    public ClassesHomeworkAnnounAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    public ClassesHomeworkAnnounAdapter(RollPagerView viewPager, Activity context) {
        super(viewPager);
        this.context = context;
    }

    private SelectSchByTeaidRsp.DataBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_classes_homework, null);
        TextView tv_homework_name = view.findViewById(R.id.tv_homework_name);
        TextView tv_homework = view.findViewById(R.id.tv_homework);
        TextView tv_now = view.findViewById(R.id.tv_now);

        tv_homework_name.setText(getItem(position).subjectName);
        if (getItem(position).lessonsSubEntityList != null && getItem(position).lessonsSubEntityList.size() > 0) {
            StringBuffer lessons = new StringBuffer();
            for (int i = 0; i < getItem(position).lessonsSubEntityList.size(); i++) {
                lessons.append(getItem(position).lessonsSubEntityList.get(i) + "\n");
            }
            tv_homework.setText(lessons.toString());
        }
        return view;
    }

    @Override
    public int getRealCount() {
        return list != null ? list.size() : 0;
    }

    public void notifyData(List<SelectSchByTeaidRsp.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
