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
import com.yyide.chatim.model.table.ListItem;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class MyTableAdapter extends BaseQuickAdapter<ListItem, BaseViewHolder> {

    public MyTableAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ListItem item) {
        TextView className = baseViewHolder.getView(R.id.item_table_my_className);// 班级姓名
        TextView time = baseViewHolder.getView(R.id.item_table_my_time);//会议时间
        TextView desc = baseViewHolder.getView(R.id.item_table_my_desc);//会议提醒
        TextView title = baseViewHolder.getView(R.id.item_table_my_title);

        desc.setText("课前提醒：暂无提醒");
        className.setText(item.getSubjectName());
        title.setText(item.getName());
        String timeStr = item.getStartTime() + "-" + item.getEndTime();
        time.setText(timeStr);
    }
}
