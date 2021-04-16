package com.yyide.chatim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class SwichClassAdapter extends BaseAdapter {
    public List<GetUserSchoolRsp.DataBean.FormBean> list = new ArrayList<>();
    public int index = -1;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GetUserSchoolRsp.DataBean.FormBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.swich_class_item, null, false);

        TextView className = VHUtil.ViewHolder.get(view, R.id.className);

        TextView select = VHUtil.ViewHolder.get(view, R.id.select);
        if ("Y".equals(getItem(position).teacherInd)) {
            className.setText(getItem(position).classesName + "\t" + "班主任");
        } else {
            className.setText(getItem(position).classesName);
        }
        select.setVisibility(index == position ? View.VISIBLE : View.GONE);
        return view;
    }

    public void notifyData(List<GetUserSchoolRsp.DataBean.FormBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }
}
