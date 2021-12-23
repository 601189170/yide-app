package com.yyide.chatim.adapter.gate

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.yyide.chatim.R
import com.yyide.chatim.activity.gate.GateDetailInfoActivity
import com.yyide.chatim.databinding.ItemGateDetailRecordBinding
import com.yyide.chatim.databinding.ItemGateThroughListFourColumnDataBinding
import com.yyide.chatim.databinding.ItemGateThroughListThreeColumnDataBinding
import com.yyide.chatim.databinding.ItemGateThroughListTwoColumnDataBinding

/**
 *
 * @author liu tao
 * @date 2021/12/21 16:28
 * @description 闸机通行数据展示，三种情况
 *       1，两列 姓名--班级/部门
 *       2，三列 姓名--出校/入校时间 地址
 *       3，四列 姓名--班级--出入校时间 地址
 */
class GateThroughListAdapter(
    val context: Context,
    private val dataList: List<GateThroughData>,
    val onClickItemListener: (position: Int) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //两列布局
    val VIEW_TYPE_2_COLUMN = 2

    //三列布局
    val VIEW_TYPE_3_COLUMN = 3

    //四列布局
    val VIEW_TYPE_4_COLUMN = 4

    class ViewHolder2(val binding: ItemGateThroughListTwoColumnDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ViewHolder3(val binding: ItemGateThroughListThreeColumnDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ViewHolder4(val binding: ItemGateThroughListFourColumnDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_2_COLUMN -> {
                return ViewHolder2(
                    ItemGateThroughListTwoColumnDataBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            VIEW_TYPE_3_COLUMN -> {
                return ViewHolder3(
                    ItemGateThroughListThreeColumnDataBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            VIEW_TYPE_4_COLUMN -> {
                return ViewHolder4(
                    ItemGateThroughListFourColumnDataBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            else -> {
                return ViewHolder3(
                    ItemGateThroughListThreeColumnDataBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val throughData = dataList[position]
        when (getItemViewType(position)) {
            VIEW_TYPE_2_COLUMN -> {
                val binding = (holder as ViewHolder2).binding
                binding.tvName.text = throughData.name
                binding.tvClass.text = throughData.clazz
            }
            VIEW_TYPE_3_COLUMN -> {
                val binding = (holder as ViewHolder3).binding
                binding.tvName.text = throughData.name
                binding.tvTime.text = throughData.time
                binding.tvAddress.text = throughData.address
            }
            VIEW_TYPE_4_COLUMN -> {
                val binding = (holder as ViewHolder4).binding
                binding.tvName.text = throughData.name
                binding.tvClass.text = throughData.clazz
                binding.tvTime.text = throughData.time
                binding.tvAddress.text = throughData.address
            }
            else -> {

            }
        }
        holder.itemView.setOnClickListener {
            onClickItemListener(position)
        }
    }

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int = dataList[position].type
}