package com.yyide.chatim_pro.activity.weekly.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentStatePagerAdapter
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.SpData
import com.yyide.chatim_pro.databinding.FragmentTeacherAttendanceWeeklyBinding
import com.yyide.chatim_pro.model.GetUserSchoolRsp
import com.yyide.chatim_pro.model.WeeklyDateBean

/**
 *
 * 教师查看考勤统计详情
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class TeacherAttendanceHomeFragment : Fragment() {

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
        fun newInstance(
            dateTime: WeeklyDateBean.DataBean.TimesBean,
            classBean: GetUserSchoolRsp.DataBean.FormBean
        ) =
            TeacherAttendanceHomeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("item", dateTime)
                    putSerializable("classBean", classBean)
                }
            }
    }

    private lateinit var dateTime: WeeklyDateBean.DataBean.TimesBean
    private var classBean = SpData.getClassInfo()
    private fun initView() {
        arguments?.apply {
            dateTime = getSerializable("item") as WeeklyDateBean.DataBean.TimesBean
            classBean = getSerializable("classBean") as GetUserSchoolRsp.DataBean.FormBean
        }
        if (classBean == null) {
            classBean = SpData.getClassInfo()
        }
        val mTitles: MutableList<String> = ArrayList()
        mTitles.add(getString(R.string.weekly_student_tab))
        mTitles.add(getString(R.string.weekly_teacher_tab))
        viewBinding.viewpager.adapter = object :
            FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> {
                        TeacherAttendanceHomeStudentFragment.newInstance(dateTime, classBean)
                    }
                    else -> {
                        TeacherAttendanceHomeTeacherFragment.newInstance(dateTime)
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