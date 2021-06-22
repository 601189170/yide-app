package com.yyide.chatim.activity.newnotice.fragment

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.yyide.chatim.R
import java.util.*

class PagerSnapHelperAdapter(private val mDataList: ArrayList<String>) : RecyclerView.Adapter<PagerSnapHelperAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
        val view = View.inflate(viewGroup.context, R.layout.item_notice_confirm_detail, null)
        val contentView = view.findViewById<View>(R.id.tv_notice_content)

        // 创建一个ViewHolder
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.e("xiaxl: ", "---onBindViewHolder---")

        // 绑定数据到ViewHolder上
        viewHolder.itemView.tag = position
        //
        viewHolder.mTextView.text = "$position item"
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mTextView: TextView = itemView.findViewById<View>(R.id.tv_notice_title) as TextView
    }
}