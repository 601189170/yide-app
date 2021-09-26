package com.yyide.chatim.activity.weekly.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.viewModels
import com.yyide.chatim.R
import com.yyide.chatim.activity.newnotice.fragment.NoticeBrandPersonnelFragment
import com.yyide.chatim.activity.newnotice.fragment.NoticePersonnelFragment
import com.yyide.chatim.activity.weekly.details.viewmodel.SchoolStudentAttendanceViewModel
import com.yyide.chatim.databinding.*
import com.yyide.chatim.utils.DateUtils

/**
 *
 * 校长查看学生考勤统计详情
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class SchoolStudentAttendanceFragment : Fragment() {

    private lateinit var viewBinding: FragmentSchoolStudentWeeklyAttendanceBinding
    private val viewModel : SchoolStudentAttendanceViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSchoolStudentWeeklyAttendanceBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SchoolStudentAttendanceFragment().apply {}
    }

    private fun initView() {
        val mTitles: MutableList<String> = ArrayList()
        mTitles.add("学生出入校1")
        mTitles.add("学生出入校2")
        //需要动态数据设置该tab
        viewBinding.viewpager.adapter = object :
            FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                return SchoolStudentChildAttendanceFragment.newInstance()
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
        request()
    }

    private fun request(){
        val startData = DateUtils.getDate(DateUtils.getBeginDayOfWeek().time)
        val endData = DateUtils.getDate(System.currentTimeMillis())
        viewModel.requestSchoolAttendance(startData, endData)

        viewModel.schoolStudentAttendanceLiveData.observe(viewLifecycleOwner) {
            val video = it.getOrNull()
            if (null != video) {

            } else {//接口返回空的情况处理

            }
        }
    }
}