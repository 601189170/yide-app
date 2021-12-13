package com.yyide.chatim.adapter.schedule

import android.content.Intent
import android.widget.ImageView
import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.schedule.ScheduleEditActivity
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.model.schedule.Schedule
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim.utils.loge

class ScheduleTodayAdapter(data: List<ScheduleData>) :
    BaseMultiItemQuickAdapter<ScheduleData, BaseViewHolder>() {
    init {
        super.setList(data)
        addItemType(Schedule.TYPE_EXPIRED_COMPLETED, R.layout.schedule_item_expired_completed)
        addItemType(
            Schedule.TYPE_EXPIRED_NOT_COMPLETED,
            R.layout.schedule_item_expired_not_completed
        )
        addItemType(
            Schedule.TYPE_UNEXPIRED_COMPLETED,
            R.layout.schedule_item_unexpired_completed
        )
        addItemType(
            Schedule.TYPE_UNEXPIRED_NOT_COMPLETED,
            R.layout.schedule_item_unexpired_not_completed
        )
    }
    /**
     * 相同的布局设置
     */
    fun commonConvert(holder: BaseViewHolder, item: ScheduleData) {
        loge("ScheduleData ${JSON.toJSONString(item)}")
        holder.setText(R.id.tv_schedule_name, item.name)
        loadImage(
            item.type.toInt(),
            DateUtils.dateExpired(item.endTime),
            holder.getView(R.id.iv_schedule_type_img)
        )

        val simplifiedDataTime =
            ScheduleDaoUtil.toDateTime(item.moreDayStartTime ?: item.startTime).simplifiedDataTime()
        val simplifiedDataTime1 =
            ScheduleDaoUtil.toDateTime(item.moreDayEndTime ?: item.endTime).simplifiedDataTime()
        //是否跨天
        val moreDay = simplifiedDataTime != simplifiedDataTime1
        //1.适用日程：全天跨天非重复
        //显示：2021年1月3日 - 2021年1月8日 （全天）
        if (item.isAllDay == "1" && moreDay && item.isRepeat == "0") {
            val target = "yyyy年MM月dd日"
            val startTime = DateUtils.formatTime(item.moreDayStartTime ?: item.startTime, "", target)
            val endTime = DateUtils.formatTime(item.moreDayEndTime ?: item.endTime, "", target)
            holder.setText(R.id.tv_schedule_time_interval, "$startTime - $endTime".plus("（全天）"))
            return
        }
        //2.适用日程：非全天跨天非重复
        //显示：2021年1月3日 08：00 - 2021年1月8日 09：00
        if (item.isAllDay == "0" && moreDay && item.isRepeat == "0") {
            val target = "yyyy年MM月dd日 HH:mm"
            val startTime = DateUtils.formatTime(item.moreDayStartTime ?: item.startTime, "", target)
            val endTime = DateUtils.formatTime(item.moreDayEndTime ?: item.endTime, "", target)
            holder.setText(R.id.tv_schedule_time_interval, "$startTime - $endTime")
            return
        }
        //3.适用日程：全天不跨天非重复、全天不跨天重复
        //显示：2021年1月3日 （全天）
        if ((item.isAllDay == "1" && !moreDay && item.isRepeat == "0") || (item.isAllDay == "1" && !moreDay && item.isRepeat != "0")){
            val target = "yyyy年MM月dd日"
            val startTime = DateUtils.formatTime(item.moreDayStartTime ?: item.startTime, "", target)
            //val endTime = DateUtils.formatTime(item.moreDayEndTime ?: item.endTime, "", target)
            holder.setText(R.id.tv_schedule_time_interval, "$startTime".plus("（全天）"))
            return
        }
        //4.适用日程：非全天不跨天重复、非全天不跨天非重复
        //显示：2021年1月3日 08：00 - 09：00
        if ((item.isAllDay == "0" && !moreDay && item.isRepeat != "0") || (item.isAllDay == "0" && !moreDay && item.isRepeat == "0")){
            val target = "yyyy年MM月dd日 HH:mm"
            val target2 = "HH:mm"
            val startTime = DateUtils.formatTime(item.moreDayStartTime ?: item.startTime, "", target)
            val endTime = DateUtils.formatTime(item.moreDayEndTime ?: item.endTime, "", target2)
            holder.setText(R.id.tv_schedule_time_interval, "$startTime - $endTime")
            return
        }
        //其他情况
        val target = if (item.isAllDay != "1") "MM月dd日 HH:mm" else "MM月dd日"
        val startTime = DateUtils.formatTime(item.moreDayStartTime ?: item.startTime, "", target)
        val endTime = DateUtils.formatTime(item.moreDayEndTime ?: item.endTime, "", target)
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
            else -> {
            }
        }

//        holder.itemView.setOnClickListener {
//            val intent = Intent(context, ScheduleEditActivity::class.java)
//            context.startActivity(intent)
//        }
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