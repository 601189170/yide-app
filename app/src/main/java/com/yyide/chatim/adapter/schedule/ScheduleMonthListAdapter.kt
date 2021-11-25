package com.yyide.chatim.adapter.schedule

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.model.schedule.Schedule
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.utils.ColorUtil
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.DisplayUtils

/**
 *
 * @author liu tao
 * @date 2021/9/15 19:39
 * @description 描述
 */
class ScheduleMonthListAdapter: BaseQuickAdapter<ScheduleData, BaseViewHolder>(R.layout.item_dialog_month_schedule) {
    override fun convert(baseViewHolder: BaseViewHolder, schedule: ScheduleData) {
        baseViewHolder.setText(R.id.tv_title, schedule.name)
        val imageView: ImageView = baseViewHolder.getView<ImageView>(R.id.iv_type)
        when (schedule.type.toInt()) {
            Schedule.SCHEDULE_TYPE_SCHEDULE -> imageView.setImageResource(R.drawable.type_schedule_icon)
            Schedule.SCHEDULE_TYPE_SCHOOL_SCHEDULE -> imageView.setImageResource(R.drawable.type_school_schedule_icon)
            Schedule.SCHEDULE_TYPE_CONFERENCE -> imageView.setImageResource(R.drawable.type_conference_icon)
            Schedule.SCHEDULE_TYPE_CLASS_SCHEDULE -> imageView.setImageResource(R.drawable.type_class_schedule_icon)
            else -> {
            }
        }
        if (schedule.moreDay == 1){
            val startDate: String = schedule.moreDayStartTime
            val endDate: String = schedule.moreDayEndTime
            baseViewHolder.setText(
                R.id.tv_date,
                DateUtils.formatTime(
                    startDate,
                    "",
                    "HH:mm"
                ) + "-" + DateUtils.formatTime(endDate, "", "HH:mm")
            )
        }else{
            val startDate: String = schedule.startTime
            val endDate: String = schedule.endTime
            baseViewHolder.setText(
                R.id.tv_date,
                DateUtils.formatTime(
                    startDate,
                    "",
                    "HH:mm"
                ) + "-" + DateUtils.formatTime(endDate, "", "HH:mm")
            )
        }

        //设置日程标签的背景
        val label = schedule.label
        if (label.isNotEmpty()) {
            baseViewHolder.getView<TextView>(R.id.tv_label).visibility = View.VISIBLE
            val drawable = GradientDrawable()
            drawable.cornerRadius = DisplayUtils.dip2px(context, 2f).toFloat()
            drawable.setColor(ColorUtil.parseColor(label[0].colorValue))
            baseViewHolder.getView<TextView>(R.id.tv_label).background = drawable
            baseViewHolder.getView<TextView>(R.id.tv_label).text = label[0].labelName
            if (label.size>1){
                baseViewHolder.getView<TextView>(R.id.tv_label_number).visibility = View.VISIBLE
                baseViewHolder.getView<TextView>(R.id.tv_label_number).text = "+${label.size-1}"
            }else{
                baseViewHolder.getView<TextView>(R.id.tv_label_number).visibility = View.GONE
            }

        } else {
            baseViewHolder.getView<TextView>(R.id.tv_label).visibility = View.INVISIBLE
            baseViewHolder.getView<TextView>(R.id.tv_label_number).visibility = View.GONE
        }

    }

}