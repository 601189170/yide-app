package com.yyide.chatim.activity.weekly.details.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.model.GetUserSchoolRsp

class ClassAdapter :
    BaseQuickAdapter<GetUserSchoolRsp.DataBean.FormBean, BaseViewHolder>(R.layout.swich_class_item) {
    private var classId = ""
    override fun convert(holder: BaseViewHolder, item: GetUserSchoolRsp.DataBean.FormBean) {
        holder.setText(R.id.className, item.classesName)
        holder.getView<View>(R.id.select).visibility =
            if (classId == item.classesId) View.VISIBLE else View.GONE
        if (this.itemCount - 1 == holder.layoutPosition) {
            holder.getView<View>(R.id.view_line).visibility = View.GONE
        } else {
            holder.getView<View>(R.id.view_line).visibility = View.VISIBLE
        }
    }

    fun setClassId(classId: String){
        this.classId = classId
    }
}