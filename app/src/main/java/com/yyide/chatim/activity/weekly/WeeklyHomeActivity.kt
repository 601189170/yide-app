package com.yyide.chatim.activity.weekly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityWeeklyHomeBinding

/**
 * 周报首页
 */
class WeeklyHomeActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityWeeklyHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityWeeklyHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_weekly_home
    }

    private fun initView() {
        viewBinding.top.backLayout.setOnClickListener { finish() }
        viewBinding.top.title.text = getString(R.string.weekly_home_title)
    }
}