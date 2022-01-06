package com.yyide.chatim.activity.gate

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.yyide.chatim.R
import com.yyide.chatim.activity.PhotoViewActivity.Companion.start
import com.yyide.chatim.adapter.attendance.v2.StudentDayStatisticsListAdapter
import com.yyide.chatim.adapter.gate.GateDetailInfo
import com.yyide.chatim.adapter.gate.GateDetailInfoListAdapter
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim.databinding.ActivityGateDetailInfoBinding
import com.yyide.chatim.dialog.DeptSelectPop
import com.yyide.chatim.fragment.leave.RequestLeaveStaffFragment
import com.yyide.chatim.model.LeaveDeptRsp
import com.yyide.chatim.model.gate.Result
import com.yyide.chatim.model.gate.StudentInfoBean
import com.yyide.chatim.model.gate.ThroughPeopleDetail
import com.yyide.chatim.utils.DatePickerDialogUtil
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim.utils.loge
import com.yyide.chatim.viewmodel.gate.ThroughPeopleDetailViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.joda.time.DateTime
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
    private var peopleType: String? = null
    private var queryTime: String? = null
    private var siteId: String? = null
    private var userId: String? = null
    private lateinit var curDateTime: DateTime
    private val eventList = mutableListOf<LeaveDeptRsp.DataBean>()
    private val throughPeopleDetailViewModel: ThroughPeopleDetailViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gateDetailInfoBinding = ActivityGateDetailInfoBinding.inflate(layoutInflater)
        setContentView(gateDetailInfoBinding.root)
        peopleType = intent.getStringExtra("peopleType")
        queryTime = intent.getStringExtra("queryTime")
        siteId = intent.getStringExtra("siteId")
        userId = intent.getStringExtra("userId")
        if (!TextUtils.isEmpty(queryTime)) {
            curDateTime = ScheduleDaoUtil.toDateTime(queryTime ?: "")
        } else {
            curDateTime = DateTime.now().simplifiedDataTime()
            queryTime = curDateTime.toStringTime()
        }
        initView()
        lifecycleScope.launch {
            throughPeopleDetailViewModel.throughPeopleDetail.collect {
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
            throughPeopleDetailViewModel.studentListInfo.collect {
                when (it) {
                    is Result.Success -> {
                        handleStudentListData(it.data)
                    }
                    is Result.Error -> {
                        loge("${it.exception?.localizedMessage}")
                        ToastUtils.showShort("${it.exception?.localizedMessage}")
                    }
                }
            }
        }
        if (!TextUtils.isEmpty(userId)) {
            throughPeopleDetailViewModel.queryPersonalPassageDetails(
                peopleType,
                queryTime,
                userId,
                siteId
            )
        } else {
            //没有传userid为家长视角，现需要请求学生列表
            peopleType = "1"
            throughPeopleDetailViewModel.queryStudentInfoByUserId()
        }
    }

    /**
     * 处理学生列表数据
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun handleStudentListData(data: List<StudentInfoBean>?) {
        if (data == null || data.isEmpty()) {
            return
        }
        eventList.clear()
        data.forEach {
            val dataBean = LeaveDeptRsp.DataBean()
            dataBean.deptId = it.userId
            dataBean.deptName = it.userName
            dataBean.isDefault = 0
            eventList.add(dataBean)
        }
        if (eventList.isNotEmpty()) {
            eventList[0].isDefault = 1
        }

        val eventOptional = eventList.filter { it.isDefault == 1 }
        if (eventOptional.isNotEmpty()) {
            val dataBean = eventOptional[0]
            val deptName = dataBean.deptName
            userId = dataBean.deptId
            throughPeopleDetailViewModel.queryPersonalPassageDetails(
                peopleType,
                queryTime,
                userId,
                siteId
            )
            val drawable =
                ResourcesCompat.getDrawable(resources, R.drawable.gate_down_icon, null)?.apply {
                    setBounds(0, 0, minimumWidth, minimumHeight)
                }
            gateDetailInfoBinding.top.title.text = "${deptName}的通行数据"
            gateDetailInfoBinding.top.title.isEnabled = true
            gateDetailInfoBinding.top.title.setCompoundDrawables(null, null, drawable, null)
            gateDetailInfoBinding.top.title.setOnClickListener {
                val deptSelectPop = DeptSelectPop(this, 4, eventList)
                deptSelectPop.setOnCheckedListener { dataBean ->
                    loge("选择的学生：$dataBean")
                    userId = dataBean.deptId
                    gateDetailInfoBinding.top.title.text = "${dataBean.deptName}的通行数据"
                    throughPeopleDetailViewModel.queryPersonalPassageDetails(
                        peopleType,
                        queryTime,
                        userId,
                        siteId
                    )
                }
            }
        }
    }

    /**
     * 请求数据成功，更新UI
     */
    private fun handleData(data: ThroughPeopleDetail?) {
        if (data == null) {
            return
        }
        gateDetailInfoBinding.top.title.text = "${data.name}的通行数据"
        gateDetailInfoBinding.tvUsername.text = "${data.name}"
        gateDetailInfoBinding.tvTodayPass.text =
            String.format(getString(R.string.gate_today_through_number), data.number)
        if (TextUtils.isEmpty(data.earliestTime)) {
            gateDetailInfoBinding.groupEarliestPunchIn.visibility = View.GONE
        } else {
            gateDetailInfoBinding.groupEarliestPunchIn.visibility = View.VISIBLE
        }
        data.earliestTime?.let {
            val earliestThroughTime =
                ScheduleDaoUtil.toDateTime(it).toStringTime("HH:mm") + " 入-${data.earliestAddress}"
            gateDetailInfoBinding.tvEarliestPunchInTitle.text = earliestThroughTime
        }
        if (TextUtils.isEmpty(data.latestTime)) {
            gateDetailInfoBinding.groupLatestPunchOut.visibility = View.GONE
        } else {
            gateDetailInfoBinding.groupLatestPunchOut.visibility = View.VISIBLE
        }
        data.latestTime?.let {
            val latestThroughTime =
                ScheduleDaoUtil.toDateTime(it).toStringTime("HH:mm") + " 出-${data.latestAddress}"
            gateDetailInfoBinding.tvLatestPunchOutTitle.text = latestThroughTime
        }

        if (TextUtils.isEmpty(data.earliestTime) && TextUtils.isEmpty(data.earliestTime)) {
            gateDetailInfoBinding.vLine.visibility = View.GONE
        }

        showFaceImage(gateDetailInfoBinding.ivStudentHead, data.image ?: "")
        dataList.clear()
        data.trajectoryInfo?.forEach {
            val toDateTime = ScheduleDaoUtil.toDateTime(it.inOutTime ?: "")
            val date = toDateTime.toStringTime("yyyy/MM/dd")
            val time = toDateTime.toStringTime("HH:mm:ss")
            val title = "${it.siteName}-${it.machineName}"
            val type = it.inOut?.toInt() ?: 2
            dataList.add(GateDetailInfo(date, time, type, title))
        }
        gateDetailInfoListAdapter.notifyDataSetChanged()
    }

    private fun initView() {
        gateDetailInfoBinding.tvDatePick.text = curDateTime.toStringTime("yyyy/MM/dd")
        gateDetailInfoBinding.top.backLayout.setOnClickListener {
            finish()
        }
        //初始化详情记录列表
        gateDetailInfoBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        gateDetailInfoListAdapter = GateDetailInfoListAdapter(this, dataList)
        gateDetailInfoBinding.recyclerView.adapter = gateDetailInfoListAdapter

        //选择日期
        gateDetailInfoBinding.tvDatePick.setOnClickListener {
            DatePickerDialogUtil.showDate(
                this,
                "选择日期",
                curDateTime.toStringTime(""),
                startTimeListener
            )
        }
    }

    //日期选择监听
    private val startTimeListener =
        OnDateSetListener { _, millseconds: Long ->
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val timingTime = simpleDateFormat.format(Date(millseconds))
            curDateTime = ScheduleDaoUtil.toDateTime(timingTime)
            queryTime = timingTime
            loge("startTimeListener: $timingTime")
            val toStringTime = curDateTime.toStringTime("yyyy/MM/dd")
            gateDetailInfoBinding.tvDatePick.text = toStringTime
            throughPeopleDetailViewModel.queryPersonalPassageDetails(
                peopleType,
                queryTime,
                userId,
                siteId
            )
        }

    /**
     * @param imageView
     * @param path
     */
    private fun showFaceImage(imageView: ImageView, path: String) {
        loge("showFaceImage: $path")
        if (TextUtils.isEmpty(path)) {
            return
        }
        imageView.visibility = View.VISIBLE
        var facePath = path
        if (!path.contains("https://") && !path.contains("http://")) {
            facePath = "http://$path"
        }
        Glide.with(this)
            .load(facePath)
            .circleCrop()
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            //.placeholder(R.drawable.default_head)
            //.error(R.drawable.default_head)
            // .skipMemoryCache(true)
            // .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView)
        val finalFacePath = facePath
        imageView.setOnClickListener { v: View? ->
            start(
                this,
                finalFacePath
            )
        }
    }
}