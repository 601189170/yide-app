package com.yyide.chatim_pro.adapter.schedule

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.model.schedule.Schedule
import com.yyide.chatim_pro.model.schedule.ScheduleData
import com.yyide.chatim_pro.utils.ColorUtil
import com.yyide.chatim_pro.utils.DateUtils
import com.yyide.chatim_pro.utils.DisplayUtils
import com.yyide.chatim_pro.utils.loge

/**
 *
 * @author liu tao
 * @date 2021/9/15 19:39
 * @description 描述
 */
class ScheduleMonthListAdapter :
    BaseQuickAdapter<ScheduleData, BaseViewHolder>(R.layout.item_dialog_month_schedule) {
    override fun convert(baseViewHolder: BaseViewHolder, schedule: ScheduleData) {
        loge("ScheduleMonthListAdapter")
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
        //全天不跨天   全天
        //全天跨天     全天 第1天，共3天
        //非全天跨天   第1天，共3天
        //非全天不跨天 08：00 - 10：00
        //是否跨天
        val moreDay = schedule.moreDay == 1//simplifiedDataTime != simplifiedDataTime1
        //1.适用日程：全天跨天非重复
        //全天跨天     全天 第1天，共3天
        if (schedule.isAllDay == "1" && moreDay && schedule.isRepeat == "0") {
            val timeDesc =
                "全天".plus(" 第${schedule.moreDayIndex}天，").plus("共${schedule.moreDayCount}天")
            baseViewHolder.setText(R.id.tv_date, timeDesc)
            baseViewHolder.setGone(R.id.iv_time, true)
        }

        //2.适用日程：非全天跨天非重复
        //非全天跨天   第1天，共3天
        if (schedule.isAllDay == "0" && moreDay && schedule.isRepeat == "0") {
            val timeDesc =
                "".plus(" 第${schedule.moreDayIndex}天，").plus("共${schedule.moreDayCount}天")
            baseViewHolder.setText(R.id.tv_date, timeDesc)
            baseViewHolder.setGone(R.id.iv_time, true)
        }

        //3.适用日程：全天不跨天非重复、全天不跨天重复
        //全天不跨天   全天
        if ((schedule.isAllDay == "1" && !moreDay && schedule.isRepeat == "0") || (schedule.isAllDay == "1" && !moreDay && schedule.isRepeat != "0")) {
            baseViewHolder.setText(R.id.tv_date, "全天")
            baseViewHolder.setGone(R.id.iv_time, true)
        }

        //4.适用日程：非全天不跨天重复、非全天不跨天非重复
        //非全天不跨天 08：00 - 10：00
        if ((schedule.isAllDay == "0" && !moreDay && schedule.isRepeat != "0") || (schedule.isAllDay == "0" && !moreDay && schedule.isRepeat == "0")) {
            val startDate: String = schedule.moreDayStartTime
            val endDate: String = schedule.moreDayEndTime
            val timeDesc =
                DateUtils.formatTime(startDate, "", "HH:mm") + "-" + DateUtils.formatTime(
                    endDate,
                    "",
                    "HH:mm"
                )
            baseViewHolder.setText(R.id.tv_date, timeDesc)
            baseViewHolder.setGone(R.id.iv_time, false)
        }

//        if (schedule.moreDay == 1){
//            val startDate: String = schedule.moreDayStartTime
//            val endDate: String = schedule.moreDayEndTime
//            baseViewHolder.setText(
//                R.id.tv_date,
//                DateUtils.formatTime(
//                    startDate,
//                    "",
//                    "HH:mm"
//                ) + "-" + DateUtils.formatTime(endDate, "", "HH:mm")
//            )
//        }else{
//            val startDate: String = schedule.startTime
//            val endDate: String = schedule.endTime
//            baseViewHolder.setText(
//                R.id.tv_date,
//                DateUtils.formatTime(
//                    startDate,
//                    "",
//                    "HH:mm"
//                ) + "-" + DateUtils.formatTime(endDate, "", "HH:mm")
//            )
//        }

        //设置日程标签的背景
        val label = schedule.labelList
        if (label.isNotEmpty()) {
            baseViewHolder.getView<TextView>(R.id.tv_label).visibility = View.VISIBLE
            val drawable = GradientDrawable()
            drawable.cornerRadius = DisplayUtils.dip2px(context, 2f).toFloat()
            drawable.setColor(ColorUtil.parseColor(label[0].colorValue))
            baseViewHolder.getView<TextView>(R.id.tv_label).background = drawable
            baseViewHolder.getView<TextView>(R.id.tv_label).text = label[0].labelName
            if (label.size > 1) {
                baseViewHolder.getView<TextView>(R.id.tv_label_number).visibility = View.VISIBLE
                baseViewHolder.getView<TextView>(R.id.tv_label_number).text = "+${label.size - 1}"
            } else {
                baseViewHolder.getView<TextView>(R.id.tv_label_number).visibility = View.GONE
            }

        } else {
            baseViewHolder.getView<TextView>(R.id.tv_label).visibility = View.INVISIBLE
            baseViewHolder.getView<TextView>(R.id.tv_label_number).visibility = View.GONE
        }

        val tabRecyclerview = baseViewHolder.getView<RecyclerView>(R.id.rv_label_list)
        tabRecyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = ScheduleTabListAdapter()
        tabRecyclerview.adapter = adapter
            adapter.setList(schedule.labelList)

    }

}