package com.yyide.chatim.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.yyide.chatim.R;
import com.yyide.chatim.model.AppItemBean;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class AppItemAdapter extends BaseAdapter {
    public List<AppItemBean.DataBean.RecordsBean.ListBean> list = new ArrayList<>();

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public AppItemBean.DataBean.RecordsBean.ListBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.icon_item, null, false);

        ImageView item = VHUtil.ViewHolder.get(view, R.id.item);
        TextView name = VHUtil.ViewHolder.get(view, R.id.name);
        name.setText(getItem(position).getName());
        if (!TextUtils.isEmpty(getItem(position).getImg())) {
            GlideUtil.loadCircleImage(view.getContext(), getItem(position).getImg(), item);
        }
        return view;
    }

    public void notifyData(List<AppItemBean.DataBean.RecordsBean.ListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
