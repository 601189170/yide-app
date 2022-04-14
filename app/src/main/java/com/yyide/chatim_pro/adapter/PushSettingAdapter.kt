package com.yyide.chatim_pro.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yyide.chatim_pro.databinding.ItemPushSettingBinding
import com.yyide.chatim_pro.model.PushSettingBean

/**
 *
 * @author liu tao
 * @date 2022/1/14 11:51
 * @description 推送配置
 */
class PushSettingAdapter(
    val context: Context,
    private val dataList: List<PushSettingBean>,
    val onClickItemListener: (position: Int, isChecked: Boolean) -> Unit
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
        holder.binding.tvName.text = dataList[position].moduleName
        if (!TextUtils.isEmpty(dataList[position].msgControl)) {
            holder.binding.switchPush.isChecked = "0" == dataList[position].msgControl
        } else {
            holder.binding.switchPush.isChecked = false
        }
        holder.binding.switchPush.setOnCheckedChangeListener { _, isChecked ->
            onClickItemListener(position, isChecked)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}