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
import androidx.viewpager.widget.ViewPager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.weekly.details.adapter.ClassAdapter
import com.yyide.chatim.activity.weekly.details.adapter.DateAdapter
import com.yyide.chatim.activity.weekly.details.adapter.HotAdapter
import com.yyide.chatim.activity.weekly.details.viewmodel.TeacherAttendanceViewModel
import com.yyide.chatim.activity.weekly.home.WeeklyUtil
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.databinding.FragmentTeacherAttendanceChildWeeklyBinding
import com.yyide.chatim.databinding.ItemHBinding
import com.yyide.chatim.databinding.ItemHotBinding
import com.yyide.chatim.databinding.ItemWeeklyAttendanceBinding
import com.yyide.chatim.dialog.AttendancePop
import com.yyide.chatim.model.WeeklyDateBean
import com.yyide.chatim.model.WeeklyTeacherAttend
import com.yyide.chatim.model.WeeklyTeacherDetail
import com.yyide.chatim.utils.DateUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * 教师/班主任 查看教师详情
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class TeacherAttendanceHomeTeacherFragment : BaseFragment() {

    private lateinit var viewBinding: FragmentTeacherAttendanceChildWeeklyBinding
    private val viewModel: TeacherAttendanceViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTeacherAttendanceChildWeeklyBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TeacherAttendanceHomeTeacherFragment().apply {}
    }

    private fun request(dateTime: WeeklyDateBean.DataBean.TimesBean?) {
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
            viewModel.requestAttendanceTeacherDetail(
                classId,
                teacherId,
                dateTime.startTime,
                dateTime.endTime
            )
        }
    }

    private fun initView() {
        teacherId = SpData.getIdentityInfo().teacherId
        if (SpData.getClassInfo() != null) {
            classId = SpData.getClassInfo().classesId
        }
        initClassMenu()
        initDate()
        viewModel.teacherAttendanceLiveData.observe(viewLifecycleOwner) {
            dismiss()
            val result = it.getOrNull()
            if (null != result) {
                initHotScroll(result.teacherAttend)
                initViewpager(result.teacherDetail)
            } else {//接口返回空的情况处理

            }
        }
    }

    private fun initViewpager(detailList: List<WeeklyTeacherDetail>?) {
        if (detailList != null) {
            viewBinding.viewpager.offscreenPageLimit = 3
            viewBinding.viewpager.adapter = object :
                FragmentStatePagerAdapter(
                    childFragmentManager,
                    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
                ) {
                override fun getItem(position: Int): Fragment {
                    return TeacherAttendanceTeacherChildFragment.newInstance(
                        detailList[position],
                        detailList[position].attendName
                    )
                }

                override fun getCount(): Int {
                    return detailList.size
                }

                override fun getPageTitle(position: Int): CharSequence? {
                    return detailList[position].attendName
                }
            }
            viewBinding.slidingTabLayout.setViewPager(viewBinding.viewpager)
            viewBinding.slidingTabLayout.currentTab = 0
        }
    }

    private var classId = ""
    private var teacherId = ""
    private var timePosition = -1
    private lateinit var dateTime: WeeklyDateBean.DataBean.TimesBean
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

    private fun initClassMenu() {
        if (SpData.getClassInfo() != null) {
            viewBinding.tvClassName.text = SpData.getClassInfo().classesName
        }
        val classList = SpData.getClassList()
        val adapterEvent = ClassAdapter()
        if (classList != null) {
            if (classList.size > 1) {
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
                        classId = adapterEvent.getItem(index).classesId
                        request(dateTime)
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
            adapterEvent.setList(classList)
        }
    }

    private var spanCount = 3
    private fun initHotScroll(attendance: List<WeeklyTeacherAttend>?) {
        if (attendance != null && attendance.size < 3) {
            spanCount = attendance.size
        }
        val splitList = splitList(attendance, spanCount)
        val hotList = mutableListOf<Int>()
        if (splitList != null) {
            for (item in splitList.indices) {
                hotList.add(item)
            }
        }
        //设置班级考勤
        viewBinding.hotRecyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        viewBinding.hotRecyclerview.adapter = adapterHot
        if (hotList != null && hotList.size > 1) {
            adapterHot.setList(hotList)
        }
        val viewList = mutableListOf<View>()
        splitList?.forEachIndexed { index, schoolAttendance ->
            viewList.add(attendanceBanner(splitList[index], spanCount))
        }
        val announAdapter = HotAdapter(viewList)
        viewBinding.announRoll.adapter = announAdapter
        viewBinding.announRoll.addOnPageChangeListener(object :
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
        list: List<WeeklyTeacherAttend>?,
        len: Int
    ): List<List<WeeklyTeacherAttend>>? {
        if (list == null || list.isEmpty() || len < 1) {
            return Collections.emptyList()
        }
        val result: MutableList<List<WeeklyTeacherAttend>> =
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

    private fun attendanceBanner(attendance: List<WeeklyTeacherAttend>, spanCount: Int): View {
        val view = ItemHBinding.inflate(layoutInflater)
        view.attendanceRecyclerview.layoutManager = GridLayoutManager(activity, spanCount)
        val adapterAttendance = object :
            BaseQuickAdapter<WeeklyTeacherAttend, BaseViewHolder>(R.layout.item_weekly_attendance) {
            override fun convert(holder: BaseViewHolder, item: WeeklyTeacherAttend) {
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

}