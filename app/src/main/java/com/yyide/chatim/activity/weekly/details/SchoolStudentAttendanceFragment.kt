package com.yyide.chatim.activity.weekly.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.yyide.chatim.R
import com.yyide.chatim.activity.newnotice.fragment.NoticeBrandPersonnelFragment
import com.yyide.chatim.activity.newnotice.fragment.NoticePersonnelFragment
import com.yyide.chatim.databinding.FragmentHomeworkWeeklyDetailsBinding
import com.yyide.chatim.databinding.FragmentParentsWeeklyBinding
import com.yyide.chatim.databinding.FragmentSchoolAttendanceWeeklyDetailsBinding

/**
 *
 * 校长查看学生考勤统计详情
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class SchoolStudentAttendanceFragment : Fragment() {

    private lateinit var viewBinding: FragmentSchoolAttendanceWeeklyDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSchoolAttendanceWeeklyDetailsBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SchoolStudentAttendanceFragment().apply {}
    }

    private fun initView() {
        val mTitles: MutableList<String> = ArrayList()
        //需要动态数据设置该tab
        viewBinding.viewpager.offscreenPageLimit = 3
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
    }
}