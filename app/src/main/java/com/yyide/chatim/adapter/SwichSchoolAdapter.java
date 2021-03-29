package com.yyide.chatim.adapter;

import android.text.TextUtils;
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
    public List<GetUserSchoolRsp.DataBean> list=new ArrayList<>();
   public int index=-1;

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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.swich_school_item, null, false);

        TextView school_info = VHUtil.ViewHolder.get(view, R.id.school_info);
        TextView school_name = VHUtil.ViewHolder.get(view, R.id.school_name);
        TextView select = VHUtil.ViewHolder.get(view, R.id.select);
        school_name.setText(getItem(position).schoolName);
        school_info.setText(getItem(position).realname);
        int ids =0;
        if (!TextUtils.isEmpty(SpData.SchoolId())){
            ids= Integer.parseInt(SpData.SchoolId());
        }

        if (list.get(position).schoolId==ids){
            select.setVisibility(View.VISIBLE);
        }else {
            select.setVisibility(View.GONE);
        }
//        select.setVisibility(index==position?View.VISIBLE:View.GONE);
        return view;
    }
    public void notifyData( List<GetUserSchoolRsp.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public void setIndex(int index){
        this.index=index;
        notifyDataSetChanged();
    }
}
