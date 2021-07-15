package com.yyide.chatim.activity.newnotice

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseMvpActivity
import com.yyide.chatim.databinding.ActivityNoticeReleaseBinding
import com.yyide.chatim.databinding.NoticeBlankPreviewBinding
import com.yyide.chatim.dialog.SwitchNoticeTimePop
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.NoticeBlankReleaseBean
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
    private val paramsMap = mutableMapOf<String, NoticeBlankReleaseBean>()
    private val list = ArrayList<NoticeBlankReleaseBean.RecordListBean>()

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
        releaseBinding!!.top.tvRight.setText(R.string.notice_preview_title)
        releaseBinding!!.top.tvRight.visibility = View.VISIBLE
        releaseBinding!!.top.tvRight.setOnClickListener { showPreView() }
        releaseBinding!!.top.backLayout.setOnClickListener { finish() }
        releaseBinding!!.btnPush.setOnClickListener { pushNotice() }
        releaseBinding!!.clRange.setOnClickListener {
            val intent = Intent(NoticeGeneralActivity@ this, NoticeDesignatedPersonnelActivity::class.java)
            intent.putParcelableArrayListExtra("list", list)
            intent.putExtra("isCheck", isConfirm)
            startActivity(intent)
        }
        initListener()
    }

    private fun showPreView() {
        val alertDialog = AlertDialog.Builder(this)
        val previewBinding = NoticeBlankPreviewBinding.inflate(layoutInflater)
        alertDialog.setView(previewBinding.root)
        val dialog = alertDialog.create()
        val m = windowManager
        m.defaultDisplay //为获取屏幕宽、高
        val p = dialog.window!!.attributes //获取对话框当前的参数值
        p.height = ((ScreenUtils.getScreenHeight() * 0.8).toInt())
//        p.width = ((ScreenUtils.getScreenWidth() * 0.6).toInt())
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        //设置主窗体背景颜色为黑色
        previewBinding.nestedScrollView.layoutParams.height = ((ScreenUtils.getScreenHeight() * 0.6).toInt())
        previewBinding.root.setOnClickListener { v: View? -> dialog.dismiss() }
        previewBinding.tvTitle.text = releaseBinding?.etInputTitle?.text.toString().trim()
        previewBinding.tvContent.text = "\t " + releaseBinding?.etInputContent?.text.toString().trim()
        dialog.window!!.attributes = p //设置生效
        dialog.show()
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
            isTimer = !isChecked
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
        val etContent = releaseBinding!!.etInputContent.text.toString().trim()
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
            teacherNumber <= 0 && patriarchNumber <= 0 && brandNumber <= 0 -> {
                ToastUtils.showShort("请选择通知人员")
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(messageEvent: EventMessage) {
        if (BaseConstant.TYPE_NOTICE_RANGE == messageEvent.code) {
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

    //处理请求参数
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
            releaseBinding!!.tvRange.text = descNumber.toString().removeSuffix("、")
        } else {
            if (TextUtils.isEmpty(descNumber.toString())) {
                releaseBinding!!.tvRange.text = getString(R.string.please_select)
            } else {
                releaseBinding!!.tvRange.text = descNumber.toString()
            }
        }
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
            Handler().postDelayed({
                EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_NOTICE_PUSH_BLANK, ""))
                EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_UPDATE_NOTICE_MY_RELEASE, ""))
                ToastUtils.showLong(model.msg)
                finish()
            }, 500)
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