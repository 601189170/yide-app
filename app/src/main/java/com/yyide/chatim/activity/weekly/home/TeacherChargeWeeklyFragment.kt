package com.yyide.chatim.activity.weekly.home

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tencent.mmkv.MMKV
import com.yyide.chatim.R
import com.yyide.chatim.activity.weekly.home.viewmodel.SchoolViewModel
import com.yyide.chatim.activity.weekly.home.viewmodel.TeacherViewModel
import com.yyide.chatim.base.MMKVConstant
import com.yyide.chatim.databinding.DialogWeekMessgeBinding
import com.yyide.chatim.databinding.FragmentTeacherChargeWeeklyBinding
import com.yyide.chatim.dialog.AttendancePop
import com.yyide.chatim.dialog.WeeklyTopPop
import com.yyide.chatim.model.AttendanceCheckRsp
import com.yyide.chatim.utils.DateUtils
import java.util.HashSet


/**
 *
 * 班主任/教师 周报统计界面
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class TeacherChargeWeeklyFragment : Fragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        request()
        viewBinding.tvEvent.text = "事件"
        viewBinding.tvDescs.text = WeeklyUtil.getDesc()
        viewBinding.tvEvent.setOnClickListener {
            val attendancePop = AttendancePop(activity, adapterEvent, "请选择考勤事件")
            attendancePop.setOnSelectListener { index: Int ->
                this.index = index
                viewBinding.tvEvent.text = adapterEvent.getItem(index).toString()
            }
        }

        viewBinding.tvTime.text = "9-24 10:28"
        viewBinding.tvTime.setOnClickListener {
            val attendancePop = AttendancePop(activity, adapterDate, "请选择时间")
            attendancePop.setOnSelectListener { index: Int ->
                this.indexDate = index
                viewBinding.tvTime.text = adapterDate.getItem(index).toString()
            }
        }
        initChart()
    }

    private fun request() {
        viewModel.teacherLiveData.observe(viewLifecycleOwner) {
            val bean = it.getOrNull()
            if (null != bean) {
                viewBinding.clContent.visibility = View.VISIBLE
                viewBinding.cardViewNoData.visibility = View.GONE
            } else {//接口返回空的情况处理
                viewBinding.clContent.visibility = View.GONE
                viewBinding.cardViewNoData.visibility = View.VISIBLE
            }
        }
        val startData = DateUtils.getDate(DateUtils.getBeginDayOfWeek().time)
        val endData = DateUtils.getDate(System.currentTimeMillis())
        var classId = ""
        var teacherId = ""
        viewModel.requestTeacherWeekly(classId, teacherId, startData, endData)
    }

    private fun initChart() {
        viewBinding.attendance.layoutCharts.recyclerview.layoutManager =
            LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        viewBinding.attendance.layoutCharts.recyclerview.adapter = chartsAdapter
        chartsAdapter.setOnItemClickListener { adapter, view, position ->

            selectPosition = if (selectPosition != position) {
                position
            } else {
                -1
            }
            chartsAdapter.notifyDataSetChanged()
            //show(view, chartsAdapter.getItem(position), 60)
        }
        val datas = mutableListOf<String>()
        datas.add("考勤考勤1")
        datas.add("考勤考勤2")
        datas.add("考勤考勤3")
        datas.add("考勤考勤4")
        datas.add("考勤考勤5")
        chartsAdapter.setList(datas)
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
            chartsAdapter.notifyDataSetChanged()
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
        BaseQuickAdapter<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean?, BaseViewHolder>(R.layout.swich_class_item) {
        override fun convert(
            holder: BaseViewHolder,
            item: AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean?
        ) {
            holder.setText(R.id.className, item?.attName)
            holder.getView<View>(R.id.select).visibility =
                if (index == holder.layoutPosition) View.VISIBLE else View.GONE
            if (this.itemCount - 1 == holder.layoutPosition) {
                holder.getView<View>(R.id.view_line).visibility = View.GONE
            } else {
                holder.getView<View>(R.id.view_line).visibility = View.VISIBLE
            }
        }
    }

    private var indexDate = -1
    private val adapterDate = object :
        BaseQuickAdapter<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean?, BaseViewHolder>(R.layout.swich_class_item) {
        override fun convert(
            holder: BaseViewHolder,
            item: AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean?
        ) {
            holder.setText(R.id.className, item?.attName)
            holder.getView<View>(R.id.select).visibility =
                if (indexDate == holder.layoutPosition) View.VISIBLE else View.GONE
            if (this.itemCount - 1 == holder.layoutPosition) {
                holder.getView<View>(R.id.view_line).visibility = View.GONE
            } else {
                holder.getView<View>(R.id.view_line).visibility = View.VISIBLE
            }
        }
    }

    private var selectPosition = -1
    private val chartsAdapter =
        object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_weekly_charts_vertical) {
            override fun convert(holder: BaseViewHolder, item: String) {
                val rodom = (10..100).random()
                val progressBar = holder.getView<ProgressBar>(R.id.progressbar)
                val constraintLayout = holder.getView<ConstraintLayout>(R.id.constraintLayout)
                progressBar.progress = rodom
                holder.setText(R.id.tv_week, item)
                holder.setText(R.id.tv_progress, "${rodom}%")
                constraintLayout.setBackgroundColor(context.resources.getColor(R.color.transparent))
                if (selectPosition == holder.bindingAdapterPosition) {
                    constraintLayout.setBackgroundColor(context.resources.getColor(R.color.charts_bg))
                }
                //when (holder.bindingAdapterPosition) {
//                0 -> {
//                    holder.setText(R.id.tv_week, "周一")
//                }
//                1 -> {
//                    holder.setText(R.id.tv_week, "周二")
//                }
//                2 -> {
//                    holder.setText(R.id.tv_week, "周三")
//                }
//                3 -> {
//                    holder.setText(R.id.tv_week, "周四")
//                }
//                4 -> {
//                    holder.setText(R.id.tv_week, "周五")
//                }
//                5 -> {
//                    holder.setText(R.id.tv_week, "周六")
//                }
//                6 -> {
//                    holder.setText(R.id.tv_week, "周日")
//                }
            }
        }

}