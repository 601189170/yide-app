package com.yyide.chatim.activity.schedule

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleSettingsBinding
import com.yyide.chatim.model.schedule.Remind
import com.yyide.chatim.utils.loge
import com.yyide.chatim.viewmodel.SettingsViewModel

class ScheduleSettingsActivity : BaseActivity() {
    lateinit var scheduleSettingsBinding: ActivityScheduleSettingsBinding
    val settingsViewModel: SettingsViewModel by viewModels()
    val list = Remind.getList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleSettingsBinding = ActivityScheduleSettingsBinding.inflate(layoutInflater)
        setContentView(scheduleSettingsBinding.root)
        initView()
        settingsViewModel.getRequestSettingsResult().observe(this, {
            loge("日程设置请求结果：$it")
            updateSettings()
        })
    }

    private fun updateSettings() {
        loge("---updateSettings: 更新日程设置---")
        val classScheduleEnable = settingsViewModel.classScheduleEnable.value ?: false
        scheduleSettingsBinding.cbClassSchedule.isChecked = classScheduleEnable
        scheduleSettingsBinding.gpClassSchedule.visibility =
            if (classScheduleEnable) View.VISIBLE else View.GONE
        scheduleSettingsBinding.gpClassSchedule.requestLayout()
        val schoolCalendarEnable = settingsViewModel.schoolCalendarEnable.value ?: false
        scheduleSettingsBinding.cbSchoolCalendar.isChecked = schoolCalendarEnable
        scheduleSettingsBinding.gpSchoolCalendar.visibility =
            if (schoolCalendarEnable) View.VISIBLE else View.GONE
        scheduleSettingsBinding.gpSchoolCalendar.requestLayout()
        val conferenceEnable = settingsViewModel.conferenceEnable.value ?: false
        scheduleSettingsBinding.cbConference.isChecked = conferenceEnable
        scheduleSettingsBinding.gpConference.visibility =
            if (conferenceEnable) View.VISIBLE else View.GONE
        scheduleSettingsBinding.gpConference.requestLayout()

        scheduleSettingsBinding.cbShowTimeline.isChecked =
            settingsViewModel.curTimelineEnable.value ?: false
        scheduleSettingsBinding.cbShowHistory.isChecked =
            settingsViewModel.historyScheduleEnable.value ?: false
        scheduleSettingsBinding.cbClassRemind.isChecked =
            settingsViewModel.classRemindEnable.value ?: false
        scheduleSettingsBinding.cbHomeworkReleaseRemind.isChecked =
            settingsViewModel.homeworkReleaseRemindEnable.value ?: false
        scheduleSettingsBinding.cbSchoolCalendarRemind.isChecked =
            settingsViewModel.schoolCalendarRemindEnable.value ?: false
        scheduleSettingsBinding.cbConferenceRemind.isChecked =
            settingsViewModel.conferenceRemindEnable.value ?: false

        scheduleSettingsBinding.tvClassRemindType.text =
            remindTypeText(settingsViewModel.classRemindTime.value)
        scheduleSettingsBinding.tvHomeworkReleaseRemindType.text =
            remindTypeText(settingsViewModel.homeworkReleaseRemindTime.value)
        scheduleSettingsBinding.tvSchoolCalendarRemindType.text =
            remindTypeText(settingsViewModel.schoolCalendarRemindTime.value)
        scheduleSettingsBinding.tvConferenceRemindType.text =
            remindTypeText(settingsViewModel.conferenceRemindTime.value)
    }

    fun remindTypeText(remindType: String?): String {
        return list.find { it.id == remindType }?.title ?: ""
    }

    private fun initView() {
        scheduleSettingsBinding.top.title.text = "日程设置"
        scheduleSettingsBinding.top.backLayout.setOnClickListener {
            finish()
        }
        scheduleSettingsBinding.top.tvRight.visibility = View.VISIBLE
        scheduleSettingsBinding.top.tvRight.text = "完成"
        scheduleSettingsBinding.top.tvRight.setTextColor(resources.getColor(R.color.colorPrimary))
        scheduleSettingsBinding.top.tvRight.setOnClickListener {
            settingsViewModel.saveSettings()
            settingsViewModel.getModifySettingsResult().observe(this, {
                if (it) {
                    ToastUtils.showShort("设置修改成功！")
                    finish()
                } else {
                    ToastUtils.showShort("设置修改失败！")
                }
            })
        }
        scheduleSettingsBinding.cbClassSchedule.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                scheduleSettingsBinding.gpClassSchedule.visibility = View.VISIBLE
                scheduleSettingsBinding.vLine1.visibility = View.VISIBLE
            } else {
                scheduleSettingsBinding.gpClassSchedule.visibility = View.GONE
                scheduleSettingsBinding.vLine1.visibility = View.INVISIBLE
            }
            scheduleSettingsBinding.cbClassRemind.isChecked = isChecked
            scheduleSettingsBinding.cbHomeworkReleaseRemind.isChecked = isChecked
            settingsViewModel.classScheduleEnable.postValue(isChecked)
            settingsViewModel.classRemindEnable.postValue(isChecked)
            settingsViewModel.homeworkReleaseRemindEnable.postValue(isChecked)
        }
        scheduleSettingsBinding.cbSchoolCalendar.isChecked = true
        scheduleSettingsBinding.gpSchoolCalendar.visibility = View.VISIBLE
        scheduleSettingsBinding.gpSchoolCalendar.requestLayout()
        scheduleSettingsBinding.cbSchoolCalendar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                scheduleSettingsBinding.gpSchoolCalendar.visibility = View.VISIBLE
                scheduleSettingsBinding.vLine4.visibility = View.VISIBLE
            } else {
                scheduleSettingsBinding.gpSchoolCalendar.visibility = View.GONE
                scheduleSettingsBinding.vLine4.visibility = View.INVISIBLE
            }
            scheduleSettingsBinding.cbSchoolCalendarRemind.isChecked = isChecked
            settingsViewModel.schoolCalendarEnable.postValue(isChecked)
            settingsViewModel.schoolCalendarRemindEnable.postValue(isChecked)
        }

        scheduleSettingsBinding.cbConference.isChecked = true
        scheduleSettingsBinding.gpConference.visibility = View.GONE
        scheduleSettingsBinding.gpConference.requestLayout()
        scheduleSettingsBinding.cbConference.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                scheduleSettingsBinding.gpConference.visibility = View.VISIBLE
                scheduleSettingsBinding.vLine6.visibility = View.VISIBLE
            } else {
                scheduleSettingsBinding.gpConference.visibility = View.GONE
                scheduleSettingsBinding.vLine6.visibility = View.INVISIBLE
            }
            scheduleSettingsBinding.cbConferenceRemind.isChecked = isChecked
            settingsViewModel.conferenceEnable.postValue(isChecked)
            settingsViewModel.conferenceRemindEnable.postValue(isChecked)
        }
        scheduleSettingsBinding.cbClassRemind.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.classRemindEnable.postValue(isChecked)
        }
        scheduleSettingsBinding.cbHomeworkReleaseRemind.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.homeworkReleaseRemindEnable.postValue(isChecked)
        }
        scheduleSettingsBinding.cbSchoolCalendarRemind.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.schoolCalendarRemindEnable.postValue(isChecked)
        }
        scheduleSettingsBinding.cbConferenceRemind.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.conferenceRemindEnable.postValue(isChecked)
        }
        scheduleSettingsBinding.cbShowTimeline.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.curTimelineEnable.postValue(isChecked)
        }
        scheduleSettingsBinding.cbShowHistory.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.historyScheduleEnable.postValue(isChecked)
        }

        scheduleSettingsBinding.clClassRemindType.setOnClickListener {
            //上课提醒类型
            val intent = Intent(this, ScheduleRemindActivity::class.java)
            val remind =
                Remind.getList().filter { it.id == settingsViewModel.classRemindTime.value }.first()
            intent.putExtra("data", JSON.toJSONString(remind))
            startActivityForResult(intent, REQUEST_CODE_CLASS_REMIND_SELECT)
        }
        scheduleSettingsBinding.clHomeworkReleaseRemindType.setOnClickListener {
            //作业发布提醒类型
            val intent = Intent(this, ScheduleRemindActivity::class.java)
            val remind = Remind.getList()
                .filter { it.id == settingsViewModel.homeworkReleaseRemindTime.value }.first()
            intent.putExtra("data", JSON.toJSONString(remind))
            startActivityForResult(intent, REQUEST_CODE_HOMEWORK_RELEASE_REMIND_SELECT)
        }
        scheduleSettingsBinding.clSchoolCalendarRemindType.setOnClickListener {
            //校历提醒类型
            val intent = Intent(this, ScheduleRemindActivity::class.java)
            val remind = Remind.getList()
                .filter { it.id == settingsViewModel.schoolCalendarRemindTime.value }.first()
            intent.putExtra("data", JSON.toJSONString(remind))
            startActivityForResult(intent, REQUEST_CODE_SCHOOL_CALENDAR_REMIND_SELECT)
        }
        scheduleSettingsBinding.clConferenceRemindType.setOnClickListener {
            //会议提醒
            val intent = Intent(this, ScheduleRemindActivity::class.java)
            val remind =
                Remind.getList().filter { it.id == settingsViewModel.conferenceRemindTime.value }
                    .first()
            intent.putExtra("data", JSON.toJSONString(remind))
            startActivityForResult(intent, REQUEST_CODE_CONFERENCE_REMIND_SELECT)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val stringExtra = data.getStringExtra("data")
            val remind = JSON.parseObject(stringExtra, Remind::class.java)
            loge("id=${remind.id},name=${remind.title}")
            when (requestCode) {
                REQUEST_CODE_CLASS_REMIND_SELECT -> {
                    settingsViewModel.classRemindTime.postValue(remind.id)
                    scheduleSettingsBinding.tvClassRemindType.text = remind.title
                }
                REQUEST_CODE_HOMEWORK_RELEASE_REMIND_SELECT -> {
                    settingsViewModel.homeworkReleaseRemindTime.postValue(remind.id)
                    scheduleSettingsBinding.tvHomeworkReleaseRemindType.text = remind.title
                }
                REQUEST_CODE_SCHOOL_CALENDAR_REMIND_SELECT -> {
                    settingsViewModel.schoolCalendarRemindTime.postValue(remind.id)
                    scheduleSettingsBinding.tvSchoolCalendarRemindType.text = remind.title
                }
                REQUEST_CODE_CONFERENCE_REMIND_SELECT -> {
                    settingsViewModel.conferenceRemindTime.postValue(remind.id)
                    scheduleSettingsBinding.tvConferenceRemindType.text = remind.title
                }
                else -> {
                }
            }
        }
    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_settings

    companion object {
        const val REQUEST_CODE_CLASS_REMIND_SELECT = 100
        const val REQUEST_CODE_HOMEWORK_RELEASE_REMIND_SELECT = 101
        const val REQUEST_CODE_SCHOOL_CALENDAR_REMIND_SELECT = 102
        const val REQUEST_CODE_CONFERENCE_REMIND_SELECT = 103
    }
}