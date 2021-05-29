package com.yyide.chatim.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class SwichSchoolAdapter extends BaseAdapter {
    public List<GetUserSchoolRsp.DataBean> list = new ArrayList<>();
    public int index = -1;

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.swich_school_item, null, false);

        TextView school_info = VHUtil.ViewHolder.get(view, R.id.school_info);
        TextView school_name = VHUtil.ViewHolder.get(view, R.id.school_name);
        TextView select = VHUtil.ViewHolder.get(view, R.id.select);
        View view_line = VHUtil.ViewHolder.get(view, R.id.view_line);
        ImageView img = VHUtil.ViewHolder.get(view, R.id.img);

        school_name.setText(getItem(position).schoolName);
        school_info.setText(getItem(position).realname);
        if (list.get(position).userId == SpData.getIdentityInfo().userId) {
            select.setVisibility(View.VISIBLE);
        } else {
            select.setVisibility(View.GONE);
        }
        Log.d("position", position + "");
        if (getCount() -1 == position) {
            view_line.setVisibility(View.GONE);
        } else {
            view_line.setVisibility(View.VISIBLE);
        }
//        select.setVisibility(index==position?View.VISIBLE:View.GONE);
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
