package com.yyide.chatim.activity.newnotice.fragment

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class NoticeReleaseAdapter(layoutResId: Int) : BaseQuickAdapter<String?, BaseViewHolder>(layoutResId), LoadMoreModule {
    override fun convert(holder: BaseViewHolder, s: String?) {}
}