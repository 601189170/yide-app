package com.yyide.chatim.activity.weekly.home

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tencent.mmkv.MMKV
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.weekly.WeeklyDetailsActivity
import com.yyide.chatim.activity.weekly.details.adapter.DateAdapter
import com.yyide.chatim.activity.weekly.details.adapter.HotAdapter
import com.yyide.chatim.activity.weekly.home.viewmodel.SchoolViewModel
import com.yyide.chatim.activity.weekly.home.viewmodel.TeacherViewModel
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.base.MMKVConstant
import com.yyide.chatim.databinding.*
import com.yyide.chatim.dialog.AttendancePop
import com.yyide.chatim.dialog.WeeklyTopPop
import com.yyide.chatim.model.*
import com.yyide.chatim.utils.DateUtils
import java.util.*
import kotlin.collections.ArrayList


/**
 *
 * 班主任/教师 周报统计界面
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class TeacherChargeWeeklyFragment : BaseFragment() {

    private lateinit var viewBinding: FragmentTeacherChargeWeeklyBinding
    private val viewModel: TeacherViewModel by viewModels()

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
            TeacherChargeWeeklyFragment().apply {}
    }

    private var classId = ""
    private var teacherId = ""
    private lateinit var dateTime: WeeklyDateBean.DataBean.TimeBean
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        request()
        SpData.getClassInfo().apply {
            viewBinding.tvEvent.text = classesName
        }

        SpData.getClassList().apply {
            if (size > 1) {
                viewBinding.tvEvent.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    getResources().getDrawable(R.mipmap.icon_down),
                    null
                )
                viewBinding.tvEvent.setOnClickListener {
                    val attendancePop = AttendancePop(activity, adapterEvent, "请选择班级")
                    attendancePop.setOnSelectListener { index: Int ->
                        viewBinding.tvEvent.text = adapterEvent.getItem(index).classesName
                        classId = adapterEvent.getItem(index).classesId
                        requestTeacher(dateTime)
                    }

                }
            } else {
                viewBinding.tvEvent.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
            adapterEvent.setList(this)
        }
        viewBinding.tvDescs.text = WeeklyUtil.getDesc()

        viewBinding.attendance.cardView.setOnClickListener {
            WeeklyDetailsActivity.jump(mActivity, WeeklyDetailsActivity.SCHOOL_ATTENDANCE_TYPE)
        }

        viewBinding.homework.cardViewWork.setOnClickListener {
            WeeklyDetailsActivity.jump(mActivity, WeeklyDetailsActivity.SCHOOL_ATTENDANCE_TYPE)
        }
        viewBinding.tvDescs.text = WeeklyUtil.getDesc()
        teacherId = SpData.getIdentityInfo().teacherId
        if (SpData.getClassInfo() != null) {
            classId = SpData.getClassInfo().classesId
        }
        dateTime = WeeklyUtil.getDateTime()!!
        if (dateTime != null){
            viewBinding.tvTime.text = getString(
                R.string.startTime_endTime, DateUtils.formatTime(
                    dateTime.startTime,
                    "yyyy-MM-dd HH:mm:ss",
                    "MM/dd"
                ), DateUtils.formatTime(
                    dateTime.endTime,
                    "yyyy-MM-dd HH:mm:ss",
                    "MM/dd"
                )
            )
        }
        requestTeacher(dateTime)
        val dateLists = WeeklyUtil.getDateTimes()
        val adapterDate = DateAdapter()
        if (dateLists.isNotEmpty()) {
            adapterDate.setList(dateLists)
        }
        viewBinding.tvTime.setOnClickListener {
            val attendancePop = AttendancePop(activity, adapterDate, "请选择时间")
            attendancePop.setOnSelectListener { index: Int ->
//                indexDate = index
                viewBinding.tvTime.text = getString(
                    R.string.startTime_endTime, DateUtils.formatTime(
                        dateLists[index].startTime,
                        "yyyy-MM-dd HH:mm:ss",
                        "MM/dd"
                    ), DateUtils.formatTime(
                        dateLists[index].endTime,
                        "yyyy-MM-dd HH:mm:ss",
                        "MM/dd"
                    )
                )
                requestTeacher(dateTime)
            }
        }
    }

    private fun requestTeacher(dateTime: WeeklyDateBean.DataBean.TimeBean?) {
        if (dateTime != null) {
            loading()
            viewModel.requestTeacherWeekly(classId, teacherId, dateTime.startTime, dateTime.endTime)
        }
    }

    private fun request() {
        viewModel.teacherLiveData.observe(viewLifecycleOwner) {
            dismiss()
            val result = it.getOrNull()
            if (null != result) {
                viewBinding.clContent.visibility = View.VISIBLE
                viewBinding.cardViewNoData.visibility = View.GONE
                viewBinding.attendance.tvAttendDesc.text = result.rank
                setSummary(result.summary)
                setAttendance(result.attend)
            } else {//接口返回空的情况处理
                viewBinding.clContent.visibility = View.GONE
                viewBinding.cardViewNoData.visibility = View.VISIBLE
            }
        }
    }

    private fun setSummary(summary: WeeklyTeacherSummary) {
        viewBinding.summary.tvWeeklyAttendance.text = summary.attend
        viewBinding.summary.tvWeeklyHomework.text = summary.work
        viewBinding.summary.tvWeeklyShopping.text = summary.expend
    }

    private val spanCount = 3
    private fun setAttendance(attend: WeeklyTeacherAttendance) {
        viewBinding.attendance.tvWeeklyAttendance.text = attend.attend
        viewBinding.attendance.tvWeeklyHomework.text = attend.course
        //设置班级考勤
        viewBinding.attendance.hotRecyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        viewBinding.attendance.hotRecyclerview.adapter = adapterHot

        val splitList = splitList(attend.classAttend, spanCount)
        val hotList = mutableListOf<Int>()
        if (splitList != null) {
            for (item in splitList.indices) {
                hotList.add(item)
            }
        }
        adapterHot.setList(hotList)
        if (splitList != null && splitList.isNotEmpty()) {
            initHotScroll(splitList)
        }

        //我的考勤
        viewBinding.attendance.layoutCharts.recyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        viewBinding.attendance.layoutCharts.recyclerview.adapter =
            adapterAttendance

        adapterAttendance.setOnItemClickListener { adapter, view, position ->
            selectPosition = if (selectPosition != position) {
                position
            } else {
                -1
            }
            adapterAttendance.notifyDataSetChanged()
        }
        adapterAttendance.setList(attend.teacherAttend)
    }

    private fun initHotScroll(attendance: List<List<WeeklyTeacherClassAttendance>>) {
        val viewList = mutableListOf<View>()
        attendance.forEachIndexed { index, schoolAttendance ->
            viewList.add(attendanceBanner(attendance[index]))
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

    @SuppressLint("ResourceAsColor", "ResourceType")
    private fun show(view: View, desc: String, number: Int) {
        val inflate = DialogWeekMessgeBinding.inflate(layoutInflater)
        val popWindow = PopupWindow(
            inflate.root,
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true
        )
        inflate.tvName.text = desc
        inflate.tvProgress.text = "${number}%"
        popWindow.setBackgroundDrawable(context?.getDrawable(android.R.color.transparent))
        popWindow.setOnDismissListener {
            selectPosition = -1
            adapterAttendance.notifyDataSetChanged()
        }
        //获取需要在其上方显示的控件的位置信息
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        //在控件上方显示
        popWindow.showAtLocation(
            view,
            Gravity.NO_GRAVITY,
            (location[0]) - inflate.root.width / 2,
            location[1] - inflate.root.height
        )
    }

    private var index = -1
    private val adapterEvent = object :
        BaseQuickAdapter<GetUserSchoolRsp.DataBean.FormBean, BaseViewHolder>(R.layout.swich_class_item) {
        override fun convert(
            holder: BaseViewHolder,
            item: GetUserSchoolRsp.DataBean.FormBean
        ) {
            holder.setText(R.id.className, item.classesName)
            holder.getView<View>(R.id.select).visibility =
                if (index == holder.layoutPosition) View.VISIBLE else View.GONE
            if (this.itemCount - 1 == holder.layoutPosition) {
                holder.getView<View>(R.id.view_line).visibility = View.GONE
            } else {
                holder.getView<View>(R.id.view_line).visibility = View.VISIBLE
            }
        }
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

    private fun attendanceBanner(attendance: List<WeeklyTeacherClassAttendance>): View {
        val view = ItemHBinding.inflate(layoutInflater)
        view.attendanceRecyclerview.layoutManager = GridLayoutManager(activity, spanCount)
        val adapterAttendance = object :
            BaseQuickAdapter<WeeklyTeacherClassAttendance, BaseViewHolder>(R.layout.item_weekly_attendance) {
            override fun convert(holder: BaseViewHolder, item: WeeklyTeacherClassAttendance) {
                val bind = ItemWeeklyAttendanceBinding.bind(holder.itemView)
                bind.viewLine.visibility =
                    if (holder.bindingAdapterPosition == 0) View.GONE else View.VISIBLE
                bind.tvEventName.text = item.name
                bind.tvAttendance.text = "${item.value}%"
            }
        }
        view.attendanceRecyclerview.adapter = adapterAttendance
        adapterAttendance.setList(attendance)
        return view.root
    }

    fun setAnimation(view: ProgressBar, mProgressBar: Int) {
        val animator = ValueAnimator.ofInt(0, mProgressBar).setDuration(600)
        animator.addUpdateListener { valueAnimator: ValueAnimator ->
            view.progress = valueAnimator.animatedValue as Int
        }
        animator.start()
    }

    /**
     * 考勤数据
     */
    private var selectPosition = -1
    private val adapterAttendance = object :
        BaseQuickAdapter<WeeklyTeacherTeacherAttend, BaseViewHolder>(R.layout.item_weekly_charts_vertical) {
        override fun convert(holder: BaseViewHolder, item: WeeklyTeacherTeacherAttend) {
            val bind = ItemWeeklyChartsVerticalBinding.bind(holder.itemView)
            bind.tvProgress.text = "${item.value}%"
            bind.tvWeek.text = item.name
            setAnimation(bind.progressbar, if (item.value <= 0) 0 else item.value.toInt())
            bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.transparent))
            if (selectPosition == holder.bindingAdapterPosition) {
                bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.charts_bg))
            }
        }
    }
}