package com.yyide.chatim.activity.book.adapter

import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.book.BookPatriarchDetailActivity
import com.yyide.chatim.activity.book.BookPatriarchDetailActivity.Companion.start
import com.yyide.chatim.databinding.ItemNewBookGuardianBinding
import com.yyide.chatim.databinding.ItemNewBookStudentBinding
import com.yyide.chatim.model.*
import com.yyide.chatim.utils.GlideUtil

class BookSearchStudentAdapter :
    BaseQuickAdapter<Student, BaseViewHolder>(R.layout.item_new_book_student_search) {
    override fun convert(holder: BaseViewHolder, item: Student) {
        val bind = ItemNewBookStudentBinding.bind(holder.itemView)
        bind.tvName.text =
            if (TextUtils.isEmpty(item.name)) "未知姓名" else item.name + " (${item.typeName})"
        GlideUtil.loadImageHead(
            context,
            item.faceInformation,
            bind.img
        )
        val guardianAdapter = GuardianAdapter()
        bind.recyclerView.layoutManager = LinearLayoutManager(context)
        bind.recyclerView.adapter = guardianAdapter
        guardianAdapter.setList(item.parentList)
        guardianAdapter.setOnItemClickListener { adapter, view, position ->
            val item1 = guardianAdapter.getItem(position)
            val guardianItem = BookGuardianItem(
                item1.name,
                item1.id,
                item1.phone,
                item1.userId,
                item1.relation,
                item1.workUnit,
                item1.faceInformation,
                item1.singleParent
            )
            start(context, guardianItem)
        }
    }

    inner class GuardianAdapter :
        BaseQuickAdapter<Parent, BaseViewHolder>(R.layout.item_new_book_guardian) {
        override fun convert(holder: BaseViewHolder, item: Parent) {
            val bind = ItemNewBookGuardianBinding.bind(holder.itemView)
            bind.tvName.text = item.name
            bind.tvGuardianName.text = item.getRelation()
        }
    }
}