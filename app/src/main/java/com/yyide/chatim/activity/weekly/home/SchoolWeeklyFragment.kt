package com.yyide.chatim.activity.weekly.home

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.weekly.WeeklyDetailsActivity
import com.yyide.chatim.activity.weekly.home.viewmodel.SchoolViewModel
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.databinding.DialogWeekMessgeBinding
import com.yyide.chatim.databinding.FragmentSchoolWeeklyBinding
import com.yyide.chatim.databinding.ItemWeeklyChartsVerticalBinding
import com.yyide.chatim.model.*
import com.yyide.chatim.utils.DateUtils

/**
 *
 * 校长周报统计界面
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class SchoolWeeklyFragment : BaseFragment() {

    private lateinit var viewBinding: FragmentSchoolWeeklyBinding
    private val viewModel: SchoolViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSchoolWeeklyBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SchoolWeeklyFragment().apply {}
    }

    private fun initView() {
        viewModel.schoolLiveData.observe(viewLifecycleOwner) {
            dismiss()
            val result = it.getOrNull()
            if (null != result) {
                viewBinding.clContent.visibility = View.VISIBLE
                viewBinding.cardViewNoData.visibility = View.GONE
                //本周小结
                setSummary(result.summary)
                //考勤
                initAttendance(result.attend)
                //作业
//                setWorkView(result.work)
                if (!viewBinding.layoutWeeklySummary.root.isShown && !viewBinding.layoutWeeklySchoolAttendance.root.isShown) {
                    viewBinding.clContent.visibility = View.GONE
                    viewBinding.cardViewNoData.visibility = View.VISIBLE
                }
            } else {//接口返回空的情况处理
                viewBinding.clContent.visibility = View.GONE
                viewBinding.cardViewNoData.visibility = View.VISIBLE
            }
        }
        viewBinding.tvDescs.text = WeeklyUtil.getDesc()
        viewBinding.layoutWeeklySchoolAttendance.cardView.setOnClickListener {
            WeeklyDetailsActivity.jump(
                mActivity,
                WeeklyDetailsActivity.SCHOOL_ATTENDANCE_TYPE,
                "",
                "",
                dateTime
            )
        }

        viewBinding.layoutWeeklyHomeworkSummary.cardView.setOnClickListener {
            WeeklyDetailsActivity.jump(
                mActivity,
                WeeklyDetailsActivity.SCHOOL_HOMEWORK_TYPE,
                "",
                "",
                dateTime
            )
        }
        initDate()
    }

    private lateinit var dateTime: WeeklyDateBean.DataBean.TimesBean
    private var timePosition = -1
    private fun initDate() {
        //获取日期时间
        val dateLists = WeeklyUtil.getDateTimes()
        if (dateLists.isNotEmpty()) {
            timePosition = dateLists.size - 1
            dateTime = dateLists[dateLists.size - 1]
            request(dateTime)
            viewBinding.tvStartTime.setOnClickListener {
                if (dateLists.isNotEmpty()) {
                    if (timePosition > 0) {
                        timePosition -= 1
                        dateTime = dateLists[timePosition]
                        request(dateTime)
                    }
                }
            }
            viewBinding.tvEndTime.setOnClickListener {
                if (dateLists.isNotEmpty()) {
                    if (timePosition < (dateLists.size - 1)) {
                        timePosition += 1
                        dateTime = dateLists[timePosition]
                        request(dateTime)
                    }
                }
            }
        }
    }

    private fun request(dateTime: WeeklyDateBean.DataBean.TimesBean) {
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
        viewModel.requestSchoolWeekly(dateTime.startTime, dateTime.endTime)
    }

    private fun setSummary(summary: SchoolHomeSummary?) {
        viewBinding.layoutWeeklySummary.root.visibility = View.VISIBLE
        if (summary != null) {
            viewBinding.layoutWeeklySummary.tvWeeklyAttendance.text = summary.attend
            viewBinding.layoutWeeklySummary.tvWeeklyHomework.text = summary.work
            viewBinding.layoutWeeklySummary.tvWeeklyShopping.text = summary.expend
        }
        if (summary != null
            && TextUtils.isEmpty(summary.attend)
            && TextUtils.isEmpty(summary.expend)
            && TextUtils.isEmpty(summary.work)
        ) {
            viewBinding.layoutWeeklySummary.root.visibility = View.GONE
        }
    }

    private fun initAttendance(attend: SchoolHomeAttendance?) {
        if (attend != null) {
            viewBinding.layoutWeeklySchoolAttendance.root.visibility = View.VISIBLE
            viewBinding.layoutWeeklySchoolAttendance.textView1.visibility = View.VISIBLE
            viewBinding.layoutWeeklySchoolAttendance.layoutCharts2.root.visibility =
                View.VISIBLE
            viewBinding.layoutWeeklySchoolAttendance.textView.visibility = View.VISIBLE
            viewBinding.layoutWeeklySchoolAttendance.layoutCharts.root.visibility = View.VISIBLE
            //学生考勤
            if (attend.studentAttend != null) {
                viewBinding.layoutWeeklySchoolAttendance.layoutCharts.recyclerview.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                viewBinding.layoutWeeklySchoolAttendance.layoutCharts.recyclerview.adapter =
                    adapterStudentAttendance
                adapterStudentAttendance.setList(attend.studentAttend)
                adapterStudentAttendance.setOnItemClickListener { adapter, view, position ->
                    selectStudentPosition = if (selectStudentPosition != position) {
                        position
                    } else {
                        -1
                    }
                    adapterStudentAttendance.notifyDataSetChanged()
                }
            } else {
                viewBinding.layoutWeeklySchoolAttendance.textView.visibility = View.GONE
                viewBinding.layoutWeeklySchoolAttendance.layoutCharts.root.visibility = View.GONE
            }

            //教师考勤
            if (attend.teacherAttend != null) {
                viewBinding.layoutWeeklySchoolAttendance.layoutCharts2.recyclerview.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                viewBinding.layoutWeeklySchoolAttendance.layoutCharts2.recyclerview.adapter =
                    adapterTeacherAttendance
                adapterTeacherAttendance.setList(attend.teacherAttend)
                adapterTeacherAttendance.setOnItemClickListener { adapter, view, position ->
                    selectTeacherPosition = if (selectTeacherPosition != position) {
                        position
                    } else {
                        -1
                    }
                    adapterTeacherAttendance.notifyDataSetChanged()
                }
            } else {
                viewBinding.layoutWeeklySchoolAttendance.textView1.visibility = View.GONE
                viewBinding.layoutWeeklySchoolAttendance.layoutCharts2.root.visibility =
                    View.GONE
            }
            if (attend.teacherAttend != null && attend.studentAttend != null && attend.teacherAttend!!.isEmpty() && attend.studentAttend!!.isEmpty()) {
                viewBinding.layoutWeeklySchoolAttendance.root.visibility = View.GONE
            }
        } else {
            viewBinding.layoutWeeklySchoolAttendance.root.visibility = View.GONE
        }
    }

    private fun setWorkView(work: SchoolHomeWork?) {

    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    private fun show(view: View, desc: String, number: String) {
        val inflate = DialogWeekMessgeBinding.inflate(layoutInflater)
        val popWindow = PopupWindow(
            inflate.root,
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true
        )
        inflate.tvName.text = desc
        inflate.tvProgress.text = number
        popWindow.setBackgroundDrawable(context?.getDrawable(android.R.color.transparent))
        popWindow.setOnDismissListener {
            if (selectTeacherPosition > -1) {
                adapterTeacherAttendance.notifyItemChanged(selectTeacherPosition)
                selectTeacherPosition = -1
            } else if (selectStudentPosition > -1) {
                adapterStudentAttendance.notifyItemChanged(selectStudentPosition)
                selectStudentPosition = -1
            }
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

    /**
     * 学生考勤数据
     */
    private var selectStudentPosition = -1
    private val adapterStudentAttendance = object :
        BaseQuickAdapter<SchoolHomeStudentAttend, BaseViewHolder>(R.layout.item_weekly_charts_vertical) {
        override fun convert(holder: BaseViewHolder, item: SchoolHomeStudentAttend) {
            val bind = ItemWeeklyChartsVerticalBinding.bind(holder.itemView)
            //bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.transparent))
            bind.tvProgress.text = "${item.value}%"
            bind.tvWeek.text = item.name
            //bind.progressbar.progress = if (item.value <= 0) 0 else item.value.toInt()
            WeeklyUtil.setAnimation(
                bind.progressbar,
                if (item.value <= 0) 0 else item.value.toInt()
            )
        }
    }

    /**
     * 教师考勤数据
     */
    private var selectTeacherPosition = -1
    private val adapterTeacherAttendance = object :
        BaseQuickAdapter<SchoolHomeTeacherAttend, BaseViewHolder>(R.layout.item_weekly_charts_vertical) {
        override fun convert(holder: BaseViewHolder, item: SchoolHomeTeacherAttend) {
            val bind = ItemWeeklyChartsVerticalBinding.bind(holder.itemView)
            bind.tvProgress.text = "${item.value}%"
            bind.tvWeek.text = item.name
//            bind.progressbar.progress = if (item.value <= 0) 0 else item.value.toInt()
            WeeklyUtil.setAnimation(
                bind.progressbar,
                if (item.value <= 0) 0 else item.value.toInt()
            )
        }
    }

}