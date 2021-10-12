package com.yyide.chatim.activity.weekly.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.weekly.details.adapter.DateAdapter
import com.yyide.chatim.activity.weekly.home.WeeklyUtil
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.databinding.FragmentTeacherHomeworkWeeklyBinding
import com.yyide.chatim.dialog.AttendancePop
import com.yyide.chatim.model.GetUserSchoolRsp
import com.yyide.chatim.model.WeeklyDateBean
import com.yyide.chatim.utils.DateUtils

/**
 *
 * 教师/班主任 查看作业统计详情
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class TeacherHomeworkFragment : BaseFragment() {

    private lateinit var viewBinding: FragmentTeacherHomeworkWeeklyBinding

    companion object {
        @JvmStatic
        fun newInstance() =
            TeacherHomeworkFragment().apply {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTeacherHomeworkWeeklyBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    private fun initView() {
        childFragmentManager.beginTransaction().replace(
            viewBinding.flContent.id,
            TeacherHomeworkChartFragment.newInstance()
        ).commit()
        viewBinding.rgTimeType.setOnCheckedChangeListener { group: RadioGroup?, checkedId: Int ->
            when (checkedId) {
                viewBinding.rbAccordingChart.id -> {
                    childFragmentManager.beginTransaction().replace(
                        viewBinding.flContent.id,
                        TeacherHomeworkChartFragment.newInstance()
                    ).commit()
                }
                viewBinding.rbAccordingData.id -> {
                    childFragmentManager.beginTransaction().replace(
                        viewBinding.flContent.id,
                        TeacherHomeworkDataFragment.newInstance()
                    ).commit()
                }
            }
        }
        initClass()
        initDate()
    }

    private var classId = ""
    private var teacherId = ""
    private lateinit var dateTime: WeeklyDateBean.DataBean.TimesBean
    private fun requestTeacher(dateTime: WeeklyDateBean.DataBean.TimesBean?) {
        if (dateTime != null) {
            //loading()
            //viewModel.requestTeacherWeekly(classId, teacherId, dateTime.startTime, dateTime.endTime)
        }
    }

    private fun initDate() {
        //获取日期时间
        dateTime = WeeklyUtil.getDateTime()!!
        if (dateTime != null) {
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

    private fun initClass() {
        SpData.getClassInfo().apply {
            viewBinding.tvClassName.text = classesName
        }

        SpData.getClassList().apply {
            if (size > 1) {
                viewBinding.tvClassName.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    resources.getDrawable(R.mipmap.icon_down),
                    null
                )
                viewBinding.tvClassName.setOnClickListener {
                    val attendancePop = AttendancePop(activity, adapterEvent, "请选择班级")
                    attendancePop.setOnSelectListener { index: Int ->
                        viewBinding.tvClassName.text = adapterEvent.getItem(index).classesName
                        //classId = adapterEvent.getItem(index).classesId
                        //requestTeacher(dateTime)
                    }

                }
            } else {
                viewBinding.tvClassName.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    null,
                    null
                )
            }
            adapterEvent.setList(this)
        }
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
}