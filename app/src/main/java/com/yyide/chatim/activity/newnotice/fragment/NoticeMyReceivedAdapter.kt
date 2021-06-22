package com.yyide.chatim.activity.newnotice.fragment

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class NoticeMyReceivedAdapter(layoutResId: Int) : BaseQuickAdapter<String?, BaseViewHolder>(layoutResId), LoadMoreModule {
    override fun convert(holder: BaseViewHolder, s: String?) {
        //String html = "已确认<font color='#2C8AFF'>"+et_buy_sum.getText().toString()+"</font>/";
        //tv_profit_think.setText(Html.fromHtml(html));
    }
}