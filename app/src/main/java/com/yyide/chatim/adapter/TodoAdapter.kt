package com.yyide.chatim.adapter

import android.view.View
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.databinding.ItemTodoLeaveBinding
import com.yyide.chatim.model.TodoRsp

class TodoAdapter : BaseMultiItemQuickAdapter<TodoRsp.TodoItemBean, BaseViewHolder>(),
    LoadMoreModule {
    init {
        addItemType(TodoRsp.TodoItemBean.ITEM_TYPE_MESSAGE, R.layout.message_item)
        addItemType(TodoRsp.TodoItemBean.ITEM_TYPE_LEAVE, R.layout.item_todo_leave)
        addItemType(TodoRsp.TodoItemBean.ITEM_TYPE_NOTICE, R.layout.message_item)
    }

    override fun convert(holder: BaseViewHolder, item: TodoRsp.TodoItemBean) {
        when (item.itemType) {
            TodoRsp.TodoItemBean.ITEM_TYPE_MESSAGE -> {}
            TodoRsp.TodoItemBean.ITEM_TYPE_LEAVE -> setLeaveItem(holder, item)
            TodoRsp.TodoItemBean.ITEM_TYPE_NOTICE -> {}
        }
    }

    private fun setLeaveItem(holder: BaseViewHolder, item: TodoRsp.TodoItemBean) {
        val vb = ItemTodoLeaveBinding.bind(holder.itemView)
        vb.tvDate.text = item.startTime
        vb.tvContent.text = item.title
        if (item.identity == "2") {// 2 家长
            vb.group.visibility = View.VISIBLE
            val leaveBean = item.getJsonData()
            if (leaveBean != null) {
                vb.tvStartTime.text = "开始时间：${leaveBean.startTime}"
                vb.tvEndTime.text = "结束时间：${leaveBean.endTime}"
                vb.tvLeaveName.text = "请假学生：${leaveBean.student}"
            }
        } else {
            vb.group.visibility = View.GONE
        }
        when (item.status) {//0 已拒绝  1待审批 2 已通过
            0 -> {
                vb.tvStatus.text = "已拒绝"
                vb.btnRefuse.visibility = View.INVISIBLE
                vb.btnPass.visibility = View.INVISIBLE
                vb.tvStatus.setTextColor(context.resources.getColor(R.color.text_666666))
            }
            1 -> {
                vb.tvStatus.text = "待审批"
                vb.btnRefuse.visibility = View.VISIBLE
                vb.btnPass.visibility = View.VISIBLE
                vb.tvStatus.setTextColor(context.resources.getColor(R.color.color_ffc328))
            }
            2 -> {
                vb.tvStatus.text = "已处理"
                vb.btnRefuse.visibility = View.INVISIBLE
                vb.btnPass.visibility = View.INVISIBLE
                vb.tvStatus.setTextColor(context.resources.getColor(R.color.text_666666))
            }
        }
        vb.btnPass.setOnClickListener {
            if (mLeaveCallBack != null) {
                mLeaveCallBack?.pass(item.taskId)
            }
        }
        vb.btnRefuse.setOnClickListener {
            if (mLeaveCallBack != null) {
                mLeaveCallBack?.reFuse(item.taskId)
            }
        }
    }

    fun setLeaveCallBack(onLeaveCallBack: LeaveCallBack) {
        this.mLeaveCallBack = onLeaveCallBack
    }

    private var mLeaveCallBack: LeaveCallBack? = null

    interface LeaveCallBack {
        fun pass(id: String)
        fun reFuse(id: String)
    }

    private fun setTodoItem(holder: BaseViewHolder, o: TodoRsp.TodoItemBean) {
//        holder.setText(R.id.tv_leave, o.firstData)
//            .setText(R.id.tv_title, o.title)
//        //GlideUtil.loadCircleImage(getContext(), getContext().getResources().getDrawable(R.mipmap.ic_launcher_logo), holder.getView(R.id.img));
//        //处理内容解析
//        try {
//            if (TodoRsp.DataBean.RecordsBean.IS_TEXT_TYPE == o.isText) {
//                holder.setText(R.id.tv_leave_type, o.content)
//            } else {
//                val content = o.content
//                if (!TextUtils.isEmpty(content)) {
//                    val type = object : TypeToken<List<String?>?>() {}.type
//                    val strings = JSON.parseObject<List<String>>(content, type)
//                    val stringBuffer = StringBuilder()
//                    if (strings != null) {
//                        for (i in strings.indices) {
//                            stringBuffer.append(strings[i])
//                            if (i < strings.size - 1) {
//                                stringBuffer.append("\n").append("")
//                            }
//                        }
//                    }
//                    holder.setText(R.id.tv_leave_type, stringBuffer.toString())
//                }
//            }
//        } catch (e: Exception) {
//            holder.setText(R.id.tv_leave_type, o.content)
//            e.printStackTrace()
//        }
//        val textView2 = holder.getView<TextView>(R.id.tv_agree)
//        //如果身份是家长隐藏按钮
//        if (!SpData.getIdentityInfo().staffIdentity()) {
//            textView2.visibility = View.GONE
//        } else {
//            textView2.visibility = View.VISIBLE
//        }
//        textView2.setOnClickListener { v: View? ->
//            val intent = Intent(context, LeaveFlowDetailActivity::class.java)
//            intent.putExtra("type", 2)
//            intent.putExtra("id", o.callId)
//            startActivity(intent)
//        }
    }

}