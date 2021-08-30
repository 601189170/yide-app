package com.yyide.chatim.activity.book.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.databinding.ItemListBinding
import com.yyide.chatim.databinding.ItemNewBookBinding
import com.yyide.chatim.databinding.ItemNewBookGuardianBinding
import com.yyide.chatim.databinding.ItemNewBookStudentBinding

import com.yyide.chatim.model.BookClassesItem
import com.yyide.chatim.utils.GlideUtil

class BookClassesAdapter :
    BaseMultiItemQuickAdapter<BookClassesItem, BaseViewHolder>() {

    private val TAG = BookClassesAdapter::class.java.simpleName

    companion object {
        val ITEM_TYPE_CLASSES = 0
        val ITEM_TYPE_STUDNET = 1
        val ITEM_TYPE_GUARDIAN = 2
        val ITEM_TYPE_DEPARTMENT = 3
    }

    init {
        addItemType(ITEM_TYPE_CLASSES, R.layout.item_new_book)
        addItemType(ITEM_TYPE_STUDNET, R.layout.item_new_book_student)
        addItemType(ITEM_TYPE_GUARDIAN, R.layout.item_new_book_guardian)
        addItemType(ITEM_TYPE_DEPARTMENT, R.layout.item_list)
    }

    private var staffAdapter = BookStaffAdapter()

    @SuppressLint("NotifyDataSetChanged")
    override fun convert(holder: BaseViewHolder, item: BookClassesItem) {
        when (holder.itemViewType) {
            ITEM_TYPE_STUDNET -> {
                val bind = ItemNewBookStudentBinding.bind(holder.itemView)
                val student = item.student
                bind.tvName.text = if (TextUtils.isEmpty(student?.name)) "未知姓名" else student?.name
                GlideUtil.loadImageHead(
                    context,
                    student?.faceInformation,
                    bind.img
                )
            }
            ITEM_TYPE_GUARDIAN -> {
                val bind = ItemNewBookGuardianBinding.bind(holder.itemView)
                val guardian = item.guardian
                if (guardian != null) {
                    bind.tvName.text = guardian.name
                    bind.tvGuardianName.text = guardian.getRelation()
                }
            }
            ITEM_TYPE_DEPARTMENT -> {
                val bind = ItemListBinding.bind(holder.itemView)
                bind.recyclerview.layoutManager = LinearLayoutManager(context)
                bind.recyclerview.adapter = staffAdapter
                staffAdapter.setList(item.departmentList)
            }
            else -> {
                initClass(holder, item)
            }
        }
    }

    private fun initClass(
        holder: BaseViewHolder,
        item: BookClassesItem
    ) {
        val view = ItemNewBookBinding.bind(holder.itemView)
        view.tvName.text = item.name
        Log.e(TAG, "onBindViewHolder isUnfold: " + item.unfold)
        if (item.unfold) {
            view.ivUnfold.setImageDrawable(context.resources.getDrawable(R.mipmap.icon_unfold))
        } else {
            view.ivUnfold.setImageDrawable(context.resources.getDrawable(R.mipmap.icon_fold))
        }

        view.root.setOnClickListener { v: View? ->
            item.unfold = !item.unfold
            val dataList = setData(item)
            if (item.unfold) {
                if (item.studentList.isNotEmpty()) {
                    data.addAll(holder.bindingAdapterPosition + 1, dataList)
                    notifyItemRangeInserted(holder.bindingAdapterPosition + 1, dataList.size)
                }
            } else {
                if (item.studentList.isNotEmpty()) {
                    data.removeAll(dataList)
                    notifyItemRangeRemoved(
                        holder.bindingAdapterPosition + 1, dataList.size
                    )
                }
            }
            notifyItemChanged(holder.adapterPosition)
        }
    }

    private fun setData(dataList: BookClassesItem): List<BookClassesItem> {
        var resultData = mutableListOf<BookClassesItem>()
        dataList.studentList.forEach { bookStudentItem ->
            val student = BookClassesItem()
            student.student = bookStudentItem
            student.itemType = ITEM_TYPE_STUDNET
            resultData.add(student)
            bookStudentItem.guardianList?.forEach { guardianItem ->
                val guardian = BookClassesItem()
                guardian.guardian = guardianItem
                guardian.itemType = ITEM_TYPE_GUARDIAN
                resultData.add(guardian)
            }
        }
        return resultData
    }
}