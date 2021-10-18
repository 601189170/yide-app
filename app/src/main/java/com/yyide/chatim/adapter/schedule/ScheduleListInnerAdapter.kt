package com.yyide.chatim.adapter.schedule

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yanzhenjie.recyclerview.*
import com.yyide.chatim.R
import com.yyide.chatim.activity.schedule.ScheduleLabelCreateActivity
import com.yyide.chatim.model.schedule.DayViewScheduleData
import com.yyide.chatim.model.schedule.ScheduleInner
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.SpaceItemDecoration

/**
 *
 * @author liu tao
 * @date 2021/9/10 17:21
 * @description 描述
 */
class ScheduleListInnerAdapter :
    BaseQuickAdapter<DayViewScheduleData, BaseViewHolder>(R.layout.item_schedule_list_inner_layer) {
    var listViewEvent:ListViewEvent? = null
    override fun convert(holder: BaseViewHolder, item: DayViewScheduleData) {
        val day = item.dateTime.dayOfMonth
        val week = item.dateTime.dayOfWeek+1
        holder.setText(R.id.tv_title_day, "${day}日")
        holder.setText(R.id.tv_title_week, DateUtils.getWeek(week))
        val innerRecyclerView: SwipeRecyclerView = holder.getView(R.id.rv_schedule_list)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        innerRecyclerView.layoutManager = linearLayoutManager
        val scheduleAdapter = ScheduleAdapter(item.list)
        innerRecyclerView.setSwipeMenuCreator(swipeMenuCreator)
        innerRecyclerView.setOnItemMenuClickListener { menuBridge, position ->
            menuBridge.closeMenu()
            val direction = menuBridge.direction // 左侧还是右侧菜单。
            val menuPosition = menuBridge.position // 菜单在RecyclerView的Item中的Position。
            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                if (menuPosition == 0) {
                    loge("修改便签")
                    listViewEvent?.modifyItem(item.list[position])
                    return@setOnItemMenuClickListener
                }
                if (menuPosition == 1) {
                    loge("删除标签")
                    listViewEvent?.deleteItem(item.list[position])
                    return@setOnItemMenuClickListener
                }

            } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
                //loge("list第$position; 左侧菜单第$menuPosition")
            }
        }
        innerRecyclerView.addItemDecoration(SpaceItemDecoration(10))
        innerRecyclerView.adapter = scheduleAdapter
        scheduleAdapter.setOnItemClickListener { _, _, position ->
            listViewEvent?.clickItem(item.list[position])
        }
    }
    fun addListViewEvent(listViewEvent:ListViewEvent){
        this.listViewEvent = listViewEvent
    }
    private val swipeMenuCreator: SwipeMenuCreator = object : SwipeMenuCreator {
        override fun onCreateMenu(
            swipeLeftMenu: SwipeMenu,
            swipeRightMenu: SwipeMenu,
            position: Int
        ) {
            val width = DisplayUtils.dip2px(context, 63f)
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            run {
                val modifyItem: SwipeMenuItem =
                    SwipeMenuItem(context).setBackground(R.drawable.selector_blue)
                        //.setImage(R.drawable.ic_action_delete)
                        .setText("标为\n完成")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height)
                swipeRightMenu.addMenuItem(modifyItem) // 添加菜单到右侧。
                val delItem: SwipeMenuItem =
                    SwipeMenuItem(context).setBackground(R.drawable.selector_red)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height)
                swipeRightMenu.addMenuItem(delItem) // 添加菜单到右侧。
            }
        }
    }

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private val mMenuItemClickListener =
        OnItemMenuClickListener { menuBridge, position ->
            menuBridge.closeMenu()
            val direction = menuBridge.direction // 左侧还是右侧菜单。
            val menuPosition = menuBridge.position // 菜单在RecyclerView的Item中的Position。
            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                if (menuPosition == 0) {
                    loge("修改便签")

                    return@OnItemMenuClickListener
                }
                if (menuPosition == 1) {
                    loge("删除标签")

                    return@OnItemMenuClickListener
                }

            } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
                loge("list第$position; 左侧菜单第$menuPosition")
            }
        }
}