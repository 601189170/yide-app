package com.yyide.chatim.activity.newnotice;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

public class NoticeMyReceivedAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements LoadMoreModule {
    public NoticeMyReceivedAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, String s) {
        //String html = "已确认<font color='#2C8AFF'>"+et_buy_sum.getText().toString()+"</font>/";
        //tv_profit_think.setText(Html.fromHtml(html));
    }
}
