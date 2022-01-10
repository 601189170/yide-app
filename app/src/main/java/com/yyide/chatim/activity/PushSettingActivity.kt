package com.yyide.chatim.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.tencent.mmkv.MMKV
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.base.MMKVConstant
import com.yyide.chatim.databinding.ActivityPushSettingBinding
import com.yyide.chatim.model.PushSettingBean
import com.yyide.chatim.model.gate.Result
import com.yyide.chatim.utils.loge
import com.yyide.chatim.viewmodel.gate.PushSettingViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * 推送配置
 */
class PushSettingActivity : BaseActivity() {

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
                        pushSettingViewModel.queryUserNoticeOnOffByUserId()
                    }
                    is Result.Error -> {
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
    private fun handleData(data: List<PushSettingBean.OffFormListBean>?) {
        if (data == null || data.isEmpty()) {
            viewBinding.gateSwitchPush.isChecked = true
            viewBinding.weeklySwitchPush.isChecked = true
            return
        }
        //	类型 1 闸机推送（出入校） 2 考勤 3 考勤周报 4 通知公告 5 请假【目前只有1和3】
        data.forEach {
            when (it.type) {
                "1" -> {
                    viewBinding.gateSwitchPush.isChecked = it.onOff != "2"
                }
                "3" -> {
                    viewBinding.weeklySwitchPush.isChecked = it.onOff != "2"
                }
                else -> {}
            }
        }
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_push_setting
    }

    private fun initView() {
        viewBinding.top.backLayout.setOnClickListener { finish() }
        viewBinding.top.title.text = getString(R.string.push_setting_title)
        viewBinding.weeklySwitchPush.setOnCheckedChangeListener { _, isChecked ->
            if (!viewBinding.weeklySwitchPush.isPressed) {
                return@setOnCheckedChangeListener
            }
            updatePushSetting("3", isChecked)
        }
        //闸机通行统计
        viewBinding.gateSwitchPush.setOnCheckedChangeListener { _, isChecked ->
            if (!viewBinding.gateSwitchPush.isPressed){
                return@setOnCheckedChangeListener
            }
            updatePushSetting("1", isChecked)
        }
    }

    /**
     * 更新推送配置
     */
    private fun updatePushSetting(type: String, isChecked: Boolean) {
        val list = mutableListOf<PushSettingBean.OffFormListBean>()
        val onOff = if (isChecked) "1" else "2"
        list.add(PushSettingBean.OffFormListBean(type = type, onOff = onOff))
        pushSettingViewModel.updateUserNoticeOnOffByUserIdAndType(list)
    }
}