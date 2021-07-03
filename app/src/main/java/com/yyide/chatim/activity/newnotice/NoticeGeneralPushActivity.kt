package com.yyide.chatim.activity.newnotice

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.CompoundButton
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseMvpActivity
import com.yyide.chatim.databinding.ActivityNoticeReleaseBinding
import com.yyide.chatim.dialog.SwitchNoticeTimePop
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.NoticeBlankReleaseBean
import com.yyide.chatim.model.NoticePersonnelBean
import com.yyide.chatim.model.ResultBean
import com.yyide.chatim.presenter.NoticeReleasePresenter
import com.yyide.chatim.view.NoticeBlankReleaseView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * DESC 普通通知发布
 * AUTHOR LRZ
 * TIME 2021年6月21日
 * VERSION 1.0
 */
class NoticeGeneralPushActivity : BaseMvpActivity<NoticeReleasePresenter>(), NoticeBlankReleaseView {
    private var releaseBinding: ActivityNoticeReleaseBinding? = null
    private var isConfirm = false
    private var isTimer = false
    private var timeData: String = ""

    override fun getContentViewID(): Int {
        return R.layout.activity_notice_release
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        releaseBinding = ActivityNoticeReleaseBinding.inflate(layoutInflater)
        setContentView(releaseBinding!!.root)
        EventBus.getDefault().register(this)
        initView()
    }

    private fun initView() {
        releaseBinding!!.top.title.setText(R.string.notice_release_title)
        releaseBinding!!.top.backLayout.setOnClickListener { finish() }
        releaseBinding!!.btnPush.setOnClickListener { pushNotice() }
        releaseBinding!!.clRange.setOnClickListener {
            val intent = Intent()
            intent.setClass(NoticeGeneralActivity@ this, NoticeDesignatedPersonnelActivity::class.java)
            startActivity(intent)
        }
        initListener()
    }

