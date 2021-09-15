package com.yyide.chatim.adapter.schedule

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.model.schedule.Schedule
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.DisplayUtils

/**
 *
 * @author liu tao
 * @date 2021/9/15 19:39
 * @description 描述
 */
class ScheduleMonthListAdapter: BaseQuickAdapter<Schedule, BaseViewHolder>(R.layout.item_dialog_month_schedule) {
    override fun convert(baseViewHolder: BaseViewHolder, schedule: Schedule) {
        baseViewHolder.setText(R.id.tv_title, schedule.title)
        val imageView: ImageView = baseViewHolder.getView<ImageView>(R.id.iv_type)
        when (schedule.type) {
            Schedule.SCHEDULE_TYPE_SCHEDULE -> imageView.setImageResource(R.drawable.type_schedule_icon)
            Schedule.SCHEDULE_TYPE_SCHOOL_SCHEDULE -> imageView.setImageResource(R.drawable.type_school_schedule_icon)
            Schedule.SCHEDULE_TYPE_CONFERENCE -> imageView.setImageResource(R.drawable.type_conference_icon)
            Schedule.SCHEDULE_TYPE_CLASS_SCHEDULE -> imageView.setImageResource(R.drawable.type_class_schedule_icon)
            else -> {
            }
        }
        val startDate: String = schedule.startDate
        val endDate: String = schedule.endDate
        baseViewHolder.setText(
            R.id.tv_date,
            DateUtils.formatTime(
                startDate,
                "",
                "HH:mm"
            ) + "-" + DateUtils.formatTime(endDate, "", "HH:mm")
        )
        //设置日程标签的背景
        val drawable = GradientDrawable()
        drawable.cornerRadius = DisplayUtils.dip2px(context,2f).toFloat()
        drawable.setColor(Color.parseColor("#BF57DA"))
        baseViewHolder.getView<TextView>(R.id.tv_label).background = drawable
    }

}