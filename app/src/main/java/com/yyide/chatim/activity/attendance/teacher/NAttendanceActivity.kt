package com.yyide.chatim.activity.attendance.teacher

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tbruyelle.rxpermissions3.RxPermissions
import com.yyide.chatim.R
import com.yyide.chatim.activity.attendance.teacher.viewmodel.TeacherAttendanceViewModel
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityAttendanceTeacherBinding
import com.yyide.chatim.databinding.ItemPunchRecordBinding
import com.yyide.chatim.model.attendance.teacher.SignTimeListItem
import com.yyide.chatim.utils.*


/**
 *
 * @author shenzhibin
 * @date 2022/3/28 15:29
 * @description v2版本的考勤activity
 */
class NAttendanceActivity :
    KTBaseActivity<ActivityAttendanceTeacherBinding>(ActivityAttendanceTeacherBinding::inflate) {

    private val viewModel by viewModels<TeacherAttendanceViewModel>()

    //定位
    private var locationClient: AMapLocationClient? = null

    private lateinit var punchRecordAdapter : BaseQuickAdapter<SignTimeListItem, BaseViewHolder>

    companion object {
        fun startGo(context: Context) {
            val intent = Intent(context, NAttendanceActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()
        initListener()
        viewModel.queryPunchMessage()
        viewModel.queryAttendanceRule()
    }

    private fun requestPermission() {
        val rxPermissions = RxPermissions(this)
        var isAllGranted = true
        rxPermissions
            .requestEach(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
            ).subscribe {
                when {
                    it.granted -> {
                        logd("同意权限${it.name}")
                    }
                    it.shouldShowRequestPermissionRationale -> {
                        isAllGranted = false
                        showShotToast("需要申请${it.name}权限")
                    }
                    else -> {
                        isAllGranted = false
                        // 权限被拒绝
                        AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage(it.name)
                            .setPositiveButton("开启") { dialog: DialogInterface?, which: Int ->
                                val localIntent = Intent()
                                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                                localIntent.data = Uri.fromParts("package", packageName, null)
                                startActivity(localIntent)
                            }
                            .setNegativeButton("取消", null)
                            .create().show()
                    }
                }
            }

        if (isAllGranted){
            initLocation()
        }
    }


    override fun initView() {
        super.initView()

        binding.clTeacherAttendanceTop.title.text = "考勤打卡"

        GlideUtil.loadImageHead(
            applicationContext,
            viewModel.userInfo.avatar,
            binding.rivTeacherAttendanceImg
        )

        punchRecordAdapter = object : BaseQuickAdapter<SignTimeListItem, BaseViewHolder>(R.layout.item_punch_record) {
            override fun convert(holder: BaseViewHolder, item: SignTimeListItem) {
                val binding = ItemPunchRecordBinding.bind(holder.itemView)
                val signTypeStr = if (item.signType == 0) getString(R.string.sign_in) else getString(R.string.sign_out)
                val timeStr = "$signTypeStr${item.shouldSignTime}"
                val signStr = if (item.signResult == "未打卡") getString(R.string.not_punched) else "已打卡"
                val actualSignTime = "${item.actualSignTime}$signStr"
                binding.tvPunchTime.text = timeStr
                binding.tvPunchState.text = actualSignTime
                when(item.signResult){
                    "已打卡" -> {
                        binding.tvPunchStateLogo.text = ""
                        binding.tvPunchStateLogo.setBackgroundResource(R.mipmap.attendance_success_logo)
                    }
                    "迟到" ->{
                        binding.tvPunchStateLogo.text = "迟到"
                        binding.tvPunchStateLogo.setTextColor(R.color.late.asColor())
                        binding.tvPunchStateLogo.setBackgroundResource(R.drawable.bg_yellow_2)
                    }
                    "早退" ->{
                        binding.tvPunchStateLogo.text = "早退"
                        binding.tvPunchStateLogo.setTextColor(R.color.leave_early.asColor())
                        binding.tvPunchStateLogo.setBackgroundResource(R.drawable.bg_rad_2)
                    }
                    else ->{
                        binding.tvPunchStateLogo.text = ""
                        binding.tvPunchStateLogo.background = null
                    }
                }
            }
        }

        binding.rvTeacherAttendancePunchRecord.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.HORIZONTAL, false
        )
        binding.rvTeacherAttendancePunchRecord.adapter = punchRecordAdapter

        // 更新打卡信息
        viewModel.punchMessage.observe(this){
            binding.tvTeacherAttendanceJob.text = it.groupName
            binding.tvTeacherAttendanceName.text = it.personName
            if (it.signTimeList.size > 2) {
                binding.rvTeacherAttendancePunchRecord.addItemDecoration(SignSpacesItemDecoration(5,applicationContext))
            }else{
                binding.rvTeacherAttendancePunchRecord.addItemDecoration(SignSpacesItemDecoration(20,applicationContext))
            }
            punchRecordAdapter.setList(it.signTimeList)
        }

        // 实时更新考勤状态
        viewModel.punchInfo.observe(this){
            when(it.type){
                viewModel.punchTypeNOT -> {
                    binding.clTeacherAttendancePunch.setBackgroundResource(R.mipmap.attendance_fail_bg)
                    binding.tvTeacherAttendancePunchState.text = "无法打卡"
                    binding.ivTeacherAttendanceTipLogo.setImageResource(R.mipmap.attendance_warn_logo)
                    binding.tvTeacherAttendanceTip.text = getString(R.string.warn_out_of_range)
                }
                viewModel.punchTypeFieldwork ->{
                    binding.clTeacherAttendancePunch.setBackgroundResource(R.mipmap.attendance_fieldwork_bg)
                    binding.tvTeacherAttendancePunchState.text = "外勤打卡"
                    binding.ivTeacherAttendanceTipLogo.setImageResource(R.mipmap.address_logo)
                    binding.tvTeacherAttendanceTip.text = it.showContent
                }
                else -> {
                    binding.clTeacherAttendancePunch.setBackgroundResource(R.mipmap.attendance_can_punch)
                    binding.tvTeacherAttendancePunchState.text = "打卡"
                    binding.clTeacherAttendancePunch.setBackgroundResource(R.mipmap.attendance_can_punch_logo)
                    val showStr = String.format(getString(R.string.attendance_in_range),it.showContent)
                    binding.tvTeacherAttendancePunchState.text = showStr
                }
            }
        }
    }


    private fun initListener() {
        binding.clTeacherAttendanceTop.backLayout.setOnClickListener { finish() }

        binding.tvTeacherAttendanceStatistics.setOnClickListener {
            TeacherStatisticsActivity.startGo(this@NAttendanceActivity)
        }

        binding.tvTeacherAttendanceRule.setOnClickListener {
            AttendanceRuleActivity.startGo(this@NAttendanceActivity)
        }

        binding.clTeacherAttendancePunch.setOnClickListener {
            if (viewModel.punchInfo.value == null || viewModel.punchInfo.value?.type == viewModel.punchTypeNOT){
                return@setOnClickListener
            }
            viewModel.requestPunch()
        }

    }

    /**
     * 初始化高德定位
     */
    private fun initLocation() {

        logd("mac = ${WifiTool.getConnectedWifiMacAddress(applicationContext)}")
        AMapLocationClient.updatePrivacyAgree(applicationContext,true)
        AMapLocationClient.updatePrivacyShow(applicationContext,true,true)
        locationClient = AMapLocationClient(applicationContext)
        val locationOption = AMapLocationClientOption()
        locationOption.isMockEnable = false
        locationClient?.let {
            it.setLocationOption(locationOption)
            it.setLocationListener { locationInfo ->
                locationInfo?.let { info ->
                    if (info.errorCode == 0) {
                        viewModel.judgePunchFunction(info)
                    } else {
                        loge("errorCode=${info.errorCode},errorInfo=${info.errorInfo}")
                    }
                }
            }
        }

    }


    override fun onStart() {
        super.onStart()
        locationClient?.startLocation()
    }

    override fun onStop() {
        super.onStop()
        locationClient?.stopLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationClient?.let {
            it.setLocationListener(null)
            it.onDestroy()
        }
    }

}