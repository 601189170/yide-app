package com.yyide.chatim.activity.schedule

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleAddressBinding
import com.yyide.chatim.databinding.ActivityScheduleParticipantBinding
import com.yyide.chatim.model.schedule.SiteNameRsp
import com.yyide.chatim.viewmodel.SiteManageViewModel

/**
 * 选择参与人
 */
class ScheduleParticipantActivity : BaseActivity() {
    lateinit var scheduleParticipantBinding: ActivityScheduleParticipantBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleParticipantBinding = ActivityScheduleParticipantBinding.inflate(layoutInflater)
        setContentView(scheduleParticipantBinding.root)
        initView()
        initData()
    }

    private fun initData() {

    }

    private fun initView() {
        scheduleParticipantBinding.top.title.text = "添加参与人"
        scheduleParticipantBinding.top.backLayout.setOnClickListener {
            finish()
        }
        scheduleParticipantBinding.top.tvRight.visibility = View.VISIBLE
        scheduleParticipantBinding.top.tvRight.text = "确定"
        scheduleParticipantBinding.top.tvRight.setTextColor(resources.getColor(R.color.colorPrimary))
        scheduleParticipantBinding.top.tvRight.setOnClickListener {
            finish()
        }

    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_participant
}