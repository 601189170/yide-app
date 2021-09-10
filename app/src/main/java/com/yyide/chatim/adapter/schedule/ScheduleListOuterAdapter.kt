package com.yyide.chatim.adapter.schedule

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
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
class ScheduleListOuterAdapter: BaseQuickAdapter<ScheduleOuter, BaseViewHolder>(R.layout.item_schedule_list_outer_layer) {
    override fun convert(holder: BaseViewHolder, item: ScheduleOuter) {
        val month = item.month
        holder.setText(R.id.tv_title_month, DateUtils.formatTime(month, "", "MM月"))
        val outerRecyclerView: RecyclerView =
            holder.getView(R.id.rv_schedule_list)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        outerRecyclerView.layoutManager = linearLayoutManager
        val adapterInner = ScheduleListInnerAdapter()
        adapterInner.setList(item.list)
        outerRecyclerView.addItemDecoration(SpaceItemDecoration(10))
        outerRecyclerView.adapter = adapterInner
    }
}