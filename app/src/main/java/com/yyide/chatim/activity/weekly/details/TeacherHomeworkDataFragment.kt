package com.yyide.chatim.activity.weekly.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yyide.chatim.databinding.FragmentTeacherHomeworkWeeklyBinding

/**
 *
 * 教师/班主任 查看考勤子项
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class TeacherHomeworkDataFragment : Fragment() {

    private lateinit var viewBinding: FragmentTeacherHomeworkWeeklyBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            TeacherHomeworkDataFragment().apply {}
    }

    private fun initView() {

    }
}