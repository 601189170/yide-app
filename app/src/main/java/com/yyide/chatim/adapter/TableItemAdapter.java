package com.yyide.chatim.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.model.listTimeDataByAppRsp;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class TableItemAdapter extends BaseAdapter {
    public List<listTimeDataByAppRsp.DataBean.SubListBean> list = new ArrayList<>();

    public int position = -1;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public listTimeDataByAppRsp.DataBean.SubListBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.course_card, null, false);
        TextView text_view = VHUtil.ViewHolder.get(view, R.id.text_view);
        LinearLayout layout = VHUtil.ViewHolder.get(view, R.id.layout);
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, SizeUtils.dp2px(80));
//        layout.setLayoutParams(layoutParams);
        //text_view.setText(getItem(position).subjectName + "\n" + getItem(position).fromDateTime + "\n" + getItem(position).toDateTime);
        text_view.setText(getItem(position).subjectName);

        if (position % 7 == this.position) {
            layout.setBackground(view.getContext().getResources().getDrawable(R.drawable.bg_table_ls));
        } else {
            layout.setBackground(view.getContext().getResources().getDrawable(R.drawable.bg_white2));
        }
        return view;
    }

    public void notifyData(List<listTimeDataByAppRsp.DataBean.SubListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setIndex(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

}
