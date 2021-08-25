package com.yyide.chatim.activity.book.adapter

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.databinding.ItemNewBookBinding

class BookPatriarchAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_new_book){
    override fun convert(holder: BaseViewHolder, item: String) {
        val view = ItemNewBookBinding.bind(holder.itemView)
//        view.tvName.text = item.name
//        view.ivUnfold.setImageDrawable(context.resources.getDrawable(R.mipmap.icon_fold))
//
//        if (item.studentList != null && item.studentList.isNotEmpty()) {
//            Log.e(TAG, "onBindViewHolder isUnfold: " + item.unfold)
//            if (item.unfold) {
//                view.ivUnfold.setImageDrawable(context.resources.getDrawable(R.mipmap.icon_unfold))
//                view.recyclerview.visibility = View.VISIBLE
//            } else {
//                view.ivUnfold.setImageDrawable(context.resources.getDrawable(R.mipmap.icon_fold))
//                view.recyclerview.visibility = View.GONE
//            }
//            view.recyclerview.layoutManager = LinearLayoutManager(context)
//            view.recyclerview.adapter = studentAdapter
//            studentAdapter.setList(item.studentList)
//
//            view.root.setOnClickListener { v: View? ->
//                item.unfold = !item.unfold
//                notifyDataSetChanged()
//            }
//        }
    }
}