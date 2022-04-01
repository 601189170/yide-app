package com.yyide.chatim.activity.attendance.teacher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.yyide.chatim.R
import com.yyide.chatim.activity.attendance.teacher.viewmodel.AttendanceRuleViewModel
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityAttendanceV2RuleBinding
import com.yyide.chatim.utils.GlideUtil
import com.yyide.chatim.utils.hide
import com.yyide.chatim.utils.remove
import com.yyide.chatim.utils.show

/**
 *
 * @author shenzhibin
 * @date 2022/3/30 16:57
 * @description 描述
 */
class AttendanceRuleActivity:KTBaseActivity<ActivityAttendanceV2RuleBinding>(ActivityAttendanceV2RuleBinding::inflate) {

    private val viewModel by viewModels<AttendanceRuleViewModel>()

    companion object{
        fun startGo(context: Context){
            val intent = Intent(context, AttendanceRuleActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
        viewModel.queryAttendanceRule()
    }


    override fun initView() {
        super.initView()

        GlideUtil.loadImageHead(
            applicationContext,
            viewModel.userInfo.avatar,
            binding.rivTeacherAttendanceRuleImg
        )

        binding.teacherAttendanceRuleTop.title.text = "查看规则"

        viewModel.attendanceRule.observe(this){

            binding.tvTeacherAttendanceRuleName.text = it.personName
            binding.tvTeacherAttendanceJob.text = it.groupName

            val showTimeStr = "${it.workDay}/r/n${it.restDay}休息"
            binding.tvTeacherAttendanceRuleTime.text = showTimeStr

            val sb = StringBuilder()
            it.addressList.forEach { address ->
                sb.append(address.addressName).append("/r/n")
            }
            binding.tvTeacherAttendanceRuleRange.text = sb.toString()

            val wifiSb = StringBuilder()
            it.wifiList.forEach { wifi ->
                wifiSb.append(wifi.wifiName).append("/r/n")
            }
            binding.tvTeacherAttendanceRuleBle.text = wifiSb.toString()

            binding.tvTeacherAttendanceRuleBle.text = ""

        }


    }

    private fun initListener(){
        binding.teacherAttendanceRuleTop.backLayout.setOnClickListener { finish() }

        binding.ivTeacherAttendanceRuleTimeLogo.setOnClickListener {
            val v = binding.tvTeacherAttendanceRuleTime.visibility
            if (v == View.VISIBLE){
                binding.ivTeacherAttendanceRuleTimeLogo.setImageResource(R.mipmap.icon_down)
                binding.tvTeacherAttendanceRuleTime.remove()
            }else{
                binding.ivTeacherAttendanceRuleTimeLogo.setImageResource(R.mipmap.icon_up)
                binding.tvTeacherAttendanceRuleTime.show()
            }
        }


        binding.ivTeacherAttendanceRuleRangeLogo.setOnClickListener {
            val v = binding.tvTeacherAttendanceRuleRange.visibility
            if (v == View.VISIBLE){
                binding.ivTeacherAttendanceRuleRangeLogo.setImageResource(R.mipmap.icon_down)
                binding.tvTeacherAttendanceRuleRange.remove()
                binding.tvTeacherAttendanceRuleWifiTitle.remove()
                binding.tvTeacherAttendanceRuleWifi.remove()
                binding.tvTeacherAttendanceRuleBleTitle.remove()
                binding.tvTeacherAttendanceRuleBle.remove()
            }else{
                binding.ivTeacherAttendanceRuleRangeLogo.setImageResource(R.mipmap.icon_down)
                binding.tvTeacherAttendanceRuleRange.show()
                binding.tvTeacherAttendanceRuleWifiTitle.show()
                binding.tvTeacherAttendanceRuleWifi.show()
                binding.tvTeacherAttendanceRuleBleTitle.show()
                binding.tvTeacherAttendanceRuleBle.show()
            }
        }

    }

}