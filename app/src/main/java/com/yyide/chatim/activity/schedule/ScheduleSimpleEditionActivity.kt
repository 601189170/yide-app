package com.yyide.chatim.activity.schedule

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.compareDate
import com.yyide.chatim.databinding.ActivityScheduleSimpleEditionBinding
import com.yyide.chatim.model.schedule.Remind
import com.yyide.chatim.model.schedule.Remind.Companion.getList
import com.yyide.chatim.model.schedule.Remind.Companion.getList2
import com.yyide.chatim.model.schedule.Remind.Companion.getNotRemind
import com.yyide.chatim.model.schedule.Repetition
import com.yyide.chatim.model.schedule.ScheduleDataBean
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.viewmodel.ScheduleEditViewModel
import com.yyide.chatim.viewmodel.toScheduleDataBean
import com.yyide.chatim.viewmodel.toScheduleEditViewModel
import java.util.concurrent.atomic.AtomicReference

class ScheduleSimpleEditionActivity : BaseActivity() {
    lateinit var scheduleSimpleEditionBinding: ActivityScheduleSimpleEditionBinding
    var dateStart = AtomicReference("")
    var dateEnd = AtomicReference("")
    private val scheduleEditViewModel: ScheduleEditViewModel by viewModels()
    val list = getList()
    val list2 = getList2()
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleSimpleEditionBinding = ActivityScheduleSimpleEditionBinding.inflate(layoutInflater)
        setContentView(scheduleSimpleEditionBinding.root)
        initView()
    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_simple_edition
    private val remindLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        //选择提醒
        if (it.resultCode == RESULT_OK && it.data != null) {
            val stringExtra = it.data?.getStringExtra("data")
            val remind = JSON.parseObject(stringExtra, Remind::class.java)
            loge("id=${remind.id},name=${remind.title}")
            scheduleSimpleEditionBinding.tvRemind.text = remind.title
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

    private val repetitionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        //选择重复
        if (it.resultCode == RESULT_OK && it.data != null) {
            val stringExtra = it.data?.getStringExtra("data")
            val repetition = JSON.parseObject(stringExtra, Repetition::class.java)
            loge("title=${repetition.title},rule=${repetition.rule}")
            if (repetition.code == 8) {
                scheduleSimpleEditionBinding.tvRepetition.text =
                    ScheduleRepetitionRuleUtil.ruleToString(JSON.toJSONString(repetition.rule))
            } else {
                scheduleSimpleEditionBinding.tvRepetition.text = repetition.title
            }
            scheduleEditViewModel.repetitionLiveData.value = repetition
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initView() {
        val stringExtra = intent.getStringExtra("data")
        val scheduleDataBean = JSON.parseObject(stringExtra, ScheduleDataBean::class.java)
        scheduleDataBean?.toScheduleEditViewModel(scheduleEditViewModel)

        scheduleSimpleEditionBinding.top.title.text = "自定义时间"
        scheduleSimpleEditionBinding.top.backLayout.setOnClickListener {
            finish()
        }

        if (scheduleEditViewModel.remindLiveData.value == null) {
            scheduleSimpleEditionBinding.tvRemind.text = "不提醒"
            scheduleEditViewModel.remindLiveData.value = getNotRemind()
        }

        scheduleSimpleEditionBinding.clRemind.setOnClickListener {
            val intent = Intent(this, ScheduleRemindActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(scheduleEditViewModel.remindLiveData.value))
            intent.putExtra("allDay", scheduleEditViewModel.allDayLiveData.value)
            remindLauncher.launch(intent)
        }

        scheduleSimpleEditionBinding.clRepetition.setOnClickListener {
            val intent = Intent(this, ScheduleRepetitionActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(scheduleEditViewModel.repetitionLiveData.value))
            val startTime = scheduleEditViewModel.startTimeLiveData.value
            intent.putExtra("startTime",startTime)
            repetitionLauncher.launch(intent)
        }

        scheduleSimpleEditionBinding.clStartTime.setOnClickListener {
            if (scheduleSimpleEditionBinding.llVLine.visibility == View.GONE) {
                scheduleSimpleEditionBinding.llVLine.visibility = View.VISIBLE
                scheduleSimpleEditionBinding.dateTimePicker.visibility = View.VISIBLE
            }
            scheduleSimpleEditionBinding.vDateTopMarkLeft.visibility = View.VISIBLE
            scheduleSimpleEditionBinding.vDateTopMarkRight.visibility = View.INVISIBLE
            scheduleSimpleEditionBinding.dateTimePicker.setDefaultMillisecond(
                DateUtils.formatTime(
                    dateStart.get(),
                    ""
                )
            )
        }
        scheduleSimpleEditionBinding.clEndTime.setOnClickListener {
            if (scheduleSimpleEditionBinding.llVLine.visibility == View.GONE) {
                scheduleSimpleEditionBinding.llVLine.visibility = View.VISIBLE
                scheduleSimpleEditionBinding.dateTimePicker.visibility = View.VISIBLE
            }
            scheduleSimpleEditionBinding.vDateTopMarkLeft.visibility = View.INVISIBLE
            scheduleSimpleEditionBinding.vDateTopMarkRight.visibility = View.VISIBLE
            scheduleSimpleEditionBinding.dateTimePicker.setDefaultMillisecond(
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
            scheduleSimpleEditionBinding.checkBox.setChecked(true)
            scheduleSimpleEditionBinding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation2)
            scheduleSimpleEditionBinding.tvTimeStart.visibility = View.GONE
            scheduleSimpleEditionBinding.tvTimeEnd.visibility = View.GONE
        }

        scheduleSimpleEditionBinding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            //设置是否全天
            scheduleEditViewModel.allDayLiveData.value = isChecked
            if (isChecked) {
                scheduleSimpleEditionBinding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation2)
                scheduleSimpleEditionBinding.tvTimeStart.visibility = View.GONE
                scheduleSimpleEditionBinding.tvTimeEnd.visibility = View.GONE
                //修改提醒类型 全天
                val optionalRemind =
                    list2.stream().filter(Remind::checked).findFirst()
                if (optionalRemind.isPresent) {
                    val remind = optionalRemind.get()
                    scheduleEditViewModel.remindLiveData.value = remind
                    scheduleSimpleEditionBinding.tvRemind.text = remind.title
                } else {
                    scheduleEditViewModel.remindLiveData.value = getNotRemind()
                    scheduleSimpleEditionBinding.tvRemind.text = "不提醒"
                }
            } else {
                scheduleSimpleEditionBinding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation)
                scheduleSimpleEditionBinding.tvTimeStart.visibility = View.VISIBLE
                scheduleSimpleEditionBinding.tvTimeEnd.visibility = View.VISIBLE
                //修改提醒类型 非全天
                val optionalRemind =
                    list.stream().filter(Remind::checked).findFirst()
                if (optionalRemind.isPresent) {
                    val remind = optionalRemind.get()
                    scheduleEditViewModel.remindLiveData.value = remind
                    scheduleSimpleEditionBinding.tvRemind.text = remind.title
                } else {
                    scheduleEditViewModel.remindLiveData.value = getNotRemind()
                    scheduleSimpleEditionBinding.tvRemind.text = "不提醒"
                }
            }
        }

