package com.yyide.chatim.adapter.schedule

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.promoterSelf
import com.yyide.chatim.model.schedule.Schedule
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim.utils.loge

/**
 * 日程列表视图
 */
class ScheduleListAdapter :
    BaseMultiItemQuickAdapter<ScheduleData, BaseViewHolder>() {
    init {

        addItemType(
            Schedule.TYPE_EXPIRED_COMPLETED,
            R.layout.schedule_item_list_view_expired_completed
        )
        addItemType(
            Schedule.TYPE_EXPIRED_NOT_COMPLETED,
            R.layout.schedule_item_list_view_expired_not_completed
        )
        addItemType(
            Schedule.TYPE_UNEXPIRED_COMPLETED,
            R.layout.schedule_item_list_view_unexpired_completed
        )
        addItemType(
            Schedule.TYPE_UNEXPIRED_NOT_COMPLETED,
            R.layout.schedule_item_list_view_unexpired_not_completed
        )
        addItemType(Schedule.TYPE_TIME_AXIS, R.layout.schedule_item_time_axis)
        addItemType(Schedule.TYPE_LIST_VIEW_HEAD, R.layout.schedule_item_list_view_month_head)
    }

    /**
     * 相同的布局设置
     */
    fun commonConvert(holder: BaseViewHolder, item: ScheduleData) {
        loge("ScheduleData ${JSON.toJSONString(item)}")
        holder.setText(R.id.tv_schedule_name, item.name)
        holder.getView<TextView>(R.id.iv_mine_label).visibility = if (item.promoterSelf()) View.VISIBLE else View.GONE
        if (item.isFirstDayOfMonth) {
            holder.getView<Group>(R.id.group_day).visibility = View.VISIBLE
            val dateTime = ScheduleDaoUtil.toDateTime(item.moreDayStartTime)
            val day = dateTime.dayOfMonth
            val week = dateTime.dayOfWeek + 1
//            holder.setText(R.id.tv_title_day, "${day}日")
//            holder.setText(R.id.tv_title_week, DateUtils.getWeek(week))
        } else {
            holder.getView<Group>(R.id.group_day).visibility = View.INVISIBLE
        }
        loadImage(
            item.type.toInt(),
            DateUtils.dateExpired(item.moreDayEndTime),
            holder.getView(R.id.iv_schedule_type_img)
        )
        //日期设置
        val simplifiedDataTime =
            ScheduleDaoUtil.toDateTime(item.moreDayStartTime ?: item.startTime).simplifiedDataTime()
        val simplifiedDataTime1 =
            ScheduleDaoUtil.toDateTime(item.moreDayEndTime ?: item.endTime).simplifiedDataTime()
        //是否跨天
        val moreDay = item.moreDay == 1//simplifiedDataTime != simplifiedDataTime1
        //1.适用日程：全天跨天非重复
        //显示：2021年1月3日 - 2021年1月8日 （全天）
        //全天跨天     全天 第1天，共3天
        if (item.isAllDay == "1" && moreDay && item.isRepeat == "0") {
            val target = "yyyy年MM月dd日"
            val startTime = DateUtils.formatTime(item.moreDayStartTime ?: item.startTime, "", target)
            val endTime = DateUtils.formatTime(item.moreDayEndTime ?: item.endTime, "", target)
            val timeDesc = "全天".plus(" 第${item.moreDayIndex}天，").plus("共${item.moreDayCount}天")
            //holder.setText(R.id.tv_schedule_time_interval, "$startTime - $endTime".plus("（全天）"))
            holder.setText(R.id.tv_schedule_time_interval, timeDesc)
            return
        }
        //2.适用日程：非全天跨天非重复
        //显示：2021年1月3日 08：00 - 2021年1月8日 09：00
        //非全天跨天   08：00 - 09：00  第1天，共3天
        if (item.isAllDay == "0" && moreDay && item.isRepeat == "0") {
            val target = "HH:mm"
            val startTime = DateUtils.formatTime(item.moreDayStartTime ?: item.startTime, "", target)
            val endTime = DateUtils.formatTime(item.moreDayEndTime ?: item.endTime, "", target)
            val timeDesc = startTime.plus(" - ").plus(endTime).plus("  第${item.moreDayIndex}天，").plus("共${item.moreDayCount}天")
            holder.setText(R.id.tv_schedule_time_interval, timeDesc)
            return
        }
        //3.适用日程：全天不跨天非重复、全天不跨天重复
        //显示：2021年1月3日 （全天）
        //全天不跨天   全天
        if ((item.isAllDay == "1" && !moreDay && item.isRepeat == "0") || (item.isAllDay == "1" && !moreDay && item.isRepeat != "0")){
            val target = "yyyy年MM月dd日"
            val startTime = DateUtils.formatTime(item.moreDayStartTime ?: item.startTime, "", target)
            //val endTime = DateUtils.formatTime(item.moreDayEndTime ?: item.endTime, "", target)
            holder.setText(R.id.tv_schedule_time_interval, "全天")
            return
        }
        //4.适用日程：非全天不跨天重复、非全天不跨天非重复
        //显示：2021年1月3日 08：00 - 09：00
        //非全天不跨天 08：00 - 10：00
        if ((item.isAllDay == "0" && !moreDay && item.isRepeat != "0") || (item.isAllDay == "0" && !moreDay && item.isRepeat == "0")){
            val target = "HH:mm"
            val target2 = "HH:mm"
            val startTime = DateUtils.formatTime(item.moreDayStartTime ?: item.startTime, "", target)
            val endTime = DateUtils.formatTime(item.moreDayEndTime ?: item.endTime, "", target2)
            holder.setText(R.id.tv_schedule_time_interval, "$startTime - $endTime")
            return
        }

        val target = if (item.isAllDay != "1") "MM月dd日 HH:mm" else "MM月dd日"
        val startTime = DateUtils.formatTime(item.moreDayStartTime, "", target)
        val endTime = DateUtils.formatTime(item.moreDayEndTime, "", target)
        holder.setText(R.id.tv_schedule_time_interval, "$startTime-$endTime")

    }

    override fun convert(holder: BaseViewHolder, item: ScheduleData) {
        when (holder.itemViewType) {
            Schedule.TYPE_EXPIRED_COMPLETED -> {
                commonConvert(holder, item)
            }
            Schedule.TYPE_EXPIRED_NOT_COMPLETED -> {
                commonConvert(holder, item)
            }
            Schedule.TYPE_UNEXPIRED_COMPLETED -> {
                commonConvert(holder, item)
            }
            Schedule.TYPE_UNEXPIRED_NOT_COMPLETED -> {
                commonConvert(holder, item)
            }
            Schedule.TYPE_LIST_VIEW_HEAD -> {
                val month = DateUtils.formatTime(item.startTime, "", "MM月")
                holder.setText(R.id.tv_month_head_title, month)
            }
            Schedule.TYPE_TIME_AXIS ->{
                loge("分割线view")
                val layoutParams = holder.itemView.layoutParams
                layoutParams.height = DisplayUtils.dip2px(context,10f)
                holder.itemView.requestLayout()
            }
            else -> {
            }
        }
    }

    private fun loadImage(type: Int, expired: Boolean, imageView: ImageView) {
        when (type) {
            Schedule.SCHEDULE_TYPE_SCHEDULE -> {
                imageView.setImageResource(if (expired) R.drawable.type_schedule_finished_icon else R.drawable.type_schedule_icon)
            }
            Schedule.SCHEDULE_TYPE_SCHOOL_SCHEDULE -> {
                imageView.setImageResource(if (expired) R.drawable.type_school_schedule_finished_icon else R.drawable.type_school_schedule_icon)
            }
            Schedule.SCHEDULE_TYPE_CONFERENCE -> {
                imageView.setImageResource(if (expired) R.drawable.type_conference_finished_icon else R.drawable.type_conference_icon)
            }
            Schedule.SCHEDULE_TYPE_CLASS_SCHEDULE -> {
                imageView.setImageResource(if (expired) R.drawable.type_class_schedule_finished_icon else R.drawable.type_class_schedule_icon)
            }
            else -> {
            }
        }
    }

}