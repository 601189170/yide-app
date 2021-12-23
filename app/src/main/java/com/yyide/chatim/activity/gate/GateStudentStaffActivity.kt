package com.yyide.chatim.activity.gate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityGateStudentStaffBinding
import com.yyide.chatim.fragment.gate.GateStudentStaffFragment
import java.util.ArrayList

/**
 *
 * @author liu tao
 * @date 2021/12/23 14:43
 * @description 闸机通行数据首页 学生+教职工
 */
class GateStudentStaffActivity : BaseActivity() {
    private lateinit var activityGateStudentStaffBinding: ActivityGateStudentStaffBinding
    val mTitles: MutableList<String> = ArrayList()
    val fragments = mutableListOf<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityGateStudentStaffBinding = ActivityGateStudentStaffBinding.inflate(layoutInflater)
        setContentView(activityGateStudentStaffBinding.root)
        initView()
    }

    private fun initView() {
        activityGateStudentStaffBinding.top.title.text = "校门口通行数据"
        activityGateStudentStaffBinding.top.backLayout.setOnClickListener {
            finish()
        }
        activityGateStudentStaffBinding.top.ivRight.visibility = View.VISIBLE
        activityGateStudentStaffBinding.top.ivRight.setImageResource(R.drawable.gate_search_icon)
        activityGateStudentStaffBinding.top.ivRight.setOnClickListener {
            //搜索入口
        }

        mTitles.add("学生")
        mTitles.add("教职工")
        fragments.add(GateStudentStaffFragment(1))
        fragments.add(GateStudentStaffFragment(2))
        activityGateStudentStaffBinding.viewpager.offscreenPageLimit = 2
        activityGateStudentStaffBinding.viewpager.adapter = object :
            FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getCount(): Int {
                return mTitles.size
            }

            override fun getPageTitle(position: Int): CharSequence {
                return mTitles[position]
            }
        }
        activityGateStudentStaffBinding.slidingTabLayout.setViewPager(
            activityGateStudentStaffBinding.viewpager
        )
        activityGateStudentStaffBinding.slidingTabLayout.currentTab = 0

    }
}