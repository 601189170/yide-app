package com.yyide.chatim.activity.gate

import android.os.Bundle
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityGateDataSearchBinding

/**
 *
 * @author liu tao
 * @date 2021/12/24 9:18
 * @description 闸机搜索界面
 */
class GateDataSearchActivity : BaseActivity() {
    private lateinit var activityGateDataSearchBinding: ActivityGateDataSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityGateDataSearchBinding = ActivityGateDataSearchBinding.inflate(layoutInflater)
        setContentView(activityGateDataSearchBinding.root)
        initView()
    }

    private fun initView() {
        activityGateDataSearchBinding.cancel.setOnClickListener {
            finish()
        }
    }
}