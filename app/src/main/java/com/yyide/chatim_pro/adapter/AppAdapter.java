package com.yyide.chatim_pro.adapter;

import android.widget.GridView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.model.AppItemBean;
import com.yyide.chatim_pro.utils.JumpUtil;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Administrator on 2019/3/29.
 */

public class AppAdapter extends BaseQuickAdapter<AppItemBean.DataBean.RecordsBean, BaseViewHolder> {

    public AppAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, AppItemBean.DataBean.RecordsBean recordsBean) {
        TextView title = holder.getView(R.id.title);
        GridView grid = holder.getView(R.id.grid);
        title.setText(recordsBean.getName());
        AppItemAdapter adapter = new AppItemAdapter();
        grid.setAdapter(adapter);
        if (recordsBean.getList() != null && recordsBean.getList().size() > 0) {
            adapter.notifyData(recordsBean.getList());
        }
        grid.setOnItemClickListener((parent, view1, position1, id) -> {
            AppItemBean.DataBean.RecordsBean.ListBean item = adapter.getItem(position1);
            JumpUtil.appOpen(getContext(), item.getName(), item.getPath(), item.getName());
        });
    }
}
