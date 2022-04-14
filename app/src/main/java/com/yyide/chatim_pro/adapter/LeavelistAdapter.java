package com.yyide.chatim_pro.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.utils.VHUtil;

/**
 * Created by Administrator on 2019/3/29.
 */

public class LeavelistAdapter extends BaseAdapter {

    public String[] list={"我的信息","修改密码","帮助中心","权限设置","清理缓存","版本更新","关于"};
    public int LEAVESTUAS=1;

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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leave_list_item, null, false);
        TextView stuas = VHUtil.ViewHolder.get(view, R.id.stuas);
        TextView name = VHUtil.ViewHolder.get(view, R.id.name);
        TextView time = VHUtil.ViewHolder.get(view, R.id.time);
//        item.setText(getItem(position));
        switch (position){
            case 0:
                stuas.setBackground(view.getContext().getResources().getDrawable(R.drawable.bg_stuas_leave1));
                break;
            case 1:
                stuas.setBackground(view.getContext().getResources().getDrawable(R.drawable.bg_stuas_leave2));
                break;
            case 2:
                stuas.setBackground(view.getContext().getResources().getDrawable(R.drawable.bg_stuas_leave3));
                break;
            case 3:
                stuas.setBackground(view.getContext().getResources().getDrawable(R.drawable.bg_stuas_leave4));
                break;


        }

        return view;
    }


}
