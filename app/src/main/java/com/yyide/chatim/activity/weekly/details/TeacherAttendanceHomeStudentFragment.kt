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
import com.yyide.chatim.activity.weekly.details.adapter.HotAdapter
import com.yyide.chatim.activity.weekly.details.viewmodel.TeacherAttendanceStudentViewModel
import com.yyide.chatim.activity.weekly.home.WeeklyUtil
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.databinding.FragmentTeacherAttendanceChildWeeklyBinding
import com.yyide.chatim.databinding.ItemHBinding
import com.yyide.chatim.databinding.ItemHotBinding
import com.yyide.chatim.databinding.ItemWeeklyAttendanceBinding
import com.yyide.chatim.dialog.AttendancePop
import com.yyide.chatim.model.GetUserSchoolRsp
import com.yyide.chatim.model.StudentAttend
import com.yyide.chatim.model.StudentDetail
import com.yyide.chatim.model.WeeklyDateBean
import com.yyide.chatim.utils.DateUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * 教师/班主任 查看学生详情
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class TeacherAttendanceHomeStudentFragment : BaseFragment() {

    private lateinit var viewBinding: FragmentTeacherAttendanceChildWeeklyBinding
    private val viewModel: TeacherAttendanceStudentViewModel by viewModels()
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
        fun newInstance(
            dateTime: WeeklyDateBean.DataBean.TimesBean,
            classBean: GetUserSchoolRsp.DataBean.FormBean
        ) =
            TeacherAttendanceHomeStudentFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("item", dateTime)
                    putSerializable("classBean", classBean)
                }
            }
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
            if (classBean != null && classBean.classesId != null) {
                loading()
                viewModel.requestAttendanceStudentDetail(
                    classBean.classesId,
                    teacherId,
                    dateTime.startTime,
                    dateTime.endTime
                )
            } else {
                viewBinding.clTop.visibility = View.GONE
                viewBinding.viewpager.visibility = View.INVISIBLE
                viewBinding.slidingTabLayout.visibility = View.INVISIBLE
                viewBinding.cardViewNoData.visibility = View.VISIBLE
            }
        }
    }

    private fun initView() {
        teacherId = SpData.getIdentityInfo().id.toString()
        initDate()
        initClassMenu()
        viewBinding.appBarLayout.visibility = View.INVISIBLE
        viewModel.teacherAttendanceStudentLiveData.observe(viewLifecycleOwner) {
            val result = it.getOrNull()
            viewBinding.appBarLayout.visibility = View.VISIBLE
            if (null != result) {
                viewBinding.cardViewNoData.visibility = View.GONE
                viewBinding.clTop.visibility = View.VISIBLE
                initHotScroll(result.studentAttend)
                initViewpager(result.studentDetail)
                if (result.studentDetail != null
                    && result.studentAttend != null
                    && result.studentDetail.isEmpty()
                    && result.studentAttend.isEmpty()
                ) {
                    viewBinding.cardViewNoData.visibility = View.VISIBLE
                    viewBinding.clTop.visibility = View.GONE
                    viewBinding.slidingTabLayout.visibility = View.GONE
                    viewBinding.viewpager.visibility = View.GONE
                } else {
                    viewBinding.cardViewNoData.visibility = View.GONE
                    viewBinding.clTop.visibility = View.VISIBLE
                    viewBinding.slidingTabLayout.visibility = View.VISIBLE
                    viewBinding.viewpager.visibility = View.VISIBLE
                }
            } else {
                viewBinding.cardViewNoData.visibility = View.VISIBLE
                viewBinding.clTop.visibility = View.GONE
                viewBinding.slidingTabLayout.visibility = View.GONE
                viewBinding.viewpager.visibility = View.GONE
            }
            dismiss()
        }
    }

    private fun initViewpager(detailList: List<StudentDetail>?) {
        if (detailList != null && detailList.isNotEmpty()) {
            viewBinding.slidingTabLayout.visibility = View.VISIBLE
            viewBinding.viewpager.visibility = View.VISIBLE
           //viewBinding.viewpager.offscreenPageLimit = detailList.size
            viewBinding.viewpager.adapter = object :
                FragmentStatePagerAdapter(
                    childFragmentManager,
                    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
                ) {
                override fun getItem(position: Int): Fragment {
                    return TeacherAttendanceStudentChildFragment.newInstance(
                        detailList[position],
                        detailList[position].attendName
                    )
                }

                override fun getCount(): Int {
                    return detailList.size
                }

                override fun getPageTitle(position: Int): CharSequence {
                    return detailList[position].attendName
                }
            }
            viewBinding.slidingTabLayout.setViewPager(viewBinding.viewpager)
            viewBinding.slidingTabLayout.currentTab = 0
            viewBinding.slidingTabLayout.notifyDataSetChanged()
        } else {
            viewBinding.viewpager.visibility = View.INVISIBLE
            viewBinding.slidingTabLayout.visibility = View.INVISIBLE
        }
    }

    private var teacherId = ""
    private var timePosition = -1
    private lateinit var dateTime: WeeklyDateBean.DataBean.TimesBean
    private lateinit var classBean: GetUserSchoolRsp.DataBean.FormBean
    private fun initDate() {
        arguments?.apply {
            dateTime = getSerializable("item") as WeeklyDateBean.DataBean.TimesBean
            classBean = getSerializable("classBean") as GetUserSchoolRsp.DataBean.FormBean
        }
        //获取日期时间
        val dateLists = WeeklyUtil.getDateTimes()
        if (dateLists.isNotEmpty()) {
            timePosition = WeeklyUtil.getTimePosition(dateTime, dateLists)
            //dateTime = dateLists[dateLists.size - 1]
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
        var classesId = ""
        if (classBean != null && classBean.classesId != null) {
            viewBinding.tvClassName.text = classBean.classesName + "的周报"
            classesId = classBean.classesId
        }
        val classList = SpData.getDuplicationClassList()
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
                        classesId = adapterEvent.getItem(index).classesId
                        adapterEvent.setClassId(classesId)
                        viewBinding.tvClassName.text =
                            adapterEvent.getItem(index).classesName + "的周报"
                        classBean = adapterEvent.getItem(index)
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
            adapterEvent.setClassId(classesId)
            adapterEvent.setList(classList)
        }
    }

    private var spanCount = 3
    private fun initHotScroll(attendance: List<StudentAttend>?) {
        adapterHot.setList(null)
        spanCount = if (attendance != null && attendance.size < 3) {
            attendance.size
        } else {
            3
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
        list: List<StudentAttend>?,
        len: Int
    ): List<List<StudentAttend>>? {
        if (list == null || list.isEmpty() || len < 1) {
            return Collections.emptyList()
        }
        val result: MutableList<List<StudentAttend>> =
            ArrayList()
        val size = list.size
        val count = (size + len - 1) / len
        for (i in 0 until count) {
            val subList =
                list.subList(i * len, if ((i + 1) * len > size) size else len * (i + 1))
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

    private fun attendanceBanner(attendance: List<StudentAttend>, spanCount: Int): View {
        val view = ItemHBinding.inflate(layoutInflater)
        view.attendanceRecyclerview.layoutManager = GridLayoutManager(activity, spanCount)
        val adapterAttendance = object :
            BaseQuickAdapter<StudentAttend, BaseViewHolder>(R.layout.item_weekly_attendance) {
            override fun convert(holder: BaseViewHolder, item: StudentAttend) {
                val bind = ItemWeeklyAttendanceBinding.bind(holder.itemView)
                bind.viewLine.visibility =
                    if (holder.bindingAdapterPosition == 0) View.GONE else View.VISIBLE
                bind.tvEventName.text = item.name
                bind.tvAttendance.text = item.value
            }
        }
        view.attendanceRecyclerview.adapter = adapterAttendance
        adapterAttendance.setList(attendance)
        return view.root
    }

}