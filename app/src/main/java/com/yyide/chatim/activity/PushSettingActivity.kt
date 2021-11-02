package com.yyide.chatim.activity

import android.os.Bundle
import com.tencent.mmkv.MMKV
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.base.MMKVConstant
import com.yyide.chatim.databinding.ActivityPushSettingBinding

/**
 * 推送配置
 */
class PushSettingActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityPushSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPushSettingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_push_setting
    }

    private fun initView() {
        viewBinding.top.backLayout.setOnClickListener { finish() }
        viewBinding.top.title.text = getString(R.string.push_setting_title)
        viewBinding.weeklySwitchPush.isChecked = MMKV.defaultMMKV().decodeBool(MMKVConstant.YD_WEEKLY_PUSH_SETTING, false)
        viewBinding.weeklySwitchPush.setOnCheckedChangeListener { buttonView, isChecked ->
            MMKV.defaultMMKV().encode(MMKVConstant.YD_WEEKLY_PUSH_SETTING, isChecked)
        }
    }
}