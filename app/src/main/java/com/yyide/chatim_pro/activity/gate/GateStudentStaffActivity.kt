package com.yyide.chatim_pro.activity.gate

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.activity.book.BookSearchActivity
import com.yyide.chatim_pro.base.BaseActivity
import com.yyide.chatim_pro.databinding.ActivityGateStudentStaffBinding
import com.yyide.chatim_pro.fragment.gate.GateStaffFragment
import com.yyide.chatim_pro.fragment.gate.GateStudentFragment
import com.yyide.chatim_pro.model.gate.Result
import com.yyide.chatim_pro.model.gate.SiteBean
import com.yyide.chatim_pro.utils.SelectorDialogUtil
import com.yyide.chatim_pro.utils.loge
import com.yyide.chatim_pro.viewmodel.gate.GateSiteViewModel
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
    private var siteData = mutableListOf<SiteBean>()
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
//        val children = siteBean.children
//        if (children.isEmpty()) {
//            //楼栋有数据但是场地为空
//            return
//        }
        siteData.clear()
        siteData.addAll(data)
//        val childrenBean = children[0]
        siteId = siteBean.id ?: ""
        val title = String.format(getString(R.string.gate_page_title), siteBean.name)
        activityGateStudentStaffBinding.top.title.text = title
        activityGateStudentStaffBinding.top.title.isEnabled = false
        if (data.size > 1) {
            //场地可选择
            val drawable =
                ResourcesCompat.getDrawable(resources, R.drawable.gate_down_icon, null)?.apply {
                    setBounds(0, 0, minimumWidth, minimumHeight)
                }
            activityGateStudentStaffBinding.top.title.isEnabled = true
            activityGateStudentStaffBinding.top.title.setCompoundDrawables(
                null,
                null,
                drawable,
                null
            )
        }
        //通知子fragment场地请求结果
        siteViewModel.curSiteId.value = siteId
    }

    private fun initView() {
        activityGateStudentStaffBinding.top.title.text = "通行数据"
        activityGateStudentStaffBinding.top.title.setOnClickListener {
            SelectorDialogUtil.showSiteSelector(this, "切换场地",siteId,siteData,1){siteBean, childBean ->
                this.siteId = siteBean.id//childBean.id
                val title = String.format(getString(R.string.gate_page_title), siteBean.name)
                activityGateStudentStaffBinding.top.title.text = title
                //通知子fragment场地请求结果
                siteViewModel.curSiteId.value = siteId
            }
        }
        activityGateStudentStaffBinding.top.backLayout.setOnClickListener {
            finish()
        }
        activityGateStudentStaffBinding.top.ivRight.visibility = View.VISIBLE
        activityGateStudentStaffBinding.top.ivRight.setImageResource(R.drawable.gate_search_icon)
        activityGateStudentStaffBinding.top.ivRight.setOnClickListener {
            //搜索入口
            //startActivity(Intent(this, GateDataSearchActivity::class.java))
            val intent = Intent(this, BookSearchActivity::class.java)
            intent.putExtra("from", BookSearchActivity.FROM_GATE)
            startActivity(intent)
        }

        mTitles.add("学生")
        mTitles.add("教职工")
        fragments.add(GateStudentFragment())
        fragments.add(GateStaffFragment())
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