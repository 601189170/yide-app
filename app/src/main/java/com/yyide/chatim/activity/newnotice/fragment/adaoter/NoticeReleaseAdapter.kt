package com.yyide.chatim.activity.newnotice.fragment.adaoter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.databinding.ItemNoticePushBinding
import com.yyide.chatim.model.NoticeReleaseTemplateBean

class NoticeReleaseAdapter(layoutResId: Int) : BaseQuickAdapter<NoticeReleaseTemplateBean.DataBean.RecordsBean, BaseViewHolder>(layoutResId), LoadMoreModule {
    override fun convert(holder: BaseViewHolder, itemBean: NoticeReleaseTemplateBean.DataBean.RecordsBean) {
        val pushBinding = ItemNoticePushBinding.bind(holder.itemView)
        pushBinding.tvNoticeTitle.text = itemBean.name
        if (itemBean.type == 0) {

        } else {

        }
    }
}