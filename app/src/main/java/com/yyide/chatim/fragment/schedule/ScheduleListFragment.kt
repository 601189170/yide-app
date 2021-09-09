package com.yyide.chatim.fragment.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.yide.calendar.OnCalendarClickListener
import com.yide.calendar.schedule.ScheduleLayout
import com.yide.calendar.schedule.ScheduleRecyclerView
import com.yyide.chatim.R
import com.yyide.chatim.databinding.FragmentScheduleListBinding
import com.yyide.chatim.utils.loge

/**
 *
 * @author liu tao
 * @date 2021/9/7 14:19
 * @description 日程/列表
 */
class ScheduleListFragment : Fragment(), OnCalendarClickListener {
    lateinit var fragmentScheduleListBinding: FragmentScheduleListBinding
    private var slSchedule: ScheduleLayout? = null
    private var rvScheduleList: ScheduleRecyclerView? = null
    private var rLNoTask: RelativeLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentScheduleListBinding = FragmentScheduleListBinding.inflate(layoutInflater)
        return fragmentScheduleListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        slSchedule = view.findViewById(R.id.slSchedule)
        rvScheduleList = view.findViewById(R.id.rvScheduleList)
        rLNoTask = view.findViewById(R.id.rlNoTask)
        slSchedule?.setOnCalendarClickListener(this)
        initScheduleList()
    }

    private fun initScheduleList() {
        rvScheduleList = slSchedule?.schedulerRecyclerView
        val manager = LinearLayoutManager(activity)
        manager.setOrientation(LinearLayoutManager.VERTICAL)
        rvScheduleList?.setLayoutManager(manager)
        val itemAnimator = DefaultItemAnimator()
        itemAnimator.supportsChangeAnimations = false
        rvScheduleList?.setItemAnimator(itemAnimator)
       // val mScheduleAdapter = ScheduleAdapter(activity, context)
       // rvScheduleList?.setAdapter(mScheduleAdapter)
    }

    override fun onClickDate(year: Int, month: Int, day: Int) {
        loge("onClickDate year=$year,month=$month,day=$day")
    }

    override fun onPageChange(year: Int, month: Int, day: Int) {
        loge("onClickDate year=$year,month=$month,day=$day")
    }
}