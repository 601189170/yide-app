package com.yyide.chatim_pro.activity.newnotice.fragment.adaoter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.databinding.ItemNoticeMyReceviedBinding
import com.yyide.chatim_pro.model.NoticeItemBean
import com.yyide.chatim_pro.utils.DateUtils
import com.yyide.chatim_pro.utils.GlideUtil


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

        if (item.confirmOrRead) {//是否已读
            view.ivIconImg.setImageResource(R.mipmap.icon_notice_active)
        } else {
            view.ivIconImg.setImageResource(R.mipmap.icon_notice_unread)
        }

        view.tvNoticeTitle.text = item.title
        when {
            DateUtils.isToday(DateUtils.parseTimestamp(item.timerDate, "")) -> {//今天
                view.tvNoticeTime.text = context.getString(R.string.notice_toDay, DateUtils.formatTime(item.timerDate, "yyyy-MM-dd HH:mm:ss", "HH:mm"))
            }
            DateUtils.isYesterday(DateUtils.parseTimestamp(item.timerDate, "")) -> {//昨天
                view.tvNoticeTime.text = context.getString(R.string.notice_yesterday, DateUtils.formatTime(item.timerDate, "yyyy-MM-dd HH:mm:ss", "HH:mm"))
            }
            else -> {
                view.tvNoticeTime.text = DateUtils.formatTime(item.timerDate, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm")
            }
        }
        view.tvNoticeAuthor.text = item.publisher
    }
}