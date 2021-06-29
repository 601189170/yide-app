package com.yyide.chatim.activity.newnotice

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.webkit.WebViewFragment
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.R
import com.yyide.chatim.activity.WebViewActivity
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.base.WebFragment
import com.yyide.chatim.databinding.ActivityNoticePushDetailBinding
import com.yyide.chatim.databinding.NoticePreviewBinding
import com.yyide.chatim.dialog.SwitchNoticeTimePop

/**
 * 模板发布详情
 */
class NoticeTemplateDetailActivity : BaseActivity() {
    private var pushDetailBinding: ActivityNoticePushDetailBinding? = null
    override fun getContentViewID(): Int {
        return R.layout.activity_notice_push_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pushDetailBinding = ActivityNoticePushDetailBinding.inflate(layoutInflater)
        setContentView(pushDetailBinding!!.root)
        initView()
    }

    private fun initView() {
        pushDetailBinding!!.include.title.setText(R.string.notice_release_title)
        pushDetailBinding!!.include.tvRight.setText(R.string.notice_preview_title)
        pushDetailBinding!!.include.tvRight.visibility = View.VISIBLE
        pushDetailBinding!!.include.backLayout.setOnClickListener { finish() }
        pushDetailBinding!!.include.tvRight.setOnClickListener { showPreView() }
        pushDetailBinding!!.switchPush.isChecked = true
        pushDetailBinding!!.switchPush.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                pushDetailBinding!!.clThing.visibility = View.INVISIBLE
            } else {
                pushDetailBinding!!.clThing.visibility = View.VISIBLE
            }
        }
        pushDetailBinding!!.clThing.setOnClickListener { showSelectTime() }
        pushDetailBinding!!.clSelect.setOnClickListener {
//            val intent = Intent()
//            intent.setClass(NoticeTemplateDetailActivity@ this, NoticeDesignatedPersonnelActivity::class.java)
//            startActivity(intent)
            WebViewActivity.start(this, "http://192.168.3.147:8005/#/notice/edit/1406884372284653569", "")
        }
        pushDetailBinding!!.btnConfirm.setOnClickListener { sendNotice() }

        supportFragmentManager.beginTransaction().add(R.id.fl_content, WebFragment.newInstance("http://192.168.3.147:8005/#/notice/edit/1406884372284653569")).commit()
    }

    private fun showPreView() {
        val alertDialog = AlertDialog.Builder(this)
        val previewBinding = NoticePreviewBinding.inflate(layoutInflater)
        alertDialog.setView(previewBinding.root)
        val dialog = alertDialog.create()
        val m = windowManager
        val d = m.defaultDisplay //为获取屏幕宽、高
        val p = dialog.window!!.attributes //获取对话框当前的参数值
        p.height = ScreenUtils.getScreenHeight() //高度设置为屏幕的0.3
        p.width = ScreenUtils.getScreenWidth() //宽度设置为屏幕的0.5
        //设置主窗体背景颜色为黑色
        previewBinding.tvNoticeContent.movementMethod = ScrollingMovementMethod.getInstance()
        previewBinding.root.setOnClickListener { v: View? -> dialog.dismiss() }
        dialog.setOnDismissListener { window.decorView.alpha = 1f }
        dialog.setOnShowListener { window.decorView.alpha = 0f }
        //previewBinding.tvNoticeTitle.setText("");
        //previewBinding.tvNoticeContent.setText("");
        dialog.window!!.decorView.setPadding(0, 0, 0, 0);
        dialog.window!!.attributes = p //设置生效
        dialog.show()
    }

    private fun showSelectTime() {
        val switchNoticeTimePop = SwitchNoticeTimePop(this)
        switchNoticeTimePop.setSelectClasses { date: String? -> pushDetailBinding!!.tvPushTime.text = date }
    }

    private fun sendNotice() {
        val time = pushDetailBinding!!.tvPushTime.text.toString().trim { it <= ' ' }
        if (!pushDetailBinding!!.switchPush.isChecked && TextUtils.isEmpty(time)) {
            ToastUtils.showShort(R.string.notice_timing_push_input)
        } else {

        }
    }
}