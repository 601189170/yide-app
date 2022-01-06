package com.yyide.chatim.adapter.gate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yyide.chatim.databinding.ItemGateStudentStaffBinding
import com.yyide.chatim.model.gate.GateBaseInfoBean

/**
 *
 * @author liu tao
 * @date 2021/12/23 16:44
 * @description 描述
 */
class GateStudentStaffBranchAdapter(
    val context: Context,
    private val dataList: List<GateBaseInfoBean>,
    val onClickItemListener:(position:Int)->Unit
) : RecyclerView.Adapter<GateStudentStaffBranchAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemGateStudentStaffBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemGateStudentStaffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.binding.tvTitle.text = data.name
        holder.binding.tvGoIntoNumber.text = "${data.intoNumber}"
        holder.binding.tvGoOutNumber.text = "${data.outNumber}"
        holder.binding.tvThroughNumber.text = "${data.totalNumber}"
        holder.itemView.setOnClickListener {
            onClickItemListener(position)
        }
    }

    override fun getItemCount(): Int = dataList.size
}