package com.yyide.chatim.activity.weekly.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentStatePagerAdapter
import com.yyide.chatim.databinding.FragmentSchoolTeacherWeeklyAttendanceBinding

/**
 *
 * 班主任查看教师考勤周报
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class TeacherChargeAttendanceFragment : Fragment() {

    private lateinit var viewBinding: FragmentSchoolTeacherWeeklyAttendanceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            TeacherChargeAttendanceFragment().apply {}
    }

    private fun initView() {
        val mTitles: MutableList<String> = ArrayList()
        //需要动态数据设置该tab
        viewBinding.viewpager.offscreenPageLimit = 3
        viewBinding.viewpager.adapter = object :
            FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                return SchoolTeacherChildAttendanceFragment.newInstance()
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