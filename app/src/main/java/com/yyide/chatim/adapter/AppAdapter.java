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

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.leave.AskForLeaveActivity;
import com.yyide.chatim.activity.WebViewActivity;
import com.yyide.chatim.activity.notice.NoticeAnnouncementActivity;
import com.yyide.chatim.model.AppItemBean;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.utils.VHUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
        if (recordsBean.getList() != null) {
            adapter.notifyData(recordsBean.getList());
        }
        grid.setOnItemClickListener((parent, view1, position1, id) -> {
            Intent intent;
            switch (adapter.getItem(position1).getName()) {
                case "通知公告":
                    intent = new Intent(view1.getContext(), NoticeAnnouncementActivity.class);
                    view1.getContext().startActivity(intent);
                    break;
                case "请假":
                    //ToastUtils.showShort("请假");
                    intent = new Intent(view1.getContext(), AskForLeaveActivity.class);
                    view1.getContext().startActivity(intent);
                    break;
                case "调课":
                    ToastUtils.showShort("调课");
                    break;
                default:
                    if ("#".equals(adapter.getItem(position1).getPath().trim())) {
                        ToastUtils.showShort("暂无权限");
                    } else {
                        intent = new Intent(view1.getContext(), WebViewActivity.class);
                        intent.putExtra("url", adapter.getItem(position1).getPath());
                        view1.getContext().startActivity(intent);
                    }
                    break;
            }
        });
    }
}
