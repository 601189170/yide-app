package com.yyide.chatim.activity.weekly.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentStatePagerAdapter
import com.yyide.chatim.R
import com.yyide.chatim.databinding.FragmentSchoolAttendanceWeeklyDetailsBinding
import com.yyide.chatim.databinding.FragmentTeacherAttendanceWeeklyBinding

/**
 *
 * 教师查看考勤统计详情
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class TeacherAttendanceFragment : Fragment() {

    private lateinit var viewBinding: FragmentTeacherAttendanceWeeklyBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTeacherAttendanceWeeklyBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TeacherAttendanceFragment().apply {}
    }

    private fun initView() {
        val mTitles: MutableList<String> = ArrayList()
        mTitles.add(getString(R.string.weekly_teacher_tab))
        mTitles.add(getString(R.string.weekly_student_tab))
        viewBinding.viewpager.offscreenPageLimit = 3
        viewBinding.viewpager.adapter = object :
            FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> {
                        SchoolStudentAttendanceFragment.newInstance()
                    }
                    else -> {
                        SchoolTeacherAttendanceFragment.newInstance()
                    }
                }
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