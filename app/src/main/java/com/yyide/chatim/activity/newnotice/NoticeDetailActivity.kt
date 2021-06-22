package com.yyide.chatim.activity.newnotice

import android.os.Bundle
import android.view.View
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityNoticeDetail2Binding

/**
 * desc （通知公告）我的发布通知详情
 * author lrz
 * time 2021年6月19日
 */
class NoticeDetailActivity : BaseActivity() {
    private var detailBinding: ActivityNoticeDetail2Binding? = null
    override fun getContentViewID(): Int {
        return R.layout.activity_notice_detail2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityNoticeDetail2Binding.inflate(layoutInflater)
        setContentView(detailBinding!!.root)
        initView()
    }

    private fun initView() {
        detailBinding!!.include.title.setText(R.string.notice_my_push_title)
        detailBinding!!.include.backLayout.setOnClickListener { v: View? -> finish() }
    }
}