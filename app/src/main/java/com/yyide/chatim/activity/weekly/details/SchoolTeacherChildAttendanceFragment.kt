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
import com.yyide.chatim.databinding.*

/**
 *
 * 校长查看子项教师考勤
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class SchoolTeacherChildAttendanceFragment : Fragment() {

    private lateinit var viewBinding: FragmentSchoolTeacherChildWeeklyAttendanceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSchoolTeacherChildWeeklyAttendanceBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SchoolTeacherChildAttendanceFragment().apply {}
    }

    private fun initView() {

    }
}