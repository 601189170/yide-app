package com.yyide.chatim.activity.newnotice

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.R
import com.yyide.chatim.activity.newnotice.dialog.NoticeImageDialog
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseMvpActivity
import com.yyide.chatim.databinding.ActivityNoticePushDetailBinding
import com.yyide.chatim.dialog.SwitchNoticeTimePop
import com.yyide.chatim.model.*
import com.yyide.chatim.presenter.NoticeTemplateGeneralPresenter
import com.yyide.chatim.utils.GlideUtil
import com.yyide.chatim.view.NoticeTemplateGeneralView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 模板发布详情
 */
class NoticeTemplatePushActivity : BaseMvpActivity<NoticeTemplateGeneralPresenter>(), NoticeTemplateGeneralView {
    private var pushDetailBinding: ActivityNoticePushDetailBinding? = null
    private var imgPath: String = ""
    private var isConfirm = false
    private var notifyRange: Int = 0//notifyRange为1时必填
    private lateinit var webModel: WebModel
    private var subIds = mutableListOf<String>()
    private var pushDate: String = ""
    private val REQUEST_CODE = 100
    private val paramsMap = mutableMapOf<String, NoticeBlankReleaseBean>()
    private val list = ArrayList<NoticeBlankReleaseBean.RecordListBean>()

    override fun getContentViewID(): Int {
        return R.layout.activity_notice_push_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pushDetailBinding = ActivityNoticePushDetailBinding.inflate(layoutInflater)
        setContentView(pushDetailBinding!!.root)
        EventBus.getDefault().register(this)
        initView()
    }

