package com.yyide.chatim.activity.newnotice.fragment.adaoter

import android.view.View
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.databinding.ItemNoticeMyReceviedBinding
import com.yyide.chatim.model.NoticeItemBean
import com.yyide.chatim.utils.GlideUtil


class NoticeMyReceivedAdapter(layoutResId: Int) : BaseQuickAdapter<NoticeItemBean.DataBean.RecordsBean, BaseViewHolder>(layoutResId), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: NoticeItemBean.DataBean.RecordsBean) {
        val view = ItemNoticeMyReceviedBinding.bind(holder.itemView)
        if (item.type == 0) {//空白模板文本
            view.clTemplate.visibility = View.INVISIBLE
            view.clBlank.visibility = View.VISIBLE
            view.tvTitle.text = item.title
            view.tvContent.text = item.content
        } else {
            view.clBlank.visibility = View.GONE
            view.clTemplate.visibility = View.VISIBLE
            GlideUtil.loadImageRadius(context, item.imgpath, view.ivNoticeImg, 2, true)
        }
        view.tvNoticeTitle.text = item.title
        view.tvNoticeTime.text = item.timerDate
        view.tvNoticeAuthor.text = item.publisher
    }
}