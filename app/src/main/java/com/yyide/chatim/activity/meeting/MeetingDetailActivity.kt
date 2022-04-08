package com.yyide.chatim.activity.meeting


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.meeting.viewmodel.MeetingDetailViewModel
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.constant.ResultCodeStr
import com.yyide.chatim.databinding.ActivityMeetingDetailBinding
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.schedule.Remind
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.utils.*
import com.yyide.chatim.view.DialogUtil
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author shenzhibin
 * @date 2022/3/22 17:20
 * @description 会议详情页面
 */
class MeetingDetailActivity :
    KTBaseActivity<ActivityMeetingDetailBinding>(ActivityMeetingDetailBinding::inflate) {


    private val viewModel: MeetingDetailViewModel by viewModels()

    companion object {
        fun jump(context: Context, id: String) {
            val intent = Intent(context, MeetingDetailActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }


    private val jumpActivityWithResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            parserLastActivityData(result)
        }

    private val timeFormat = "MM月dd日 HH:mm"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.detailID = intent.getStringExtra("id") ?: ""
        initData()
    }

    private val remindList = Remind.getList()
    private val remindList2 = Remind.getList2()

    override fun initView() {
        super.initView()

        binding.meetingDetailTop.backLayout.setOnClickListener {
            finish()
        }

        binding.meetingDetailTop.title.text = getString(R.string.meeting_detail_title)

        viewModel.delResultListData.observe(this) {
            hideLoading()
            logd("delResult = $it")
            EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_UPDATE_SCHEDULE_LIST_DATA, ""))
            EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_MEETING_UPDATE_LIST, ""))
            finish()
        }

        binding.meetingDetailTop.ivEdit.setOnClickListener { _ ->
            if (viewModel.detailID != "") {
                MeetingSaveActivity.jumpUpdateWithCode(
                    this,
                    viewModel.detailID,
                    jumpActivityWithResult
                )
            }
        }

        binding.meetingDetailTop.ivRight.setOnClickListener { view ->
            DialogUtil.showScheduleDelDialog(
                this,
                view,
                object : DialogUtil.OnClickListener {
                    override fun onCancel(view: View?) {

                    }

                    override fun onEnsure(view: View?) {
                        if (viewModel.detailID != "") {
                            showLoading()
                            viewModel.requestDeleteMeeting()
                        }
                    }
                },
            )
        }

        viewModel.meetingDetailLiveData.observe(this) { emitResult ->
            hideLoading()
            val detailData = emitResult.getOrNull()
            if (detailData != null) {
                showDetailInfo(detailData)
            } else {
                showLongToast("无法获得会议详情")
            }
        }

    }

    fun initData() {
        if (viewModel.detailID != "") {
            showLoading()
            viewModel.requestMeetingDetail()
        }
    }

    private fun showDetailInfo(data: ScheduleData) {
        if (data.promoter == SpData.getUserId()) {
            binding.meetingDetailTop.ivEdit.show()
            binding.meetingDetailTop.ivRight.show()
        } else {
            binding.meetingDetailTop.ivEdit.remove()
            binding.meetingDetailTop.ivRight.remove()
        }

        // 会议名称
        binding.meetingDetailTitle.text = data.name

        // 会议时间
        val timeStr = "${
            DateUtils.formatTime(
                data.startTime,
                "",
                timeFormat,
                true
            )
        } 至 ${
            DateUtils.formatTime(
                data.endTime,
                "",
                timeFormat,
                true
            )
        }"
        binding.meetingDetailTimeDescribe.text = timeStr

        //日程提醒remind
        if (data.remindTypeInfo == "10") {
            binding.meetingDetailRemindDescribe.text = Remind.getNotRemind().title
        } else {
            if (data.isAllDay == "1") {
                remindList2.forEach { remind ->
                    if (remind.id == data.remindTypeInfo) {
                        binding.meetingDetailRemindDescribe.text = remind.title
                        return@forEach
                    }
                }
            } else {
                remindList.forEach { remind ->
                    if (remind.id == data.remindTypeInfo) {
                        binding.meetingDetailRemindDescribe.text = remind.title
                        return@forEach
                    }
                }
            }
        }

        // 参与人
        val stringBuilder = StringBuilder()
        data.participantList.forEachIndexed { index, participantListBean ->
            if (participantListBean.realname.isNullOrEmpty()){
                stringBuilder.append(participantListBean.name)
            }else{
                stringBuilder.append(participantListBean.realname)
            }
            if (index != data.participantList.lastIndex) {
                stringBuilder.append("、")
            }
        }

        binding.meetingDetailPeopleDescribe.text = stringBuilder.toString()

        //会议地点
        binding.meetingDetailAddressDescribe.text = data.siteName

        // 备注
        binding.meetingDetailRemarkDescribe.text = data.remark
    }

    private fun parserLastActivityData(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val jumpType = result.data?.getStringExtra(ResultCodeStr.NORMAL_TYPE_CODE) ?: ""

            when (jumpType) {
                ResultCodeStr.MEETING_DETAIL_EDIT_DEL -> {
                    finish()
                }
                ResultCodeStr.MEETING_DETAIL_EDIT_UPDATE -> {
                    initData()
                }
            }
        }
    }


}