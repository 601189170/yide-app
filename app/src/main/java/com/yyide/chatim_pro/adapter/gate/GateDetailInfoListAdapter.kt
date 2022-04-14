package com.yyide.chatim_pro.adapter.gate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.databinding.ItemGateDetailRecordBinding

/**
 *
 * @author liu tao
 * @date 2021/12/21 16:28
 * @description 描述
 */
class GateDetailInfoListAdapter(val context: Context, private val dataList: List<GateDetailInfo>) :
    RecyclerView.Adapter<GateDetailInfoListAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemGateDetailRecordBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGateDetailRecordBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gateDetailInfo = dataList[position]
        holder.binding.tvTime.text = gateDetailInfo.time
        holder.binding.tvDate.text = gateDetailInfo.date
        holder.binding.tvEventTitle.text = gateDetailInfo.title
        if (position == 0) {
            holder.binding.vLineTop.visibility = View.INVISIBLE
            holder.binding.vLineBottom.visibility = View.VISIBLE
        } else if (position == dataList.size - 1) {
            holder.binding.vLineTop.visibility = View.VISIBLE
            holder.binding.vLineBottom.visibility = View.INVISIBLE
        } else {
            holder.binding.vLineTop.visibility = View.VISIBLE
            holder.binding.vLineBottom.visibility = View.VISIBLE
        }
        if (gateDetailInfo.type == 2) {
            //入校
            holder.binding.tvEventType.text = "入"
            holder.binding.tvEventType.setTextColor(context.resources.getColor(R.color.blue11))
            holder.binding.vDot.background =
                ResourcesCompat.getDrawable(context.resources, R.drawable.dot_blue_shape, null)
        } else {
            //离校
            holder.binding.tvEventType.text = "出"
            holder.binding.tvEventType.setTextColor(context.resources.getColor(R.color.cpb_red_dark))
            holder.binding.vDot.background =
                ResourcesCompat.getDrawable(context.resources, R.drawable.dot_red_shape, null)
        }

    }

    override fun getItemCount(): Int = dataList.size
}