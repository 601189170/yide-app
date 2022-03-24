package com.yyide.chatim.activity.schedule

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import androidx.activity.viewModels
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleTimetableBinding
import com.yyide.chatim.model.schedule.Remind
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.MaxTextLengthFilter
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.viewmodel.ScheduleMangeViewModel
import com.yyide.chatim.viewmodel.TimetableViewModel
import java.util.*

class ScheduleTimetableClassActivity : BaseActivity() {
    lateinit var scheduleTimetableBinding: ActivityScheduleTimetableBinding
    private val timetableViewModel by viewModels<TimetableViewModel>()
    private val scheduleMangeViewModel: ScheduleMangeViewModel by viewModels()
    val list = Remind.getList()
    var remind = Remind.getNotRemind()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleTimetableBinding = ActivityScheduleTimetableBinding.inflate(layoutInflater)
        setContentView(scheduleTimetableBinding.root)
        initView()
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_schedule_timetable
    }

    private fun initView() {
        val stringExtra = intent.getStringExtra("data")
        val scheduleData = JSON.parseObject(stringExtra, ScheduleData::class.java)
        scheduleData?.let {
            //显示日程详情
            val remind = list.first { remind -> remind.id == it.remindTypeInfo }
            if (remind != null){
                this.remind = remind
            }
            timetableViewModel.scheduleData.value = it
            scheduleTimetableBinding.tvScheduleTitle.text = it.name
            //scheduleTimetableBinding.tvAddress.text = it.siteName
            scheduleTimetableBinding.tvRemind.text = remind.title
            scheduleTimetableBinding.tvRemark.setText(it.remark)
            val date = DateUtils.formatTime(it.startTime, "", "", true)
            val startTime = DateUtils.formatTime(it.startTime, "", "")
            val endTime = DateUtils.formatTime(it.endTime, "", "")
            scheduleTimetableBinding.tvDate.text = String.format(getString(R.string.schedule_timetable_date),date,startTime,endTime)
        }

        scheduleTimetableBinding.top.title.text = "课表编辑"
        scheduleTimetableBinding.top.backLayout.setOnClickListener {
           finish()
        }
        scheduleTimetableBinding.btnCommit.setOnClickListener {
            timetableViewModel.scheduleData.value?.remark = scheduleTimetableBinding.tvRemark.text.toString()
            val repetition = timetableViewModel.scheduleData.value?.rrule
            if (repetition  == null) {
                timetableViewModel.modifySchedule()
                return@setOnClickListener
            }
            DialogUtil.showRepetitionScheduleModifyDialog(this) {
                loge("编辑日程重复性类型：$it")
                if (it == -1) {
                    //finish()
                    return@showRepetitionScheduleModifyDialog
                }
                //修改日程
                timetableViewModel.scheduleData.value?.updateType = "$it"
                timetableViewModel.scheduleData.value?.updateDate = DateUtils.switchTime(Date(),"yyyy-MM-dd")
                timetableViewModel.modifySchedule()
            }
        }
        scheduleTimetableBinding.clRemind.setOnClickListener {
            val intent = Intent(this, ScheduleRemindActivity::class.java)
            intent.putExtra("data", "")
            intent.putExtra("allDay", "")
            startActivityForResult(intent, ScheduleEditActivityMain.REQUEST_CODE_REMIND_SELECT)
        }

        //日程修改监听
        timetableViewModel.saveOrModifyResult.observe(this, {
            if (it) {
                scheduleMangeViewModel.getAllScheduleList()
                return@observe
            }
            ToastUtils.showShort("修改课表日程失败")
            finish()
        })
        //获取日程监听
        scheduleMangeViewModel.requestAllScheduleResult.observe(this, {
            if (it) {
                ToastUtils.showShort("修改课表日程成功")
                finish()
                return@observe
            }
            ToastUtils.showShort("修改课表日程失败")
            finish()
        })

        //限制输入框字数
        scheduleTimetableBinding.tvRemark.filters = arrayOf<InputFilter>(MaxTextLengthFilter(200))
    }

    companion object {
        fun jump(context: Context, scheduleData: ScheduleData) {
            val intent = Intent(context, ScheduleTimetableClassActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(scheduleData))
            context.startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //选择提醒
        if (requestCode == ScheduleEditActivityMain.REQUEST_CODE_REMIND_SELECT && resultCode == RESULT_OK && data != null) {
            val stringExtra = data.getStringExtra("data")
            remind = JSON.parseObject(stringExtra, Remind::class.java)
            loge("id=${remind.id},name=${remind.title}")
            scheduleTimetableBinding.tvRemind.text = remind.title
            timetableViewModel.scheduleData.value?.remindTypeInfo = remind.id
            list.forEach {
                it.checked = it.id == remind.id
            }
            return
        }
    }
}