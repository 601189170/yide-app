package com.yyide.chatim.activity.weekly.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.attendance.fragment.SchoolStudentAttendanceFragment
import com.yyide.chatim.databinding.FragmentParentsWeeklyBinding
import com.yyide.chatim.dialog.AttendancePop
import com.yyide.chatim.dialog.WeeklyTopPop
import com.yyide.chatim.model.AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean

/**
 *
 * 家长周报统计界面
 * date 2021年9月15日15:11:01
 * author LRZ
 */
open class ParentsWeeklyFragment : Fragment() {

    private lateinit var viewBinding: FragmentParentsWeeklyBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentParentsWeeklyBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ParentsWeeklyFragment().apply {}
    }

    private fun initView() {
        viewBinding.tvEvent.setOnClickListener {
            val attendancePop = AttendancePop(activity, adapterEvent, "请选择考勤事件")
            attendancePop.setOnSelectListener { index: Int ->

            }
        }

        viewBinding.tvTime.setOnClickListener {
            val attendancePop = AttendancePop(activity, adapterDate, "请选择考勤事件")
            attendancePop.setOnSelectListener { index: Int ->

            }
        }
    }

    private var index = -1

    private val adapterEvent = object :
        BaseQuickAdapter<SchoolPeopleAllFormBean?, BaseViewHolder>(R.layout.swich_class_item) {
        override fun convert(holder: BaseViewHolder, item: SchoolPeopleAllFormBean?) {
            holder.setText(R.id.className, item?.attName)
            holder.getView<View>(R.id.select).visibility =
                if (index == holder.adapterPosition) View.VISIBLE else View.GONE
            if (this.itemCount - 1 == holder.adapterPosition) {
                holder.getView<View>(R.id.view_line).visibility = View.GONE
            } else {
                holder.getView<View>(R.id.view_line).visibility = View.VISIBLE
            }
        }
    }

    private var indexDate = -1

    private val adapterDate = object :
        BaseQuickAdapter<SchoolPeopleAllFormBean?, BaseViewHolder>(R.layout.swich_class_item) {
        override fun convert(holder: BaseViewHolder, item: SchoolPeopleAllFormBean?) {
            holder.setText(R.id.className, item?.attName)
            holder.getView<View>(R.id.select).visibility =
                if (indexDate == holder.adapterPosition) View.VISIBLE else View.GONE
            if (this.itemCount - 1 == holder.adapterPosition) {
                holder.getView<View>(R.id.view_line).visibility = View.GONE
            } else {
                holder.getView<View>(R.id.view_line).visibility = View.VISIBLE
            }
        }
    }
}