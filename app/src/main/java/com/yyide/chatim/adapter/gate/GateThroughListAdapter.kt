package com.yyide.chatim.adapter.gate

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim.databinding.ItemGateThroughListFourColumnDataBinding
import com.yyide.chatim.databinding.ItemGateThroughListOneColumnDataBinding
import com.yyide.chatim.databinding.ItemGateThroughListThreeColumnDataBinding
import com.yyide.chatim.databinding.ItemGateThroughListTwoColumnDataBinding
import com.yyide.chatim.model.gate.GateThroughPeopleListBean
import com.yyide.chatim.utils.loge

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
    private val dataList: List<GateThroughPeopleListBean.PeopleListBean>,
    val onClickItemListener: (position: Int) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val VIEW_TYPE_1_COLUMN = 1

    //两列布局
    val VIEW_TYPE_2_COLUMN = 2

    //三列布局
    val VIEW_TYPE_3_COLUMN = 3

    //四列布局
    val VIEW_TYPE_4_COLUMN = 4

    class ViewHolder1(val binding: ItemGateThroughListOneColumnDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ViewHolder2(val binding: ItemGateThroughListTwoColumnDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ViewHolder3(val binding: ItemGateThroughListThreeColumnDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ViewHolder4(val binding: ItemGateThroughListFourColumnDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_1_COLUMN -> {
                return ViewHolder1(
                    ItemGateThroughListOneColumnDataBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
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
        //2021/12/03 08:00 2021-12-31 09:40:51
        var date = ""
        if (!TextUtils.isEmpty(throughData.addTime)) {
            date = ScheduleDaoUtil.toDateTime(throughData.addTime ?: "")
                .toStringTime("yyyy/MM/dd HH:mm")
        }
        loge("${getItemViewType(position)} ,${throughData}")
        when (getItemViewType(position)) {
            VIEW_TYPE_1_COLUMN -> {
                val binding = (holder as ViewHolder1).binding
                binding.tvName.text = throughData.studentName
            }
            VIEW_TYPE_2_COLUMN -> {
                val binding = (holder as ViewHolder2).binding
                binding.tvName.text = throughData.studentName
                binding.tvClass.text = throughData.className
            }
            VIEW_TYPE_3_COLUMN -> {
                val binding = (holder as ViewHolder3).binding
                binding.tvName.text = throughData.studentName
                binding.tvTime.text = date
                binding.tvAddress.text = throughData.siteName
            }
            VIEW_TYPE_4_COLUMN -> {
                val binding = (holder as ViewHolder4).binding
                binding.tvName.text = throughData.studentName
                binding.tvClass.text = throughData.className
                binding.tvTime.text = date
                binding.tvAddress.text = throughData.siteName
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