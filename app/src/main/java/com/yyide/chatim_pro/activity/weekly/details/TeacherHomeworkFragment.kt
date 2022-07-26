package com.yyide.chatim_pro.activity.weekly.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.SpData
import com.yyide.chatim_pro.activity.weekly.home.WeeklyUtil
import com.yyide.chatim_pro.base.BaseFragment
import com.yyide.chatim_pro.databinding.FragmentTeacherHomeworkWeeklyBinding
import com.yyide.chatim_pro.databinding.ItemWeeklyAttendanceBinding
import com.yyide.chatim_pro.dialog.AttendancePop
import com.yyide.chatim_pro.model.GetUserSchoolRsp
import com.yyide.chatim_pro.model.SchoolHomeAttend
import com.yyide.chatim_pro.model.WeeklyDateBean
import com.yyide.chatim_pro.utils.DateUtils

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
        fun newInstance(dateTime: WeeklyDateBean.DataBean.TimesBean) =
            TeacherHomeworkFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("item", dateTime)
                }
            }
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
        setData()
    }

    private fun setData() {
        viewBinding.workRecyclerview.layoutManager = GridLayoutManager(activity, 4)
        viewBinding.workRecyclerview.adapter = workAdapter
        val datas1 = mutableListOf<SchoolHomeAttend>()
        datas1.add(SchoolHomeAttend("总作业", "", 50.3))
        datas1.add(SchoolHomeAttend("上周作业", "", 50.3))
        datas1.add(SchoolHomeAttend("每天作业", "", 50.3))
        datas1.add(SchoolHomeAttend("其他反馈", "", 50.3))
        workAdapter.setList(datas1)
    }

    private lateinit var dateTime: WeeklyDateBean.DataBean.TimesBean
    private var timePosition = -1
    private fun initDate() {
        //获取日期时间
        arguments?.apply {
            dateTime = getSerializable("item") as WeeklyDateBean.DataBean.TimesBean
        }
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
        //loading()
        //viewModel.requestSchoolWeekly(dateTime.startTime, dateTime.endTime)
    }

    private fun initClass() {
        SpData.getClassInfo().apply {
            viewBinding.tvClassName.text = classesName
        }

        SpData.getDuplicationClassList().apply {
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

    private val workAdapter =
        object :
            BaseQuickAdapter<SchoolHomeAttend, BaseViewHolder>(R.layout.item_weekly_attendance) {
            override fun convert(holder: BaseViewHolder, item: SchoolHomeAttend) {
                val viewBind = ItemWeeklyAttendanceBinding.bind(holder.itemView)
                viewBind.tvAttendance.text = "${item.value}"
                viewBind.tvEventName.text = item.name
                viewBind.viewLine.visibility =
                    if (holder.adapterPosition == 0) View.GONE else View.VISIBLE
            }

        }
}