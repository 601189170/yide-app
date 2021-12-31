package com.yyide.chatim.activity.gate

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.yyide.chatim.R
import com.yyide.chatim.adapter.gate.GateThroughData
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim.databinding.ActivityGateClassTeacherBinding
import com.yyide.chatim.dialog.DeptSelectPop
import com.yyide.chatim.dialog.SwitchTableClassPop
import com.yyide.chatim.fragment.gate.PeopleThroughListFragment
import com.yyide.chatim.model.LeaveDeptRsp
import com.yyide.chatim.model.SelectTableClassesRsp
import com.yyide.chatim.model.gate.ClassListOfTeacherBean
import com.yyide.chatim.model.gate.Result
import com.yyide.chatim.model.gate.SiteBean
import com.yyide.chatim.utils.DatePickerDialogUtil
import com.yyide.chatim.utils.loge
import com.yyide.chatim.viewmodel.gate.ClassTeacherViewModel
import com.yyide.chatim.viewmodel.gate.GateSiteViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author lt
 * @date 2021年12月22日09:29:54
 * @desc 班主任视角查看班级通行数据
 *  查询条件：
 *          1，事件列表
 *          2，日期
 *          3，班级
 */
class GateClassTeacherActivity : BaseActivity() {
    private lateinit var gateClassTeacherBinding: ActivityGateClassTeacherBinding
    val mTitles: MutableList<String> = ArrayList()
    val fragments = mutableListOf<Fragment>()
    private var currentDate = ""
    private var classId = ""
    private var siteId = ""
    private val classList = mutableListOf<LeaveDeptRsp.DataBean>()
    private val viewModel: ClassTeacherViewModel by viewModels()
    private val siteViewModel: GateSiteViewModel by viewModels()
    private var siteData = mutableListOf<SelectTableClassesRsp.DataBean>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gateClassTeacherBinding = ActivityGateClassTeacherBinding.inflate(layoutInflater)
        setContentView(gateClassTeacherBinding.root)
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
        //班级列表
        lifecycleScope.launch {
            viewModel.classDataList.collect {
                when (it) {
                    is Result.Success -> {
                        //班级列表数据数据
                        handleClassData(it.data)
                    }
                    is Result.Error -> {
                        loge("${it.exception?.localizedMessage}")
                        ToastUtils.showShort("${it.exception?.localizedMessage}")
                    }
                }
            }
        }
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
        siteId = childrenBean.id?:""
        val title = String.format(getString(R.string.gate_page_title),siteBean.name)
        gateClassTeacherBinding.top.title.text = title
        if (data.size>1 ||children.size > 1){
            //场地可选择
            val drawable =
                ResourcesCompat.getDrawable(resources, R.drawable.gate_down_icon, null)?.apply {
                    setBounds(0, 0, minimumWidth, minimumHeight)
                }
            gateClassTeacherBinding.top.title.setCompoundDrawables(null,null,drawable,null)
        }
        //请求班级列表
        viewModel.queryClassInfoByTeacherId()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun handleClassData(data: List<ClassListOfTeacherBean>?) {
        if (data == null || data.isEmpty()) {
            return
        }
        initClassData(data)
        if (data.size == 1) {
            val classListOfTeacherBean = data[0]
            classId = classListOfTeacherBean.classId ?: ""
            val className = classListOfTeacherBean.className
            gateClassTeacherBinding.tvClassPick.text = className
            gateClassTeacherBinding.tvClassPick.setCompoundDrawables(null, null, null, null)
            return
        }
        val classListOfTeacherBean = data[0]
        val className = classListOfTeacherBean.className
        val drawable =
            ResourcesCompat.getDrawable(resources, R.drawable.icon_arrow_down, null)?.apply {
                setBounds(0, 0, minimumWidth, minimumHeight)
            }
        gateClassTeacherBinding.tvClassPick.setCompoundDrawables(null, null, null, drawable)
        gateClassTeacherBinding.tvClassPick.text = className
        val deptSelectPop = DeptSelectPop(this, 2, classList)
        deptSelectPop.setOnCheckedListener { dataBean ->
            loge("选择的班级：$dataBean")
            classId = dataBean.deptId
            gateClassTeacherBinding.tvClassPick.text = dataBean.deptName
        }
    }

    /**
     * 初始化班级列表数据
     */
    fun initClassData(data: List<ClassListOfTeacherBean>) {
        classList.clear()
        data.forEach {
            val dataBean = LeaveDeptRsp.DataBean()
            dataBean.deptId = it.classId
            dataBean.deptName = it.className
            dataBean.isDefault = 0
            classList.add(dataBean)
        }
        if (classList.isNotEmpty()) {
            classList[0].isDefault = 1
        }
    }

    private fun initView() {
        gateClassTeacherBinding.top.title.text = "校门口通行数据"
        gateClassTeacherBinding.top.title.setOnClickListener {
            val switchTableClassPop = SwitchTableClassPop(this, siteData)
            switchTableClassPop.setSelectClasses { id, classesName ->
                loge("id=$id,classesName=$classesName")
                var parentName = ""
                siteData.forEach {
                    val name = it.name
                    for (dataBean in it.list) {
                        if (classesName == dataBean.showName){
                            parentName = name
                            return@forEach
                        }
                    }
                }
                this.siteId = id.toString()
                val title = String.format(getString(R.string.gate_page_title),parentName)
                gateClassTeacherBinding.top.title.text = title
                //切换场地
            }
        }
        gateClassTeacherBinding.top.ivRight.visibility = View.VISIBLE
        gateClassTeacherBinding.top.ivRight.setImageResource(R.drawable.gate_search_icon)
        gateClassTeacherBinding.top.ivRight.setOnClickListener {
            //搜索入口
            startActivity(Intent(this, GateDataSearchActivity::class.java))
        }
        //班级选择
        gateClassTeacherBinding.tvClassPick.setOnClickListener {

        }
        //日期选择
        gateClassTeacherBinding.tvDatePick.setOnClickListener {
            DatePickerDialogUtil.showDate(this, "选择日期", currentDate, startTimeListener)
        }
        gateClassTeacherBinding.top.backLayout.setOnClickListener {
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

        gateClassTeacherBinding.viewpager.offscreenPageLimit = 3
        gateClassTeacherBinding.viewpager.adapter = object :
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
        gateClassTeacherBinding.slidingTabLayout.setViewPager(gateClassTeacherBinding.viewpager)
        gateClassTeacherBinding.slidingTabLayout.currentTab = 0

    }

    //日期选择监听
    private val startTimeListener =
        OnDateSetListener { timePickerView: TimePickerDialog?, millseconds: Long ->
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val timingTime = simpleDateFormat.format(Date(millseconds))
            currentDate = timingTime
            loge("startTimeListener: $timingTime")
            val toStringTime =
                ScheduleDaoUtil.toDateTime(timingTime, "yyyy-MM-dd").toStringTime("yyyy/MM/dd")
            gateClassTeacherBinding.tvDatePick.text = toStringTime
        }
}