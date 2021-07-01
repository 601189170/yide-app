package com.yyide.chatim.activity.newnotice

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseMvpActivity
import com.yyide.chatim.databinding.ActivityNoticePushDetailBinding
import com.yyide.chatim.databinding.NoticePreviewBinding
import com.yyide.chatim.dialog.SwitchNoticeTimePop
import com.yyide.chatim.model.NoticeBlankReleaseBean
import com.yyide.chatim.model.NoticeReleaseTemplateBean
import com.yyide.chatim.model.ResultBean
import com.yyide.chatim.model.WebModel
import com.yyide.chatim.presenter.NoticeTemplateGeneralPresenter
import com.yyide.chatim.utils.GlideUtil
import com.yyide.chatim.view.NoticeTemplateGeneralView

/**
 * 模板发布详情
 */
class NoticeTemplatePushActivity : BaseMvpActivity<NoticeTemplateGeneralPresenter>(), NoticeTemplateGeneralView {
    private var pushDetailBinding: ActivityNoticePushDetailBinding? = null
    private lateinit var imgPath: String
    private var isConfirm = false
    private var notifyRange: Int = 0//notifyRange为1时必填
    private lateinit var webModel: WebModel
    private lateinit var subIds: MutableList<String>
    private val REQUEST_CODE = 100
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
        webModel = intent.getSerializableExtra("params") as WebModel
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
            val intent = Intent()
            intent.setClass(this, NoticeDesignatedPersonnelActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
        pushDetailBinding!!.btnConfirm.setOnClickListener { sendNotice() }
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenHeight() - pushDetailBinding!!.include.root.height)
        lp.setMargins(SizeUtils.dp2px(12f), SizeUtils.dp2px(10f), SizeUtils.dp2px(12f), SizeUtils.dp2px(10f))
        pushDetailBinding!!.ivImg.layoutParams = lp

        //处理数据展示模板图片
        if (webModel.params != null && webModel.params.subs != null) {
            for (i in webModel.params.subs) {
                if (i.showType == 2) {//2手机使用模板图片
                    imgPath = i.imgpath
                }
                subIds.add(i.messageTemplateDetailId)
                GlideUtil.loadImageRadius(this, imgPath, pushDetailBinding!!.ivImg, SizeUtils.dp2px(4f))
            }
        }
    }

    private fun showPreView() {
        val alertDialog = AlertDialog.Builder(this)
        val previewBinding = NoticePreviewBinding.inflate(layoutInflater)
        alertDialog.setView(previewBinding.root)
        val dialog = alertDialog.create()
        val m = windowManager
        m.defaultDisplay //为获取屏幕宽、高
        val p = dialog.window!!.attributes //获取对话框当前的参数值
        p.height = ScreenUtils.getScreenHeight() //高度设置为屏幕的0.3
        p.width = ScreenUtils.getScreenWidth() //宽度设置为屏幕的0.5
        //设置主窗体背景颜色为黑色
        GlideUtil.loadImageRadius(this, imgPath, previewBinding.ivImg, SizeUtils.dp2px(4f))
        previewBinding.root.setOnClickListener { v: View? -> dialog.dismiss() }
        //previewBinding.tvNoticeTitle.setText("");
        //previewBinding.tvNoticeContent.setText("");
        dialog.window!!.decorView.setPadding(0, 0, 0, 0)
        dialog.window!!.attributes = p //设置生效
        dialog.show()
        dialog.setOnDismissListener { window.decorView.alpha = 1f }
        dialog.setOnShowListener { window.decorView.alpha = 0f }
    }

    private fun showSelectTime() {
        val switchNoticeTimePop = SwitchNoticeTimePop(this)
        switchNoticeTimePop.setSelectClasses { date: String? -> pushDetailBinding!!.tvPushTime.text = date }
    }

    /**
     * 发布模板通知
     */
    private fun sendNotice() {
        val time = pushDetailBinding!!.tvPushTime.text.toString().trim { it <= ' ' }
        if (!pushDetailBinding!!.switchPush.isChecked && TextUtils.isEmpty(time)) {
            ToastUtils.showShort(R.string.notice_timing_push_input)
        } else {
            if (webModel.params != null) {
                val itemBean = NoticeBlankReleaseBean()
                //空白模板为固定ID
                itemBean.messageTemplateId = webModel.params.tempId
                itemBean.title = webModel.params.tempTitle
                itemBean.isTimer = pushDetailBinding!!.switchPush.isChecked
                itemBean.isConfirm = isConfirm
                itemBean.notifyRange = notifyRange
                itemBean.timerDate = pushDetailBinding!!.tvPushTime.text.toString().trim()
                itemBean.subIds = subIds
                mvpPresenter.releaseNotice(itemBean)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun createPresenter(): NoticeTemplateGeneralPresenter {
        return NoticeTemplateGeneralPresenter(this)
    }

    override fun showError() {

    }

    override fun getTemplateDetail(model: NoticeReleaseTemplateBean?) {
        if (model != null && model.code == BaseConstant.REQUEST_SUCCES2) {

        }
    }

    override fun pushTemplateSuccess(model: ResultBean?) {
        if (model != null && model.code == BaseConstant.REQUEST_SUCCES2) {
            ToastUtils.showShort(model.msg)
        }
    }

    @SuppressLint("LongLogTag")
    override fun getTemplateDetailFail(msg: String?) {
        Log.d("NoticeTemplateDetailActivity", "getTemplateDetailFail$msg");
    }
}