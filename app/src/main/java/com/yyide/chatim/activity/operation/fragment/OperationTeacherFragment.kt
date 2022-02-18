package com.yyide.chatim.activity.operation.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yyide.chatim.R
import com.yyide.chatim.databinding.OperationFragmentBinding
import com.yyide.chatim.databinding.OperationTearcherFragmentBinding

/**
 * @Desc 作业-教师
 * @Data 2022年2月17日13:35:32
 * @Author lrz
 */
class OperationTeacherFragment : Fragment() {

    companion object {
        fun newInstance() = OperationTeacherFragment()
    }

    private lateinit var viewModel: OperationTearcherViewModel
    private lateinit var viewBinding: OperationTearcherFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = OperationTearcherFragmentBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OperationTearcherViewModel::class.java)
        initView()
    }

    private fun initView() {
        viewBinding.top.title.text = getString(R.string.operation_title)
        viewBinding.top.backLayout.setOnClickListener { activity?.finish() }
        
        viewBinding.layoutTime.tvToDay.setOnClickListener { }
        viewBinding.layoutTime.tvWeek.setOnClickListener { }
        viewBinding.layoutTime.tvLastWeek.setOnClickListener { }
        viewBinding.layoutTime.tvMonth.setOnClickListener { }
        viewBinding.layoutTime.tvLastMonth.setOnClickListener { }
        viewBinding.layoutTime.tvStartTime.setOnClickListener { }
        viewBinding.layoutTime.tvEndTime.setOnClickListener { }

        //选择班级-课程
        viewBinding.tvClassName.setOnClickListener { }

    }

}