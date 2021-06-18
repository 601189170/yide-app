package com.yyide.chatim.activity.newnotice;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

public class NoticePushAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements LoadMoreModule {
    public NoticePushAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, String s) {

    }
}
