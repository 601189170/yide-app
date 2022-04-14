package com.yyide.chatim_pro.adapter

import android.widget.GridView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.model.NewAppRspJ
import com.yyide.chatim_pro.utils.JumpUtil
import com.yyide.chatim_pro.utils.LogUtil

/**
 * @ProjectName : yideapp
 * @Author : J
 * @Time : 2022/4/7 18:22
 * @Description : 文件描述
 */
class NewAppAdapter() : BaseQuickAdapter<NewAppRspJ, BaseViewHolder>(R.layout.app_item) {
    override fun convert(holder: BaseViewHolder, item: NewAppRspJ) {
        val title = holder.getView<TextView>(R.id.title)
        val grid = holder.getView<GridView>(R.id.grid)
        title.text = item.categoryName;
        val adapter = NewAppItemAdapter()
        grid.adapter = adapter

        if (item.categoryName == "常用应用") {
            item.apps = ArrayList()
            var mCommon = NewAppRspJ.AppsDTO()
            mCommon.appurl = ""
            mCommon.id = ""
            mCommon.name = "编辑"
            item.apps.add(mCommon)
        }
        if (item.apps != null && item.apps.isNotEmpty()) {
            adapter.notifyData(item.apps);
        }
        grid.setOnItemClickListener { _, _, position, _ ->
            val gridItem = adapter.getItem(position)
            LogUtil.e("gridItem.name" + gridItem.name + "gridItem.appurl" + gridItem.appurl)
            JumpUtil.appOpen(context, gridItem.name, gridItem.appurl, gridItem.name)
        }
    }

}




