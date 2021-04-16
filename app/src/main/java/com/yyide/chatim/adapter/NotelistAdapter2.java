package com.yyide.chatim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.model.TeacherlistRsp;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.utils.StringUtils;
import com.yyide.chatim.utils.VHUtil;

import org.raphets.roundimageview.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class NotelistAdapter2 extends BaseAdapter {


    List<TeacherlistRsp.DataBean.RecordsBean> list = new ArrayList<>();

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public TeacherlistRsp.DataBean.RecordsBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_list_item2, null, false);
        TextView item = VHUtil.ViewHolder.get(view, R.id.name);
        TextView title = VHUtil.ViewHolder.get(view, R.id.tv_name_title);
        RoundImageView img = VHUtil.ViewHolder.get(view, R.id.img);
        title.setText(StringUtils.subString(getItem(position).name, 2));
        item.setText(getItem(position).name);
        return view;
    }

    public void notifdata(List<TeacherlistRsp.DataBean.RecordsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
