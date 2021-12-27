package com.yyide.chatim.adapter;

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
import com.yyide.chatim.utils.GlideUtil;
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
        TextView tvDesc = VHUtil.ViewHolder.get(view, R.id.tvDesc);
        View view_line = VHUtil.ViewHolder.get(view, R.id.view_line);
        ImageView img = VHUtil.ViewHolder.get(view, R.id.img);
        GlideUtil.loadImageHead(viewGroup.getContext(), getItem(position).img, img);
        GetUserSchoolRsp.DataBean item = getItem(position);

        school_name.setText(item.schoolName);
        school_info.setText(item.getIdentityResult());

//        List<GetUserSchoolRsp.DataBean.FormBean> listClass = item.form;
        //处理家长身份显示多个学生
//        if (!item.staffIdentity()) {
//            if (listClass != null && listClass.size() > 0) {
//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder.append("(");
//                for (int i = 0; i < listClass.size(); i++) {
//                    GetUserSchoolRsp.DataBean.FormBean studentItem = listClass.get(i);
//                    if (i == listClass.size() - 1) {
//                        stringBuilder.append(studentItem.studentName);
//                    } else {
//                        stringBuilder.append(studentItem.studentName).append("/");
//                    }
//                }
//                stringBuilder.append(")");
//                tvDesc.setText(stringBuilder.toString());
//            }
//        } else {
//            tvDesc.setText("");
//        }

        if (list.get(position).userId.equals(SpData.getUserId())) {
            select.setVisibility(View.VISIBLE);
        } else {
            select.setVisibility(View.GONE);
        }
        Log.d("position", position + "");
        if (getCount() - 1 == position) {
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
