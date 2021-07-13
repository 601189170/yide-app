package com.yyide.chatim.activity.newnotice.dialog

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.yyide.chatim.databinding.NoticePreviewBinding
import com.yyide.chatim.utils.GlideUtil

class NoticeImageDialog {

    companion object{
        fun showPreView(context: Activity, imgPath: String) {
            val alertDialog = AlertDialog.Builder(context)
            val previewBinding = NoticePreviewBinding.inflate(context.layoutInflater)
            alertDialog.setView(previewBinding.root)
            val dialog = alertDialog.create()
            val m = context.windowManager
            m.defaultDisplay //为获取屏幕宽、高
            val p = dialog.window!!.attributes //获取对话框当前的参数值
            p.height = ScreenUtils.getScreenHeight() //高度设置为屏幕的0.3
            p.width = ScreenUtils.getScreenWidth() //宽度设置为屏幕的0.5
            //设置主窗体背景颜色为黑色
            GlideUtil.loadImageRadius(context, imgPath, previewBinding.ivImg, SizeUtils.dp2px(10f))
            previewBinding.root.setOnClickListener { v: View? -> dialog.dismiss() }
            //previewBinding.tvNoticeTitle.setText("");
            //previewBinding.tvNoticeContent.setText("");
            dialog.window!!.decorView.setPadding(0, 0, 0, 0)
            dialog.window!!.attributes = p //设置生效
            dialog.show()
            dialog.setOnDismissListener { context.window.decorView.alpha = 1f }
            dialog.setOnShowListener { context.window.decorView.alpha = 0f }
        }
    }
}