package com.yyide.chatim_pro.adapter.address

import com.baozi.treerecyclerview.base.ViewHolder
import com.baozi.treerecyclerview.item.TreeItem
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.activity.schedule.ScheduleAddressActivity
import com.yyide.chatim_pro.model.address.SiteListItem
import com.yyide.chatim_pro.utils.hide
import com.yyide.chatim_pro.utils.show

/**
 *
 * @author shenzhibin
 * @date 2022/3/24 9:24
 * @description 选择场所的子item
 */
class AddressItem : TreeItem<SiteListItem>() {
    override fun onBindViewHolder(viewHolder: ViewHolder) {
        viewHolder.setText(R.id.second_tv_title,data.name)
        if (!data.check){
            viewHolder.getImageView(R.id.second_iv_remind).hide()
        }else{
            viewHolder.getImageView(R.id.second_iv_remind).show()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.second_item_remind_list
    }

    override fun onClick(viewHolder: ViewHolder) {
        super.onClick(viewHolder)
        val ac = viewHolder.itemView.context as ScheduleAddressActivity
        ac.notifyCurrentSelectAddressInfo(data)
    }

}