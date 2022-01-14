package com.yyide.chatim.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yyide.chatim.databinding.ItemPushSettingBinding
import com.yyide.chatim.model.PushSettingBean

/**
 *
 * @author liu tao
 * @date 2022/1/14 11:51
 * @description 推送配置
 */
class PushSettingAdapter(
    val context: Context,
    private val dataList: List<PushSettingBean>,
    val onClickItemListener: (position: Int,isChecked:Boolean) -> Unit
) : RecyclerView.Adapter<PushSettingAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemPushSettingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPushSettingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text = dataList[position].name
        holder.binding.switchPush.isChecked = dataList[position].onOff
        holder.binding.switchPush.setOnCheckedChangeListener { _, isChecked ->
            onClickItemListener(position,isChecked)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}