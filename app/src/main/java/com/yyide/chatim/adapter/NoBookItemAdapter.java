package com.yyide.chatim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.model.ListByAppRsp;
import com.yyide.chatim.model.ListByAppRsp2;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class NoBookItemAdapter extends BaseAdapter {
   public List<ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO> list=new ArrayList<>();


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notebook_item, null, false);
        TextView item = VHUtil.ViewHolder.get(view, R.id.name);
        TextView msg_number = VHUtil.ViewHolder.get(view, R.id.msg_number);
        msg_number.setText(getItem(position).employeeAddBookDTOList.size()+"");
        item.setText(getItem(position).name);
        return view;
    }
    public void notifyData(List<ListByAppRsp2.DataDTO.DeptVOListDTO.ChildrenDTO> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
