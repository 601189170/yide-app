package com.yyide.chatim.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
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

public class MyTableAdapter extends BaseQuickAdapter<SelectSchByTeaidRsp.DataBean, BaseViewHolder> {

    public MyTableAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, SelectSchByTeaidRsp.DataBean item) {
        TextView seciton = baseViewHolder.getView(R.id.seciton);
        TextView className = baseViewHolder.getView(R.id.className);
        TextView time = baseViewHolder.getView(R.id.time);
        TextView tool = baseViewHolder.getView(R.id.tv_tool);
        TextView homework = baseViewHolder.getView(R.id.tv_homework);
        TextView desc = baseViewHolder.getView(R.id.desc);
        ImageView dateS = baseViewHolder.getView(R.id.date);
        tool.setText("教具：" + (TextUtils.isEmpty(item.teachTool) ? "暂无教具" : item.teachTool));
        if (item.lessonsSubEntityList != null && item.lessonsSubEntityList.size() > 0) {
            StringBuilder lessons = new StringBuilder();
            for (int i = 0; i < item.lessonsSubEntityList.size(); i++) {
                lessons.append(item.lessonsSubEntityList.get(i)).append("\n");
            }
            homework.setText(lessons.toString());
        } else {
            homework.setText("暂无作业");
        }

        desc.setText(TextUtils.isEmpty(item.beforeClass) ? "暂无课前提醒" : item.beforeClass);
        dateS.setImageResource(R.mipmap.table_item_logo);
/*        //开始时间
        long fromDataTime = DateUtils.getWhenPoint(item.fromDateTime);
        //结束时间
        long toDateTime = DateUtils.getWhenPoint(item.toDateTime);
        Calendar c = Calendar.getInstance();
        String minute = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        long mMillisecond = DateUtils.getWhenPoint(minute);
        if (weekDay == 1) {//系统日历周日默认==1
            weekDay = 7;
        } else {
            weekDay = weekDay - 1;
        }
        if (item.weekTime == weekDay) {
            if (mMillisecond > toDateTime) {//课后
                dateS.setImageResource(R.drawable.icon_table_un);
            } else if (mMillisecond < fromDataTime) {//课前
                dateS.setImageResource(R.drawable.icon_table);
            } else {//正在上课
                dateS.setImageResource(R.drawable.icon_table);
            }
        } else if(item.weekTime > weekDay){
            dateS.setImageResource(R.drawable.icon_table);
        } else {
            dateS.setImageResource(R.drawable.icon_table_un);
        }*/

        className.setText(item.classesName + " \t" + item.subjectName);
        seciton.setText("0".equals(item.section) ? "早读" : "第" + item.section + "节");
        time.setText(item.fromDateTime + "-" + item.toDateTime);
    }
}
