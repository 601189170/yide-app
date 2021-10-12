package com.yyide.chatim.activity.meeting

import android.os.Bundle
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityMeetingCreateBinding

/**
 * 创建/编辑 会议
 * author lrz
 * time 2021年10月11日17:47:47
 */
class MeetingCreateActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityMeetingCreateBinding

    override fun getContentViewID(): Int {
        return R.layout.activity_meeting_create
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMeetingCreateBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()
    }

    private fun initView() {

    }
}