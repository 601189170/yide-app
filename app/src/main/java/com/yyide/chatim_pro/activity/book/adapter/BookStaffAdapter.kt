package com.yyide.chatim_pro.activity.book.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.activity.book.BookTeacherDetailActivity
import com.yyide.chatim_pro.databinding.ItemNewBookBinding
import com.yyide.chatim_pro.databinding.ItemNewBookTeacherBinding
import com.yyide.chatim_pro.model.BookDepartmentItem
import com.yyide.chatim_pro.model.BookTeacherItem
import com.yyide.chatim_pro.utils.GlideUtil

class BookStaffAdapter :
    BaseQuickAdapter<BookDepartmentItem, BaseViewHolder>(R.layout.item_new_book) {

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun convert(holder: BaseViewHolder, item: BookDepartmentItem) {
        val bind = ItemNewBookBinding.bind(holder.itemView)
        bind.tvName.text = item.name
        bind.ivUnfold.setImageDrawable(context.resources.getDrawable(R.mipmap.icon_fold))
        if (item.unfold) {
            bind.ivUnfold.setImageDrawable(context.resources.getDrawable(R.mipmap.icon_unfold))
            bind.recyclerview.visibility = View.VISIBLE
            bind.teacherList.visibility = View.VISIBLE
            val adapter = BookStaffAdapter()
            //部门
            bind.recyclerview.layoutManager = LinearLayoutManager(context)
            bind.recyclerview.adapter = adapter
            adapter.setList(item.list)
            //部门下教师
            val teacherAdapter = TeacherAdapter()
            bind.teacherList.layoutManager = LinearLayoutManager(context)
            bind.teacherList.adapter = teacherAdapter
            teacherAdapter.setList(item.teacheList)
            teacherAdapter.setOnItemClickListener { adapter, view, position ->
                BookTeacherDetailActivity.start(context, teacherAdapter.getItem(position))
            }
        } else {
            bind.ivUnfold.setImageDrawable(context.resources.getDrawable(R.mipmap.icon_fold))
            bind.recyclerview.visibility = View.GONE
            bind.teacherList.visibility = View.GONE
        }
        bind.root.setOnClickListener { v: View? ->
            item.unfold = !item.unfold
            notifyItemChanged(holder.adapterPosition)
        }
    }

    inner class TeacherAdapter :
        BaseQuickAdapter<BookTeacherItem, BaseViewHolder>(R.layout.item_new_book_teacher) {
        override fun convert(holder: BaseViewHolder, item: BookTeacherItem) {
            val bind = ItemNewBookTeacherBinding.bind(holder.itemView)
            bind.tvName.text = item.name
            GlideUtil.loadImageHead(context, item.faceInformation, bind.img)
        }
    }
}