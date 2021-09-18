package com.yyide.chatim.dialog

import android.app.Activity
import android.graphics.drawable.BitmapDrawable
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.databinding.DialogWeeklyTopBinding
import com.yyide.chatim.model.DialogMenuModel

/**
 * Created by Administrator on 2019/5/15.
 */
class WeeklyTopPop(val context: Activity?, val view: View?) : PopupWindow() {
    var popupWindow: PopupWindow? = null
    var mWindow: Window? = null

    init {
        initView()
    }

    fun initView() {
        val mView = DialogWeeklyTopBinding.inflate(context!!.layoutInflater)
        popupWindow = PopupWindow(
            mView.root,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        init(mView)
        popupWindow!!.animationStyle = R.style.popwin_anim_style_top
        mView.root.setOnClickListener { hide() }
        // 获取当前Activity的window
        popupWindow!!.contentView.setOnKeyListener { v: View?, keyCode: Int, event: KeyEvent? ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                hide()
                return@setOnKeyListener true
            }
            false
        }
        popupWindow!!.showAsDropDown(view, 0, 0, Gravity.BOTTOM)
    }

    fun hide() {
        if (popupWindow != null && popupWindow!!.isShowing) {
            popupWindow!!.dismiss()
        }
    }

    private lateinit var adapter: BaseQuickAdapter<DialogMenuModel, BaseViewHolder>
    private fun init(viewBinding: DialogWeeklyTopBinding) {
        adapter =
            object : BaseQuickAdapter<DialogMenuModel, BaseViewHolder>(R.layout.item_weekly_menu) {
                override fun convert(holder: BaseViewHolder, item: DialogMenuModel) {
                    holder.setText(R.id.tv_name, item.name)
                }
            }
        viewBinding.recyclerview.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerview.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            val item: DialogMenuModel = adapter.getItem(position) as DialogMenuModel
            if (listener != null) {
                listener?.onClickMenuListener(item.id)
            }
            hide()
        }
        var datas = arrayListOf<DialogMenuModel>()
        datas.add(DialogMenuModel(1, "申请1"))
        datas.add(DialogMenuModel(2, "申请2"))
        datas.add(DialogMenuModel(3, "申请3"))
        datas.add(DialogMenuModel(4, "申请4"))
        datas.add(DialogMenuModel(5, "申请5"))
        datas.add(DialogMenuModel(6, "申请6"))
        setData(datas)
    }

    fun setData(datas: ArrayList<DialogMenuModel>) {
        if (datas.isNotEmpty()) {
            adapter.setList(datas)
        }
    }

    interface DialogMenu {
        fun onClickMenuListener(id: Long)
    }

    private var listener: DialogMenu? = null
    fun setOnSelectDialogMenu(dialogMenu: DialogMenu) {
        this.listener = dialogMenu
    }
}