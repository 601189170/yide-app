package com.yyide.chatim.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.R
import com.yyide.chatim.adapter.PushSettingAdapter
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityPushSettingBinding
import com.yyide.chatim.model.PushSettingBean
import com.yyide.chatim.model.gate.Result
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.SpaceItemDecoration
import com.yyide.chatim.viewmodel.gate.PushSettingViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * 推送配置
 */
class PushSettingActivity : BaseActivity() {
    val dataList = mutableListOf<PushSettingBean>()
    private lateinit var viewBinding: ActivityPushSettingBinding
    private val pushSettingViewModel: PushSettingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPushSettingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()
        lifecycleScope.launch {
            pushSettingViewModel.getPushSettingList.collect {
                when (it) {
                    is Result.Success -> {
                        handleData(it.data)
                    }
                    is Result.Error -> {
                        loge("${it.exception?.localizedMessage}")
                        ToastUtils.showShort("${it.exception?.localizedMessage}")
                    }
                }
            }
        }
        lifecycleScope.launch {
            pushSettingViewModel.updatePushSettingList.collect {
                when (it) {
                    is Result.Success -> {
                        //ToastUtils.showShort("更新成功")
                        //pushSettingViewModel.queryUserNoticeOnOffByUserId()
                    }
                    is Result.Error -> {
                        pushSettingViewModel.queryUserNoticeOnOffByUserId()
                        loge("${it.exception?.localizedMessage}")
                        ToastUtils.showShort("${it.exception?.localizedMessage}")
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        pushSettingViewModel.queryUserNoticeOnOffByUserId()
    }

    private fun handleData(data: List<PushSettingBean>?) {
        dataList.clear()
        if (data == null || data.isEmpty()) {
            viewBinding.blankPage.visibility = View.VISIBLE
            viewBinding.recyclerView.visibility = View.GONE
            return
        }
        viewBinding.blankPage.visibility = View.GONE
        viewBinding.recyclerView.visibility = View.VISIBLE
        dataList.addAll(data)
        //	类型 1 闸机推送（出入校） 2 考勤 3 考勤周报 4 通知公告 5 请假【目前只有1和3】
        val adapter = PushSettingAdapter(this, dataList) { position, isChecked ->
            val pushSettingBean = dataList[position]
            updatePushSetting(pushSettingBean.moduleId, if (isChecked) 0 else 1);
        }
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerView.addItemDecoration(
            SpaceItemDecoration(DisplayUtils.dip2px(this, 10f))
        )
        viewBinding.recyclerView.adapter = adapter
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_push_setting
    }

    private fun initView() {
        viewBinding.top.backLayout.setOnClickListener { finish() }
        viewBinding.top.title.text = getString(R.string.push_setting_title)
    }

    /**
     * 更新推送配置
     */
    private fun updatePushSetting(moduleId: String?, switchKey: Int) {
        pushSettingViewModel.updateUserNoticeOnOffByUserIdAndType(moduleId, "$switchKey")
    }
}