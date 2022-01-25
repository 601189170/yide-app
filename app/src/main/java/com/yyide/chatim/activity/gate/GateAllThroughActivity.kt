package com.yyide.chatim.activity.gate

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.adapter.gate.GateThroughData
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityGateAllThroughBinding
import com.yyide.chatim.fragment.gate.PeopleThroughListFragment
import com.yyide.chatim.model.gate.GateThroughPeopleListBean
import com.yyide.chatim.model.gate.Result
import com.yyide.chatim.utils.loge
import com.yyide.chatim.viewmodel.gate.GateThroughPeopleListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.sf.saxon.functions.Collection
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
    private lateinit var siteId: String
    private lateinit var queryTime: String
    val mTitles: MutableList<String> = ArrayList()
    val fragments = mutableListOf<Fragment>()
    private val gateThroughPeopleListViewModel: GateThroughPeopleListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityGateAllThroughBinding = ActivityGateAllThroughBinding.inflate(layoutInflater)
        setContentView(activityGateAllThroughBinding.root)
        type = intent.getIntExtra("type", 1)
        siteId = intent.getStringExtra("siteId") ?: ""
        queryTime = intent.getStringExtra("queryTime") ?: ""
        initView()
        lifecycleScope.launch {
            gateThroughPeopleListViewModel.gateThroughPeopleList.collect {
                when (it) {
                    is Result.Success -> {
                        handleData(it.data)
                    }
                    is Result.Error -> {
                        loge("${it.exception?.localizedMessage}")
                        ToastUtils.showShort("${it.exception?.localizedMessage}")
                    }
                }
            }
        }
        if (type == 1) {
            gateThroughPeopleListViewModel.queryAllStudentPassageInOutDetails(
                type,
                queryTime,
                1,
                null,
                siteId
            )
        } else if (type == 2) {
            gateThroughPeopleListViewModel.queryAllTeacherPassageInOutDetails(queryTime, siteId)
        }

    }

    private fun handleData(data: GateThroughPeopleListBean?) {
        if (data == null) {
            activityGateAllThroughBinding.blankPage.visibility = View.VISIBLE
            return
        }
        activityGateAllThroughBinding.blankPage.visibility = View.GONE
        val layoutGateThroughSummaryAll = activityGateAllThroughBinding.layoutGateThroughSummaryAll
        activityGateAllThroughBinding.top.title.text = data.title
        layoutGateThroughSummaryAll.tvGateEventTitle.text = data.title
        layoutGateThroughSummaryAll.tvThroughNumber.text = "${data.totalNumber}"
        layoutGateThroughSummaryAll.tvGoIntoNumber.text = "${data.intoNumber}"
        layoutGateThroughSummaryAll.tvGoOutNumber.text = "${data.outNumber}"
        //1出2入
        mTitles.add("通行人数(${data.totalNumber})")
        mTitles.add("出校(${data.outNumber})")
        mTitles.add("入校(${data.intoNumber})")

        val dataList = mutableListOf<GateThroughPeopleListBean.PeopleListBean>()
        val dataList2 = mutableListOf<GateThroughPeopleListBean.PeopleListBean>()
        val dataList3 = mutableListOf<GateThroughPeopleListBean.PeopleListBean>()
        //全部
        data.peopleList?.forEach {
            val bean = it.copy()
            bean.type = 2
            if (type == 2){
                bean.className = bean.deptName
            }
            dataList3.add(bean)
        }

        //出校
        data.peopleList?.filter { it.inOut == "1" }?.forEach {
            val bean = it.copy()
            bean.type = 4
            if (type == 2){
                bean.className = bean.deptName
            }
            dataList.add(bean)
        }
        //入校
        data.peopleList?.filter { it.inOut == "2" }?.forEach {
            val bean = it.copy()
            bean.type = 4
            if (type == 2){
                bean.className = bean.deptName
            }
            dataList2.add(bean)
        }

        //1出校 2入校  3通行人数
        fragments.add(PeopleThroughListFragment("3", "2", "$type", queryTime, siteId, dataList3))
        fragments.add(PeopleThroughListFragment("1", "2", "$type", queryTime, siteId, dataList))
        fragments.add(PeopleThroughListFragment("2", "2", "$type", queryTime, siteId, dataList2))
        activityGateAllThroughBinding.viewpager.offscreenPageLimit = 3
        activityGateAllThroughBinding.viewpager.adapter = object :
            FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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

    private fun initView() {
//        if (type == 2) {
//            activityGateAllThroughBinding.top.title.text = "全校教职工出入校门详情"
//            activityGateAllThroughBinding.layoutGateThroughSummaryAll.tvGateEventTitle.text =
//                "全校教职工出入校门详情"
//        } else {
//            activityGateAllThroughBinding.top.title.text = "全校学生出入校门详情"
//            activityGateAllThroughBinding.layoutGateThroughSummaryAll.tvGateEventTitle.text =
//                "全校学生出入校门详情"
//        }
        activityGateAllThroughBinding.top.backLayout.setOnClickListener {
            finish()
        }
    }
}