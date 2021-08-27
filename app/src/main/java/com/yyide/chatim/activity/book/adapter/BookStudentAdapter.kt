package com.yyide.chatim.activity.book.adapter

import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.book.BookPatriarchDetailActivity
import com.yyide.chatim.databinding.ItemNewBookGuardianBinding
import com.yyide.chatim.databinding.ItemNewBookStudentBinding
import com.yyide.chatim.model.BookGuardianItem
import com.yyide.chatim.model.BookStudentItem
import com.yyide.chatim.utils.GlideUtil

class BookStudentAdapter : BaseQuickAdapter<BookStudentItem, BaseViewHolder>(R.layout.item_new_book_student) {
    override fun convert(holder: BaseViewHolder, item: BookStudentItem) {
        val bind = ItemNewBookStudentBinding.bind(holder.itemView)
        bind.tvName.text = if (TextUtils.isEmpty(item.name)) "未知姓名" else item.name
        GlideUtil.loadImageHead(
            context,
            item.faceInformation,
            bind.img
        )
        val guardianAdapter = GuardianAdapter()
        bind.recyclerView.layoutManager = LinearLayoutManager(context)
        bind.recyclerView.adapter = guardianAdapter
        guardianAdapter.setList(item.guardianList)
        guardianAdapter.setOnItemClickListener { adapter, view, position ->
            BookPatriarchDetailActivity.start(context, guardianAdapter.getItem(position))
        }
    }

    inner class GuardianAdapter :
        BaseQuickAdapter<BookGuardianItem, BaseViewHolder>(R.layout.item_new_book_guardian) {
        override fun convert(holder: BaseViewHolder, item: BookGuardianItem) {
            val bind = ItemNewBookGuardianBinding.bind(holder.itemView)
            bind.tvName.text = item.name
            bind.tvGuardianName.text = item.getRelation()
        }
    }
}