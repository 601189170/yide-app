package com.yyide.chatim.activity.newnotice

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityNoticeReleaseBinding

/**
 * DESC 普通通知发布
 * AUTHOR LRZ
 * TIME 2021年6月21日
 * VERSION 1.0
 */
class NoticeReleaseActivity : BaseActivity() {
    private var releaseBinding: ActivityNoticeReleaseBinding? = null
    override fun getContentViewID(): Int {
        return R.layout.activity_notice_release
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        releaseBinding = ActivityNoticeReleaseBinding.inflate(layoutInflater)
        setContentView(releaseBinding!!.root)
        initView()
    }

    private fun initView() {
        releaseBinding!!.top.title.setText(R.string.notice_release_title)
        releaseBinding!!.top.backLayout.setOnClickListener { v: View? -> finish() }
        initListener()
    }

    private fun initListener() {
        releaseBinding!!.tvInputTitleNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val s1 = s.toString().trim { it <= ' ' }
                if (!TextUtils.isEmpty(s1)) {
                    releaseBinding!!.tvInputTitleNumber.text = getString(R.string.notice_input_title_number, s1.length)
                }
            }
        })
        releaseBinding!!.tvInputContentNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val s1 = s.toString().trim { it <= ' ' }
                if (!TextUtils.isEmpty(s1)) {
                    releaseBinding!!.tvInputContentNumber.text = getString(R.string.notice_input_content_number, s1.length)
                }
            }
        })
    }
}