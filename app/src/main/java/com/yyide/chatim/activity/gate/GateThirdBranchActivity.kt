package com.yyide.chatim.activity.gate

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.adapter.gate.GateBranchData
import com.yyide.chatim.adapter.gate.GateStudentStaffBranchAdapter
import com.yyide.chatim.adapter.gate.GateThroughData
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityGateSecondBranchBinding
import com.yyide.chatim.databinding.ActivityGateThirdBranchBinding
import com.yyide.chatim.fragment.gate.PeopleThroughListFragment
import com.yyide.chatim.model.gate.GateThroughPeopleListBean
import com.yyide.chatim.model.gate.Result
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.SpaceItemDecoration
import com.yyide.chatim.viewmodel.gate.GateThroughPeopleListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
    private var id: String? = ""
    private var siteId: String? = ""
    private var queryTime: String? = ""
    private var peopleType: Int? = null
    private var queryType: Int? = null
    val mTitles: MutableList<String> = ArrayList()
    val fragments = mutableListOf<Fragment>()
    private val gateThroughPeopleListViewModel: GateThroughPeopleListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityGateThirdBranchBinding = ActivityGateThirdBranchBinding.inflate(layoutInflater)
        setContentView(activityGateThirdBranchBinding.root)
        title = intent.getStringExtra("title")
        id = intent.getStringExtra("id")
        siteId = intent.getStringExtra("siteId")
        queryTime = intent.getStringExtra("queryTime") ?: ""
        peopleType = intent.getIntExtra("peopleType", 1)
        queryType = intent.getIntExtra("queryType", 4)
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
        //查询数据
        gateThroughPeopleListViewModel.queryAllStudentPassageInOutDetails(
            peopleType,
            queryTime,
            queryType,
            id,
            siteId
        )
    }

    private fun handleData(data: GateThroughPeopleListBean?) {
        if (data == null) {
            activityGateThirdBranchBinding.blankPage.visibility = View.VISIBLE
            return
        }
        activityGateThirdBranchBinding.blankPage.visibility = View.GONE
        val layoutGateThroughSummaryAll = activityGateThirdBranchBinding.layoutGateThroughBranchAll
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
            bean.type = 1
            dataList3.add(bean)
        }

        //出校
        data.peopleList?.filter { it.inOut == "1" }?.forEach {
            val bean = it.copy()
            bean.type = 3
            dataList.add(bean)
        }
        //入校
        data.peopleList?.filter { it.inOut == "2" }?.forEach {
            val bean = it.copy()
            bean.type = 3
            dataList2.add(bean)
        }

        //1出校 2入校  3通行人数
        fragments.add(PeopleThroughListFragment("3", "1", "$peopleType", "$queryTime", "$siteId", dataList3))
        fragments.add(PeopleThroughListFragment("1", "1", "$peopleType", "$queryTime", "$siteId", dataList))
        fragments.add(PeopleThroughListFragment("2", "1", "$peopleType", "$queryTime", "$siteId", dataList2))
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

    private fun initView() {
        activityGateThirdBranchBinding.top.title.text = title
        activityGateThirdBranchBinding.top.backLayout.setOnClickListener {
            finish()
        }
    }
}