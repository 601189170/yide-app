package com.yyide.chatim.activity.schedule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleEditBinding

class ScheduleEditActivity : BaseActivity() {
    lateinit var scheduleEditBinding: ActivityScheduleEditBinding
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
        scheduleEditBinding.top.title.text = "日程编辑"
        scheduleEditBinding.top.backLayout.setOnClickListener {
            finish()
        }

        scheduleEditBinding.btnAddLabel.setOnClickListener {
            startActivity(Intent(this, ScheduleLabelManageActivity::class.java))
        }

        scheduleEditBinding.clRemind.setOnClickListener {
            startActivity(Intent(this, ScheduleRemindActivity::class.java))
        }
        scheduleEditBinding.clRepetition.setOnClickListener {
            startActivity(Intent(this, ScheduleRepetitionActivity::class.java))
        }
        scheduleEditBinding.clAddress.setOnClickListener {
            startActivity(Intent(this, ScheduleAddressActivity::class.java))
        }
        scheduleEditBinding.clDate.setOnClickListener {
            startActivity(Intent(this, ScheduleDateIntervalActivity::class.java))
        }
    }
}