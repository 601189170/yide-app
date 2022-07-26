package com.yyide.chatim_pro.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.SizeUtils
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.adapter.TableSectionAdapter
import com.yyide.chatim_pro.adapter.sitetable.SiteTableItemAdapter
import com.yyide.chatim_pro.adapter.sitetable.SiteTableTimeAdapter
import com.yyide.chatim_pro.base.BaseFragment
import com.yyide.chatim_pro.database.ScheduleDaoUtil
import com.yyide.chatim_pro.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim_pro.databinding.FragmentSiteTableBinding
import com.yyide.chatim_pro.dialog.TableClassPopUp
import com.yyide.chatim_pro.dialog.TableWeekPopUp
import com.yyide.chatim_pro.dialog.TableWeekPopUp.SubmitCallBack
import com.yyide.chatim_pro.dialog.TextPopUp
import com.yyide.chatim_pro.model.sitetable.SiteTableRsp
import com.yyide.chatim_pro.model.sitetable.toWeekDayList
import com.yyide.chatim_pro.model.table.ChildrenItem
import com.yyide.chatim_pro.model.table.ClassInfo
import com.yyide.chatim_pro.utils.*
import com.yyide.chatim_pro.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim_pro.viewmodel.SiteTableViewModel
import org.joda.time.DateTime
import razerdp.basepopup.BasePopupWindow


class SiteTableFragment : BaseFragment() {
    private lateinit var siteTableFragmentBinding: FragmentSiteTableBinding
    private val siteTableViewModel: SiteTableViewModel by viewModels()

    //private var data = mutableListOf<SelectTableClassesRsp.DataBean>()
    private var weekdayList = mutableListOf<TimeUtil.WeekDay>()
    private var adapter: SiteTableTimeAdapter ?= null
    var tableSectionAdapter: TableSectionAdapter? = null
    lateinit var siteTableItemAdapter: SiteTableItemAdapter
    private lateinit var leftLayout: RelativeLayout
    var index = -1
    //周次
    //private var week = 1
    //private var weekTotal = 1
    private var thisWeek = 1
    private var first = true
    //private val classList = mutableListOf<LeaveDeptRsp.DataBean>()
    //private var id: String? = null

    private val type = "2"


    // 当前所选周数
    private var selectWeek: ChildrenItem? = null


    // 当前所选场地
    private var selectClassInfo = ClassInfo()

