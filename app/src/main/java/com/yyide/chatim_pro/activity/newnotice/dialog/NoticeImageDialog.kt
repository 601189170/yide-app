package com.yyide.chatim_pro.activity.newnotice.dialog

import android.app.Activity
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.SizeUtils
import com.yyide.chatim_pro.databinding.NoticePreviewBinding
import com.yyide.chatim_pro.utils.GlideUtil

class NoticeImageDialog {

    companion object {
        fun showPreView(context: Activity, imgPath: String) {
            val alertDialog = AlertDialog.Builder(context)
            val previewBinding = NoticePreviewBinding.inflate(context.layoutInflater)
            alertDialog.setView(previewBinding.root)
            GlideUtil.loadImageRadius(context, imgPath, previewBinding.ivImg, SizeUtils.dp2px(1f))
            previewBinding.ivImg.scaleType = ImageView.ScaleType.FIT_XY
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