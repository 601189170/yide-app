package com.yyide.chatim.activity.weekly.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.weekly.WeeklyDetailsActivity
import com.yyide.chatim.activity.weekly.details.BarCharts
import com.yyide.chatim.activity.weekly.details.adapter.ClassAdapter
import com.yyide.chatim.activity.weekly.details.adapter.HotAdapter
import com.yyide.chatim.activity.weekly.home.viewmodel.TeacherViewModel
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.databinding.*
import com.yyide.chatim.dialog.AttendancePop
import com.yyide.chatim.model.*
import com.yyide.chatim.utils.DateUtils
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList


/**
 *
 * 班主任/教师 周报统计界面
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class TeacherWeeklyFragment : BaseFragment() {

    private lateinit var viewBinding: FragmentTeacherChargeWeeklyBinding
    private val viewModel: TeacherViewModel by viewModels()
    private lateinit var classBean: GetUserSchoolRsp.DataBean.FormBean

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTeacherChargeWeeklyBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TeacherWeeklyFragment().apply {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        classBean = GetUserSchoolRsp.DataBean.FormBean()
        if (SpData.getClassInfo() != null) {
            classBean = SpData.getClassInfo()
        }
        initView()
    }

    private fun initView() {
        viewModel.teacherLiveData.observe(viewLifecycleOwner) {
            dismiss()
            val result = it.getOrNull()
            if (null != result) {
                viewBinding.clContent.visibility = View.VISIBLE
                viewBinding.cardViewNoData1.visibility = View.GONE
                viewBinding.attendance.tvAttendDesc.text = result.rank
                setSummary(result.summary)
                setAttendance(result.attend)
                if (result.summary == null && result.attend == null) {
                    viewBinding.clContent.visibility = View.GONE
                    viewBinding.cardViewNoData1.visibility = View.VISIBLE
                }

            } else {//接口返回空的情况处理
                viewBinding.clContent.visibility = View.GONE
                viewBinding.cardViewNoData1.visibility = View.VISIBLE
            }
        }
        viewBinding.attendance.cardView.setOnClickListener {
            WeeklyDetailsActivity.jump(
                mActivity,
                WeeklyDetailsActivity.HEAD_TEACHER_ATTENDANCE_TYPE,
                "",
                "",
                dateTime,
                classBean
            )
        }
        viewBinding.homework.cardViewWork.setOnClickListener {
            WeeklyDetailsActivity.jump(
                mActivity, WeeklyDetailsActivity.HEAD_TEACHER_HOMEWORK_TYPE, "",
                "",
                dateTime,
                classBean
            )
        }
        viewBinding.tvDescs.text = WeeklyUtil.getDesc()
        teacherId = SpData.getIdentityInfo().teacherId
        if (classBean != null && !TextUtils.isEmpty(classBean.classesId)) {
            classId = classBean.classesId
        }

        initClassMenu()
        initDate()
//        initHomework()
    }

    private var classId = ""
    private var teacherId = ""
    private lateinit var dateTime: WeeklyDateBean.DataBean.TimesBean
    private var timePosition = -1
    private fun initDate() {
        //获取日期时间
        val dateLists = WeeklyUtil.getDateTimes()
        if (dateLists.isNotEmpty()) {
            timePosition = dateLists.size - 1
            dateTime = dateLists[dateLists.size - 1]
            requestTeacher(dateTime)
            viewBinding.tvStartTime.setOnClickListener {
                if (dateLists.isNotEmpty()) {
                    if (timePosition > 0) {
                        timePosition -= 1
                        dateTime = dateLists[timePosition]
                        requestTeacher(dateTime)
                    }
                }
            }
            viewBinding.tvEndTime.setOnClickListener {
                if (dateLists.isNotEmpty()) {
                    if (timePosition < (dateLists.size - 1)) {
                        timePosition += 1
                        dateTime = dateLists[timePosition]
                        requestTeacher(dateTime)
                    }
                }
            }
        }
    }

    private fun initClassMenu() {
        if (SpData.getClassInfo() != null) {
            viewBinding.tvEvent.text = SpData.getClassInfo().classesName + "的周报"
        }
        val classList = SpData.getClassList()
        val adapterEvent = ClassAdapter()
        if (classList != null) {
            if (classList.size > 1) {
                viewBinding.tvEvent.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    resources.getDrawable(R.mipmap.icon_down),
                    null
                )
                viewBinding.tvEvent.setOnClickListener {
                    val attendancePop = AttendancePop(activity, adapterEvent, "请选择班级")
                    attendancePop.setOnSelectListener { index: Int ->
                        viewBinding.tvEvent.text = adapterEvent.getItem(index).classesName + "的周报"
                        val item = adapterEvent.getItem(index)
                        classBean = item
                        classId = adapterEvent.getItem(index).classesId
                        adapterEvent.setClassId(classId)
                        requestTeacher(dateTime)
                    }

                }
            } else {
                viewBinding.tvEvent.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
            if (SpData.getClassInfo() != null) {
                adapterEvent.setClassId(SpData.getClassInfo().classesId)
            }
            adapterEvent.setList(classList)
        }
    }

    private fun requestTeacher(dateTime: WeeklyDateBean.DataBean.TimesBean?) {
        if (dateTime != null) {
            viewBinding.tvStartTime.text = DateUtils.formatTime(
                dateTime.startTime,
                "yyyy-MM-dd HH:mm:ss",
                "MM/dd"
            )
            viewBinding.tvEndTime.text = DateUtils.formatTime(
                dateTime.endTime,
                "yyyy-MM-dd HH:mm:ss",
                "MM/dd"
            )
            loading()
            viewModel.requestTeacherWeekly(classId, teacherId, dateTime.startTime, dateTime.endTime)
        }
    }

    private fun setSummary(summary: WeeklyTeacherSummary?) {
        if (summary != null) {
            viewBinding.summary.root.visibility = View.VISIBLE
            viewBinding.summary.tvWeeklyAttendance.text = summary.attend
            viewBinding.summary.tvWeeklyHomework.text = summary.work
            viewBinding.summary.tvWeeklyShopping.text = summary.expend
        }

        if (summary == null || (summary != null
                    && TextUtils.isEmpty(summary.attend)
                    && TextUtils.isEmpty(summary.expend)
                    && TextUtils.isEmpty(summary.work))
        ) {
            viewBinding.summary.root.visibility = View.GONE
        }
    }

    private fun initHomework() {
        val datas = mutableListOf<WeeklyTeacherClassAttendance>()
        datas.add(WeeklyTeacherClassAttendance("本班总作业数", "50份"))
        datas.add(WeeklyTeacherClassAttendance("上周总作业数", "60"))
        datas.add(WeeklyTeacherClassAttendance("作业最多科目", "语文"))
        datas.add(WeeklyTeacherClassAttendance("作业最少科目", "化学"))
        datas.add(WeeklyTeacherClassAttendance("本班总作业数", "50份"))
        datas.add(WeeklyTeacherClassAttendance("本班总作业数", "50份"))
        viewBinding.homework.root.visibility = View.VISIBLE
        //设置班级考勤
        viewBinding.homework.hotRecyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        viewBinding.homework.hotRecyclerview.adapter = adapterHot2
//        if (datas.size < spanCount) {
//            spanCount = datas.size
//        }
        val splitList = splitList(datas, 4)
        val hotList = mutableListOf<Int>()
        if (splitList != null) {
            for (item in splitList.indices) {
                hotList.add(item)
            }
        }
        if (hotList != null && hotList.size > 1) {
            adapterHot2.setList(hotList)
        }
        if (splitList != null && splitList.isNotEmpty()) {
//            initHotScroll(splitList)
            val viewList = mutableListOf<View>()
            splitList.forEachIndexed { index, schoolAttendance ->
                viewList.add(attendanceBanner(splitList[index], 4))
            }
            val announAdapter = HotAdapter(viewList)
            viewBinding.homework.announRoll.adapter = announAdapter
            viewBinding.homework.announRoll.addOnPageChangeListener(object :
                ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    val dex: Int = position % announAdapter.viewLists.size
                    hotIndex2 = dex
                    adapterHot2.notifyDataSetChanged()
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
        }
    }

    private var spanCount = 3
    private fun setAttendance(attend: WeeklyTeacherAttendance?) {
        if (attend == null) {
            viewBinding.attendance.root.visibility = View.GONE
            return
        }
        spanCount = 3
        viewBinding.attendance.root.visibility = View.VISIBLE
        if (attend != null && attend.classAttend.isEmpty() && attend.teacherAttend.isEmpty()) {
            viewBinding.attendance.root.visibility = View.GONE
        }
        if (attend != null && attend.classAttend.isEmpty()) {
            viewBinding.attendance.hotRecyclerview.visibility = View.GONE
            viewBinding.attendance.announRoll.visibility = View.GONE
        } else {
            viewBinding.attendance.hotRecyclerview.visibility = View.VISIBLE
            viewBinding.attendance.announRoll.visibility = View.VISIBLE
        }
        viewBinding.attendance.tvWeeklyAttendance.text = attend.attend
        viewBinding.attendance.tvWeeklyHomework.text = attend.course
        //设置班级考勤
        viewBinding.attendance.hotRecyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        viewBinding.attendance.hotRecyclerview.adapter = adapterHot
        if (attend?.classAttend != null && attend.classAttend.size < spanCount) {
            spanCount = attend.classAttend.size
        }
        val splitList = splitList(attend.classAttend, spanCount)
        val hotList = mutableListOf<Int>()
        if (splitList != null) {
            for (item in splitList.indices) {
                hotList.add(item)
            }
        }
        if (hotList != null && hotList.size > 1) {
            adapterHot.setList(hotList)
        } else {
            adapterHot.setList(null)
        }
        if (splitList != null && splitList.isNotEmpty()) {
            initHotScroll(splitList)
        }

        val mBarCharts = BarCharts()
        mBarCharts.showBarChart2(
            viewBinding.attendance.layoutCharts.barChart,
            mBarCharts.getBarData(attend.teacherAttend),
            attend.teacherAttend,
            true
        )
    }

    private fun initHotScroll(attendance: List<List<WeeklyTeacherClassAttendance>>) {
        val viewList = mutableListOf<View>()
        attendance.forEachIndexed { index, schoolAttendance ->
            viewList.add(attendanceBanner(attendance[index], spanCount))
        }
        val announAdapter = HotAdapter(viewList)
        viewBinding.attendance.announRoll.adapter = announAdapter
        viewBinding.attendance.announRoll.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                val dex: Int = position % announAdapter.viewLists.size
                hotIndex = dex
                adapterHot.notifyDataSetChanged()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    /**
     * 按指定大小，分隔集合，将集合按规定个数分为n个部分
     * @param <T>
     *
     * @param list
     * @param len
     * @return
    </T> */
    private fun splitList(
        list: List<WeeklyTeacherClassAttendance>?,
        len: Int
    ): List<List<WeeklyTeacherClassAttendance>>? {
        if (list == null || list.isEmpty() || len < 1) {
            return Collections.emptyList()
        }
        val result: MutableList<List<WeeklyTeacherClassAttendance>> =
            ArrayList()
        val size = list.size
        val count = (size + len - 1) / len
        for (i in 0 until count) {
            val subList = list.subList(i * len, if ((i + 1) * len > size) size else len * (i + 1))
            result.add(subList)
        }
        return result
    }

    private var hotIndex = 0
    private val adapterHot = object :
        BaseQuickAdapter<Int, BaseViewHolder>(R.layout.item_hot) {
        override fun convert(holder: BaseViewHolder, item: Int) {
            val bind = ItemHotBinding.bind(holder.itemView)
            if (hotIndex == holder.bindingAdapterPosition)
                bind.view.setBackgroundResource(R.drawable.hot_round_wilhte)
            else
                bind.view.setBackgroundResource(R.drawable.hot_round_grray)
        }
    }

    private var hotIndex2 = 0
    private val adapterHot2 = object :
        BaseQuickAdapter<Int, BaseViewHolder>(R.layout.item_hot) {
        override fun convert(holder: BaseViewHolder, item: Int) {
            val bind = ItemHotBinding.bind(holder.itemView)
            if (hotIndex2 == holder.bindingAdapterPosition)
                bind.view.setBackgroundResource(R.drawable.hot_round_wilhte)
            else
                bind.view.setBackgroundResource(R.drawable.hot_round_grray)
        }
    }

    private fun attendanceBanner(
        attendance: List<WeeklyTeacherClassAttendance>,
        size: Int
    ): View {
        val view = ItemHBinding.inflate(layoutInflater)
        view.attendanceRecyclerview.layoutManager = GridLayoutManager(activity, size)
        val attendanceBannerAdapter = object :
            BaseQuickAdapter<WeeklyTeacherClassAttendance, BaseViewHolder>(R.layout.item_weekly_attendance) {
            override fun convert(holder: BaseViewHolder, item: WeeklyTeacherClassAttendance) {
                val bind = ItemWeeklyAttendanceBinding.bind(holder.itemView)
                bind.viewLine.visibility =
                    if (holder.bindingAdapterPosition == 0) View.GONE else View.VISIBLE
                bind.tvEventName.text = item.name
                bind.tvAttendance.text = item.value
            }
        }
        view.attendanceRecyclerview.adapter = attendanceBannerAdapter
        attendanceBannerAdapter.setList(attendance)
        return view.root
    }
    
}