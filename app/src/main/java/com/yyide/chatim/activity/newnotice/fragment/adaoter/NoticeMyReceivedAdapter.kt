package com.yyide.chatim.activity.newnotice.fragment.adaoter

import android.text.Html
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.databinding.ItemNoticeMyPushBinding
import com.yyide.chatim.databinding.ItemNoticeMyReceviedBinding
import com.yyide.chatim.model.NoticeItemBean
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.GlideUtil

class NoticeMyReceivedAdapter(layoutResId: Int) : BaseQuickAdapter<NoticeItemBean.DataBean.RecordsBean, BaseViewHolder>(layoutResId), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: NoticeItemBean.DataBean.RecordsBean) {
        val view = ItemNoticeMyReceviedBinding.bind(holder.itemView)
        view.tvNoticeTitle.text = item.title
        view.tvNoticeTime.text = item.timerDate
//        view.tvNoticeAuthor.text = item.get
        GlideUtil.loadImageRadius(context, item.coverImgpath, view.ivNoticeImg, 2)

    }
}