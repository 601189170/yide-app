package com.yyide.chatim.activity.weekly

import android.os.Bundle
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.weekly.home.SchoolWeeklyFragment
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityWeeklyDetailsBinding

/**
 * 周报详情页
 */
class WeeklyDetailsActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityWeeklyDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityWeeklyDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_weekly_details
    }

    private fun initView() {
        viewBinding.back.setOnClickListener { finish() }
        when {
            SpData.getIdentityInfo().isSchool -> {
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    SchoolWeeklyFragment.newInstance()
                ).commit()
            }
        }
    }
}