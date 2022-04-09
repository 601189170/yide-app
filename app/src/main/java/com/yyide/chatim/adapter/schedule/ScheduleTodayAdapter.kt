package com.yyide.chatim.adapter.schedule

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.promoterSelf
import com.yyide.chatim.model.schedule.Schedule
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim.utils.loge

class ScheduleTodayAdapter(type: Int,data: List<ScheduleData>) :
    BaseMultiItemQuickAdapter<ScheduleData, BaseViewHolder>() {
    private var myType: Int = 1
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
        this.myType = type
    }
    private  var listener: ImgListener?=null

    fun setImgListener(listener: ImgListener){
        this.listener=listener
    }
    interface ImgListener{
        fun OnimgSelect(item: ScheduleData)
    }
    /**
     * 相同的布局设置
     */
    fun commonConvert(holder: BaseViewHolder, item: ScheduleData) {
        loge("ScheduleData ${JSON.toJSONString(item)}")
        holder.setText(R.id.tv_schedule_name, item.name)
        loadImage(
            item.type.toInt(),
            DateUtils.dateExpired(item.moreDayEndTime?:item.endTime),
            holder.getView(R.id.iv_schedule_type_img)
        )
        holder.getView<TextView>(R.id.iv_mine_label).visibility = if (item.promoterSelf()) View.VISIBLE else View.GONE
        if (item.promoterSelf()){
            holder.getView<CheckBox>(R.id.iv_finish_tag).visibility=View.VISIBLE
            holder.getView<CheckBox>(R.id.iv_finish_tag).isEnabled=true
            if (item.status == "1") {
                holder.getView<CheckBox>(R.id.iv_finish_tag).isChecked=true
            } else {
                holder.getView<CheckBox>(R.id.iv_finish_tag).isChecked=false
            }

            holder.getView<CheckBox>(R.id.iv_finish_tag).setOnClickListener(View.OnClickListener {
                if (listener!=null){
                    listener!!.OnimgSelect(item)
                }
            })
        }

        //今日清单：
        //全天不跨天   全天
        //全天跨天     全天 第1天，共3天
        //非全天跨天   08：00 - 09：00  第1天，共3天
        //非全天不跨天 08：00 - 10：00
        //
        //本周未完成：
        //全天不跨天   全天 3月5日
        //全天跨天     全天 3月5日 - 3月8日
        //非全天跨天   3月5日 08：00 - 3月8日 09：00
        //非全天不跨天 3月5日 08：00 - 10：00
        //搜索列表
        //3月5日 08：00 - 3月5日 10：00 （全天）
        val simplifiedDataTime =
            ScheduleDaoUtil.toDateTime(item.moreDayStartTime ?: item.startTime).simplifiedDataTime()
        val simplifiedDataTime1 =
            ScheduleDaoUtil.toDateTime(item.moreDayEndTime ?: item.endTime).simplifiedDataTime()
        //是否跨天
        val moreDay = item.moreDay == 1
        if (myType == TYPE_SEARCH_LIST) {
            if (item.isAllDay == "1") {
                val target = "MM月dd日"
                val startTime =
                    DateUtils.formatTime(item.moreDayStartTime ?: item.startTime, "", target)
                val endTime = DateUtils.formatTime(item.moreDayEndTime ?: item.endTime, "", target)
                holder.setText(R.id.tv_schedule_time_interval, "$startTime - $endTime".plus("（全天）"))
            } else {
                val target = "MM月dd日 HH:mm"
                val startTime =
                    DateUtils.formatTime(item.moreDayStartTime ?: item.startTime, "", target)
                val endTime = DateUtils.formatTime(item.moreDayEndTime ?: item.endTime, "", target)
                holder.setText(R.id.tv_schedule_time_interval, "$startTime - $endTime")
            }
            return
        }
        //1.适用日程：全天跨天非重复
        if (item.isAllDay == "1" && moreDay && item.isRepeat == "0") {
            if (myType == TYPE_TODAY_LIST){
                //全天跨天     全天 第1天，共3天
                holder.setText(R.id.tv_schedule_time_interval, "全天 ".plus("第${item.moreDayIndex}天，").plus("共${item.moreDayCount}天"))
                return
            }
            val target = "MM月dd日"
            val startTime = DateUtils.formatTime(item.moreDayStartTime ?: item.startTime, "", target)
            val endTime = DateUtils.formatTime(item.moreDayEndTime ?: item.endTime, "", target)
            holder.setText(R.id.tv_schedule_time_interval, "全天 ".plus("$startTime - $endTime"))
            return
        }
        //2.适用日程：非全天跨天非重复
        if (item.isAllDay == "0" && moreDay && item.isRepeat == "0") {
            if (myType == TYPE_TODAY_LIST){
                val target = "HH:mm"
                val startTime = DateUtils.formatTime(item.moreDayStartTime ?: item.startTime, "", target)
                val endTime = DateUtils.formatTime(item.moreDayEndTime ?: item.endTime, "", target)
                holder.setText(R.id.tv_schedule_time_interval, "$startTime - $endTime ".plus("第${item.moreDayIndex}天，").plus("共${item.moreDayCount}天"))
                return
            }
            val target = "MM月dd日 HH:mm"
            val startTime = DateUtils.formatTime(item.moreDayStartTime ?: item.startTime, "", target)
            val endTime = DateUtils.formatTime(item.moreDayEndTime ?: item.endTime, "", target)
            holder.setText(R.id.tv_schedule_time_interval, "$startTime - $endTime")
            return
        }
        //3.适用日程：全天不跨天非重复、全天不跨天重复
        if ((item.isAllDay == "1" && !moreDay && item.isRepeat == "0") || (item.isAllDay == "1" && !moreDay && item.isRepeat != "0")){
            if (myType == TYPE_TODAY_LIST){
                holder.setText(R.id.tv_schedule_time_interval, "全天")
                return
            }
            val target = "MM月dd日"
            val startTime = DateUtils.formatTime(item.moreDayStartTime ?: item.startTime, "", target)
            //val endTime = DateUtils.formatTime(item.moreDayEndTime ?: item.endTime, "", target)
            holder.setText(R.id.tv_schedule_time_interval, "全天".plus(startTime))
            return
        }
        //4.适用日程：非全天不跨天重复、非全天不跨天非重复
        if ((item.isAllDay == "0" && !moreDay && item.isRepeat != "0") || (item.isAllDay == "0" && !moreDay && item.isRepeat == "0")){
            if (myType == TYPE_TODAY_LIST){
                val target = "HH:mm"
                val startTime = DateUtils.formatTime(item.moreDayStartTime ?: item.startTime, "", target)
                val endTime = DateUtils.formatTime(item.moreDayEndTime ?: item.endTime, "", target)
                holder.setText(R.id.tv_schedule_time_interval, "$startTime - $endTime")
                return
            }
            val target = "MM月dd日 HH:mm"
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

    companion object{
        //今日清单列表
        const val TYPE_TODAY_LIST = 1
        //本周未完成列表
        const val TYPE_WEEK_UNDONE_LIST = 2
        //搜索列表
        const val TYPE_SEARCH_LIST = 3
    }

}