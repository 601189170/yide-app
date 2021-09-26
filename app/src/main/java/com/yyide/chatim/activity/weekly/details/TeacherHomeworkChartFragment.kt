package com.yyide.chatim.activity.weekly.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yyide.chatim.databinding.FragmentSchoolHomeworkWeeklyDetailsBinding
import com.yyide.chatim.databinding.FragmentTeacherHomeworkChartWeeklyBinding
import com.yyide.chatim.databinding.FragmentTeacherHomeworkWeeklyBinding

/**
 *
 * 教师/班主任 查看作业统计详情
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class TeacherHomeworkChartFragment : Fragment() {

    private lateinit var viewBinding: FragmentTeacherHomeworkChartWeeklyBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTeacherHomeworkChartWeeklyBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TeacherHomeworkChartFragment().apply {}
    }

    private fun initView() {

    }
}