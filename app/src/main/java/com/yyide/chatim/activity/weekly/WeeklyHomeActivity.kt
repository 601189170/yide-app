package com.yyide.chatim.activity.weekly

import android.os.Bundle
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.weekly.home.ParentsWeeklyFragment
import com.yyide.chatim.activity.weekly.home.SchoolWeeklyFragment
import com.yyide.chatim.activity.weekly.home.TeacherWeeklyFragment
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
        when {
            SpData.getIdentityInfo().isSchool -> {
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    SchoolWeeklyFragment.newInstance()
                ).commit()
            }
            SpData.getIdentityInfo().isParent -> {
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    ParentsWeeklyFragment.newInstance()
                ).commit()
            }
            SpData.getIdentityInfo().isTeacherOrCharge -> {
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    TeacherWeeklyFragment.newInstance()
                ).commit()
            }
        }
    }
}