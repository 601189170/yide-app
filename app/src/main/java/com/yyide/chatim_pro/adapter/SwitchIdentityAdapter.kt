package com.yyide.chatim_pro.adapter

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.databinding.ItemSelectIdentityBinding
import com.yyide.chatim_pro.model.SchoolRsp

/**
 * 选择身份
 */
class SwitchIdentityAdapter :
    BaseQuickAdapter<SchoolRsp.IdentityBean, BaseViewHolder>(R.layout.item_select_identity) {
    private var selectIndex = 0
    fun setSelectIndex(position: Int) {
        selectIndex = position
        notifyDataSetChanged()
    }

    fun getSelectItem(): SchoolRsp.IdentityBean? {
        if (data != null && data.size > 0) {
            return getItem(selectIndex)
        }
        return null
    }

    override fun convert(holder: BaseViewHolder, item: SchoolRsp.IdentityBean) {
        val viewbinding = ItemSelectIdentityBinding.bind(holder.itemView)
        if (item.identity == SchoolRsp.IdentityBean.IDENTITY_PARENTS) {
            viewbinding.ivHead.setImageResource(R.mipmap.icon_parents_head)
        } else {
            viewbinding.ivHead.setImageResource(R.mipmap.icon_techer_head)
        }

        if (selectIndex == holder.absoluteAdapterPosition) {
            viewbinding.viewBg.visibility = View.INVISIBLE
            viewbinding.tvName.textSize = 15f
            viewbinding.tvName.setTextColor(context.resources.getColor(R.color.text_333333))
            viewbinding.ivHead.layoutParams =
                ConstraintLayout.LayoutParams(SizeUtils.dp2px(96f), SizeUtils.dp2px(96f))
            viewbinding.viewBg.layoutParams =
                ConstraintLayout.LayoutParams(SizeUtils.dp2px(96f), SizeUtils.dp2px(96f))
        } else {
            viewbinding.viewBg.visibility = View.VISIBLE
            viewbinding.tvName.textSize = 13f
            viewbinding.tvName.setTextColor(context.resources.getColor(R.color.text_999999))
            viewbinding.ivHead.layoutParams =
                ConstraintLayout.LayoutParams(SizeUtils.dp2px(84f), SizeUtils.dp2px(84f))
            viewbinding.viewBg.layoutParams =
                ConstraintLayout.LayoutParams(SizeUtils.dp2px(84f), SizeUtils.dp2px(84f))
        }
        viewbinding.tvName.text = item.identityName
    }

}