    private lateinit var weekPopUp: TableWeekPopUp
    private lateinit var classPopUp: TableClassPopUp
    private lateinit var textPopUp: TextPopUp

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        siteTableFragmentBinding = FragmentSiteTableBinding.inflate(layoutInflater)
        return siteTableFragmentBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        siteTableViewModel.siteBuildingLiveData.observe(requireActivity()) {
            dismiss()
            val buildings = it
            if (buildings == null || buildings.isEmpty()) {
                //没有查询数据
                siteTableFragmentBinding.top.className.text = "暂无场地"
                siteTableFragmentBinding.top.classNameLogo.hide()
                siteTableFragmentBinding.top.className.isEnabled = false
                //siteTableFragmentBinding.empty.root.visibility = View.VISIBLE
                siteTableViewModel.getSites(type,"0")
                return@observe
            }

            //siteTableFragmentBinding.empty.root.visibility = View.GONE
            val buildingsBean = buildings[0]
            val children = buildingsBean.children
            if (children.isEmpty()) {
                siteTableFragmentBinding.top.className.text = "暂无场地"
                siteTableFragmentBinding.top.classNameLogo.hide()
                siteTableFragmentBinding.top.className.isEnabled = false
                siteTableViewModel.getSites(type,"0")
                return@observe
            }


            val firstData = ClassInfo()
            firstData.parentId = buildingsBean.id
            firstData.parentName = buildingsBean.name
            firstData.id = children[0].id
            firstData.name = children[0].name
            //默认查询第一个场地的课表
            selectClassInfo = firstData
            classPopUp.setData(buildings, selectClassInfo)
            siteTableFragmentBinding.top.className.text = "${selectClassInfo.parentName}${selectClassInfo.name}"
            siteTableViewModel.getSites(type, selectClassInfo.id)
        }
        //场地课表数据
        siteTableViewModel.siteTableLiveData.observe(requireActivity()) {

            siteTableFragmentBinding.slContent.isRefreshing = false

            if (it == null) {
                return@observe
            }

            if (first){
                thisWeek = it.thisWeek
                first = false
            }

            if (it.thisWeek == thisWeek){
                siteTableFragmentBinding.tableSiteReturnCurrent.hide()
            }else{
                siteTableFragmentBinding.tableSiteReturnCurrent.show()
            }

            if (it.weekTotal != 0){
                siteTableFragmentBinding.top.tableTopWeekTv.show()
                siteTableFragmentBinding.top.tableTopWeekLogo.show()
            }

            // 设置总周数
            val data: MutableList<ChildrenItem> = java.util.ArrayList()
            for (i in 0 until it.weekTotal) {
                val weekNum = (i + 1).toString()
                val bean = ChildrenItem("第" + weekNum + "周", "", weekNum)
                data.add(bean)
            }
            if (it.thisWeek == 0){
                siteTableFragmentBinding.top.tableTopWeekTv.text = "选择周次"
            }else{
                selectWeek = data[it.thisWeek - 1]
                siteTableFragmentBinding.top.tableTopWeekTv.text = selectWeek?.name
            }
            weekPopUp.setData(data, selectWeek)
            //本周一到周日日期标题
            weekdayList.clear()
            it.weekList?.let {
                val toWeekDayList = it.toWeekDayList()
                loge("周数据$toWeekDayList")
                weekdayList.addAll(toWeekDayList)
                //adapter.notifyData(toWeekDayList)
                adapter = SiteTableTimeAdapter(weekdayList)
                adapter?.setOnItemClickListener { view, position ->
                    adapter?.setPosition(position)
                    index = position
                    siteTableItemAdapter.setIndex(index)
                }
                siteTableFragmentBinding.top.grid.adapter = adapter
            }
            //早读 上午 下午 晚上 晚自习
            var earlyReading = 0
            //节次
            var sectionCount = 0
            //weekTotal = it.weekTotal
            it.sectionList?.let {
                earlyReading =
                    if (it.earlySelfStudyList != null) it.earlySelfStudyList!!.size else 0
                val morning = if (it.morningList != null) it.morningList!!.size else 0
                val afternoon = if (it.afternoonList != null) it.afternoonList!!.size else 0
                val evening = if (it.nightList != null) it.nightList!!.size else 0
                val eveningStudy =
                    if (it.lateSelfStudyList != null) it.lateSelfStudyList!!.size else 0
                if (earlyReading > 0) {
                    createLeftTypeView(0, 1, earlyReading) //早读
                }
                if (morning > 0) {
                    createLeftTypeView(earlyReading, 2, morning) //上午
                }
                if (afternoon > 0) {
                    createLeftTypeView(earlyReading + morning, 3, afternoon) //下午
                }
                if (evening > 0) {
                    createLeftTypeView(earlyReading + morning + afternoon, 4, evening)//晚上
                }
                if (eveningStudy > 0) {
                    createLeftTypeView(
                        morning + afternoon + earlyReading + evening,
                        5,
                        eveningStudy
                    ) //晚自习
                }

                val sectionlist = mutableListOf<String>()
                it.earlySelfStudyList?.forEach {
                    sectionlist.add(it.name + "")
                }
                it.morningList?.forEach {
                    sectionlist.add(it.name + "")
                }
                it.afternoonList?.forEach {
                    sectionlist.add(it.name + "")
                }
                it.nightList?.forEach {
                    sectionlist.add("${it.name}")
                }
                it.lateSelfStudyList?.forEach {
                    sectionlist.add(it.name + "")
                }
                tableSectionAdapter = TableSectionAdapter()
                siteTableFragmentBinding.listsection.adapter = tableSectionAdapter
                tableSectionAdapter?.notifyData(sectionlist)

                //周次选择
                //initClassData()
                //initClassView()
                sectionCount = morning + afternoon + earlyReading + evening + eveningStudy
            }

            //填课程
            val courseList = mutableListOf<SiteTableRsp.DataBean.TimetableListBean>()
            val courseBoxCount = sectionCount * 7
            for (index in 0 until courseBoxCount) {
                val listBean = SiteTableRsp.DataBean.TimetableListBean()
                courseList.add(listBean)
                it.timetableList?.forEach {
                    val week: Int = it.week
                    val column = week - 1
                    /*var column: Int = it.week % 7 - 1
                    if (week == 7) {
                        column = it.week % 7 - 1 + 7
                    }*/
                    if (index == ((it.section - 1) * 7 + column)) {
                        courseList[index] = it
                        return@forEach
                    }
                }
            }
            loge("${courseList.size}")
            siteTableItemAdapter.notifyData(courseList, -1)

            //空数据
            siteTableFragmentBinding.empty.root.visibility =
                if (it.timetableList?.isEmpty() == true) View.VISIBLE else View.GONE
            siteTableFragmentBinding.leftLayout.visibility =
                if (it.timetableList?.isEmpty() == true) View.GONE else View.VISIBLE
            siteTableFragmentBinding.listsection.visibility =
                if (it.timetableList?.isEmpty() == true) View.GONE else View.VISIBLE
            siteTableFragmentBinding.tablegrid.visibility =
                if (it.timetableList?.isEmpty() == true) View.GONE else View.VISIBLE
            //计算今日
            if (weekdayList.isNotEmpty()) {
                val dataTime = weekdayList[0].dataTime
                val nowDateTime = DateTime.now()
                val firstDayOfWeek =
                    nowDateTime.minusDays(nowDateTime.dayOfWeek % 7 - 1).simplifiedDataTime()
                if (ScheduleDaoUtil.toDateTime(dataTime, "yyyy-MM-dd")
                        .simplifiedDataTime() == firstDayOfWeek
                ) {
                    val dayOfWeek = DateTime.now().dayOfWeek
                    index = dayOfWeek % 7 - 1
                    for (i in courseList.indices) {
                        if (i % 7 == index) {
                            courseList[i].date = nowDateTime.toStringTime("yyyy-MM-dd")
                        }
                    }
                    siteTableItemAdapter.setIndex(index)
                    adapter?.setPosition(index)
                }
            }
        }
        // TODO: 场地列表接口暂时没有，直接使用指定场地id访问场地课表接口
        loading()
        siteTableViewModel.getBuildings()

    }

    private fun initView() {
        leftLayout = siteTableFragmentBinding.leftLayout

        siteTableFragmentBinding.empty.tvDesc.text = "场地未设置课表"
        /*adapter = SiteTableTimeAdapter(weekdayList)
        adapter.setOnItemClickListener { view, position ->
            Log.d("grid click", "onViewCreated: site click")
            adapter.setPosition(position)
            index = position
            siteTableItemAdapter.setIndex(index)
        }
        siteTableFragmentBinding.top.grid.adapter = adapter*/

        weekPopUp = TableWeekPopUp(this)
        weekPopUp.popupGravity = Gravity.BOTTOM
        weekPopUp.setSubmitCallBack(object : SubmitCallBack {
            override fun getSubmitData(data: ChildrenItem?) {
                if (data != null) {
                    selectWeek = data
                    siteTableFragmentBinding.top.tableTopWeekTv.text = selectWeek?.name ?: ""
                    if (selectClassInfo.id != "") {
                        siteTableViewModel.getSites(type, selectClassInfo.id, selectWeek?.id)
                    }else{
                        siteTableViewModel.getSites(type,"0", selectWeek?.id)
                    }
                }
            }
        })



        classPopUp = TableClassPopUp(this)
        classPopUp.popupGravity = Gravity.BOTTOM
        classPopUp.setSubmitCallBack(object : TableClassPopUp.SubmitCallBack {
            override fun getSubmitData(data: ClassInfo) {
                selectClassInfo = data
                siteTableFragmentBinding.top.className.text = "${selectClassInfo.parentName}${selectClassInfo.name}"
                siteTableViewModel.getSites(type, selectClassInfo.id, selectWeek?.id)
            }
        })



        textPopUp = TextPopUp(this);

        siteTableFragmentBinding.top.className.setOnClickListener {
            if (classPopUp.isShowing) {
                classPopUp.dismiss()
            } else {
                siteTableFragmentBinding.top.className.setTextColor(-0xee397b)
                siteTableFragmentBinding.top.classNameLogo.setImageResource(R.mipmap.table_week_button_pack)
                classPopUp.showPopupWindow(it)
            }
        }

        siteTableFragmentBinding.top.classNameLogo.setOnClickListener {
            if (classPopUp.isShowing) {
                classPopUp.dismiss()
            } else {
                siteTableFragmentBinding.top.className.setTextColor(-0xee397b)
                siteTableFragmentBinding.top.classNameLogo.setImageResource(R.mipmap.table_week_button_pack)
                classPopUp.showPopupWindow(it)
            }
        }

        classPopUp.onDismissListener = object : BasePopupWindow.OnDismissListener() {
            override fun onDismiss() {
                siteTableFragmentBinding.top.className.setTextColor(-0x99999a)
                siteTableFragmentBinding.top.classNameLogo.setImageResource(R.mipmap.table_week_button)
            }
        }

        siteTableFragmentBinding.top.tableTopWeekTv.setOnClickListener { v ->
            if (weekPopUp.isShowing) {
                weekPopUp.dismiss()
            } else {
                siteTableFragmentBinding.top.tableTopWeekTv.setTextColor(-0xee397b)
                siteTableFragmentBinding.top.tableTopWeekLogo.setImageResource(R.mipmap.table_week_button_pack)
                weekPopUp.showPopupWindow(v)
            }
        }

        siteTableFragmentBinding.top.tableTopWeekLogo.setOnClickListener { v ->
            if (weekPopUp.isShowing) {
                weekPopUp.dismiss()
            } else {
                siteTableFragmentBinding.top.tableTopWeekTv.setTextColor(-0xee397b)
                siteTableFragmentBinding.top.tableTopWeekLogo.setImageResource(R.mipmap.table_week_button_pack)
                weekPopUp.showPopupWindow(v)
            }
        }

        weekPopUp.onDismissListener = object : BasePopupWindow.OnDismissListener() {
            override fun onDismiss() {
                siteTableFragmentBinding.top.tableTopWeekTv.setTextColor(-0x99999a)
                siteTableFragmentBinding.top.tableTopWeekLogo.setImageResource(R.mipmap.table_week_button)
            }
        }

        siteTableItemAdapter = SiteTableItemAdapter()
        siteTableItemAdapter.setOnItemClickListener { view, content ->
            textPopUp.setText(content, view)
        }
        siteTableFragmentBinding.tablegrid.adapter = siteTableItemAdapter

        siteTableFragmentBinding.tablegrid.setOnItemClickListener { parent, view, position, id ->
            index = position % 7
            siteTableItemAdapter.setIndex(index)
            adapter?.setPosition(index)
        }

        // 下拉刷新
        siteTableFragmentBinding.slContent.setOnRefreshListener {
            if (selectClassInfo.id != "") {
                siteTableViewModel.getSites(type, selectClassInfo.id, selectWeek?.id)
            }else{
                siteTableViewModel.getSites(type,"0", selectWeek?.id)
            }
        }
        siteTableFragmentBinding.slContent.setColorSchemeColors(R.color.colorPrimary.asColor())

        //回调本周
        siteTableFragmentBinding.tableSiteReturnCurrent.setOnClickListener {
            index = -1
            selectWeek = null
            first = true
            if (selectClassInfo.id != "") {
                siteTableViewModel.getSites(type, selectClassInfo.id)
            }else{
                siteTableViewModel.getSites(type, "0")
            }
        }

    }

    /**
     * 初始化
     * @param studentBeanList
     */
    private fun initClassData() {
        /*classList.clear()
        for (index in 1 .. weekTotal) {
            val dataBean = LeaveDeptRsp.DataBean()
            dataBean.deptId = "$index"
            dataBean.classId = "$index"
            dataBean.deptName = "第".plus("$index").plus("周")
            dataBean.isDefault = 0
            if (week == index) {
                dataBean.isDefault = 1
            }
            dataBean.isCurWeek = index == thisWeek
            classList.add(dataBean)
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun initClassView() {
        /*val classOptional = classList.stream().filter { it.isDefault == 1 }.findFirst()
        if (classOptional.isPresent) {
            val clazzBean = classOptional.get()
            week = clazzBean.classId.toInt()
            if (classList.size <= 1) {
                siteTableFragmentBinding.top.tvWeek.isEnabled = false
            } else {
                siteTableFragmentBinding.top.tvWeek.setOnClickListener { v ->
                    val deptSelectPop = DeptSelectPop(activity, 6, classList)
                    deptSelectPop.setOnCheckedListener { dataBean: LeaveDeptRsp.DataBean ->
                        //班级id
                        week = dataBean.classId.toInt()
                        siteTableViewModel.getSites(type,selectClassInfo.id, "$week")
                    }
                }
            }
        }*/
    }

    //创建"第上下午"视图
    private fun createLeftTypeView(selection: Int, type: Int, length: Int) {
        val CouseHeight = requireContext().resources.getDimensionPixelOffset(R.dimen.dp_75) + 1
        val CouseWith = SizeUtils.dp2px(30f)
        val view = LayoutInflater.from(context).inflate(R.layout.course_card_type2, null)
        val params = LinearLayout.LayoutParams(CouseWith, CouseHeight * length) //设置布局高度,即跨多少节课
        val text = view.findViewById<TextView>(R.id.text_view)
        text.text = ""
        when (type) {
            1 -> {
                view.y = (CouseHeight * selection).toFloat()
                view.background = resources.getDrawable(R.drawable.bg_table_type1)
                text.text = "早\n读"
            }
            2 -> {
                view.y = (CouseHeight * selection + SizeUtils.dp2px(1f)).toFloat()
                view.background = resources.getDrawable(R.drawable.bg_table_type2)
                text.text = "上\n午"
            }
            3 -> {
                view.y = (CouseHeight * selection + SizeUtils.dp2px(2f)).toFloat()
                view.background = resources.getDrawable(R.drawable.bg_table_type3)
                text.text = "下\n午"
            }
            4 -> {
                view.y = (CouseHeight * selection + SizeUtils.dp2px(3f)).toFloat()
                view.background = resources.getDrawable(R.drawable.bg_table_type5)
                text.text = "晚\n上"
            }
            5 -> {
                view.y = (CouseHeight * selection + SizeUtils.dp2px(4f)).toFloat()
                view.background = resources.getDrawable(R.drawable.bg_table_type4)
                text.text = "晚\n自\n习"
            }
        }
        params.gravity = Gravity.CENTER
        text.layoutParams = params
        leftLayout.addView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        weekPopUp.setSubmitCallBack(null)
        classPopUp.setSubmitCallBack(null)
        siteTableItemAdapter.setOnItemClickListener(null)
        adapter?.setOnItemClickListener(null)
    }
}