    /**
     * 设置触摸事件，由于EditView与TextView都处于ScollView中，
     * 所以需要在OnTouch事件中通知父控件不拦截子控件事件
     */
    @SuppressLint("ClickableViewAccessibility")
    private val touchListener = OnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN
                || event.action == MotionEvent.ACTION_MOVE) {
            //按下或滑动时请求父节点不拦截子节点
            v.parent.requestDisallowInterceptTouchEvent(true)
        }
        if (event.action == MotionEvent.ACTION_UP) {
            //抬起时请求父节点拦截子节点
            v.parent.requestDisallowInterceptTouchEvent(false)
        }
        false
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        releaseBinding!!.switch1.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked) releaseBinding!!.clTimingTime.visibility = View.INVISIBLE else releaseBinding!!.clTimingTime.visibility = View.VISIBLE
            if (!isChecked) {
                timeData = ""
                releaseBinding!!.tvShowTimedTime.text = ""
            }
            isTimer = isChecked
        }

        releaseBinding!!.clTimingTime.setOnClickListener { showSelectTime() }
        releaseBinding!!.etInputTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val s1 = s.toString().trim { it <= ' ' }
                if (!TextUtils.isEmpty(s1)) {
                    releaseBinding!!.tvInputTitleNumber.text = getString(R.string.notice_input_title_number, s1.length)
                }
            }
        })

        releaseBinding!!.etInputContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val s1 = s.toString().trim { it <= ' ' }
                if (!TextUtils.isEmpty(s1)) {
                    releaseBinding!!.tvInputContentNumber.text = getString(R.string.notice_input_content_number, s1.length)
                }
            }
        })

        releaseBinding!!.etInputContent.setOnTouchListener(touchListener)
    }

    private fun pushNotice() {
        val etTitle = releaseBinding!!.etInputTitle.text.toString().trim()
        val etContent = releaseBinding!!.etInputContent?.text.toString().trim()
        when {
            TextUtils.isEmpty(etTitle) -> {
                releaseBinding!!.etInputTitle.error = getString(R.string.notice_input_title)
                releaseBinding!!.etInputTitle.isFocusable = true;
                releaseBinding!!.etInputTitle.isFocusableInTouchMode = true;
                releaseBinding!!.etInputTitle.requestFocus();
            }
            TextUtils.isEmpty(etContent) -> {
                releaseBinding!!.etInputContent.error = getString(R.string.notice_input_content)
                releaseBinding!!.etInputContent.isFocusable = true;
                releaseBinding!!.etInputContent.isFocusableInTouchMode = true;
                releaseBinding!!.etInputContent.requestFocus();
            }
            (!releaseBinding!!.switch1.isChecked && TextUtils.isEmpty(timeData)) -> {
                ToastUtils.showShort(R.string.notice_input_push_time)
            }
            (list.isEmpty() || list.size == 0) -> {
                ToastUtils.showShort("请选择通知范围")
            }
            else -> {
                val itemBean = NoticeBlankReleaseBean()
                //空白模板为固定ID
                itemBean.messageTemplateId = "1405486010163490820"
                itemBean.title = etTitle
                itemBean.content = etContent
                itemBean.isTimer = isTimer
                itemBean.notifyRange = 1
                itemBean.isConfirm = isConfirm
                itemBean.timerDate = timeData
                itemBean.recordList = list
                mvpPresenter.releaseNotice(itemBean)
            }
        }
    }

    val list = mutableListOf<NoticeBlankReleaseBean.RecordListBean>()

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(messageEvent: EventMessage) {
        if (BaseConstant.TYPE_NOTICE_RANGE == messageEvent.code) {
            Log.d("NoticePersonnelFragment", messageEvent.message)
            if (!TextUtils.isEmpty(messageEvent.message)) {
                val item: NoticeBlankReleaseBean = JSON.parseObject(messageEvent.message, NoticeBlankReleaseBean::class.java)
                isConfirm = item.isConfirm
                if (item.recordList != null)
                    list.addAll(list.size, item.recordList)
                setCheckNumber()
            }
        }
    }

    private var teacherNumber: Int = 0
    private var patriarchNumber: Int = 0
    private var brandNumber: Int = 0
    private var brandSiteNumber: Int = 0

    private fun setCheckNumber() {
        list.forEach { item ->
            when (item.specifieType) {
                "0" -> {//0教师 1家长 2班牌
                    teacherNumber = item.nums
                }
                "1" -> {
                    patriarchNumber = item.nums
                }
                "2" -> {
                    brandNumber = item.nums
                }
                "3" -> {
                    brandSiteNumber = item.nums
                }
            }
        }
        var descNumber: StringBuffer = StringBuffer()
        if (teacherNumber > 0) {
            descNumber.append(getString(R.string.notice_teacher_number, teacherNumber)).append("、")
        }
        if (patriarchNumber > 0) {
            descNumber.append(getString(R.string.notice_patriarch_number, patriarchNumber)).append("、")
        }
        if (brandNumber > 0) {
            descNumber.append(getString(R.string.notice_brand_check_class_number, brandNumber)).append("、")
        }
        if (brandSiteNumber > 0) {
            descNumber.append(getString(R.string.notice_brand_site_number, brandSiteNumber))
        }

        releaseBinding!!.tvRange.text = descNumber.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun showSelectTime() {
        val switchNoticeTimePop = SwitchNoticeTimePop(this)
        switchNoticeTimePop.setSelectClasses { date: String?, desc: String? ->
            releaseBinding!!.tvShowTimedTime.text = desc
            if (date != null) {
                timeData = date
            }
        }
    }

    override fun createPresenter(): NoticeReleasePresenter {
        return NoticeReleasePresenter(this)
    }

    override fun showError() {
        Log.d("NoticeReleaseActivity", "showError")
    }

    override fun getBlankReleaseSuccess(model: ResultBean?) {
        if (model != null && model.code == BaseConstant.REQUEST_SUCCES2) {
            EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_NOTICE_PUSH_BLANK, ""))
            EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_UPDATE_NOTICE_MY_RELEASE, ""))
            ToastUtils.showLong(model.msg)
            finish()
        }
    }

    override fun getBlankReleaseFail(msg: String?) {
        Log.d("NoticeReleaseActivity", "getBlankReleaseFail$msg")
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}