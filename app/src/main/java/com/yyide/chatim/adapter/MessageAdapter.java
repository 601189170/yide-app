package com.yyide.chatim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.utils.VHUtil;

/**
 * Created by Administrator on 2019/3/29.
 */

public class MessageAdapter extends BaseAdapter {

    public String[] list={"我的信息","修改密码","帮助中心","权限设置","清理缓存","版本更新","关于"};

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public String getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item, null, false);
//        TextView item = VHUtil.ViewHolder.get(view, R.id.item);
//        item.setText(getItem(position));
        return view;
    }


}
