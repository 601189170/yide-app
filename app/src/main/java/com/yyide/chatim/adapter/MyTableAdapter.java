package com.yyide.chatim.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class MyTableAdapter extends BaseAdapter {
    public List<SelectSchByTeaidRsp.DataBean> list = new ArrayList<>();

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SelectSchByTeaidRsp.DataBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mytable_item, null, false);
        TextView seciton = VHUtil.ViewHolder.get(view, R.id.seciton);
        TextView className = VHUtil.ViewHolder.get(view, R.id.className);
        TextView time = VHUtil.ViewHolder.get(view, R.id.time);
        TextView tool = VHUtil.ViewHolder.get(view, R.id.tv_tool);
        TextView homework = VHUtil.ViewHolder.get(view, R.id.tv_homework);
        TextView desc = VHUtil.ViewHolder.get(view, R.id.desc);
        ImageView dateS = VHUtil.ViewHolder.get(view, R.id.date);

        SelectSchByTeaidRsp.DataBean item = getItem(position);
        tool.setText("教具：" + (TextUtils.isEmpty(item.teachTool) ? "" : item.teachTool));
        if (item.lessonsSubEntityList != null && item.lessonsSubEntityList.size() > 0) {
            StringBuffer lessons = new StringBuffer();
            for (int i = 0; i < item.lessonsSubEntityList.size(); i++) {
                lessons.append(item.lessonsSubEntityList.get(i) + "\n");
            }
            homework.setText(lessons.toString());
        } else {
            homework.setText("");
        }

        desc.setText(item.beforeClass);
        //开始时间
        long fromDataTime = DateUtils.getWhenPoint(item.fromDateTime);
        //结束时间
        long toDateTime = DateUtils.getWhenPoint(item.toDateTime);
        Calendar c = Calendar.getInstance();
        String minute = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        long mMillisecond = DateUtils.getWhenPoint(minute);
        if (item.weekTime > (weekDay - 1)) {//课前
//            desc.setText(item.beforeClass);
            dateS.setImageResource(R.drawable.icon_table);
        } else if (item.weekTime == (weekDay - 1)) {
            if (mMillisecond > toDateTime) {//课后
//                desc.setText(item.afterClass);
                dateS.setImageResource(R.drawable.icon_table_un);
            } else if (mMillisecond < fromDataTime) {//课前
//                desc.setText(item.beforeClass);
                dateS.setImageResource(R.drawable.icon_table);
            } else {//正在上课
//                desc.setText(item.beforeClass);
                dateS.setImageResource(R.drawable.icon_table_un);
            }
        } else {
//            desc.setText(item.afterClass);
            dateS.setImageResource(R.drawable.icon_table_un);
        }
        className.setText(item.classesName);
        seciton.setText("第" + item.section + "节");
        time.setText(item.fromDateTime + "-" + item.toDateTime);

        return view;
    }

    public void notifyData(List<SelectSchByTeaidRsp.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