        scheduleSimpleEditionBinding.dateTimePicker.setOnDateTimeChangedListener { aLong ->
            val date = DateUtils.getDate(aLong)
            val time = DateUtils.formatTime(date, "", "", true)
            loge("showEditScheduleDialog: $aLong,date=$date, time=$time")
            if (scheduleSimpleEditionBinding.vDateTopMarkLeft.visibility == View.VISIBLE) {
                //左边选中设置左边的时间数据
                scheduleSimpleEditionBinding.tvDateStart.text = time
                if (!scheduleSimpleEditionBinding.checkBox.isChecked) {
                    scheduleSimpleEditionBinding.tvTimeStart.text = DateUtils.formatTime(date, "", "HH:mm")
                }
                dateStart.set(date)
            } else if (scheduleSimpleEditionBinding.vDateTopMarkRight.visibility == View.VISIBLE) {
                //右边被选中设置右边的时间数据
                scheduleSimpleEditionBinding.tvDateEnd.text = time
                if (!scheduleSimpleEditionBinding.checkBox.isChecked) {
                    scheduleSimpleEditionBinding.tvTimeEnd.text = DateUtils.formatTime(date, "", "HH:mm")
                }
                dateEnd.set(date)
            } else {
                //第一次设置两边的数据
                if (TextUtils.isEmpty(startTime)) {
                    scheduleSimpleEditionBinding.tvDateStart.text = time
                    if (!scheduleSimpleEditionBinding.checkBox.isChecked) {
                        scheduleSimpleEditionBinding.tvTimeStart.text = DateUtils.formatTime(date, "", "HH:mm")
                    }
                    dateStart.set(date)
                } else {
                    scheduleSimpleEditionBinding.tvDateStart.text = DateUtils.formatTime(startTime, "", "", true)
                    if (!scheduleSimpleEditionBinding.checkBox.isChecked) {
                        scheduleSimpleEditionBinding.tvTimeStart.text = DateUtils.formatTime(startTime, "", "HH:mm")
                    }
                    dateStart.set(startTime)
                }
                if (TextUtils.isEmpty(endTime)) {
                    scheduleSimpleEditionBinding.tvDateEnd.text = time
                    if (!scheduleSimpleEditionBinding.checkBox.isChecked) {
                        scheduleSimpleEditionBinding.tvTimeEnd.text = DateUtils.formatTime(date, "", "HH:mm")
                    }
                    dateEnd.set(date)
                } else {
                    scheduleSimpleEditionBinding.tvDateEnd.text = DateUtils.formatTime(endTime, "", "", true)
                    if (!scheduleSimpleEditionBinding.checkBox.isChecked) {
                        scheduleSimpleEditionBinding.tvTimeEnd.text = DateUtils.formatTime(endTime, "", "HH:mm")
                    }
                    dateEnd.set(endTime)
                }
            }
            null
        }
        scheduleEditViewModel.remindLiveData.value?.let {
            scheduleSimpleEditionBinding.tvRemind.text = it.title
        }

