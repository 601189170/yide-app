package com.yyide.chatim_pro.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.model.ListByAppRsp;
import com.yyide.chatim_pro.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class NotelistAdapter extends BaseAdapter {

    public List<ListByAppRsp.DataBean.ListBean> list=new ArrayList<>();

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ListByAppRsp.DataBean.ListBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_list_item, null, false);
        TextView item = VHUtil.ViewHolder.get(view, R.id.item);
        item.setText(getItem(position).name);
        return view;
    }
    public void notifydata(List<ListByAppRsp.DataBean.ListBean> list){
        this.list=list;
        notifyDataSetChanged();
    }

}
