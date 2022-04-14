package com.yyide.chatim_pro.activity.schedule

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.SpData
import com.yyide.chatim_pro.base.BaseActivity
import com.yyide.chatim_pro.database.ScheduleDaoUtil
import com.yyide.chatim_pro.databinding.ActivityScheduleFullEditionBinding
import com.yyide.chatim_pro.model.schedule.*
import com.yyide.chatim_pro.utils.*
import com.yyide.chatim_pro.view.DialogUtil
import com.yyide.chatim_pro.view.SpacesItemDecoration
import com.yyide.chatim_pro.viewmodel.ScheduleEditViewModel
import com.yyide.chatim_pro.viewmodel.toScheduleDataBean
import com.yyide.chatim_pro.viewmodel.toScheduleEditViewModel
import java.util.concurrent.atomic.AtomicReference

class ScheduleFullEditionActivity : BaseActivity() {
    lateinit var scheduleFullEditionBinding: ActivityScheduleFullEditionBinding
    private val scheduleEditViewModel: ScheduleEditViewModel by viewModels()
    var dateStart = AtomicReference("")
    var dateEnd = AtomicReference("")
    val list = Remind.getList()
    val list2 = Remind.getList2()
    private var promoter = true
    private var labelList = mutableListOf<LabelListRsp.DataBean>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleFullEditionBinding = ActivityScheduleFullEditionBinding.inflate(layoutInflater)
        setContentView(scheduleFullEditionBinding.root)
        initView()
    }

    private val remindLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //选择提醒
            if (it.resultCode == RESULT_OK && it.data != null) {
                val stringExtra = it.data?.getStringExtra("data")
                val remind = JSON.parseObject(stringExtra, Remind::class.java)
                loge("id=${remind.id},name=${remind.title}")
                scheduleFullEditionBinding.tvRemind.text = remind.title
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
            }
        }

    private val repetitionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //选择重复
            if (it.resultCode == RESULT_OK && it.data != null) {
                val stringExtra = it.data?.getStringExtra("data")
                val repetition = JSON.parseObject(stringExtra, Repetition::class.java)
                loge("title=${repetition.title},rule=${repetition.rule}")
                if (repetition.code == 8) {
                    scheduleFullEditionBinding.tvRepetition.text =
                        ScheduleRepetitionRuleUtil.ruleToString(JSON.toJSONString(repetition.rule))
                } else {
                    scheduleFullEditionBinding.tvRepetition.text = repetition.title
                }
                scheduleEditViewModel.repetitionLiveData.value = repetition
            }
        }

    private val participantLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //选择参与人
            if (it.resultCode == RESULT_OK && it.data != null) {
                val stringExtra = it.data?.getStringExtra("data")
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
            }
        }

    private fun showSelectedParticipant(list: List<ParticipantRsp.DataBean.ParticipantListBean>?) {
        val stringBuilder = StringBuilder()
        list?.map { it.name }?.forEach {
            stringBuilder.append(it).append("  ")
        }
        if (stringBuilder.isEmpty() || stringBuilder.isBlank()) {
            scheduleFullEditionBinding.tvParticipant.text = "添加参与人"
            scheduleFullEditionBinding.tvParticipant.setTextColor(resources.getColor(R.color.black11))
        } else {
            scheduleFullEditionBinding.tvParticipant.text = stringBuilder
            scheduleFullEditionBinding.tvParticipant.setTextColor(resources.getColor(R.color.black9))
        }
    }

    private val addressLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //选择场地
            if (it.resultCode == RESULT_OK && it.data != null) {
                val stringExtra = it.data?.getStringExtra("address")
                loge("onActivityResult:$stringExtra")
                val siteNameBean: SiteNameRsp.DataBean =
                    JSON.parseObject(stringExtra, SiteNameRsp.DataBean::class.java)
                val name = siteNameBean.name
                scheduleFullEditionBinding.tvAddress.setTextColor(resources.getColor(R.color.black9))
                scheduleFullEditionBinding.tvAddress.text = name
                scheduleEditViewModel.siteLiveData.value = siteNameBean
            }
        }

    private val labelLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //选择标签
            if (it.resultCode == RESULT_OK && it.data != null) {
                val stringExtra = it.data?.getStringExtra("labelList")
                loge("onActivityResult:$stringExtra")
                val parseArray: List<LabelListRsp.DataBean> =
                    JSONArray.parseArray(stringExtra, LabelListRsp.DataBean::class.java)
                if (parseArray.isNotEmpty()) {
                    labelList.clear()
                    labelList.addAll(parseArray)
                    scheduleEditViewModel.labelListLiveData.value = labelList
                    adapter.setList(labelList)
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initView() {
        scheduleFullEditionBinding.top.title.text = "创建日程"
        scheduleFullEditionBinding.top.backLayout.setOnClickListener {
            scheduleEditViewModel.scheduleTitleLiveData.value = scheduleFullEditionBinding.etScheduleTitle.text.toString()
            scheduleEditViewModel.startTimeLiveData.value = dateStart.get()
            scheduleEditViewModel.endTimeLiveData.value = dateEnd.get()
            val toScheduleDataBean = scheduleEditViewModel.toScheduleDataBean()
            val intent = intent.putExtra("data",JSON.toJSONString(toScheduleDataBean))
            setResult(RESULT_OK,intent)
            finish()
        }

        if (scheduleEditViewModel.remindLiveData.value == null) {
            scheduleFullEditionBinding.tvRemind.text = "不提醒"
            scheduleEditViewModel.remindLiveData.value = Remind.getNotRemind()
        } else {
            val title = scheduleEditViewModel.remindLiveData.value?.title
            scheduleFullEditionBinding.tvRemind.text = title
        }
        val stringExtra = intent.getStringExtra("data")
        val scheduleDataBean = JSON.parseObject(stringExtra, ScheduleDataBean::class.java)
        scheduleDataBean?.toScheduleEditViewModel(scheduleEditViewModel)
        scheduleDataBean?.let {
            //日程名称name
            scheduleFullEditionBinding.etScheduleTitle.setText(it.name)
            scheduleEditViewModel.scheduleTitleLiveData.value = it.name
            //日程开始时间startTime 日程结束时间endTime
            scheduleFullEditionBinding.tvDateStart.text =
                DateUtils.formatTime(it.startTime, "", "MM月dd日 HH:mm")
            scheduleFullEditionBinding.tvDateEnd.text =
                DateUtils.formatTime(it.endTime, "", "MM月dd日 HH:mm")
            scheduleEditViewModel.startTimeLiveData.value = it.startTime
            scheduleEditViewModel.endTimeLiveData.value = it.endTime
            //是否全天isAllDay【0：不是，1：是】
            scheduleEditViewModel.allDayLiveData.value = it.allDay
            //日程重复repetition
            Repetition.getList().forEach { repetition ->
                if (repetition.code == it.repetition?.code) {
                    scheduleFullEditionBinding.tvRepetition.text = repetition.title
                    scheduleEditViewModel.repetitionLiveData.value = repetition
                }
            }
            if (it.repetition?.rule != null && it.repetition?.code == 8) {
                //scheduleEditBinding.tvRepetition.text = "自定义重复"
                scheduleFullEditionBinding.tvRepetition.text =
                    ScheduleRepetitionRuleUtil.ruleToString(JSON.toJSONString(it.repetition?.rule))
                //val repetition = Repetition(8,"", true, it.repetition?.rule)
                scheduleEditViewModel.repetitionLiveData.value = it.repetition
            }
            //日程提醒remind
            if (it.remind?.id == "10") {
                scheduleFullEditionBinding.tvRemind.text = Remind.getNotRemind().title
                scheduleEditViewModel.remindLiveData.value = Remind.getNotRemind()
            } else {
                if (it.allDay) {
                    list2.forEach { remind ->
                        if (remind.id == it.remind?.id) {
                            scheduleFullEditionBinding.tvRemind.text = remind.title
                            scheduleEditViewModel.remindLiveData.value = remind
                            return@forEach
                        }
                    }
                } else {
                    list.forEach { remind ->
                        if (remind.id == it.remind?.id) {
                            scheduleFullEditionBinding.tvRemind.text = remind.title
                            scheduleEditViewModel.remindLiveData.value = remind
                            return@forEach
                        }
                    }
                }
            }

            //日程便签label
            labelList.clear()
            labelList.addAll(it.labelList?: mutableListOf())
            scheduleEditViewModel.labelListLiveData.value = labelList
            //参与人participant
            scheduleEditViewModel.participantList.value = it.participantList
            if (SpData.getIdentityInfo().staffIdentity()) {
                if (it.participantList?.isEmpty() == true) {
                    scheduleFullEditionBinding.clParticipant.visibility =
                        if (promoter) View.VISIBLE else View.GONE
                } else {
                    scheduleFullEditionBinding.clParticipant.visibility = View.VISIBLE
                    showSelectedParticipant(it.participantList)
                }
            } else {
                //家长不显示参与人
                scheduleFullEditionBinding.clParticipant.visibility = View.GONE
            }
            //地址 siteId
            if (it.siteName != null) {
                scheduleEditViewModel.siteLiveData.value = it.siteName
                scheduleFullEditionBinding.tvAddress.text = it.siteName?.name
                scheduleFullEditionBinding.tvAddress.setTextColor(resources.getColor(R.color.black9))
                scheduleFullEditionBinding.clAddress.visibility = View.VISIBLE
            } else {
                //地址为空的时候，如果是本人创建的日程，可以显示添加地址
                scheduleFullEditionBinding.clAddress.visibility = if (promoter) View.VISIBLE else View.GONE
            }
            //备注 remark
            if (TextUtils.isEmpty(it.remark)) {
                scheduleFullEditionBinding.clRemark.visibility = if (promoter) View.VISIBLE else View.GONE
            } else {
                scheduleFullEditionBinding.clRemark.visibility = View.VISIBLE
                scheduleFullEditionBinding.edit.setText(it.remark)
            }
            scheduleEditViewModel.remarkLiveData.value = it.remark

            if (TextUtils.isEmpty(it.remark) && it.siteName != null && !promoter){
                scheduleFullEditionBinding.vLine4.visibility = View.GONE
            }
        }

        val flexboxLayoutManager = FlexboxLayoutManager(this)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        scheduleFullEditionBinding.rvLabelList.layoutManager = flexboxLayoutManager
        scheduleFullEditionBinding.rvLabelList.addItemDecoration(
            SpacesItemDecoration(
                SpacesItemDecoration.dip2px(
                    5f
                )
            )
        )
        adapter.setList(labelList)
        scheduleFullEditionBinding.rvLabelList.adapter = adapter

        //选择提醒
        scheduleFullEditionBinding.clRemind.setOnClickListener {
            val intent = Intent(this, ScheduleRemindActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(scheduleEditViewModel.remindLiveData.value))
            intent.putExtra("allDay", scheduleEditViewModel.allDayLiveData.value)
            remindLauncher.launch(intent)
        }
        //选择重复规则
        scheduleFullEditionBinding.clRepetition.setOnClickListener {
            val intent = Intent(this, ScheduleRepetitionActivity::class.java)
            val startTime = scheduleEditViewModel.startTimeLiveData.value
            intent.putExtra("startTime",startTime)
            intent.putExtra(
                "data",
                JSON.toJSONString(scheduleEditViewModel.repetitionLiveData.value)
            )
            repetitionLauncher.launch(intent)
        }
        //选择参与人
        scheduleFullEditionBinding.clParticipant.setOnClickListener {
            val intent = Intent(this, ScheduleParticipantActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(scheduleEditViewModel.participantList.value))
            participantLauncher.launch(intent)
        }
        //选择地址
        scheduleFullEditionBinding.clAddress.setOnClickListener {
            val intent = Intent(this, ScheduleAddressActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(scheduleEditViewModel.siteLiveData.value))
            addressLauncher.launch(intent)
        }
        //选择标签
        scheduleFullEditionBinding.btnAddLabel.setOnClickListener {
            val intent = Intent(this, ScheduleAddLabelActivity::class.java)
            intent.putExtra(
                "labelList",
                JSON.toJSONString(scheduleEditViewModel.labelListLiveData.value)
            )
            labelLauncher.launch(intent)
        }
        //新增日程
        scheduleFullEditionBinding.btnCommit.setOnClickListener {
            val title = scheduleFullEditionBinding.etScheduleTitle.text.toString()
            if (TextUtils.isEmpty(title)){
                ToastUtils.showShort("需要输入日程名称")
                return@setOnClickListener
            }

            if (DialogUtil.isHasEmoji(title)){
                ToastUtils.showShort("日程名称含有非字符的数据，请重新输入!")
                return@setOnClickListener
            }
            scheduleEditViewModel.startTimeLiveData.value = dateStart.get()
            scheduleEditViewModel.endTimeLiveData.value = dateEnd.get()
            //不支持跨天且重复日程
            val isRepeat = (scheduleEditViewModel.repetitionLiveData.value?.code?:"").toString()
            val startTime = scheduleEditViewModel.startTimeLiveData.value?.toString()?:""
            val endTime = scheduleEditViewModel.endTimeLiveData.value?.toString()?:""
            if (ScheduleRepetitionRuleUtil.moreDayAndRepetitionData(isRepeat,startTime,endTime)){
                ToastUtils.showShort("暂时不支持跨天重复日程！")
                return@setOnClickListener
            }

            val toScheduleDataBean = scheduleEditViewModel.toScheduleDataBean()
            toScheduleDataBean.repetition?.let {
                val rule = it.rule
                if (rule != null) {
                    val until = rule["until"]
                    val startTime1 = toScheduleDataBean.startTime?:""
                    //截止日期需晚于日程开始日期
                    if (until != null && !TextUtils.isEmpty(startTime1) && !TextUtils.isEmpty(until.toString())) {
                        if (ScheduleDaoUtil.toDateTime(startTime1) >= ScheduleDaoUtil.toDateTime(until.toString())) {
                            ToastUtils.showShort("截止日期需晚于日程开始日期")
                            return@setOnClickListener
                        }
                    }
                }
            }
            scheduleEditViewModel.scheduleTitleLiveData.value = title
            scheduleEditViewModel.remarkLiveData.value = scheduleFullEditionBinding.edit.text.toString()

            scheduleEditViewModel.saveOrModifySchedule(false)
        }

        scheduleFullEditionBinding.clStartTime.setOnClickListener {
            if (scheduleFullEditionBinding.llVLine.visibility == View.GONE) {
                scheduleFullEditionBinding.llVLine.visibility = View.VISIBLE
                scheduleFullEditionBinding.dateTimePicker.visibility = View.VISIBLE
            }
            scheduleFullEditionBinding.vDateTopMarkLeft.visibility = View.VISIBLE
            scheduleFullEditionBinding.vDateTopMarkRight.visibility = View.INVISIBLE
            scheduleFullEditionBinding.dateTimePicker.setDefaultMillisecond(
                DateUtils.formatTime(
                    dateStart.get(),
                    ""
                )
            )
        }

        scheduleFullEditionBinding.clEndTime.setOnClickListener {
            if (scheduleFullEditionBinding.llVLine.visibility == View.GONE) {
                scheduleFullEditionBinding.llVLine.visibility = View.VISIBLE
                scheduleFullEditionBinding.dateTimePicker.visibility = View.VISIBLE
            }
            scheduleFullEditionBinding.vDateTopMarkLeft.visibility = View.INVISIBLE
            scheduleFullEditionBinding.vDateTopMarkRight.visibility = View.VISIBLE
            scheduleFullEditionBinding.dateTimePicker.setDefaultMillisecond(
                DateUtils.formatTime(
                    dateEnd.get(),
                    ""
                )
            )
        }
        val allDay = scheduleEditViewModel.allDayLiveData.value
        val startTime = scheduleEditViewModel.startTimeLiveData.value
        val endTime = scheduleEditViewModel.endTimeLiveData.value
        if (allDay != null && allDay) {
            scheduleFullEditionBinding.checkBox.isChecked = true
            scheduleFullEditionBinding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation2)
            scheduleFullEditionBinding.tvTimeStart.visibility = View.GONE
            scheduleFullEditionBinding.tvTimeEnd.visibility = View.GONE
        }

        scheduleFullEditionBinding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            //设置是否全天
            scheduleEditViewModel.allDayLiveData.value = isChecked
            if (isChecked) {
                scheduleFullEditionBinding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation2)
                scheduleFullEditionBinding.tvTimeStart.visibility = View.GONE
                scheduleFullEditionBinding.tvTimeEnd.visibility = View.GONE
                //修改提醒类型 全天
                val optionalRemind =
                    list2.stream().filter(Remind::checked).findFirst()
                if (optionalRemind.isPresent) {
                    val remind = optionalRemind.get()
                    scheduleEditViewModel.remindLiveData.value = remind
                    scheduleFullEditionBinding.tvRemind.text = remind.title
                } else {
                    scheduleEditViewModel.remindLiveData.value = Remind.getNotRemind()
                    scheduleFullEditionBinding.tvRemind.text = "不提醒"
                }
            } else {
                scheduleFullEditionBinding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation)
                scheduleFullEditionBinding.tvTimeStart.visibility = View.VISIBLE
                scheduleFullEditionBinding.tvTimeEnd.visibility = View.VISIBLE
                //修改提醒类型 非全天
                val optionalRemind =
                    list.stream().filter(Remind::checked).findFirst()
                if (optionalRemind.isPresent) {
                    val remind = optionalRemind.get()
                    scheduleEditViewModel.remindLiveData.value = remind
                    scheduleFullEditionBinding.tvRemind.text = remind.title
                } else {
                    scheduleEditViewModel.remindLiveData.value = Remind.getNotRemind()
                    scheduleFullEditionBinding.tvRemind.text = "不重复"
                }
            }
        }

        scheduleFullEditionBinding.dateTimePicker.setOnDateTimeChangedListener { aLong ->
            val date = DateUtils.getDate(aLong)
            val time = DateUtils.formatTime(date, "", "", true)
            loge("showEditScheduleDialog: $aLong,date=$date, time=$time")
            if (scheduleFullEditionBinding.vDateTopMarkLeft.visibility == View.VISIBLE) {
                //左边选中设置左边的时间数据
                scheduleFullEditionBinding.tvDateStart.text = time
                if (!scheduleFullEditionBinding.checkBox.isChecked) {
                    scheduleFullEditionBinding.tvTimeStart.text =
                        DateUtils.formatTime(date, "", "HH:mm")
                }
                dateStart.set(date)
            } else if (scheduleFullEditionBinding.vDateTopMarkRight.visibility == View.VISIBLE) {
                //右边被选中设置右边的时间数据
                scheduleFullEditionBinding.tvDateEnd.text = time
                if (!scheduleFullEditionBinding.checkBox.isChecked) {
                    scheduleFullEditionBinding.tvTimeEnd.text =
                        DateUtils.formatTime(date, "", "HH:mm")
                }
                dateEnd.set(date)
            } else {
                //第一次设置两边的数据
                if (TextUtils.isEmpty(startTime)) {
                    scheduleFullEditionBinding.tvDateStart.text = time
                    if (!scheduleFullEditionBinding.checkBox.isChecked) {
                        scheduleFullEditionBinding.tvTimeStart.text =
                            DateUtils.formatTime(date, "", "HH:mm")
                    }
                    dateStart.set(date)
                } else {
                    scheduleFullEditionBinding.tvDateStart.text =
                        DateUtils.formatTime(startTime, "", "", true)
                    if (!scheduleFullEditionBinding.checkBox.isChecked) {
                        scheduleFullEditionBinding.tvTimeStart.text =
                            DateUtils.formatTime(startTime, "", "HH:mm")
                    }
                    dateStart.set(startTime)
                }
                if (TextUtils.isEmpty(endTime)) {
                    scheduleFullEditionBinding.tvDateEnd.text = time
                    if (!scheduleFullEditionBinding.checkBox.isChecked) {
                        scheduleFullEditionBinding.tvTimeEnd.text =
                            DateUtils.formatTime(date, "", "HH:mm")
                    }
                    dateEnd.set(date)
                } else {
                    scheduleFullEditionBinding.tvDateEnd.text =
                        DateUtils.formatTime(endTime, "", "", true)
                    if (!scheduleFullEditionBinding.checkBox.isChecked) {
                        scheduleFullEditionBinding.tvTimeEnd.text =
                            DateUtils.formatTime(endTime, "", "HH:mm")
                    }
                    dateEnd.set(endTime)
                }
            }
            null
        }

        scheduleEditViewModel.remindLiveData.value?.let {
            scheduleFullEditionBinding.tvRemind.text = it.title
        }

        scheduleEditViewModel.repetitionLiveData.value?.let {
            scheduleFullEditionBinding.tvRepetition.text = it.title
            if (it.code == 8) {
                val rule = ScheduleRepetitionRuleUtil.ruleToString(JSON.toJSONString(it.rule))
                scheduleFullEditionBinding.tvRepetition.text = "重复"
                scheduleFullEditionBinding.tvCustomRule.text = rule
            }
        }
        //日程修改监听
        scheduleEditViewModel.saveOrModifyResult.observe(this, {
            if (it){
                val intent = intent.putExtra("saveOrModifyResult",true)
                setResult(RESULT_OK,intent)
                finish()
            }

        })
        //限制输入框字数
        scheduleFullEditionBinding.edit.filters = arrayOf<InputFilter>(MaxTextLengthFilter(200))
        scheduleFullEditionBinding.etScheduleTitle.filters = arrayOf<InputFilter>(MaxTextLengthFilter(20))
    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_full_edition

    val adapter = object :
        BaseQuickAdapter<LabelListRsp.DataBean, BaseViewHolder>(R.layout.item_schedule_label_flow_list) {
        override fun convert(holder: BaseViewHolder, item: LabelListRsp.DataBean) {
            holder.getView<ImageView>(R.id.iv_del).visibility =
                if (promoter) View.VISIBLE else View.INVISIBLE
            val drawable = GradientDrawable()
            drawable.cornerRadius =
                DisplayUtils.dip2px(this@ScheduleFullEditionActivity, 2f).toFloat()
            drawable.setColor(ColorUtil.parseColor(item.colorValue))
            holder.getView<TextView>(R.id.tv_label).background = drawable
            holder.setText(R.id.tv_label, item.labelName)
            holder.itemView.setOnClickListener {
                loge("item=$item")
                if (!promoter) {
                    loge("日程便签只有发起人才能删除！")
                    return@setOnClickListener
                }
                remove(item)
                labelList.remove(item)
                notifyDataSetChanged()
            }
        }
    }
}