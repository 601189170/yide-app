package com.yyide.chatim.adapter.schedule

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.Group
import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.model.schedule.Schedule
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.DisplayUtils
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

        if (item.moreDay == 1) {
            if (item.isFirstDayOfMonth) {
                holder.getView<Group>(R.id.group_day).visibility = View.VISIBLE
                val dateTime = ScheduleDaoUtil.toDateTime(item.moreDayStartTime)
                val day = dateTime.dayOfMonth
                val week = dateTime.dayOfWeek + 1
                holder.setText(R.id.tv_title_day, "${day}日")
                holder.setText(R.id.tv_title_week, DateUtils.getWeek(week))
            } else {
                holder.getView<Group>(R.id.group_day).visibility = View.INVISIBLE
            }
            val formatTime = DateUtils.formatTime(item.moreDayStartTime, "", "", true)
            holder.setText(
                R.id.tv_schedule_time_interval,
                "$formatTime    " +
                        DateUtils.formatTime(
                            item.moreDayStartTime,
                            "",
                            "HH:mm"
                        ) + "-" + DateUtils.formatTime(item.moreDayEndTime, "", "HH:mm")
            )
        } else {
            if (item.isFirstDayOfMonth) {
                holder.getView<Group>(R.id.group_day).visibility = View.VISIBLE
                val dateTime = ScheduleDaoUtil.toDateTime(item.startTime)
                val day = dateTime.dayOfMonth
                val week = dateTime.dayOfWeek + 1
                holder.setText(R.id.tv_title_day, "${day}日")
                holder.setText(R.id.tv_title_week, DateUtils.getWeek(week))
            } else {
                holder.getView<Group>(R.id.group_day).visibility = View.INVISIBLE
            }
            val formatTime = DateUtils.formatTime(item.startTime, "", "", true)
            holder.setText(
                R.id.tv_schedule_time_interval,
                "$formatTime    " +
                        DateUtils.formatTime(
                            item.startTime,
                            "",
                            "HH:mm"
                        ) + "-" + DateUtils.formatTime(item.endTime, "", "HH:mm")
            )
        }

        loadImage(
            item.type.toInt(),
            DateUtils.dateExpired(item.endTime),
            holder.getView(R.id.iv_schedule_type_img)
        )
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