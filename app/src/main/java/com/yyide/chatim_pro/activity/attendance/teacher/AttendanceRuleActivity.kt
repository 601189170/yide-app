package com.yyide.chatim_pro.activity.attendance.teacher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.activity.attendance.teacher.viewmodel.AttendanceRuleViewModel
import com.yyide.chatim_pro.base.KTBaseActivity
import com.yyide.chatim_pro.databinding.ActivityAttendanceV2RuleBinding
import com.yyide.chatim_pro.databinding.ItemTeacherRuleInfoBinding
import com.yyide.chatim_pro.model.attendance.teacher.ItemRuleInfoBean
import com.yyide.chatim_pro.utils.GlideUtil
import com.yyide.chatim_pro.utils.hide
import com.yyide.chatim_pro.utils.remove
import com.yyide.chatim_pro.utils.show

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

    private val adapter: BaseQuickAdapter<ItemRuleInfoBean, BaseViewHolder> by lazy {
        object :
            BaseQuickAdapter<ItemRuleInfoBean, BaseViewHolder>(R.layout.item_teacher_rule_info) {
            override fun convert(holder: BaseViewHolder, item: ItemRuleInfoBean) {
                val viewBind = ItemTeacherRuleInfoBinding.bind(holder.itemView)
                viewBind.itemTeacherAttendanceRuleTitleTv.text = item.title
                viewBind.itemTeacherAttendanceRuleSubTv.text = item.subTitle
            }
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
        binding.rvTeacherAttendanceRule.layoutManager = LinearLayoutManager(this)
        binding.rvTeacherAttendanceRule.adapter = adapter
        adapter.setEmptyView(R.layout.empty)

        viewModel.attendanceRule.observe(this){


            if (!it.hasScheduling){
                binding.clTeacherAttendanceRuleTime.hide()
                binding.clTeacherAttendanceRuleRange.hide()
                binding.viewEmpty.root.show()
                return@observe
            }

            binding.clTeacherAttendanceRuleTime.show()
            binding.clTeacherAttendanceRuleRange.show()
            binding.viewEmpty.root.hide()

            binding.tvTeacherAttendanceRuleName.text = it.personName
            binding.tvTeacherAttendanceJob.text = String.format(getString(R.string.attendance_group),it.groupName)

            val timeRuleSb = StringBuilder()
            for ((i,workStr) in it.workDay.withIndex()){
                timeRuleSb.append(workStr)
                if (i != (it.workDay.size - 1)){
                    timeRuleSb.append("、")
                }
            }
            timeRuleSb.append(" ").append(it.timeRange).append("\r\n")
            for ((i,restStr) in it.restDay.withIndex()){
                timeRuleSb.append(restStr)
                if (i != (it.restDay.size - 1)){
                    timeRuleSb.append("、")
                }
            }
            if (!it.restDay.isNullOrEmpty()) {
                timeRuleSb.append(" 休息")
            }
            binding.tvTeacherAttendanceRuleTime.text = timeRuleSb.toString()

            val listRuleInfo = mutableListOf<ItemRuleInfoBean>()

            for ((index,data) in it.addressList.withIndex()){
                val bean = ItemRuleInfoBean("打卡地址${index+1}",data.addressName)
                listRuleInfo.add(bean)
            }

            it.wifiList.forEach { wifi ->
                val bean = ItemRuleInfoBean(wifi.wifiName,wifi.wifiMac)
                listRuleInfo.add(bean)
            }

            for ((index,data) in it.deviceList.withIndex()){
                val bean = ItemRuleInfoBean("设备蓝牙名称${index+1}",data.deviceId)
                listRuleInfo.add(bean)
            }

            adapter.setList(listRuleInfo)


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
            val v = binding.rvTeacherAttendanceRule.visibility
            if (v == View.VISIBLE){
                binding.ivTeacherAttendanceRuleRangeLogo.setImageResource(R.mipmap.icon_down)
                binding.rvTeacherAttendanceRule.remove()
            }else{
                binding.ivTeacherAttendanceRuleRangeLogo.setImageResource(R.mipmap.icon_up)
                binding.rvTeacherAttendanceRule.show()
            }
        }

    }

}