        scheduleEditViewModel.repetitionLiveData.value?.let {
            scheduleSimpleEditionBinding.tvRepetition.text = it.title
            if (it.code == 8){
                val rule = ScheduleRepetitionRuleUtil.ruleToString(JSON.toJSONString(it.rule))
                scheduleSimpleEditionBinding.tvRepetition.text = "重复"
                scheduleSimpleEditionBinding.tvCustomRule.text = rule
            }
        }
        //完成选择时间 重复 提醒
        scheduleSimpleEditionBinding.top.tvRight.setOnClickListener { v ->
            val compareDate = compareDate(dateStart.get(), dateEnd.get())
            if (compareDate >= 0) {
                ToastUtils.showShort("开始时间不能大于或等于结束时间")
                return@setOnClickListener
            }
            scheduleEditViewModel.startTimeLiveData.value = dateStart.get()
            scheduleEditViewModel.endTimeLiveData.value = dateEnd.get()
            scheduleEditViewModel.allDayLiveData.value = scheduleSimpleEditionBinding.checkBox.isChecked
            //不支持跨天且重复日程
            val isRepeat = (scheduleEditViewModel.repetitionLiveData.value?.code?:"").toString()
            if (ScheduleRepetitionRuleUtil.moreDayAndRepetitionData(isRepeat,dateStart.get(),dateEnd.get())){
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

            val intent = intent.putExtra("data",JSON.toJSONString(toScheduleDataBean))
            setResult(RESULT_OK,intent)
            finish()
        }
    }

}