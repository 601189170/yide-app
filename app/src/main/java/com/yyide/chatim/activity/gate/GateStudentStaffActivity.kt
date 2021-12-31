package com.yyide.chatim.activity.gate

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityGateStudentStaffBinding
import com.yyide.chatim.dialog.SwitchTableClassPop
import com.yyide.chatim.fragment.gate.GateStudentStaffFragment
import com.yyide.chatim.model.SelectTableClassesRsp
import com.yyide.chatim.model.gate.Result
import com.yyide.chatim.model.gate.SiteBean
import com.yyide.chatim.utils.loge
import com.yyide.chatim.viewmodel.gate.GateSiteViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
    private val siteViewModel: GateSiteViewModel by viewModels()
    private var siteId = ""
    private var siteData = mutableListOf<SelectTableClassesRsp.DataBean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityGateStudentStaffBinding = ActivityGateStudentStaffBinding.inflate(layoutInflater)
        setContentView(activityGateStudentStaffBinding.root)
        initView()
        //场地列表
        lifecycleScope.launch {
            siteViewModel.siteDataList.collect {
                when (it) {
                    is Result.Success -> {
                        handleSiteListData(it.data)
                    }
                    is Result.Error -> {
                        loge("${it.exception?.localizedMessage}")
                        ToastUtils.showShort("${it.exception?.localizedMessage}")
                    }
                }
            }
        }
        siteViewModel.siteList()
    }

    /**
     * 处理场地
     */
    private fun handleSiteListData(data: List<SiteBean>?) {
        if (data == null || data.isEmpty()) {
            return
        }
        val siteBean = data[0]
        val children = siteBean.children
        if (children == null || children.isEmpty()) {
            //楼栋有数据但是场地为空
            return
        }
        siteData.clear()
        data.forEach {
            val dataBean = SelectTableClassesRsp.DataBean()
            dataBean.id = it.id?.toLong() ?: 0L
            dataBean.name = it.name
            dataBean.showName = it.name
            val childDataList = mutableListOf<SelectTableClassesRsp.DataBean>()
            it.children?.forEach {
                val dataBean2 = SelectTableClassesRsp.DataBean()
                dataBean2.id = it.id?.toLong() ?: 0L
                dataBean2.name = it.name
                dataBean2.showName = it.name
                childDataList.add(dataBean2)
            }
            dataBean.list = childDataList
            siteData.add(dataBean)
        }
        val childrenBean = children[0]
        siteId = childrenBean.id ?: ""
        val title = String.format(getString(R.string.gate_page_title), siteBean.name)
        activityGateStudentStaffBinding.top.title.text = title
        if (data.size > 1 || children.size > 1) {
            //场地可选择
            val drawable =
                ResourcesCompat.getDrawable(resources, R.drawable.gate_down_icon, null)?.apply {
                    setBounds(0, 0, minimumWidth, minimumHeight)
                }
            activityGateStudentStaffBinding.top.title.setCompoundDrawables(
                null,
                null,
                drawable,
                null
            )
        }
    }

    private fun initView() {
        activityGateStudentStaffBinding.top.title.text = "校门口通行数据"
        activityGateStudentStaffBinding.top.title.setOnClickListener {
            val switchTableClassPop = SwitchTableClassPop(this, siteData)
            switchTableClassPop.setSelectClasses { id, classesName ->
                loge("id=$id,classesName=$classesName")
                var parentName = ""
                siteData.forEach {
                    val name = it.name
                    for (dataBean in it.list) {
                        if (classesName == dataBean.showName) {
                            parentName = name
                            return@forEach
                        }
                    }
                }
                this.siteId = id.toString()
                val title = String.format(getString(R.string.gate_page_title), parentName)
                activityGateStudentStaffBinding.top.title.text = title
                //切换场地
            }
        }
        activityGateStudentStaffBinding.top.backLayout.setOnClickListener {
            finish()
        }
        activityGateStudentStaffBinding.top.ivRight.visibility = View.VISIBLE
        activityGateStudentStaffBinding.top.ivRight.setImageResource(R.drawable.gate_search_icon)
        activityGateStudentStaffBinding.top.ivRight.setOnClickListener {
            //搜索入口
            startActivity(Intent(this, GateDataSearchActivity::class.java))
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