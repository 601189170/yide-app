package com.yyide.chatim_pro.activity.weekly

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.base.BaseActivity
import com.yyide.chatim_pro.databinding.ActivityWeeklyHomeBinding

/**
 * 周报首页
 */
class WeeklyHomeActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityWeeklyHomeBinding

    companion object {
        fun jump(context: Context, startTime: String, endTime: String) {
            val intent = Intent(context, WeeklyHomeActivity::class.java)
            intent.putExtra("startTime", startTime)
            intent.putExtra("endTime", endTime)
            context.startActivity(intent)
        }
    }

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
//            SpData.getIdentityInfo().isSchool -> {
//                supportFragmentManager.beginTransaction().replace(
//                    viewBinding.flContent.id,
//                    SchoolWeeklyFragment.newInstance()
//                ).commit()
//            }
//            SpData.getIdentityInfo().isParent -> {
//                supportFragmentManager.beginTransaction().replace(
//                    viewBinding.flContent.id,
//                    ParentsWeeklyFragment.newInstance()
//                ).commit()
//            }
//            SpData.getIdentityInfo().isTeacherOrCharge -> {
//                supportFragmentManager.beginTransaction().replace(
//                    viewBinding.flContent.id,
//                    TeacherWeeklyFragment.newInstance()
//                ).commit()
//            }
        }
    }
}