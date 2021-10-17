package com.yyide.chatim.adapter.schedule

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.model.schedule.MonthViewScheduleData
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.model.schedule.ScheduleInner
import com.yyide.chatim.model.schedule.ScheduleOuter
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.view.SpaceItemDecoration

/**
 *
 * @author liu tao
 * @date 2021/9/10 17:17
 * @description 描述
 */
class ScheduleListOuterAdapter :
    BaseQuickAdapter<MonthViewScheduleData, BaseViewHolder>(R.layout.item_schedule_list_outer_layer) {
    var listViewEvent: ListViewEvent? = null
    var outerRecyclerView: RecyclerView? = null
    override fun convert(holder: BaseViewHolder, item: MonthViewScheduleData) {
        val month = item.dateTime.toString("yyyy-MM-dd HH:mm:ss")
        holder.setText(R.id.tv_title_month, DateUtils.formatTime(month, "", "MM月"))
        outerRecyclerView =
            holder.getView(R.id.rv_schedule_list)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        outerRecyclerView?.layoutManager = linearLayoutManager
        val adapterInner = ScheduleListInnerAdapter()
        adapterInner.setList(item.list)
        outerRecyclerView?.addItemDecoration(SpaceItemDecoration(10))
        outerRecyclerView?.adapter = adapterInner
        adapterInner.addListViewEvent(object : ListViewEvent {
            override fun deleteItem(scheduleData: ScheduleData) {
                listViewEvent?.deleteItem(scheduleData)
            }

            override fun clickItem(scheduleData: ScheduleData) {
                listViewEvent?.clickItem(scheduleData)
            }

            override fun modifyItem(scheduleData: ScheduleData) {
                listViewEvent?.modifyItem(scheduleData)
            }

        })
    }

    fun getInnerRecyclerView(): RecyclerView? {
        return outerRecyclerView
    }

    fun addListViewEvent(listViewEvent: ListViewEvent) {
        this.listViewEvent = listViewEvent
    }
}

interface ListViewEvent {
    fun deleteItem(scheduleData: ScheduleData)
    fun clickItem(scheduleData: ScheduleData)
    fun modifyItem(scheduleData: ScheduleData)
}