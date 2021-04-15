package com.yyide.chatim.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.activity.WebViewActivity;
import com.yyide.chatim.model.AppItemBean;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class AppAdapter extends BaseAdapter {
    public List<AppItemBean.DataBean.RecordsBean> list = new ArrayList<>();

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public AppItemBean.DataBean.RecordsBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_item, null, false);
        TextView title = VHUtil.ViewHolder.get(view, R.id.title);
        GridView grid = VHUtil.ViewHolder.get(view, R.id.grid);
        title.setText(getItem(position).getName());
        AppItemAdapter adapter = new AppItemAdapter();
        grid.setAdapter(adapter);
        if (getItem(position).getList() != null) {
            adapter.notifyData(getItem(position).getList());
        }
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), WebViewActivity.class);
                intent.putExtra("url", adapter.getItem(position).getPath());
                view.getContext().startActivity(intent);
            }
        });
        return view;
    }

    public void notifyData(List<AppItemBean.DataBean.RecordsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
