package com.yyide.chatim_pro.adapter.address

import com.baozi.treerecyclerview.base.ViewHolder
import com.baozi.treerecyclerview.factory.ItemHelperFactory
import com.baozi.treerecyclerview.item.TreeItem
import com.baozi.treerecyclerview.item.TreeItemGroup
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.model.address.ScheduleAddressBean

/**
 *
 * @author shenzhibin
 * @date 2022/3/24 9:12
 * @description 选择场所的父item
 */
class AddressItemParent : TreeItemGroup<ScheduleAddressBean>() {
    override fun onBindViewHolder(viewHolder: ViewHolder) {
        viewHolder.setText(R.id.tv_title,data.name)
        if (isExpand) {
            viewHolder.getImageView(R.id.iv_remind).setImageResource(R.mipmap.address_back_icon)
        }else{
            viewHolder.getImageView(R.id.iv_remind).setImageResource(R.mipmap.address_expand_icon)
        }
    }

    override fun initChild(data: ScheduleAddressBean?): MutableList<TreeItem<Any>>? {
        return ItemHelperFactory.createItems(data?.siteList,this)
    }

    override fun getLayoutId(): Int {
        return R.layout.item_dialog_schedule_remind_list
    }

}