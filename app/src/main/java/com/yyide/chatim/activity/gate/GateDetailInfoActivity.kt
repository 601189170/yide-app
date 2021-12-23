package com.yyide.chatim.activity.gate

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.yyide.chatim.R
import com.yyide.chatim.adapter.gate.GateDetailInfo
import com.yyide.chatim.adapter.gate.GateDetailInfoListAdapter
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim.databinding.ActivityGateDetailInfoBinding
import com.yyide.chatim.fragment.leave.RequestLeaveStaffFragment
import com.yyide.chatim.utils.DatePickerDialogUtil
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil
import com.yyide.chatim.utils.loge
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author lt
 * @date 2021年12月21日15:02:02
 * @desc 闸机通行详情数据
 * 教职工身份可通过切换时间切换详情数据
 * 家长身份可以查询不同孩子和不同时间的详情数据
 */
class GateDetailInfoActivity : BaseActivity() {
    private lateinit var gateDetailInfoBinding: ActivityGateDetailInfoBinding
    private lateinit var gateDetailInfoListAdapter: GateDetailInfoListAdapter
    private val dataList = mutableListOf<GateDetailInfo>()
    private var startTime = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gateDetailInfoBinding = ActivityGateDetailInfoBinding.inflate(layoutInflater)
        setContentView(gateDetailInfoBinding.root)
        initView()
    }

    private fun initView() {
        val stringExtra = intent.getStringExtra("username")?:"欧阳娜娜"
        gateDetailInfoBinding.top.title.text = "${stringExtra}的通行数据"
        gateDetailInfoBinding.top.backLayout.setOnClickListener {
            finish()
        }
        //初始化详情记录列表
        initData()
        gateDetailInfoBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        gateDetailInfoListAdapter = GateDetailInfoListAdapter(this, dataList)
        gateDetailInfoBinding.recyclerView.adapter = gateDetailInfoListAdapter

        //选择日期
        gateDetailInfoBinding.tvDatePick.setOnClickListener {
            DatePickerDialogUtil.showDate(this, "选择日期", startTime,startTimeListener)
        }
    }
    //日期选择监听
    private val startTimeListener =
        OnDateSetListener { timePickerView: TimePickerDialog?, millseconds: Long ->
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val timingTime = simpleDateFormat.format(Date(millseconds))
            startTime = timingTime
            loge("startTimeListener: $timingTime")
            val toStringTime =
                ScheduleDaoUtil.toDateTime(timingTime, "yyyy-MM-dd").toStringTime("yyyy/MM/dd")
            gateDetailInfoBinding.tvDatePick.text = toStringTime
        }

    private fun initData() {
        dataList.add(GateDetailInfo("2021/12/01", "08:00:26", 1, "校门口-校门口1号闸机"))
        dataList.add(GateDetailInfo("2021/12/01", "08:00:26", 2, "校门口-校门口2号闸机"))
        dataList.add(GateDetailInfo("2021/12/01", "08:00:26", 1, "校门口-校门口3号闸机"))
        dataList.add(GateDetailInfo("2021/12/01", "08:00:26", 2, "校门口-校门口4号闸机"))
        dataList.add(GateDetailInfo("2021/12/01", "08:00:26", 1, "校门口-校门口5号闸机"))
    }

    override fun getContentViewID(): Int = R.layout.activity_gate_detail_info
}