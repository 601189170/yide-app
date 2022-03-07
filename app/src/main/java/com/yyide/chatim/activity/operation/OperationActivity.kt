package com.yyide.chatim.activity.operation

import android.os.Bundle
import com.yyide.chatim.activity.operation.fragment.OperationFragment
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityOperationBinding

/**
 * @Desc 作业首页
 * @Data 2022年2月17日13:35:32
 * @Author lrz
 */
class OperationActivity :
    KTBaseActivity<ActivityOperationBinding>(ActivityOperationBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainerView.id, OperationFragment.newInstance()).commit()
    }
}