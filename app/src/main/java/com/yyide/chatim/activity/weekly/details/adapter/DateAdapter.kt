package com.yyide.chatim.activity.weekly.details.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.model.WeeklyDateBean
import com.yyide.chatim.utils.DateUtils

class DateAdapter :
    BaseQuickAdapter<WeeklyDateBean.DataBean.TimesBean, BaseViewHolder>(R.layout.swich_class_item) {

    override fun convert(holder: BaseViewHolder, item: WeeklyDateBean.DataBean.TimesBean) {
        holder.setText(
            R.id.className, DateUtils.formatTime(item.startTime, "yyyy-MM-dd HH:mm:ss", "MM/dd")
                    + "-" + DateUtils.formatTime(item.endTime, "yyyy-MM-dd HH:mm:ss", "MM/dd")
        )
        holder.getView<View>(R.id.select).visibility = View.GONE
//            if (indexDate == holder.absoluteAdapterPosition) View.VISIBLE else View.GONE
        if (this.itemCount - 1 == holder.absoluteAdapterPosition) {
            holder.getView<View>(R.id.view_line).visibility = View.GONE
        } else {
            holder.getView<View>(R.id.view_line).visibility = View.VISIBLE
        }
    }
}