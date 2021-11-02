package com.yyide.chatim.adapter.schedule

import android.content.Intent
import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.schedule.ScheduleEditActivity
import com.yyide.chatim.model.schedule.Schedule
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.utils.DateUtils

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

    override fun convert(holder: BaseViewHolder, item: ScheduleData) {
        when (holder.itemViewType) {
            Schedule.TYPE_EXPIRED_COMPLETED -> {
                holder.setText(R.id.tv_schedule_name, item.name)
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
                loadImage(
                    item.type.toInt(),
                    DateUtils.dateExpired(item.endTime),
                    holder.getView(R.id.iv_schedule_type_img)
                )
            }
            Schedule.TYPE_EXPIRED_NOT_COMPLETED -> {
                holder.setText(R.id.tv_schedule_name, item.name)
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
                loadImage(
                    item.type.toInt(),
                    DateUtils.dateExpired(item.endTime),
                    holder.getView(R.id.iv_schedule_type_img)
                )
            }
            Schedule.TYPE_UNEXPIRED_COMPLETED -> {
                holder.setText(R.id.tv_schedule_name, item.name)
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
                loadImage(
                    item.type.toInt(),
                    DateUtils.dateExpired(item.endTime),
                    holder.getView(R.id.iv_schedule_type_img)
                )
            }
            Schedule.TYPE_UNEXPIRED_NOT_COMPLETED -> {
                holder.setText(R.id.tv_schedule_name, item.name)
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
                loadImage(
                    item.type.toInt(),
                    DateUtils.dateExpired(item.endTime),
                    holder.getView(R.id.iv_schedule_type_img)
                )
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