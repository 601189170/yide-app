package com.yyide.chatim.adapter.schedule

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginRight
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.model.schedule.LabelListRsp
import com.yyide.chatim.utils.ColorUtil

/**
 * @ProjectName : yideapp
 * @Author : J
 * @Time : 2022/4/12 14:34
 * @Description : 列表下的标签列表
 */
class ScheduleTabListAdapter : BaseQuickAdapter<LabelListRsp.DataBean, BaseViewHolder>(R.layout.item_schedule_label_flow_list) {
    @SuppressLint("WrongConstant")
    override fun convert(holder: BaseViewHolder, item: LabelListRsp.DataBean) {
        holder.getView<ImageView>(R.id.iv_del).visibility = View.GONE
        val drawable = GradientDrawable()
        drawable.setStroke(1, ColorUtil.parseColor(item.colorValue))
        drawable.shape = GradientDrawable.LINEAR_GRADIENT;
        holder.setTextColor(R.id.tv_label, ColorUtil.parseColor(item.colorValue))
        holder.getView<TextView>(R.id.tv_label).background = drawable

       // holder.getView<TextView>(R.id.tv_label).marginRight = 5
        holder.setText(R.id.tv_label, item.labelName)
//            holder.itemView.setOnClickListener {
//                loge("item=$item")
//                if (!enableEditMode){
//                    loge("日程便签只有发起人才能删除！")
//                    return@setOnClickListener
//                }
//                remove(item)
//                labelList.remove(item)
//                notifyDataSetChanged()
//            }
    }
}