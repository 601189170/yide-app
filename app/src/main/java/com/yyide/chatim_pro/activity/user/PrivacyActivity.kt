package com.yyide.chatim_pro.activity.user

import android.os.Bundle
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.base.BaseActivity
import com.yyide.chatim_pro.databinding.ActivityPrivacyBinding

class PrivacyActivity : BaseActivity() {

    override fun getContentViewID(): Int {
        return R.layout.activity_privacy
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val privacyBinding = ActivityPrivacyBinding.inflate(layoutInflater)
        setContentView(privacyBinding.root)
        privacyBinding.top.title.text = getString(R.string.privacy_title)
        privacyBinding.top.backLayout.setOnClickListener { finish() }
    }
}