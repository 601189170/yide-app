package com.yyide.chatim_pro.activity.operation

import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.base.KTBaseActivity
import com.yyide.chatim_pro.databinding.ActivityFilterTimeBinding

/**
 * 选择筛选时间
 * @time 2022-2-22
 * @author lrz
 */
class FilterTimeActivity :
    KTBaseActivity<ActivityFilterTimeBinding>(ActivityFilterTimeBinding::inflate) {

    override fun initView() {
        binding.top.title.text = getString(R.string.filter_title)
        binding.top.backLayout.setOnClickListener { finish() }
        binding.btnConfirm.setOnClickListener { }

        binding.clDate.setOnClickListener {}
        binding.clStart.setOnClickListener {}
        binding.clEnd.setOnClickListener {}
    }
}