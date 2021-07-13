package com.yyide.chatim.activity

import android.os.Bundle
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityAgreementBinding
import com.yyide.chatim.databinding.ActivityPrivacyBinding

class AgreementActivity : BaseActivity() {

    override fun getContentViewID(): Int {
        return R.layout.activity_agreement
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val privacyBinding = ActivityAgreementBinding.inflate(layoutInflater)
        setContentView(privacyBinding.root)
        privacyBinding.top.title.text = getString(R.string.agreement_title)
        privacyBinding.top.backLayout.setOnClickListener { finish() }
    }
}
