package com.yyide.chatim.adapter.schedule

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.model.schedule.ScheduleInner
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.view.SpaceItemDecoration

/**
 *
 * @author liu tao
 * @date 2021/9/10 17:21
 * @description 描述
 */
class ScheduleListInnerAdapter :
    BaseQuickAdapter<ScheduleInner, BaseViewHolder>(R.layout.item_schedule_list_inner_layer) {
    override fun convert(holder: BaseViewHolder, item: ScheduleInner) {
        val day = item.day
        val week = item.week
        holder.setText(R.id.tv_title_day, DateUtils.formatTime(day, "", "dd日"))
        holder.setText(R.id.tv_title_week, week)
        val innerRecyclerView: RecyclerView = holder.getView(R.id.rv_schedule_list)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        innerRecyclerView.layoutManager = linearLayoutManager
        val scheduleAdapter = ScheduleAdapter(item.list)
        innerRecyclerView.addItemDecoration(SpaceItemDecoration(10))
        innerRecyclerView.adapter = scheduleAdapter
    }
}