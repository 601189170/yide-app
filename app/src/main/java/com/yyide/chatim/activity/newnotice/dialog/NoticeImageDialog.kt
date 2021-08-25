package com.yyide.chatim.activity.newnotice.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.yyide.chatim.databinding.NoticePreviewBinding
import com.yyide.chatim.utils.GlideUtil

class NoticeImageDialog {

    companion object {
        fun showPreView(context: Activity, imgPath: String) {
            val alertDialog = AlertDialog.Builder(context)
            val previewBinding = NoticePreviewBinding.inflate(context.layoutInflater)
            alertDialog.setView(previewBinding.root)
            GlideUtil.loadImageRadius(context, imgPath, previewBinding.ivImg, SizeUtils.dp2px(1f))
            val dialog = alertDialog.create()
            val p = dialog.window!!.attributes //获取对话框当前的参数值
//            p.height = (ScreenUtils.getScreenHeight() * 0.8).toInt() //高度设置为屏幕的0.3/
//            p.width = (ScreenUtils.getScreenWidth() * 0.8).toInt()//宽度设置为屏幕的0.5
            //设置主窗体背景颜色为黑色
            dialog.show()
            previewBinding.root.setOnClickListener { v: View? -> dialog.dismiss() }
//            dialog.window!!.attributes = p //设置生效
            dialog.setOnDismissListener { context.window.decorView.alpha = 1f }
            dialog.setOnShowListener { context.window.decorView.alpha = 0f }
        }
    }
}