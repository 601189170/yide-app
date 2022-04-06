package com.yyide.chatim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.model.ListByAppRsp2;
import com.yyide.chatim.model.ListByAppRsp3;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class NoBookItemAdapter_student_By_Parent extends BaseAdapter {
   public List<ListByAppRsp3.DataDTO.AdlistDTO> list=new ArrayList<>();


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ListByAppRsp3.DataDTO.AdlistDTO getItem(int position) {
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
        ImageView img = VHUtil.ViewHolder.get(view, R.id.img);
        View line = VHUtil.ViewHolder.get(view, R.id.line);
        TextView msg_number = VHUtil.ViewHolder.get(view, R.id.msg_number);
        msg_number.setText("");
        item.setText(getItem(position).name);
        line.setVisibility(position== list.size()-1?View.GONE:View.VISIBLE);


        return view;
    }
    public void notifyData(List<ListByAppRsp3.DataDTO.AdlistDTO> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
