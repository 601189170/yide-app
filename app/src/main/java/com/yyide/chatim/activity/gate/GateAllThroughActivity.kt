package com.yyide.chatim.activity.gate

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.yyide.chatim.adapter.gate.GateThroughData
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityGateAllThroughBinding
import com.yyide.chatim.fragment.gate.PeopleThroughListFragment
import java.util.ArrayList

/**
 *
 * @author liu tao
 * @date 2021/12/23 17:31
 * @description 全校学生(教职工)出入校门口情况
 *              type = 1 学生
 *              type = 2 教职工
 */
class GateAllThroughActivity : BaseActivity() {
    private lateinit var activityGateAllThroughBinding: ActivityGateAllThroughBinding
    private var type = 1
    val mTitles: MutableList<String> = ArrayList()
    val fragments = mutableListOf<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityGateAllThroughBinding = ActivityGateAllThroughBinding.inflate(layoutInflater)
        setContentView(activityGateAllThroughBinding.root)
        type = intent.getIntExtra("type", 1)
        initView()
    }

    private fun initView() {
        if (type == 2) {
            activityGateAllThroughBinding.top.title.text = "全校教职工出入校门详情"
            activityGateAllThroughBinding.layoutGateThroughSummaryAll.tvGateEventTitle.text = "全校教职工出入校门详情"
        } else {
            activityGateAllThroughBinding.top.title.text = "全校学生出入校门详情"
            activityGateAllThroughBinding.layoutGateThroughSummaryAll.tvGateEventTitle.text = "全校学生出入校门详情"
        }
        activityGateAllThroughBinding.top.backLayout.setOnClickListener {
            finish()
        }

        mTitles.add("出校(40)")
        mTitles.add("入校(50)")
        mTitles.add("通行人数(90)")
        val dataList = mutableListOf<GateThroughData>()
        for (index in 0 .. 5){
            dataList.add(GateThroughData(4,"张三$index","2021/12/03 08:00","一年级${index}班","小北门"))
        }

        val dataList2 = mutableListOf<GateThroughData>()
        for (index in 0 .. 7){
            dataList2.add(GateThroughData(4,"张三$index","2021/12/03 08:00","一年级${index}班","小北门"))
        }

        val dataList3 = mutableListOf<GateThroughData>()
        for (index in 0 .. 12){
            dataList3.add(GateThroughData(2,"张三$index","2021/12/03 08:00","一年级${index}班","小北门"))
        }
        fragments.add(PeopleThroughListFragment("1","2",dataList))
        fragments .add(PeopleThroughListFragment("2","2",dataList2))
        fragments.add(PeopleThroughListFragment("3","2",dataList3))
        activityGateAllThroughBinding.viewpager.offscreenPageLimit = 3
        activityGateAllThroughBinding.viewpager.adapter = object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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
        activityGateAllThroughBinding.slidingTabLayout.setViewPager(activityGateAllThroughBinding.viewpager)
        activityGateAllThroughBinding.slidingTabLayout.currentTab = 0

    }
}