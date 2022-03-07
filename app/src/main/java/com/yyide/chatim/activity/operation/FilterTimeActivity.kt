package com.yyide.chatim.activity.operation

import android.os.Bundle
import com.yyide.chatim.R
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityFilterTimeBinding

/**
 * 选择筛选时间
 * @time 2022-2-22
 * @author lrz
 */
class FilterTimeActivity :
    KTBaseActivity<ActivityFilterTimeBinding>(ActivityFilterTimeBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

     override fun initView() {
        binding.top.title.text = getString(R.string.filter_title)
        binding.top.backLayout.setOnClickListener { finish() }
        binding.btnConfirm.setOnClickListener { }

        binding.clDate.setOnClickListener {}
        binding.clStart.setOnClickListener {}
        binding.clEnd.setOnClickListener {}
    }
}