package com.yyide.chatim.activity.schedule

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.alibaba.fastjson.JSON
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleTimetableBinding
import com.yyide.chatim.model.schedule.Repetition
import com.yyide.chatim.model.schedule.ScheduleData

class ScheduleTimetableClassActivity : BaseActivity() {
    lateinit var scheduleTimetableBinding: ActivityScheduleTimetableBinding
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

        }

        scheduleTimetableBinding.top.title.text = "课表编辑"
        scheduleTimetableBinding.top.backLayout.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun jump(context: Context, scheduleData: ScheduleData) {
            val intent = Intent(context, ScheduleTimetableClassActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(scheduleData))
            context.startActivity(intent)
        }
    }
}