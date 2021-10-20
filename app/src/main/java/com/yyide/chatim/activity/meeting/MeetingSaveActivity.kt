package com.yyide.chatim.activity.meeting

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.R
import com.yyide.chatim.activity.meeting.viewmodel.MeetingSaveViewModel
import com.yyide.chatim.activity.schedule.ScheduleAddressActivity
import com.yyide.chatim.activity.schedule.ScheduleDateIntervalActivity
import com.yyide.chatim.activity.schedule.ScheduleParticipantActivity
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.databinding.ActivityMeetingCreateBinding
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.schedule.ParticipantRsp
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.model.schedule.SiteNameRsp
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import org.greenrobot.eventbus.EventBus

/**
 * 创建/编辑 会议
 * author lrz
 * time 2021年10月11日17:47:47
 */
class MeetingSaveActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityMeetingCreateBinding
    private val viewModel: MeetingSaveViewModel by viewModels()
    private var id = ""
    override fun getContentViewID(): Int {
        return R.layout.activity_meeting_create
    }

    companion object {
        //时间选择
        const val REQUEST_CODE_DATE_SELECT = 104

        //选择参与人
        const val REQUEST_CODE_PARTICIPANT_SELECT = 105

        //场地选择
        const val REQUEST_CODE_SITE_SELECT = 101
        private val CREATE_TYPE = 1
        private val UPDATE_TYPE = 2
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

    private fun initView() {
        val type = intent.getIntExtra("type", -1)
        viewBinding.top.title.text = getString(R.string.meeting_create_title)
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
            startActivityForResult(intent, REQUEST_CODE_SITE_SELECT)
        }
        //选择参与人
        viewBinding.tvParticipant.setOnClickListener {
            val intent = Intent(this, ScheduleParticipantActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(viewModel.participantList.value))
            startActivityForResult(intent, REQUEST_CODE_PARTICIPANT_SELECT)
        }
        viewBinding.clTime.setOnClickListener {
            val intent = Intent(this, ScheduleDateIntervalActivity::class.java)
            val allDay = viewModel.allDayLiveData.value
            val startTime = viewModel.startTimeLiveData.value
            val endTime = viewModel.endTimeLiveData.value
            intent.putExtra("allDay", allDay)
            intent.putExtra("startTime", startTime)
            intent.putExtra("endTime", endTime)
            startActivityForResult(intent, REQUEST_CODE_DATE_SELECT)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setData(item: ScheduleData) {
        viewBinding.etMeetingTitle.text = Editable.Factory.getInstance().newEditable(item.name)
        if (item.participant != null) {
            viewBinding.tvParticipant.setTextColor(resources.getColor(R.color.text_1E1E1E))
            viewBinding.tvParticipant.text = splitString(item.participant)
        }
        if (!TextUtils.isEmpty(item.siteName)) {
            viewBinding.tvSite.setTextColor(resources.getColor(R.color.text_1E1E1E))
            viewBinding.tvSite.text = item.siteName
        }
        viewModel.siteLiveData.value = SiteNameRsp.DataBean(item.siteId, item.siteName, false)
        viewModel.startTimeLiveData.value = item.startTime
        viewModel.endTimeLiveData.value = item.endTime
        viewBinding.tvTime.setTextColor(resources.getColor(R.color.text_1E1E1E))
        viewBinding.tvTime.text =
            DateUtils.formatTime(item.startTime, "", "MM月dd号 HH:mm") + " - " + DateUtils.formatTime(
                item.endTime,
                "",
                "MM月dd号 HH:mm"
            )
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
            EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_UPDATE_SCHEDULE_LIST_DATA,""))
            EventBus.getDefault()
                .post(EventMessage(BaseConstant.TYPE_MEETING_UPDATE_LIST, ""))
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
            EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_UPDATE_SCHEDULE_LIST_DATA,""))
            EventBus.getDefault()
                .post(EventMessage(BaseConstant.TYPE_MEETING_UPDATE_LIST, ""))
            finish()
        }
        when {
            TextUtils.isEmpty(title) -> {
                ToastUtils.showShort("请输入会议标题")
            }
            time == "请选择会议时间" -> {
                ToastUtils.showShort("请选择会议时间")
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
                    scheduleData.participant = viewModel.participantList.value
                }
                scheduleData.startTime = viewModel.startTimeLiveData.value
                scheduleData.endTime = viewModel.endTimeLiveData.value
                scheduleData.type = "3"
                showLoading()
                viewModel.requestMeetingSave(scheduleData)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //选择日期
        if (requestCode == REQUEST_CODE_DATE_SELECT && resultCode == RESULT_OK && data != null) {
            val allDay = data.getBooleanExtra("allDay", false)
            val startTime = data.getStringExtra("startTime")
            val endTime = data.getStringExtra("endTime")
            viewModel.allDayLiveData.value = allDay
            viewModel.startTimeLiveData.value = startTime
            viewModel.endTimeLiveData.value = endTime
            viewBinding.tvTime.setTextColor(resources.getColor(R.color.text_1E1E1E))
            viewBinding.tvTime.text =
                DateUtils.formatTime(startTime, "", "MM月dd号 HH:mm") + " - " + DateUtils.formatTime(
                    endTime,
                    "",
                    "MM月dd号 HH:mm"
                )
            return
        }

        //选择参与人
        if (requestCode == REQUEST_CODE_PARTICIPANT_SELECT && resultCode == RESULT_OK && data != null) {
            val stringExtra = data.getStringExtra("data")
            loge("onActivityResult:$stringExtra")
            val list = JSONArray.parseArray(
                stringExtra,
                ParticipantRsp.DataBean.ParticipantListBean::class.java
            )
            viewBinding.tvParticipant.setTextColor(resources.getColor(R.color.text_1E1E1E))
            viewBinding.tvParticipant.text = splitString(list)
            viewModel.participantList.value = list
            return
        }

        //选择场地
        if (requestCode == REQUEST_CODE_SITE_SELECT && resultCode == RESULT_OK && data != null) {
            val stringExtra = data.getStringExtra("address")
            loge("onActivityResult:$stringExtra")
            val siteNameBean: SiteNameRsp.DataBean =
                JSON.parseObject(stringExtra, SiteNameRsp.DataBean::class.java)
            viewBinding.tvSite.setTextColor(resources.getColor(R.color.text_1E1E1E))
            viewBinding.tvSite.text = siteNameBean.name
            viewModel.siteLiveData.value = siteNameBean
            return
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