package com.yyide.chatim.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.model.MyAppListRsp;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class MyAppItemAdapter extends BaseAdapter {
    //   public List<String> list=new ArrayList<>();
    public List<MyAppListRsp.DataBean> list = new ArrayList<>();

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public MyAppListRsp.DataBean getItem(int position) {
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
        MyAppListRsp.DataBean dataBean = getItem(position);
        if ("editor".equals(getItem(position).getAppType())) {
            item.setImageResource(R.drawable.icon_bj);
        } else {
            if (!TextUtils.isEmpty(dataBean.getImg())) {
                GlideUtil.loadCircleImage(view.getContext(), dataBean.getImg(), item);
            }
        }
        name.setText(dataBean.getName());
        return view;
    }

    public void notifyData(List<MyAppListRsp.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
