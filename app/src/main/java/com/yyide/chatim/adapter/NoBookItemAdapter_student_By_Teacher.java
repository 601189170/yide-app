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

public class NoBookItemAdapter_student_By_Teacher extends BaseAdapter {
   public List<ListByAppRsp2.DataDTO.ClassAddBookDTOListDTO> list=new ArrayList<>();


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ListByAppRsp2.DataDTO.ClassAddBookDTOListDTO getItem(int position) {
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
        TextView msg_number = VHUtil.ViewHolder.get(view, R.id.msg_number);
//        msg_number.setText(getItem(position).studentList.size()+"");
        item.setText(getItem(position).name);


        return view;
    }
    public void notifyData(List<ListByAppRsp2.DataDTO.ClassAddBookDTOListDTO> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
