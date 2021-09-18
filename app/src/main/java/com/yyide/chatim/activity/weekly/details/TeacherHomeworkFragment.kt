package com.yyide.chatim.activity.weekly.details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.yyide.chatim.R
import com.yyide.chatim.databinding.FragmentSchoolHomeworkWeeklyDetailsBinding
import com.yyide.chatim.databinding.FragmentTeacherHomeworkWeeklyBinding
import com.yyide.chatim.fragment.leave.RequestLeaveStudentFragment

/**
 *
 * 教师/班主任 查看作业统计详情
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class TeacherHomeworkFragment : Fragment() {

    private lateinit var viewBinding: FragmentTeacherHomeworkWeeklyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTeacherHomeworkWeeklyBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TeacherHomeworkFragment().apply {}
    }

    private fun initView() {
        viewBinding.rgTimeType.setOnCheckedChangeListener { group: RadioGroup?, checkedId: Int ->
            when (checkedId) {
                viewBinding.rbAccordingChart.id -> {
                    //accordingCourseLeave(true)
                }
                viewBinding.rbAccordingData.id -> {
                    //accordingCourseLeave(false)
                }
            }
        }
    }
}