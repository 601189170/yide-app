package com.yyide.chatim_pro.activity.book.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.databinding.ItemGuardianBinding
import com.yyide.chatim_pro.model.Parent
import com.yyide.chatim_pro.utils.RxPermissionUtils
import com.yyide.chatim_pro.utils.Utils

class BookGuardianAdapter :
    BaseQuickAdapter<Parent, BaseViewHolder>(R.layout.item_guardian) {

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun convert(holder: BaseViewHolder, item: Parent) {
        val bind = ItemGuardianBinding.bind(holder.itemView)
        bind.phone.text = Utils.setHideMobile(item.phone)
        if (!TextUtils.isEmpty(item.phone)){
            bind.set.visibility=View.VISIBLE
            bind.ivPhone.visibility=View.VISIBLE
        }else{
            bind.set.visibility=View.GONE
            bind.ivPhone.visibility=View.GONE
        }

        bind.set.setOnClickListener { v: View? ->

            if (item.type.equals("1") ) {
                item.type = "2"
                bind.set.text = "隐藏"
                bind.phone.text = item.phone
            } else {
                item.type = "1"
                bind.set.text = "显示"
                bind.phone.text = Utils.setHideMobile(item.phone)
            }
        }
        bind.ivPhone.setOnClickListener(View.OnClickListener {
            RxPermissionUtils.callPhone(context, item.phone)
        })

    }

}