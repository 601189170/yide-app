package com.yyide.chatim.dialog

import android.app.Activity
import android.graphics.drawable.BitmapDrawable
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.yyide.chatim.R
import com.yyide.chatim.databinding.LayoutScheduleSearchFilterBinding

/**
 * Created by Administrator on 2019/5/15.
 */
class ScheduleSearchFilterPop(val context: Activity?) : PopupWindow() {
    var popupWindow: PopupWindow? = null
    var mWindow: Window? = null

    init {
        init()
    }

    private fun init() {
        val mView = LayoutScheduleSearchFilterBinding.inflate(context!!.layoutInflater)
        popupWindow = PopupWindow(
            mView.root,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        popupWindow!!.animationStyle = R.style.popwin_anim_right_style
        mView.clBg.setOnClickListener { v ->
            popupWindow!!.dismiss()
        }
        popupWindow!!.isFocusable = false //获取焦点
        popupWindow!!.isOutsideTouchable = true
        popupWindow!!.setBackgroundDrawable(BitmapDrawable())
        popupWindow!!.isClippingEnabled = false
        // 获取当前Activity的window
        popupWindow!!.isFocusable = true
        popupWindow!!.isOutsideTouchable = false
        popupWindow!!.setBackgroundDrawable(null)
        popupWindow!!.contentView.isFocusable = true
        popupWindow!!.contentView.isFocusableInTouchMode = true
        popupWindow!!.contentView.setOnKeyListener { v: View?, keyCode: Int, event: KeyEvent? ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (popupWindow != null && popupWindow!!.isShowing) {
                    popupWindow!!.dismiss()
                }
                return@setOnKeyListener true
            }
            false
        }
        popupWindow!!.setOnDismissListener {

            //如果设置了背景变暗，那么在dissmiss的时候需要还原
            if (mWindow != null) {
                val params = mWindow!!.attributes
                params.alpha = 1.0f
                mWindow!!.attributes = params
            }
            if (popupWindow != null && popupWindow!!.isShowing) {
                popupWindow!!.dismiss()
            }
        }
        //如果设置的值在0 - 1的范围内，则用设置的值，否则用默认值
        mWindow = context.window
        val params = mWindow!!.attributes
        params.alpha = 0.7f
        mWindow!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        mWindow!!.attributes = params
        popupWindow!!.showAtLocation(
            mView.root, Gravity.RIGHT, 0, 0
        )
    }

    fun hide() {
        if (popupWindow != null && popupWindow!!.isShowing) {
            popupWindow!!.dismiss()
        }
    }

}