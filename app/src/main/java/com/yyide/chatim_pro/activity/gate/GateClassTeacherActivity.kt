package com.yyide.chatim_pro.activity.gate

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.SpData
import com.yyide.chatim_pro.activity.book.BookSearchActivity
import com.yyide.chatim_pro.base.BaseActivity
import com.yyide.chatim_pro.database.ScheduleDaoUtil
import com.yyide.chatim_pro.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim_pro.databinding.ActivityGateClassTeacherBinding
import com.yyide.chatim_pro.dialog.DeptSelectPop
import com.yyide.chatim_pro.fragment.gate.PeopleThroughListFragment
import com.yyide.chatim_pro.model.LeaveDeptRsp
import com.yyide.chatim_pro.model.gate.ClassListOfTeacherBean
import com.yyide.chatim_pro.model.gate.GateThroughPeopleListBean
import com.yyide.chatim_pro.model.gate.Result
import com.yyide.chatim_pro.model.gate.SiteBean
import com.yyide.chatim_pro.utils.DatePickerDialogUtil
import com.yyide.chatim_pro.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim_pro.utils.SelectorDialogUtil
import com.yyide.chatim_pro.utils.loge
import com.yyide.chatim_pro.viewmodel.gate.ClassTeacherViewModel
import com.yyide.chatim_pro.viewmodel.gate.GateSiteViewModel
import com.yyide.chatim_pro.viewmodel.gate.GateThroughPeopleListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.joda.time.DateTime
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
    private var siteData = mutableListOf<SiteBean>()
    private val gateThroughPeopleListViewModel: GateThroughPeopleListViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gateClassTeacherBinding = ActivityGateClassTeacherBinding.inflate(layoutInflater)
        setContentView(gateClassTeacherBinding.root)
        currentDate = DateTime.now().simplifiedDataTime().toStringTime()
        gateClassTeacherBinding.tvDatePick.text =
            ScheduleDaoUtil.toDateTime(currentDate).toStringTime("yyyy/MM/dd")
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
        //班主任视角下的学生通行数据
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
    }

    private fun handleData(data: GateThroughPeopleListBean?) {
        if (data == null) {
            gateClassTeacherBinding.blankPage.visibility = View.VISIBLE
            gateClassTeacherBinding.viewpager.visibility = View.GONE
            gateClassTeacherBinding.slidingTabLayout.visibility = View.GONE
            return
        }
        val layoutGateThroughSummaryAll = gateClassTeacherBinding.layoutGateThroughSummaryAll
        layoutGateThroughSummaryAll.tvGateEventTitle.text = data.title
        layoutGateThroughSummaryAll.tvThroughNumber.text = "${data.totalNumber}"
        layoutGateThroughSummaryAll.tvGoIntoNumber.text = "${data.intoNumber}"
        layoutGateThroughSummaryAll.tvGoOutNumber.text = "${data.outNumber}"

        if (data.peopleList== null || data.peopleList?.isEmpty() == true){
            gateClassTeacherBinding.blankPage.visibility = View.VISIBLE
            gateClassTeacherBinding.viewpager.visibility = View.GONE
            gateClassTeacherBinding.slidingTabLayout.visibility = View.GONE
            return
        }
        gateClassTeacherBinding.blankPage.visibility = View.GONE
        gateClassTeacherBinding.viewpager.visibility = View.VISIBLE
        gateClassTeacherBinding.slidingTabLayout.visibility = View.VISIBLE
        //1出2入
        mTitles.clear()
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
        fragments.clear()
        fragments.add(PeopleThroughListFragment("3", "1", "1", currentDate, siteId, dataList3))
        fragments.add(PeopleThroughListFragment("1", "1", "1", currentDate, siteId, dataList))
        fragments.add(PeopleThroughListFragment("2", "1", "1", currentDate, siteId, dataList2))

        gateClassTeacherBinding.viewpager.offscreenPageLimit = 3
        gateClassTeacherBinding.viewpager.adapter = object :
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
//        gateClassTeacherBinding.slidingTabLayout.setViewPager(gateClassTeacherBinding.viewpager)
        gateClassTeacherBinding.slidingTabLayout.setupWithViewPager(gateClassTeacherBinding.viewpager)
//        gateClassTeacherBinding.slidingTabLayout.currentTab = 0
    }

    /**
     * 处理场地
     */
    private fun handleSiteListData(data: List<SiteBean>?) {
        gateClassTeacherBinding.top.title.isEnabled = false
        if (data == null || data.isEmpty()) {
            return
        }
        val siteBean = data[0]
//        val children = siteBean.children
//        if (children == null || children.isEmpty()) {
//            //楼栋有数据但是场地为空
//            return
//        }
        siteData.clear()
        siteData.addAll(data)
//        val childrenBean = children[0]
        siteId = siteBean.id ?: ""
        val title = String.format(getString(R.string.gate_page_title), siteBean.name)
        gateClassTeacherBinding.top.title.text = title
        if (data.size > 1) {
            //场地可选择
            val drawable =
                ResourcesCompat.getDrawable(resources, R.drawable.gate_down_icon, null)?.apply {
                    setBounds(0, 0, minimumWidth, minimumHeight)
                }
            gateClassTeacherBinding.top.title.isEnabled = true
            gateClassTeacherBinding.top.title.setCompoundDrawables(null, null, drawable, null)
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
            //查询数据
            gateThroughPeopleListViewModel.queryAllStudentPassageInOutDetails(
                1,
                currentDate,
                4,
                classId,
                siteId
            )
            return
        }
        val classListOfTeacherBeanList = classList.filter { it.isDefault == 1 }
        val classListOfTeacherBean = classListOfTeacherBeanList[0]
        classId = classListOfTeacherBean.deptId ?: ""
        val className = classListOfTeacherBean.deptName
        val drawable =
            ResourcesCompat.getDrawable(resources, R.drawable.icon_arrow_down, null)?.apply {
                setBounds(0, 0, minimumWidth, minimumHeight)
            }
        gateClassTeacherBinding.tvClassPick.setCompoundDrawables(null, null, drawable, null)
        gateClassTeacherBinding.tvClassPick.text = className
        //查询数据
        gateThroughPeopleListViewModel.queryAllStudentPassageInOutDetails(
            1,
            currentDate,
            4,
            classId,
            siteId
        )
        //班级选择
        gateClassTeacherBinding.tvClassPick.setOnClickListener {
            val deptSelectPop = DeptSelectPop(this, 2, classList)
            deptSelectPop.setOnCheckedListener { dataBean ->
                loge("选择的班级：$dataBean")
                classId = dataBean.deptId
                gateClassTeacherBinding.tvClassPick.text = dataBean.deptName
                //查询数据
                gateThroughPeopleListViewModel.queryAllStudentPassageInOutDetails(
                    1,
                    currentDate,
                    4,
                    classId,
                    siteId
                )
            }
        }
    }

    /**
     * 初始化班级列表数据
     */
    fun initClassData(data: List<ClassListOfTeacherBean>) {
        classList.clear()
        val classInfo = SpData.getClassInfo()
        data.forEach {
            val dataBean = LeaveDeptRsp.DataBean()
            dataBean.deptId = it.classId
            dataBean.deptName = it.className
            dataBean.isDefault = if (classInfo.classesName == it.className) 1 else 0
            classList.add(dataBean)
        }
        if (classList.isNotEmpty() && classList.find { it.isDefault == 1 } == null) {
            classList[0].isDefault = 1
        }
    }

    private fun initView() {
        gateClassTeacherBinding.top.title.text = "校门口通行数据"
        gateClassTeacherBinding.top.title.setOnClickListener {
            SelectorDialogUtil.showSiteSelector(
                this,
                "切换场地",
                siteId,
                siteData,
                1,
            ) { siteBean, childBean ->
                this.siteId = siteBean.id
                val title = String.format(getString(R.string.gate_page_title), siteBean.name)
                gateClassTeacherBinding.top.title.text = title
                //切换场地
                gateThroughPeopleListViewModel.queryAllStudentPassageInOutDetails(
                    1,
                    currentDate,
                    4,
                    classId,
                    siteId
                )
            }
        }
        gateClassTeacherBinding.top.ivRight.visibility = View.VISIBLE
        gateClassTeacherBinding.top.ivRight.setImageResource(R.drawable.gate_search_icon)
        gateClassTeacherBinding.top.ivRight.setOnClickListener {
            //搜索入口
            //startActivity(Intent(this, GateDataSearchActivity::class.java))
            val intent = Intent(this, BookSearchActivity::class.java)
            intent.putExtra("from",BookSearchActivity.FROM_GATE)
            startActivity(intent)
        }
        //日期选择
        gateClassTeacherBinding.tvDatePick.setOnClickListener {
            val now = DateTime.now()
            val millisMax = now.millis
            val millisMin = now.minusMonths(3).millis
            DatePickerDialogUtil.showDate(this, "选择日期", currentDate, millisMin,millisMax,startTimeListener)
        }
        gateClassTeacherBinding.top.backLayout.setOnClickListener {
            finish()
        }
    }

    //日期选择监听
    private val startTimeListener =
        OnDateSetListener { timePickerView: TimePickerDialog?, millseconds: Long ->
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val timingTime = simpleDateFormat.format(Date(millseconds))
            currentDate = timingTime
            loge("startTimeListener: $timingTime")
            val toStringTime =
                ScheduleDaoUtil.toDateTime(timingTime).toStringTime("yyyy/MM/dd")
            gateClassTeacherBinding.tvDatePick.text = toStringTime
            //查询数据
            gateThroughPeopleListViewModel.queryAllStudentPassageInOutDetails(
                1,
                currentDate,
                4,
                classId,
                siteId
            )
        }
}