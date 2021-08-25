package com.yyide.chatim.activity.book.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.book.BookPatriarchDetailActivity
import com.yyide.chatim.activity.book.BookStudentDetailActivity
import com.yyide.chatim.databinding.ItemNewBookBinding
import com.yyide.chatim.databinding.ItemNewBookGuardianBinding
import com.yyide.chatim.databinding.ItemNewBookStudentBinding

import com.yyide.chatim.model.BookClassesItem
import com.yyide.chatim.model.BookGuardianItem
import com.yyide.chatim.model.BookStudentItem
import com.yyide.chatim.utils.GlideUtil

class BookClassesAdapter(layoutId: Int) :
    BaseQuickAdapter<BookClassesItem, BaseViewHolder>(layoutId) {

    private val TAG = BookClassesAdapter::class.java.simpleName

    @SuppressLint("NotifyDataSetChanged")
    override fun convert(holder: BaseViewHolder, item: BookClassesItem) {
        val view = ItemNewBookBinding.bind(holder.itemView)
        view.tvName.text = item.name
        view.ivUnfold.setImageDrawable(context.resources.getDrawable(R.mipmap.icon_fold))

        if (item.studentList != null && item.studentList.isNotEmpty()) {
            Log.e(TAG, "onBindViewHolder isUnfold: " + item.unfold)
            if (item.unfold) {
                view.ivUnfold.setImageDrawable(context.resources.getDrawable(R.mipmap.icon_unfold))
                view.recyclerview.visibility = View.VISIBLE
            } else {
                view.ivUnfold.setImageDrawable(context.resources.getDrawable(R.mipmap.icon_fold))
                view.recyclerview.visibility = View.GONE
            }
            view.recyclerview.layoutManager = LinearLayoutManager(context)
            view.recyclerview.adapter = studentAdapter
            studentAdapter.setList(item.studentList)
            studentAdapter.setOnItemClickListener { adapter, view, position ->
                BookStudentDetailActivity.start(context, studentAdapter.getItem(position))
            }
            view.root.setOnClickListener { v: View? ->
                item.unfold = !item.unfold
                notifyDataSetChanged()
            }
        }
    }

    private val studentAdapter =
        object : BaseQuickAdapter<BookStudentItem, BaseViewHolder>(R.layout.item_new_book_student) {
            override fun convert(holder: BaseViewHolder, item: BookStudentItem) {
                val bind = ItemNewBookStudentBinding.bind(holder.itemView)
                bind.tvName.text = if (TextUtils.isEmpty(item.name)) "未知姓名" else item.name
                GlideUtil.loadImageRadius2(
                    context,
                    item.faceInformation,
                    bind.img,
                    SizeUtils.dp2px(2f)
                )
                bind.recyclerView.layoutManager = LinearLayoutManager(context)
                bind.recyclerView.adapter = guardianAdapter
                guardianAdapter.setList(item.guardianList)
                guardianAdapter.setOnItemClickListener { adapter, view, position ->
                    BookPatriarchDetailActivity.start(context, guardianAdapter.getItem(position))
                }
            }
        }

    private val guardianAdapter = object :
        BaseQuickAdapter<BookGuardianItem, BaseViewHolder>(R.layout.item_new_book_guardian) {
        override fun convert(holder: BaseViewHolder, item: BookGuardianItem) {
            val bind = ItemNewBookGuardianBinding.bind(holder.itemView)
            bind.tvName.text = item.name
            bind.tvGuardianName.text = item.getRelation()
        }
    }
}