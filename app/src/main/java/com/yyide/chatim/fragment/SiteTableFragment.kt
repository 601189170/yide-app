package com.yyide.chatim.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.SizeUtils
import com.yyide.chatim.R
import com.yyide.chatim.adapter.TableSectionAdapter
import com.yyide.chatim.adapter.sitetable.SiteTableItemAdapter
import com.yyide.chatim.adapter.sitetable.SiteTableTimeAdapter
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim.databinding.FragmentSiteTableBinding
import com.yyide.chatim.dialog.DeptSelectPop
import com.yyide.chatim.dialog.SwitchTableClassPop
import com.yyide.chatim.fragment.attendance.v2.StudentDayStatisticsFragment
import com.yyide.chatim.model.LeaveDeptRsp
import com.yyide.chatim.model.SelectTableClassesRsp
import com.yyide.chatim.model.sitetable.SiteTableRsp
import com.yyide.chatim.model.sitetable.toWeekDayList
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim.utils.TimeUtil
import com.yyide.chatim.utils.loge
import com.yyide.chatim.viewmodel.SiteTableViewModel
import org.joda.time.DateTime


class SiteTableFragment : Fragment() {
    private lateinit var siteTableFragmentBinding: FragmentSiteTableBinding
    private val siteTableViewModel: SiteTableViewModel by viewModels()
    private var data = mutableListOf<SelectTableClassesRsp.DataBean>()
    private var weekdayList = mutableListOf<TimeUtil.WeekDay>()
    private lateinit var adapter: SiteTableTimeAdapter
    var tableSectionAdapter: TableSectionAdapter? = null
    lateinit var siteTableItemAdapter: SiteTableItemAdapter
    private lateinit var leftLayout: RelativeLayout
    var index = -1
    //周次
    private var week = 1
    private var weekTotal = 1
    private var thisWeek = 1
    private var first = true
    private val classList = mutableListOf<LeaveDeptRsp.DataBean>()
    private var id: String? = null
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
        siteTableViewModel.siteBuildingLiveData.observe(requireActivity()) {
            val buildings = it.buildings
            if (!it.isExistsSiteKcb || buildings == null || buildings.isEmpty()) {
                //没有查询数据
                siteTableFragmentBinding.top.className.text = "暂无场地"
                siteTableFragmentBinding.top.classlayout.isEnabled = false
                siteTableFragmentBinding.top.llWeek.visibility = View.GONE
                siteTableFragmentBinding.empty.root.visibility = View.VISIBLE
                return@observe
            }
            siteTableFragmentBinding.top.llWeek.visibility = View.VISIBLE
            siteTableFragmentBinding.empty.root.visibility = View.GONE
            val buildingsBean = buildings[0]
            val children = buildingsBean.children
            if (children == null || children.isEmpty()) {
                siteTableFragmentBinding.top.className.text = "暂无场地"
                siteTableFragmentBinding.top.classlayout.isEnabled = false
                return@observe
            }
            data.clear()
            buildings.forEach {
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
                data.add(dataBean)
            }

            val childrenBean = children[0]
            siteTableFragmentBinding.top.className.text = buildingsBean.name+"-"+childrenBean.name
            siteTableFragmentBinding.top.classlayout.isEnabled = true
            //默认查询第一个场地的课表
            id = childrenBean.id
            siteTableViewModel.getSites(childrenBean.id, null)
        }
        //场地课表数据
        siteTableViewModel.siteTableLiveData.observe(requireActivity()) {
            if (it == null) {
                return@observe
            }
            //本周次
            siteTableFragmentBinding.top.tvWeek.text = "${it.bzzc}周"
            week = it.bzzc?.toInt()?:1
            if (first){
                first = false
                thisWeek = week
            }
            //显示回调本周
            siteTableFragmentBinding.top.backCurrentWeek.visibility = if (week == thisWeek) View.GONE else View.VISIBLE
            //本周一到周日日期标题
            weekdayList.clear()
            it.weekmap?.let {
                val toWeekDayList = it.toWeekDayList()
                loge("周数据$toWeekDayList")
                weekdayList.addAll(toWeekDayList)
                adapter = SiteTableTimeAdapter(weekdayList)
                siteTableFragmentBinding.top.grid.adapter = adapter
            }
            //早读 上午 下午 晚上 晚自习
            var earlyReading = 0
            it.jcbMap?.let {
                earlyReading = if (it.zzx != null) it.zzx.size else 0
                val morning = if (it.sw != null) it.sw.size else 0
                val afternoon = if (it.xw != null) it.xw.size else 0
                val evening = if (it.ws != null) it.ws.size else 0
                val eveningStudy = if (it.wzx != null) it.wzx.size else 0
                if (earlyReading > 0) {
                    createLeftTypeView(0, 1, earlyReading) //早读
                }
                if (morning > 0) {
                    createLeftTypeView(earlyReading, 2, morning) //上午
                }
                if (afternoon > 0) {
                    createLeftTypeView(earlyReading + morning, 3, afternoon) //下午
                }
                if (evening >0){
                    createLeftTypeView(earlyReading+morning+afternoon,4,evening)//晚上
                }
                if (eveningStudy > 0) {
                    createLeftTypeView(morning + afternoon + earlyReading+evening, 5, eveningStudy) //晚自习
                }

                val sectionlist = mutableListOf<String>()
                it.zzx?.forEach {
                    sectionlist.add(it.jcmc+"")
                }
                it.sw?.forEach {
                     sectionlist.add(it.jcmc+"")
                }
                it.xw?.forEach {
                    sectionlist.add(it.jcmc+"")
                }
                it.ws?.forEach {
                    sectionlist.add("${it.jcmc}")
                }
                it.wzx?.forEach {
                    sectionlist.add(it.jcmc+"")
                }
                tableSectionAdapter = TableSectionAdapter()
                siteTableFragmentBinding.listsection.adapter = tableSectionAdapter
                tableSectionAdapter?.notifyData(sectionlist)
            }
            //节次
            var sectionCount = 0
            it.zxb?.let {
                weekTotal = it.xnzzc
                //周次选择
                initClassData()
                initClassView()
                sectionCount = it.zzxkjs + it.swkjs + it.xwkjs + it.wzxkjs +it.wskjs
//                val sectionlist = mutableListOf<String>()
//                for (i in 0 until sectionCount) {
//                    if (earlyReading > 0 && i == 0) {
//                        sectionlist.add("早读")
//                    } else {
//                        if (earlyReading > 0) {
//                            sectionlist.add("$i")
//                        } else {
//                            sectionlist.add("${(i + 1)}")
//                        }
//                    }
//                }

//                tableSectionAdapter = TableSectionAdapter()
//                siteTableFragmentBinding.listsection.adapter = tableSectionAdapter
//                tableSectionAdapter?.notifyData(sectionlist)
            }

            //填课程
            val courseList = mutableListOf<SiteTableRsp.DataBean.ListBean>()
            val courseBoxCount = sectionCount * 7
            for (index in 0 until courseBoxCount) {
                val listBean = SiteTableRsp.DataBean.ListBean()
                courseList.add(listBean)
                it.list?.forEach {
                    //if ((it.skxq - 1) == index % 7 && (it.xh) == ((index % sectionCount)+1)) {
                    if (index == ((it.xh - 1) * 7 + it.skxq % 7 - 1)) {
                        courseList[index] = it
                        return@forEach
                    }
                }
            }
            loge("${courseList.size}")
            siteTableItemAdapter.notifyData(courseList,-1)
            //空数据
            siteTableFragmentBinding.empty2.root.visibility =if (it.list?.isEmpty() == true) View.VISIBLE else View.GONE
            siteTableFragmentBinding.leftLayout.visibility = if (it.list?.isEmpty() == true) View.GONE else View.VISIBLE
            siteTableFragmentBinding.listsection.visibility = if (it.list?.isEmpty() == true) View.GONE else View.VISIBLE
            siteTableFragmentBinding.tablegrid.visibility = if (it.list?.isEmpty() == true) View.GONE else View.VISIBLE
            //计算今日
            if (weekdayList.isNotEmpty()) {
                val dataTime = weekdayList[0].dataTime
                val nowDateTime = DateTime.now()
                val firstDayOfWeek = nowDateTime.minusDays(nowDateTime.dayOfWeek % 7-1).simplifiedDataTime()
                if (ScheduleDaoUtil.toDateTime(dataTime, "yyyy-MM-dd").simplifiedDataTime() == firstDayOfWeek) {
                    val dayOfWeek = DateTime.now().dayOfWeek
                    index = dayOfWeek % 7 - 1
                    for (i in courseList.indices) {
                        if (i % 7 == index) {
                            courseList[i].date = nowDateTime.toStringTime("yyyy-MM-dd")
                        }
                    }
                    siteTableItemAdapter.setIndex(index)
                    adapter.setPosition(index)
                }
            }
        }
        siteTableViewModel.getBuildings()
        initView()
    }

    private fun initView() {
        leftLayout = siteTableFragmentBinding.leftLayout
        siteTableFragmentBinding.top.classlayout.setOnClickListener {
            val switchTableClassPop = SwitchTableClassPop(requireActivity(), data)
            switchTableClassPop.setSelectClasses { id, classesName ->
                loge("id=$id,classesName=$classesName")
                var parentName = ""
                data.forEach {
                    val name = it.name
                    for (dataBean in it.list) {
                        if (classesName == dataBean.showName){
                            parentName = name
                            return@forEach
                        }
                    }
                }
                this.id = id.toString()
                siteTableFragmentBinding.top.className.text = parentName.plus("-").plus(classesName)
                siteTableViewModel.getSites(id.toString(), null)
            }
        }
        siteTableItemAdapter = SiteTableItemAdapter()
        siteTableFragmentBinding.tablegrid.adapter = siteTableItemAdapter

        siteTableFragmentBinding.tablegrid.setOnItemClickListener { parent, view, position, id ->
            index = position % 7
            siteTableItemAdapter.setIndex(index)
            adapter.setPosition(index)
        }
        siteTableFragmentBinding.top.grid.setOnItemClickListener { _, _, position, _ ->
            adapter.setPosition(position)
            index = position
            siteTableItemAdapter.setIndex(index)
        }
        //回调本周
        siteTableFragmentBinding.top.backCurrentWeek.setOnClickListener {
            index = -1
            siteTableViewModel.getSites(id, null)
        }

    }

    /**
     * 初始化
     * @param studentBeanList
     */
    private fun initClassData() {
        classList.clear()
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
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun initClassView() {
        val classOptional = classList.stream().filter { it.isDefault == 1 }.findFirst()
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
                        siteTableViewModel.getSites(id, "$week")
                    }
                }
            }
        }
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
            5->{
                view.y = (CouseHeight * selection + SizeUtils.dp2px(4f)).toFloat()
                view.background = resources.getDrawable(R.drawable.bg_table_type4)
                text.text = "晚\n自\n习"
            }
        }
        params.gravity = Gravity.CENTER
        text.layoutParams = params
        leftLayout.addView(view)
    }
}