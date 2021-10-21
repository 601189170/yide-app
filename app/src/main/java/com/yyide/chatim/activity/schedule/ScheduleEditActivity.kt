package com.yyide.chatim.activity.schedule

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.databinding.ActivityScheduleEditBinding
import com.yyide.chatim.model.schedule.*
import com.yyide.chatim.model.schedule.Remind.Companion.getList
import com.yyide.chatim.model.schedule.Remind.Companion.getList2
import com.yyide.chatim.utils.ColorUtil
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.view.SpacesItemDecoration
import com.yyide.chatim.viewmodel.ScheduleEditViewModel
import com.yyide.chatim.viewmodel.ScheduleMangeViewModel

class ScheduleEditActivity : BaseActivity() {
    lateinit var scheduleEditBinding: ActivityScheduleEditBinding
    private var labelList = mutableListOf<LabelListRsp.DataBean>()
    private val scheduleEditViewModel: ScheduleEditViewModel by viewModels()
    private val scheduleMangeViewModel: ScheduleMangeViewModel by viewModels()
    val list = getList()
    val list2 = getList2()
    private var sourceRepetitionRule: Repetition? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleEditBinding = ActivityScheduleEditBinding.inflate(layoutInflater)
        setContentView(scheduleEditBinding.root)
        initView()
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_schedule_edit
    }

    private fun initView() {
        scheduleEditBinding.clParticipant.visibility = if (SpData.getIdentityInfo().staffIdentity()) View.VISIBLE else View.GONE
        val stringExtra = intent.getStringExtra("data")
        val scheduleData = JSON.parseObject(stringExtra, ScheduleData::class.java)
        scheduleData?.let {
            //显示日程详情
            //日程id
            scheduleEditViewModel.scheduleIdLiveData.value = it.id
            //日程名称name
            scheduleEditBinding.etScheduleTitle.setText(it.name)
            scheduleEditViewModel.scheduleTitleLiveData.value = it.name
            //日程状态status
            scheduleEditBinding.checkBox.isChecked = it.status == "1"
            scheduleEditViewModel.scheduleStatusLiveData.value = it.status
            //日程开始时间startTime 日程结束时间endTime
            scheduleEditBinding.tvDateStart.text =
                DateUtils.formatTime(it.startTime, "", "MM月dd日 HH:mm")
            scheduleEditBinding.tvDateEnd.text =
                DateUtils.formatTime(it.endTime, "", "MM月dd日 HH:mm")
            scheduleEditViewModel.startTimeLiveData.value = it.startTime
            scheduleEditViewModel.endTimeLiveData.value = it.endTime
            val bottomStartTime = DateUtils.formatTime(it.startTime,"","MM月dd HH:mm")
            val bottomEndTime = DateUtils.formatTime(it.endTime,"","MM月dd HH:mm")
            //scheduleEditBinding.tvBottomDate.text = String.format(getString(R.string.schedule_edit_bottom_date),bottomStartTime,bottomEndTime)
            //是否全天isAllDay【0：不是，1：是】
            scheduleEditViewModel.allDayLiveData.value = it.isAllDay == "1"
            //日程重复repetition
            Repetition.getList().forEach { repetition ->
                if ("${repetition.code}" == it.isRepeat) {
                    scheduleEditBinding.tvRepetition.text = repetition.title
                    scheduleEditViewModel.repetitionLiveData.value = repetition
                    sourceRepetitionRule = repetition
                }
            }
            if (it.rrule != null && it.isRepeat == "8") {
                scheduleEditBinding.tvRepetition.text = "自定义重复"
                val repetition = Repetition(8,"", true, it.rrule)
                scheduleEditViewModel.repetitionLiveData.value = repetition
                sourceRepetitionRule = repetition
            }
            //日程提醒remind
            if (it.remindTypeInfo == "10") {
                scheduleEditBinding.tvRemind.text = Remind.getNotRemind().title
                scheduleEditViewModel.remindLiveData.value = Remind.getNotRemind()
            } else {
                if (it.isAllDay == "1") {
                    list2.forEach { remind ->
                        if (remind.id == it.remindTypeInfo) {
                            scheduleEditBinding.tvRemind.text = remind.title
                            scheduleEditViewModel.remindLiveData.value = remind
                            return@forEach
                        }
                    }
                } else {
                    list.forEach { remind ->
                        if (remind.id == it.remindTypeInfo) {
                            scheduleEditBinding.tvRemind.text = remind.title
                            scheduleEditViewModel.remindLiveData.value = remind
                            return@forEach
                        }
                    }
                }
            }

            //日程便签label
            labelList.clear()
            labelList.addAll(it.label)
            scheduleEditViewModel.labelListLiveData.value = labelList
            //参与人participant
            scheduleEditViewModel.participantList.value = it.participant
            showSelectedParticipant(it.participant)
            //地址 siteId
            if (!TextUtils.isEmpty(it.siteId)) {
                val dataBean = SiteNameRsp.DataBean(it.siteId, it.siteName, true)
                scheduleEditViewModel.siteLiveData.value = dataBean
                scheduleEditBinding.tvAddress.text = it.siteName
                scheduleEditBinding.tvAddress.setTextColor(resources.getColor(R.color.black9))
            }
            //备注 remark
            scheduleEditBinding.edit.setText(it.remark)
            scheduleEditViewModel.remarkLiveData.value = it.remark
        }

        scheduleEditBinding.top.title.text = "日程编辑"
        scheduleEditBinding.top.backLayout.setOnClickListener {
            finish()
        }
        scheduleEditBinding.btnCommit.setOnClickListener {
            if (TextUtils.isEmpty(scheduleEditBinding.etScheduleTitle.text.toString())){
                ToastUtils.showShort("需要输入日程名称")
                return@setOnClickListener
            }
            val rule = sourceRepetitionRule?.rule
            scheduleEditViewModel.scheduleTitleLiveData.value = scheduleEditBinding.etScheduleTitle.text.toString()
            val repetition = scheduleEditViewModel.repetitionLiveData.value
            if ((repetition == null || repetition.rule?.isEmpty() != false) && (sourceRepetitionRule == null || rule == null)) {
                scheduleEditViewModel.saveOrModifySchedule(true)
                return@setOnClickListener
            }
            DialogUtil.showRepetitionScheduleModifyDialog(this) {
                loge("编辑日程重复性类型：$it")
                if (it == -1) {
                    //finish()
                    return@showRepetitionScheduleModifyDialog
                }
                //修改日程
                scheduleEditViewModel.updateTypeLiveData.value = "$it"
                scheduleEditViewModel.saveOrModifySchedule(true)
            }
        }
        scheduleEditBinding.top.ivRight.visibility = View.VISIBLE
        scheduleEditBinding.top.ivRight.setOnClickListener {
            DialogUtil.showScheduleDelDialog(
                this,
                scheduleEditBinding.top.ivRight,
                object : DialogUtil.OnClickListener {
                    override fun onCancel(view: View?) {

                    }

                    override fun onEnsure(view: View?) {
                        val scheduleId = scheduleEditViewModel.scheduleIdLiveData.value ?: ""
                        scheduleEditViewModel.deleteScheduleById(scheduleId)
                    }

                })
        }

        scheduleEditBinding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            scheduleEditViewModel.scheduleStatusLiveData.value = if (isChecked) "1" else "0"
        }

        scheduleEditBinding.edit.addTextChangedListener {
            scheduleEditViewModel.remarkLiveData.value = it.toString()
        }

        scheduleEditBinding.btnAddLabel.setOnClickListener {
            val intent = Intent(this, ScheduleAddLabelActivity::class.java)
            intent.putExtra(
                "labelList",
                JSON.toJSONString(scheduleEditViewModel.labelListLiveData.value)
            )
            startActivityForResult(intent, REQUEST_CODE_LABEL_SELECT)
        }

        scheduleEditBinding.clRemind.setOnClickListener {
            val intent = Intent(this, ScheduleRemindActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(scheduleEditViewModel.remindLiveData.value))
            intent.putExtra("allDay", scheduleEditViewModel.allDayLiveData.value)
            startActivityForResult(intent, REQUEST_CODE_REMIND_SELECT)
        }
        scheduleEditBinding.clRepetition.setOnClickListener {
            val intent = Intent(this, ScheduleRepetitionActivity::class.java)
            intent.putExtra(
                "data",
                JSON.toJSONString(scheduleEditViewModel.repetitionLiveData.value)
            )
            startActivityForResult(intent, REQUEST_CODE_REPETITION_SELECT)
        }
        scheduleEditBinding.clAddress.setOnClickListener {
            val intent = Intent(this, ScheduleAddressActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(scheduleEditViewModel.siteLiveData.value))
            startActivityForResult(intent, REQUEST_CODE_SITE_SELECT)
        }
        scheduleEditBinding.clDate.setOnClickListener {
            val intent = Intent(this, ScheduleDateIntervalActivity::class.java)
            val allDay = scheduleEditViewModel.allDayLiveData.value
            val startTime = scheduleEditViewModel.startTimeLiveData.value
            val endTime = scheduleEditViewModel.endTimeLiveData.value
            intent.putExtra("allDay", allDay)
            intent.putExtra("startTime", startTime)
            intent.putExtra("endTime", endTime)
            startActivityForResult(intent, REQUEST_CODE_DATE_SELECT)
        }
        scheduleEditBinding.clParticipant.setOnClickListener {
            val intent = Intent(this, ScheduleParticipantActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(scheduleEditViewModel.participantList.value))
            startActivityForResult(intent, REQUEST_CODE_PARTICIPANT_SELECT)
        }

        val flexboxLayoutManager = FlexboxLayoutManager(this)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        scheduleEditBinding.rvLabelList.layoutManager = flexboxLayoutManager
        scheduleEditBinding.rvLabelList.addItemDecoration(
            SpacesItemDecoration(
                SpacesItemDecoration.dip2px(
                    5f
                )
            )
        )
        adapter.setList(labelList)
        scheduleEditBinding.rvLabelList.adapter = adapter
        //日程删除监听
        scheduleEditViewModel.deleteResult.observe(this, {
            if (it) {
                //日程删除成功 并删除本地数据
                val scheduleId = scheduleEditViewModel.scheduleIdLiveData.value ?: ""
                ScheduleDaoUtil.deleteScheduleData(scheduleId)
                finish()
            }
        })
        //日程修改监听
        scheduleEditViewModel.saveOrModifyResult.observe(this, {
            if (it) {
                scheduleMangeViewModel.getAllScheduleList()
                return@observe
            }
            ToastUtils.showShort("修改日程失败")
            finish()
        })
        //获取日程监听
        scheduleMangeViewModel.requestAllScheduleResult.observe(this, {
            if (it) {
                ToastUtils.showShort("修改日程成功")
                finish()
                return@observe
            }
            ToastUtils.showShort("修改日程失败")
            finish()
        })
    }

    val adapter = object :
        BaseQuickAdapter<LabelListRsp.DataBean, BaseViewHolder>(R.layout.item_schedule_label_flow_list) {
        override fun convert(holder: BaseViewHolder, item: LabelListRsp.DataBean) {
            val drawable = GradientDrawable()
            drawable.cornerRadius = DisplayUtils.dip2px(this@ScheduleEditActivity, 2f).toFloat()
            drawable.setColor(ColorUtil.parseColor(item.colorValue))
            holder.getView<TextView>(R.id.tv_label).background = drawable
            holder.setText(R.id.tv_label, item.labelName)
            holder.itemView.setOnClickListener {
                loge("item=$item")
                remove(item)
                labelList.remove(item)
                notifyDataSetChanged()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //选择标签
        if (requestCode == REQUEST_CODE_LABEL_SELECT && resultCode == RESULT_OK && data != null) {
            val stringExtra = data.getStringExtra("labelList")
            loge("onActivityResult:$stringExtra")
            val parseArray: List<LabelListRsp.DataBean> =
                JSONArray.parseArray(stringExtra, LabelListRsp.DataBean::class.java)
            if (parseArray.isNotEmpty()) {
                labelList.clear()
                labelList.addAll(parseArray)
                scheduleEditViewModel.labelListLiveData.value = labelList
                adapter.setList(labelList)
            }
            return
        }

        //选择场地
        if (requestCode == REQUEST_CODE_SITE_SELECT && resultCode == RESULT_OK && data != null) {
            val stringExtra = data.getStringExtra("address")
            loge("onActivityResult:$stringExtra")
            val siteNameBean: SiteNameRsp.DataBean =
                JSON.parseObject(stringExtra, SiteNameRsp.DataBean::class.java)
            val name = siteNameBean.name
            scheduleEditBinding.tvAddress.setTextColor(resources.getColor(R.color.black9))
            scheduleEditBinding.tvAddress.text = name
            scheduleEditViewModel.siteLiveData.value = siteNameBean
            return
        }

        //选择日期
        if (requestCode == REQUEST_CODE_DATE_SELECT && resultCode == RESULT_OK && data != null) {
            val allDay = data.getBooleanExtra("allDay", false)
            val startTime = data.getStringExtra("startTime")
            val endTime = data.getStringExtra("endTime")
            loge("allDay=$allDay,startTime=$startTime,endTime=$endTime")
            scheduleEditBinding.tvDateStart.text =
                DateUtils.formatTime(startTime, "", "MM月dd日 HH:mm")
            scheduleEditBinding.tvDateEnd.text = DateUtils.formatTime(endTime, "", "MM月dd日 HH:mm")
            scheduleEditViewModel.startTimeLiveData.value = startTime
            scheduleEditViewModel.endTimeLiveData.value = endTime
            scheduleEditViewModel.allDayLiveData.value = allDay
            if (allDay) {
                val selectedRemind = list2.filter { it.checked }
                if (selectedRemind.isNotEmpty()) {
                    scheduleEditViewModel.remindLiveData.value = selectedRemind[0]
                    scheduleEditBinding.tvRemind.text = selectedRemind[0].title
                } else {
                    scheduleEditViewModel.remindLiveData.value = Remind.getNotRemind()
                    scheduleEditBinding.tvRemind.text = Remind.getNotRemind().title
                }

            } else {
                val selectedRemind = list.filter { it.checked }
                if (selectedRemind.isNotEmpty()) {
                    scheduleEditViewModel.remindLiveData.value = selectedRemind[0]
                    scheduleEditBinding.tvRemind.text = selectedRemind[0].title
                } else {
                    scheduleEditViewModel.remindLiveData.value = Remind.getNotRemind()
                    scheduleEditBinding.tvRemind.text = Remind.getNotRemind().title
                }
            }
            return
        }
        //选择提醒
        if (requestCode == REQUEST_CODE_REMIND_SELECT && resultCode == RESULT_OK && data != null) {
            val stringExtra = data.getStringExtra("data")
            val remind = JSON.parseObject(stringExtra, Remind::class.java)
            loge("id=${remind.id},name=${remind.title}")
            scheduleEditBinding.tvRemind.text = remind.title
            scheduleEditViewModel.remindLiveData.value = remind
            if (scheduleEditViewModel.allDayLiveData.value == true) {
                list2.forEach {
                    it.checked = it.id == remind.id
                }
            } else {
                list.forEach {
                    it.checked = it.id == remind.id
                }
            }
            return
        }
        //选择重复
        if (requestCode == REQUEST_CODE_REPETITION_SELECT && resultCode == RESULT_OK && data != null) {
            val stringExtra = data.getStringExtra("data")
            val repetition = JSON.parseObject(stringExtra, Repetition::class.java)
            loge("title=${repetition.title},rule=${repetition.rule}")
            scheduleEditBinding.tvRepetition.text = repetition.title
            scheduleEditViewModel.repetitionLiveData.value = repetition
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
            if (list != null) {
                scheduleEditViewModel.participantList.value = list
                //list.map { ScheduleData.ParticipantBean(it.id) }
                //显示参与人
                showSelectedParticipant(list)

            }
            return
        }
    }

    private fun showSelectedParticipant(list: MutableList<ParticipantRsp.DataBean.ParticipantListBean>) {
        val stringBuilder = StringBuilder()
        list.map { it.realname }.forEach {
            stringBuilder.append(it).append("  ")
        }
        if (stringBuilder.isEmpty() || stringBuilder.isBlank()) {
            scheduleEditBinding.tvParticipant.text = "添加参与人"
            scheduleEditBinding.tvParticipant.setTextColor(resources.getColor(R.color.black11))
        } else {
            scheduleEditBinding.tvParticipant.text = stringBuilder
            scheduleEditBinding.tvParticipant.setTextColor(resources.getColor(R.color.black9))
        }
    }

    companion object {
        //标签选择
        const val REQUEST_CODE_LABEL_SELECT = 100

        //场地选择
        const val REQUEST_CODE_SITE_SELECT = 101

        //提醒选择
        const val REQUEST_CODE_REMIND_SELECT = 102

        //重复选择
        const val REQUEST_CODE_REPETITION_SELECT = 103

        //时间选择
        const val REQUEST_CODE_DATE_SELECT = 104

        //选择参与人
        const val REQUEST_CODE_PARTICIPANT_SELECT = 105
    }
}