package com.yyide.chatim.activity.meeting

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.blankj.utilcode.util.ToastUtils
import com.huawei.hms.push.utils.DateUtil
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.meeting.viewmodel.MeetingSaveViewModel
import com.yyide.chatim.activity.schedule.*
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim.databinding.ActivityMeetingCreateBinding
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.schedule.ParticipantRsp
import com.yyide.chatim.model.schedule.Remind
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.model.schedule.SiteNameRsp
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * 创建/编辑 会议
 * author lrz
 * time 2021年10月11日17:47:47
 */
class MeetingSaveActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityMeetingCreateBinding
    private val viewModel: MeetingSaveViewModel by viewModels()
    private var id = ""
    private var resultCode = 0
    override fun getContentViewID(): Int {
        return R.layout.activity_meeting_create
    }

    companion object {
        //时间选择
        const val REQUEST_CODE_DATE_SELECT = 104

        //选择参与人
        const val REQUEST_CODE_PARTICIPANT_SELECT = 105

        //提醒选择
        const val REQUEST_CODE_REMIND_SELECT = 102

        //场地选择
        const val REQUEST_CODE_SITE_SELECT = 101
        private const val CREATE_TYPE = 1
        private const val UPDATE_TYPE = 2
        fun jumpUpdate(context: Context, id: String) {
            val intent = Intent(context, MeetingSaveActivity::class.java)
            intent.putExtra("type", UPDATE_TYPE)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }

        fun jumpCreate(context: Context) {
            val intent = Intent(context, MeetingSaveActivity::class.java)
            intent.putExtra("type", CREATE_TYPE)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMeetingCreateBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        val type = intent.getIntExtra("type", -1)
        viewBinding.top.title.text = getString(R.string.meeting_create_title)
        val defaultTwoTimeListOfDateTime = ScheduleDaoUtil.defaultTwoTimeListOfDateTime()
        if (defaultTwoTimeListOfDateTime.size == 2) {
            val startTime = defaultTwoTimeListOfDateTime[0].toStringTime()
            val endTime = defaultTwoTimeListOfDateTime[1].toStringTime()
            viewModel.startTimeLiveData.value = startTime
            viewModel.endTimeLiveData.value = endTime
        }

        viewBinding.tvTime.text =
            DateUtils.formatTime(
                viewModel.startTimeLiveData.value,
                "",
                "MM月dd日 HH:mm"
            ) + " - " + DateUtils.formatTime(
                viewModel.endTimeLiveData.value,
                "",
                "MM月dd日 HH:mm"
            )
        if (type == UPDATE_TYPE) {
            id = intent.getStringExtra("id")
            viewModel.scheduleId.value = id
            getDetail()
            viewBinding.top.ivRight.visibility = View.VISIBLE
            viewBinding.top.title.text = getString(R.string.meeting_update_title)
            viewBinding.btnConfirm.text = getString(R.string.meeting_update_title)
            viewBinding.top.ivRight.setOnClickListener {
                DialogUtil.showScheduleDelDialog(
                    this,
                    viewBinding.top.ivRight,
                    object : DialogUtil.OnClickListener {
                        override fun onCancel(view: View?) {

                        }

                        override fun onEnsure(view: View?) {
                            val scheduleId = viewModel.scheduleId.value ?: ""
                            del(scheduleId)
                        }
                    })
            }
        }
        viewBinding.btnConfirm.setOnClickListener { sava() }
        viewBinding.top.backLayout.setOnClickListener { finish() }
        //选择场地
        viewBinding.tvSite.setOnClickListener {
            val intent = Intent(this, ScheduleAddressActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(viewModel.siteLiveData.value))
            startActivity.launch(intent)
            resultCode = REQUEST_CODE_SITE_SELECT
        }
        //选择参与人
        viewBinding.tvParticipant.setOnClickListener {
            val intent = Intent(this, ScheduleParticipantActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(viewModel.participantList.value))
            startActivity.launch(intent)
            resultCode = REQUEST_CODE_PARTICIPANT_SELECT

        }

        //选择时间
        viewBinding.clTime.setOnClickListener {
            val intent = Intent(this, ScheduleDateIntervalActivity::class.java)
            val allDay = viewModel.allDayLiveData.value
            val startTime = viewModel.startTimeLiveData.value
            val endTime = viewModel.endTimeLiveData.value
            intent.putExtra("allDay", allDay)
            intent.putExtra("startTime", startTime)
            intent.putExtra("endTime", endTime)
            startActivity.launch(intent)
            resultCode = REQUEST_CODE_DATE_SELECT
        }

        viewBinding.clRemind.setOnClickListener {
            val intent = Intent(this, ScheduleRemindActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(viewModel.remindLiveData.value))
            intent.putExtra("allDay", viewModel.allDayLiveData.value)
            startActivity.launch(intent)
            resultCode = REQUEST_CODE_REMIND_SELECT
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData(item: ScheduleData) {
        if (item.promoter != SpData.getUserId()) {
            viewBinding.btnConfirm.visibility = View.GONE
            viewBinding.tvParticipant.isClickable = false
            viewBinding.tvSite.isClickable = false
            viewBinding.clTime.isClickable = false
            viewBinding.etMeetingTitle.isFocusable = false
            viewBinding.top.ivRight.visibility = View.GONE
        }
        viewBinding.etMeetingTitle.text = Editable.Factory.getInstance().newEditable(item.name)
        if (item.participantList != null) {
            viewBinding.tvParticipant.setTextColor(resources.getColor(R.color.text_1E1E1E))
            viewBinding.tvParticipant.text = splitString(item.participantList)
        }
        if (!TextUtils.isEmpty(item.siteName)) {
            viewBinding.tvSite.setTextColor(resources.getColor(R.color.text_1E1E1E))
            viewBinding.tvSite.text = item.siteName
        }
        viewModel.participantList.value = item.participantList
        viewModel.siteLiveData.value = SiteNameRsp.DataBean(item.siteId, item.siteName, false)
        viewModel.startTimeLiveData.value = item.startTime
        viewModel.endTimeLiveData.value = item.endTime
        viewModel.allDayLiveData.value = item.isAllDay == "1"
        viewBinding.tvTime.setTextColor(resources.getColor(R.color.text_1E1E1E))
        viewBinding.tvTime.text =
            DateUtils.formatTime(item.startTime, "", "MM月dd日 HH:mm") + " - " + DateUtils.formatTime(
                item.endTime,
                "",
                "MM月dd日 HH:mm"
            )
        viewBinding.etRemark.setText(item.remark)
        //日程提醒remind
        if (item.remindTypeInfo == "10") {
            viewBinding.tvRemind.text = Remind.getNotRemind().title
            viewModel.remindLiveData.value = Remind.getNotRemind()
        } else {
            if (item.isAllDay == "1") {
                list2.forEach { remind ->
                    if (remind.id == item.remindTypeInfo) {
                        viewBinding.tvRemind.text = remind.title
                        viewModel.remindLiveData.value = remind
                        return@forEach
                    }
                }
            } else {
                list.forEach { remind ->
                    if (remind.id == item.remindTypeInfo) {
                        viewBinding.tvRemind.text = remind.title
                        viewModel.remindLiveData.value = remind
                        return@forEach
                    }
                }
            }
        }
    }

    private fun getDetail() {
        showLoading()
        viewModel.meetingDetailLiveData.observe(this) {
            hideLoading()
            val result = it.getOrNull()
            if (result != null) {
                setData(result)
            }
        }
        viewModel.requestMeetingDetail(id)
    }

    private fun del(scheduleId: String) {
        showLoading()
        viewModel.meetingSaveLiveData.observe(this) {
            hideLoading()
            EventBus.getDefault()
                .post(EventMessage(BaseConstant.TYPE_UPDATE_SCHEDULE_LIST_DATA, ""))
            EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_MEETING_UPDATE_LIST, ""))
            finish()
        }
        viewModel.requestDel(scheduleId)
    }

    /**
     * 新增、修改 会议
     */
    private fun sava() {
        val title = viewBinding.etMeetingTitle.text.toString().trim()
        val time = viewBinding.tvTime.text.toString().trim()
        viewModel.meetingSaveLiveData.observe(this) {
            hideLoading()
            EventBus.getDefault()
                .post(EventMessage(BaseConstant.TYPE_UPDATE_SCHEDULE_LIST_DATA, ""))
            EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_MEETING_UPDATE_LIST, ""))
            finish()
        }
        val startTime = viewModel.startTimeLiveData.value ?: ""
        val endTime = viewModel.endTimeLiveData.value ?: ""
        when {
            TextUtils.isEmpty(title) -> {
                ToastUtils.showShort("请输入会议标题")
            }
            time == getString(R.string.meeting_please_input_time) -> {
                ToastUtils.showShort(getString(R.string.meeting_please_input_time))
            }
            DateUtils.parseTimestamp(startTime, null) >= DateUtils.parseTimestamp(
                endTime,
                null
            ) -> {
                ToastUtils.showShort("结束时间需大于开始时间")
            }
            else -> {
                val scheduleData = ScheduleData()
                if (!TextUtils.isEmpty(id)) {
                    scheduleData.id = id
                }
                if (viewModel.siteLiveData.value != null) {
                    scheduleData.siteId = viewModel.siteLiveData.value?.id
                }
                scheduleData.name = title
                if (viewModel.participantList.value != null) {
                    scheduleData.participantList = viewModel.participantList.value
                }
                scheduleData.remark = viewBinding.etRemark.text.toString()
                scheduleData.remindType = if (viewModel.allDayLiveData.value == true) "1" else "0"
                scheduleData.remindTypeInfo = viewModel.remindLiveData.value?.id
                scheduleData.startTime = startTime
                scheduleData.endTime = endTime
                scheduleData.type = "3"
                scheduleData.isAllDay = if (viewModel.allDayLiveData.value == true) "1" else "0"
                showLoading()
                viewBinding.btnConfirm.isClickable = false
                viewModel.requestMeetingSave(scheduleData)
            }
        }
    }

    val list = Remind.getList()
    val list2 = Remind.getList2()

    @SuppressLint("SetTextI18n")
    private val startActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //此处是跳转的result回调方法
            if (it.data != null && it.resultCode == Activity.RESULT_OK) {
                when (resultCode) {//选择日期
                    REQUEST_CODE_DATE_SELECT -> {
                        val allDay = it.data!!.getBooleanExtra("allDay", false)
                        val startTime = it.data!!.getStringExtra("startTime")
                        val endTime = it.data!!.getStringExtra("endTime")
                        viewModel.allDayLiveData.value = allDay
                        viewModel.startTimeLiveData.value = startTime
                        viewModel.endTimeLiveData.value = endTime
                        viewBinding.tvTime.setTextColor(resources.getColor(R.color.text_1E1E1E))
                        viewBinding.tvTime.text =
                            DateUtils.formatTime(
                                startTime,
                                "",
                                "MM月dd日 HH:mm"
                            ) + " - " + DateUtils.formatTime(
                                endTime,
                                "",
                                "MM月dd日 HH:mm"
                            )
                        if (allDay) {
                            val selectedRemind = list2.filter { it.checked }
                            if (selectedRemind.isNotEmpty()) {
                                viewModel.remindLiveData.value = selectedRemind[0]
                                viewBinding.tvRemind.text = selectedRemind[0].title
                            } else {
                                viewModel.remindLiveData.value = Remind.getNotRemind()
                                viewBinding.tvRemind.text = Remind.getNotRemind().title
                            }

                        } else {
                            val selectedRemind = list.filter { it.checked }
                            if (selectedRemind.isNotEmpty()) {
                                viewModel.remindLiveData.value = selectedRemind[0]
                                viewBinding.tvRemind.text = selectedRemind[0].title
                            } else {
                                viewModel.remindLiveData.value = Remind.getNotRemind()
                                viewBinding.tvRemind.text = Remind.getNotRemind().title
                            }
                        }
                    }
                    REQUEST_CODE_PARTICIPANT_SELECT -> {//选择参与人
                        val stringExtra = it.data!!.getStringExtra("data")
                        loge("onActivityResult:$stringExtra")
                        val list = JSONArray.parseArray(
                            stringExtra,
                            ParticipantRsp.DataBean.ParticipantListBean::class.java
                        )
                        viewBinding.tvParticipant.setTextColor(resources.getColor(R.color.text_1E1E1E))
                        viewBinding.tvParticipant.text = splitString(list)
                        viewModel.participantList.value = list
                    }
                    REQUEST_CODE_SITE_SELECT -> {//选择场地
                        val stringExtra = it.data!!.getStringExtra("address")
                        loge("onActivityResult:$stringExtra")
                        val siteNameBean: SiteNameRsp.DataBean =
                            JSON.parseObject(stringExtra, SiteNameRsp.DataBean::class.java)
                        viewBinding.tvSite.setTextColor(resources.getColor(R.color.text_1E1E1E))
                        viewBinding.tvSite.text = siteNameBean.name
                        viewModel.siteLiveData.value = siteNameBean
                    }
                    REQUEST_CODE_REMIND_SELECT -> {//选择提醒
                        val stringExtra = it.data!!.getStringExtra("data")
                        val remind = JSON.parseObject(stringExtra, Remind::class.java)
                        loge("id=${remind.id},name=${remind.title}")
                        viewBinding.tvRemind.text = remind.title
                        viewModel.remindLiveData.value = remind
                        if (viewModel.allDayLiveData.value == true) {
                            list2.forEach {
                                it.checked = it.id == remind.id
                            }
                        } else {
                            list.forEach {
                                it.checked = it.id == remind.id
                            }
                        }
                    }
                }
            }
        }

    private fun splitString(list: List<ParticipantRsp.DataBean.ParticipantListBean>): String {
        val sb = StringBuffer()
        list.forEachIndexed { index, participantListBean ->
            if (index == list.size - 1) {
                sb.append(participantListBean.realname)
            } else {
                sb.append(participantListBean.realname).append("，")
            }
        }
        return sb.toString()
    }

}