    private fun initView() {
        webModel = intent.getSerializableExtra("params") as WebModel
        pushDetailBinding!!.include.title.setText(R.string.notice_release_title)
        pushDetailBinding!!.include.tvRight.setText(R.string.notice_preview_title)
        pushDetailBinding!!.include.tvRight.visibility = View.VISIBLE
        pushDetailBinding!!.include.backLayout.setOnClickListener { finish() }
        pushDetailBinding!!.include.tvRight.setOnClickListener { NoticeImageDialog.showPreView(this, imgPath) }
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
            val intent = Intent(NoticeGeneralActivity@ this, NoticeDesignatedPersonnelActivity::class.java)
            intent.putParcelableArrayListExtra("list", list)
            intent.putExtra("isCheck", isConfirm)
            startActivity(intent)
        }
        pushDetailBinding!!.btnConfirm.setOnClickListener { sendNotice() }
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (ScreenUtils.getScreenHeight() * 0.6).toInt())
        lp.setMargins(SizeUtils.dp2px(12f), SizeUtils.dp2px(10f), SizeUtils.dp2px(12f), SizeUtils.dp2px(10f))
        lp.gravity = Gravity.CENTER
        pushDetailBinding!!.ivImg.layoutParams = lp

        //处理数据展示模板图片
        if (webModel.params != null && webModel.params.subs != null) {
            for (i in webModel.params.subs) {
                if (i.showType == 2) {//2手机使用模板图片
                    imgPath = i.imgpath
                }
                subIds.add(i.id)
            }
            GlideUtil.loadImageRadius(this, imgPath, pushDetailBinding!!.ivImg, SizeUtils.dp2px(4f))
        }
    }

    private fun showSelectTime() {
        val switchNoticeTimePop = SwitchNoticeTimePop(this)
        switchNoticeTimePop.setSelectClasses { date: String?, desc: String? ->
            pushDetailBinding!!.tvPushTime.text = desc
            if (date != null) {
                pushDate = date
            }
        }
    }

    /**
     * 发布模板通知
     */
    private fun sendNotice() {
        if (!pushDetailBinding!!.switchPush.isChecked && TextUtils.isEmpty(pushDate)) {
            ToastUtils.showShort(R.string.notice_timing_push_input)
        } else if (list.isEmpty() || list.size == 0) {
            ToastUtils.showShort("请选择通知范围")
        } else {
            if (webModel.params != null) {
                val itemBean = NoticeBlankReleaseBean()
                itemBean.messageTemplateId = webModel.params.tempId
                itemBean.title = webModel.params.tempTitle
                itemBean.isTimer = !pushDetailBinding!!.switchPush.isChecked
                itemBean.isConfirm = isConfirm
                itemBean.notifyRange = notifyRange
                itemBean.timerDate = pushDate
                itemBean.notifyRange = 1
                itemBean.subIds = subIds
                itemBean.recordList = list
                mvpPresenter.releaseNotice(itemBean)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(messageEvent: EventMessage) {
        if (BaseConstant.TYPE_NOTICE_RANGE == messageEvent.code) {
            Log.d("NoticePersonnelFragment", messageEvent.message)
            if (!TextUtils.isEmpty(messageEvent.message)) {
                val item: NoticeBlankReleaseBean = JSON.parseObject(messageEvent.message, NoticeBlankReleaseBean::class.java)
                isConfirm = item.isConfirm
                when (messageEvent.type) {
                    "0" -> {
                        paramsMap["teacher"] = item
                    }
                    "1" -> {
                        paramsMap["patriarch"] = item
                    }
                    "2", "3" -> {
                        paramsMap["brandClass"] = item
                    }
                }
                setCheckNumber()
            }
        }
    }

    private var teacherNumber: Int = 0
    private var patriarchNumber: Int = 0
    private var brandNumber: Int = 0
    private fun setCheckNumber() {
        //清空当前数据
        list.clear()
        teacherNumber = 0
        patriarchNumber = 0
        brandNumber = 0
        for (key in paramsMap.entries) {
            val item: NoticeBlankReleaseBean = key.value
            when (key.key) {
                "teacher" -> {
                    if (item.recordList == null) {
                        teacherNumber = 0
                    }
                }
                "patriarch" -> {
                    if (item.recordList == null) {
                        patriarchNumber = 0
                    }
                }
                "brandClass" -> {
                    if (item.recordList == null) {
                        brandNumber = 0
                    }
                }
            }

            item.recordList?.forEach { item ->
                when (item.specifieType) {
                    "0" -> {//0教师 1家长 2班牌
                        teacherNumber += item.nums
                    }
                    "1" -> {
                        patriarchNumber += item.nums
                    }
                    "2", "3" -> {
                        brandNumber = item.nums
                    }
                }
            }
        }

        for (item in paramsMap.values) {
            if (item.recordList != null && item.recordList.size > 0) {
                list.addAll(list.size, item.recordList)
            }
        }
        Log.d("NoticePersonnelFragment", "paramsMap：" + JSON.toJSONString(paramsMap))

        var descNumber = StringBuffer()
        if (teacherNumber > 0) {
            descNumber.append(getString(R.string.notice_teacher_number, teacherNumber)).append("、")
        }

        if (patriarchNumber > 0) {
            descNumber.append(getString(R.string.notice_patriarch_number, patriarchNumber)).append("、")
        }

        if (brandNumber > 0) {
            descNumber.append(getString(R.string.notice_brand_check_class_number, brandNumber)).append("、")
        }

        if (!TextUtils.isEmpty(descNumber.toString()) && descNumber.toString().endsWith("、")) {
            pushDetailBinding!!.tvRange.text = descNumber.toString().removeSuffix("、")
        } else {
            if (TextUtils.isEmpty(descNumber.toString())) {
                pushDetailBinding!!.tvRange.text = getString(R.string.please_select)
            } else {
                pushDetailBinding!!.tvRange.text = descNumber.toString()
            }
        }
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
            Handler().postDelayed({
                EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_NOTICE_PUSH_BLANK, ""))
                EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_UPDATE_NOTICE_MY_RELEASE, ""))
                ToastUtils.showLong(model.msg)
                finish()
            }, 500)
        }
    }

    @SuppressLint("LongLogTag")
    override fun getTemplateDetailFail(msg: String?) {
        Log.d("NoticeTemplateDetailActivity", "getTemplateDetailFail$msg");
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}