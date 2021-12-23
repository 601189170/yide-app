package com.yyide.chatim.activity.gate

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.yyide.chatim.adapter.gate.GateBranchData
import com.yyide.chatim.adapter.gate.GateStudentStaffBranchAdapter
import com.yyide.chatim.adapter.gate.GateThroughData
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityGateSecondBranchBinding
import com.yyide.chatim.databinding.ActivityGateThirdBranchBinding
import com.yyide.chatim.fragment.gate.PeopleThroughListFragment
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.view.SpaceItemDecoration
import java.util.ArrayList

/**
 *
 * @author liu tao
 * @date 2021/12/23 18:19
 * @description 第二级部门（最终通行记录列表）
 *              type = 1 学生
 *              type = 2 教职工
 */
class GateThirdBranchActivity : BaseActivity() {
    private lateinit var activityGateThirdBranchBinding: ActivityGateThirdBranchBinding
    private var title: String? = ""
    val mTitles: MutableList<String> = ArrayList()
    val fragments = mutableListOf<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityGateThirdBranchBinding = ActivityGateThirdBranchBinding.inflate(layoutInflater)
        setContentView(activityGateThirdBranchBinding.root)
        title = intent.getStringExtra("title")
        initView()
    }

    private fun initView() {
        activityGateThirdBranchBinding.top.title.text = title
        activityGateThirdBranchBinding.top.backLayout.setOnClickListener {
            finish()
        }
        mTitles.add("出校(40)")
        mTitles.add("入校(50)")
        mTitles.add("通行人数(90)")
        val dataList = mutableListOf<GateThroughData>()
        for (index in 0..5) {
            dataList.add(GateThroughData(3, "张三$index", "2021/12/03 08:00", "", "小北门"))
        }

        val dataList2 = mutableListOf<GateThroughData>()
        for (index in 0..7) {
            dataList2.add(GateThroughData(3, "张三$index", "2021/12/03 08:00", "", "小北门"))
        }

        val dataList3 = mutableListOf<GateThroughData>()
        for (index in 0..12) {
            dataList3.add(GateThroughData(2, "张三$index", "2021/12/03 08:00", "一年级${index}班", "小北门"))
        }

        fragments.add(PeopleThroughListFragment("1", "1", dataList))
        fragments.add(PeopleThroughListFragment("2", "1", dataList2))
        fragments.add(PeopleThroughListFragment("3", "1", dataList3))

        activityGateThirdBranchBinding.viewpager.offscreenPageLimit = 3
        activityGateThirdBranchBinding.viewpager.adapter = object :
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
        activityGateThirdBranchBinding.slidingTabLayout.setViewPager(activityGateThirdBranchBinding.viewpager)
        activityGateThirdBranchBinding.slidingTabLayout.currentTab = 0
    }
}