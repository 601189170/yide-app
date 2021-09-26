package com.yyide.chatim.activity.weekly.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yyide.chatim.databinding.FragmentSchoolHomeworkWeeklyDetailsBinding

/**
 *
 * 校长查看作业统计详情
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class SchoolHomeworkFragment : Fragment() {

    private lateinit var viewBinding: FragmentSchoolHomeworkWeeklyDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSchoolHomeworkWeeklyDetailsBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SchoolHomeworkFragment().apply {}
    }

    private fun initView() {

    }
}