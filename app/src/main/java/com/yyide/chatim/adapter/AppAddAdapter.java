package com.yyide.chatim.adapter;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.AppAddActivity;
import com.yyide.chatim.model.AppAddRsp;
import com.yyide.chatim.utils.GlideUtil;

import org.jetbrains.annotations.NotNull;

public class AppAddAdapter extends BaseQuickAdapter<AppAddRsp.DataBean.RecordsBean, BaseViewHolder>  implements LoadMoreModule {

    public AppAddAdapter() {
        super(R.layout.item_app_add);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, AppAddRsp.DataBean.RecordsBean item) {
        ImageView iv_app_icon = holder.getView(R.id.iv_app_icon);
        TextView tv_add = holder.getView(R.id.tv_add);
        if (!TextUtils.isEmpty(item.getImg())) {
            GlideUtil.loadCircleImage(getContext(), item.getImg(), iv_app_icon);
        }
        holder.setText(R.id.tv_name, item.getName());
        if (item.getIsAdd()) {
            tv_add.setClickable(false);
            tv_add.setText("已添加");
            tv_add.setBackgroundResource(R.drawable.app_add_bg);
            tv_add.setCompoundDrawablePadding(0);
            tv_add.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            tv_add.setTextColor(getContext().getResources().getColor(R.color.text_999999));
        } else {
            tv_add.setClickable(true);
            tv_add.setText("添加");
            tv_add.setCompoundDrawablePadding(10);
            tv_add.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.icon_app_add), null, null, null);
            tv_add.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            tv_add.setBackgroundResource(R.drawable.app_add_bg_blue);
            tv_add.setOnClickListener(v -> {

                if(mAddClickListener != null){
                    mAddClickListener.OnAddClickListener(item);
                }
            });
        }
    }

    public interface AddClickListener{
        void OnAddClickListener(AppAddRsp.DataBean.RecordsBean item);
    }

    private AddClickListener mAddClickListener;

    public void setAddClickListener(AddClickListener mAddClickListener) {
        this.mAddClickListener = mAddClickListener;
    }
}
