package com.yyide.chatim.adapter.schedule

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.model.schedule.Schedule
import com.yyide.chatim.model.schedule.SchoolCalendarRsp
import com.yyide.chatim.utils.DateUtils

class SchoolCalendarAdapter(data: List<SchoolCalendarRsp.DataBean>) :
    BaseMultiItemQuickAdapter<SchoolCalendarRsp.DataBean, BaseViewHolder>() {
    init {
        super.setList(data)
        addItemType(
            Schedule.SCHOOL_CALENDAR_TYPE_HEAD,
            R.layout.item_school_calendar_head
        )
        addItemType(
            Schedule.SCHOOL_CALENDAR_TYPE_ITEM,
            R.layout.item_school_calendar
        )
    }

    override fun convert(holder: BaseViewHolder, item: SchoolCalendarRsp.DataBean) {
        when (holder.itemViewType) {
            Schedule.SCHOOL_CALENDAR_TYPE_HEAD -> {

            }
            Schedule.SCHOOL_CALENDAR_TYPE_ITEM -> {
                val date = DateUtils.formatTime(item.startTime, "yyyy-MM-dd", "MM月dd日")
                holder.setText(R.id.tv_date,date)
                holder.setText(R.id.tv_remark,item.remark)
                if (item.moreDay == 1) {
                    val date2 = DateUtils.formatTime(item.endTime, "yyyy-MM-dd", "MM月dd日")
                    holder.setText(R.id.tv_date,date.plus("-").plus(date2))
                }
            }
            else -> {
            }
        }
    }

}