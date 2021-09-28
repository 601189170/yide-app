package com.yyide.chatim.activity.weekly.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.weekly.details.adapter.DateAdapter
import com.yyide.chatim.activity.weekly.details.adapter.HotAdapter
import com.yyide.chatim.activity.weekly.details.viewmodel.SchoolTeacherAttendanceViewModel
import com.yyide.chatim.activity.weekly.home.WeeklyUtil
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.databinding.FragmentSchoolTeacherWeeklyAttendanceBinding
import com.yyide.chatim.databinding.ItemHBinding
import com.yyide.chatim.databinding.ItemHotBinding
import com.yyide.chatim.databinding.ItemWeeklyAttendanceBinding
import com.yyide.chatim.dialog.AttendancePop
import com.yyide.chatim.model.Detail
import com.yyide.chatim.model.SchoolAttendance
import com.yyide.chatim.model.SchoolWeeklyTeacherBean
import com.yyide.chatim.model.WeeklyDateBean
import com.yyide.chatim.utils.DateUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * 校长查看教师考勤统计详情
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class SchoolTeacherAttendanceFragment : BaseFragment() {

    private lateinit var viewBinding: FragmentSchoolTeacherWeeklyAttendanceBinding
    private val viewModel: SchoolTeacherAttendanceViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSchoolTeacherWeeklyAttendanceBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SchoolTeacherAttendanceFragment().apply {}
    }

    private fun initView() {
        request()
    }

    private fun initViewPager(events: List<Detail>) {
        val mTitles: MutableList<String> = ArrayList()
        for (item in events) {
            mTitles.add(item.name)
        }
        //需要动态数据设置该tab
        viewBinding.viewpager.adapter = object :
            FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                return SchoolTeacherChildAttendanceFragment.newInstance(events[position])
            }

            override fun getCount(): Int {
                return mTitles.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mTitles[position]
            }
        }
        viewBinding.slidingTabLayout.setViewPager(viewBinding.viewpager)
        viewBinding.slidingTabLayout.currentTab = 0
    }

    private fun request() {
        viewModel.schoolTeacherAttendanceLiveData.observe(viewLifecycleOwner) {
            dismiss()
            val result = it.getOrNull()
            if (null != result) {
                setWeekly(result)
            } else {//接口返回空的情况处理

            }
        }
        val dateTime = WeeklyUtil.getDateTime()
        if (dateTime != null) {
            loading()
            viewModel.requestSchoolTeacherAttendance(dateTime.startTime, dateTime.endTime)
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
        val dateLists = WeeklyUtil.getDateTimes()
        val adapterDate = DateAdapter()
        adapterDate.setList(dateLists)
        viewBinding.tvTime.setOnClickListener {
            val attendancePop = AttendancePop(activity, adapterDate, "请选择时间")
            attendancePop.setOnSelectListener { index: Int ->
                //indexDate = index
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
                viewModel.requestSchoolTeacherAttendance(
                    dateLists[index].startTime,
                    dateLists[index].endTime
                )
            }
        }
    }

    private val spanCount = 3
    private fun setWeekly(result: SchoolWeeklyTeacherBean) {
        viewBinding.hotRecyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        viewBinding.hotRecyclerview.adapter = adapterHot

        val splitList = splitList(result.attend, spanCount)
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
        initViewPager(result.detail)
    }

    private fun initHotScroll(attendance: List<List<SchoolAttendance>>) {
        val viewList = mutableListOf<View>()
        attendance.forEachIndexed { index, schoolAttendance ->
            viewList.add(attendanceBanner(attendance[index]))
        }
        val announAdapter = HotAdapter(viewList)
        viewBinding.announRoll.adapter = announAdapter
        viewBinding.announRoll.addOnPageChangeListener(object : OnPageChangeListener {
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

    private fun attendanceBanner(attendance: List<SchoolAttendance>): View {
        val view = ItemHBinding.inflate(layoutInflater)
        view.attendanceRecyclerview.layoutManager = GridLayoutManager(activity, spanCount)
        val adapterAttendance = object :
            BaseQuickAdapter<SchoolAttendance, BaseViewHolder>(R.layout.item_weekly_attendance) {
            override fun convert(holder: BaseViewHolder, item: SchoolAttendance) {
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

    /**
     * 按指定大小，分隔集合，将集合按规定个数分为n个部分
     * @param <T>
     *
     * @param list
     * @param len
     * @return
    </T> */
    private fun <SchoolAttendance> splitList(
        list: List<SchoolAttendance>?,
        len: Int
    ): List<List<SchoolAttendance>>? {
        if (list == null || list.isEmpty() || len < 1) {
            return Collections.emptyList()
        }
        val result: MutableList<List<SchoolAttendance>> = ArrayList()
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

}