package com.yyide.chatim.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.tencent.liteav.demo.beauty.utils.ResourceUtils.getResources
import com.yyide.chatim.R
import com.yyide.chatim.model.NewAppRspJ
import com.yyide.chatim.utils.GlideUtil
import com.yyide.chatim.utils.VHUtil

/**
 * @ProjectName : yideapp
 * @Author : J
 * @Time : 2022/4/7 18:56
 * @Description : 文件描述
 */
class NewAppItemAdapter() : BaseAdapter() {
    var list: MutableList<NewAppRspJ.AppsDTO>? = ArrayList()
    override fun getCount(): Int {
        return if (list != null) list!!.size else 0
    }

    override fun getItem(position: Int): NewAppRspJ.AppsDTO {
        return list!![position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        if (view == null) view =
            LayoutInflater.from(parent!!.context).inflate(R.layout.icon_item, null, false)
        val item = VHUtil.ViewHolder.get<ImageView>(view, R.id.item)
        val name = VHUtil.ViewHolder.get<TextView>(view, R.id.name)

        name.text = getItem(position).name
        if (getItem(position).name == "编辑"){
            item.setImageResource(R.drawable.icon_bj)
            name.setTextColor(getResources().getColor(R.color.colorPrimary))
        }/*else{
            name.setTextColor(getResources().getColor(R.color.colorPrimary))
        }*/
        if (!TextUtils.isEmpty(getItem(position).logo)) {
            GlideUtil.loadImageRadius(view!!.context, getItem(position).logo, item,10)
        }
        return view
    }

    fun notifyData(list: MutableList<NewAppRspJ.AppsDTO>) {
        this.list = list
        notifyDataSetChanged()
    }

}
/*var list: List<NewAppRsp>? = ArrayList()

override fun getCount(): Int {
    return if (list != null) list!!.size else 0
}

override fun getItem(position: Int): AppItemBean.DataBean.RecordsBean.ListBean {
    return list!![position]
}

override fun getItemId(position: Int): Long {
    return 0
}

fun getView(position: Int, view: View?, viewGroup: ViewGroup): View? {
    var view = view
    if (view == null) view =
        LayoutInflater.from(viewGroup.context).inflate(R.layout.icon_item, null, false)
    val item = VHUtil.ViewHolder.get<ImageView>(view, R.id.item)
    val name = VHUtil.ViewHolder.get<TextView>(view, R.id.name)
    name.text = getItem(position).name
    if (!TextUtils.isEmpty(getItem(position).img)) {
        GlideUtil.loadCircleImage(view!!.context, getItem(position).img, item)
    }
    return view
}

fun notifyData(list: List<AppItemBean.DataBean.RecordsBean.ListBean>?) {
    this.list = list
    notifyDataSetChanged()
}*/