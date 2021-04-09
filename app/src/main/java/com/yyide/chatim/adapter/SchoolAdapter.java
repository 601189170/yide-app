package com.yyide.chatim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class SchoolAdapter extends BaseAdapter {
    public List<GetUserSchoolRsp.DataBean> list = new ArrayList<>();
    public int index = -1;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GetUserSchoolRsp.DataBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.school_item, null, false);
        CheckedTextView name = VHUtil.ViewHolder.get(view, R.id.name);
        name.setText(getItem(position).schoolName);
        int ids = SpData.getIdentityInfo().schoolId;
        if (list.get(position).schoolId == ids) {
            name.setChecked(true);
        } else {
            name.setChecked(false);
        }
        return view;
    }

    public void notifyData(List<GetUserSchoolRsp.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

}
