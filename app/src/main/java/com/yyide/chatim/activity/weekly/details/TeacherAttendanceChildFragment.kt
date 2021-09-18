package com.yyide.chatim.activity.weekly.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentStatePagerAdapter
import com.yyide.chatim.databinding.FragmentSchoolTeacherWeeklyAttendanceBinding
import com.yyide.chatim.databinding.FragmentTeacherChildWeeklyAttendanceBinding

/**
 * Child
 * 班主任查看教师考勤周报
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class TeacherAttendanceChildFragment : Fragment() {

    private lateinit var viewBinding: FragmentTeacherChildWeeklyAttendanceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTeacherChildWeeklyAttendanceBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TeacherAttendanceChildFragment().apply {}
    }

    private fun initView() {

    }
}