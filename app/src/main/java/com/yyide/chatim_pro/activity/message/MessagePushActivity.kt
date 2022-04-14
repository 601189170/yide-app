package com.yyide.chatim_pro.activity.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.yyide.chatim_pro.adapter.message.MessageFragmentPager
import com.yyide.chatim_pro.base.KTBaseActivity
import com.yyide.chatim_pro.databinding.ActivityMessagePushBinding

/**
 *
 * @author shenzhibin
 * @date 2022/4/6 15:20
 * @description dev2 信息发布界面
 */
class MessagePushActivity :
    KTBaseActivity<ActivityMessagePushBinding>(ActivityMessagePushBinding::inflate) {

    companion object {

        fun startGo(context: Context) {
            val intent = Intent(context, MessagePushActivity::class.java)
            context.startActivity(intent)
        }

        const val PAGE_NOTICE = 0
        const val PAGE_LOST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
    }

    override fun initView() {
        super.initView()

        binding.messagePushTop.title.text = "信息发布"
        binding.messagePushTop.backLayout.setOnClickListener {
            finish()
        }

        binding.messagePushVp.adapter = MessageFragmentPager(this)
        TabLayoutMediator(
            binding.messagePushTl,
            binding.messagePushVp,
            true,
            false
        ) { tab, position ->
            when (position) {
                PAGE_NOTICE -> {
                    tab.text = "通知公告"
                }
                PAGE_LOST -> {
                    tab.text = "失物招领"
                }
            }
        }.attach()
    }

    private fun initListener() {

    }

}