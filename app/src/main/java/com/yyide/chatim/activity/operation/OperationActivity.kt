package com.yyide.chatim.activity.operation

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.alibaba.fastjson.JSON
import com.yyide.chatim.activity.operation.fragment.OperationFragment
import com.yyide.chatim.activity.operation.viewmodel.OperationViewModel
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityOperationBinding

/**
 * @Desc 作业首页
 * @Data 2022年2月17日13:35:32
 * @Author lrz
 */
class OperationActivity :
    KTBaseActivity<ActivityOperationBinding>(ActivityOperationBinding::inflate) {
    private lateinit var viewModel: OperationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OperationViewModel::class.java)

        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainerView.id, OperationFragment.newInstance()).commit()
        viewModel.TeacherWorkListLiveData.observe(this){
            Log.e("TAG", "initView: "+ JSON.toJSONString(it) )
            Log.e("TAG", "OperationActivity: "+ JSON.toJSONString(it.getOrNull()) )
            if (it.isSuccess){
                val result = it.getOrNull()
                if (result!=null){
//                    mAdapter.setList(result.data.list);
                }
            }
        }
    }
}