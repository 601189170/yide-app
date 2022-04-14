package com.yyide.chatim_pro.activity.meeting

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.activity.meeting.viewmodel.MeetingHistoryViewModel
import com.yyide.chatim_pro.base.BaseActivity
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.databinding.ActivityMeetingHomeBinding
import com.yyide.chatim_pro.databinding.ItemMeetingHomeBinding
import com.yyide.chatim_pro.model.EventMessage
import com.yyide.chatim_pro.model.schedule.ScheduleData
import com.yyide.chatim_pro.utils.DateUtils
import com.yyide.chatim_pro.utils.logd
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 会议首页
 * author lrz
 * time 2021年10月11日17:47:47
 */
class MeetingHomeActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityMeetingHomeBinding
    private val viewModel: MeetingHistoryViewModel by viewModels()
    private val size = 20
    private var current = 1

    override fun getContentViewID(): Int {
        return R.layout.activity_meeting_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMeetingHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        EventBus.getDefault().register(this)
//        setStatusBar(Color.parseColor("#F2F7FA"))
        initView()
    }

    private fun initView() {

        val subStr = "${DateUtils.switchTime(Date(), "MM月dd日")} 今日"
        viewBinding.meetingHomeSubTitle.text = subStr


        viewModel.meetingListData.observe(this) {
            viewBinding.swipeRefreshLayout.isRefreshing = false
            val result = it.getOrNull()
            if (result != null) {
                if (current == 1) {
                    adapter.setList(result.list)
                } else {
                    adapter.addData(result.list)
                }
                if (result.list.size < size) {
                    //如果不够一页,显示没有更多数据布局
                    adapter.loadMoreModule.loadMoreEnd()
                } else {
                    adapter.loadMoreModule.loadMoreComplete()
                }
            }
        }
        viewBinding.swipeRefreshLayout.isRefreshing = true
        viewBinding.swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        viewBinding.swipeRefreshLayout.setOnRefreshListener {
            request()
        }
        viewBinding.ivAdd.setOnClickListener {
            MeetingSaveActivity.jumpCreate(this)
        }
        viewBinding.clHistory.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MeetingHistoryActivity::class.java
                )
            )
        }
        viewBinding.backLayout.setOnClickListener { finish() }
        viewBinding.recyclerviewMeeting.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerviewMeeting.adapter = adapter
        adapter.setEmptyView(R.layout.empty)
        adapter.loadMoreModule.setOnLoadMoreListener {
            adapter.loadMoreModule.isEnableLoadMore = true
            current++
            request()
        }
        adapter.setOnItemClickListener { adapter, view, position ->
            logd("setOnItemClickListener jump")
            val item = adapter.getItem(position) as ScheduleData
            MeetingDetailActivity.jump(this, item.id)
        }
        request()
    }

    private fun request() {
        viewModel.requestMeetingListData(size, current, "", 3)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(messageEvent: EventMessage) {
        if (BaseConstant.TYPE_MEETING_UPDATE_LIST == messageEvent.code) {
            current = 1
            request()
        }
    }

    private val adapter =
        object : BaseQuickAdapter<ScheduleData, BaseViewHolder>(R.layout.item_meeting_home),
            LoadMoreModule {
            @SuppressLint("SetTextI18n")
            override fun convert(holder: BaseViewHolder, item: ScheduleData) {
                val viewBind = ItemMeetingHomeBinding.bind(holder.itemView)
                viewBind.tvTitle.text = item.name
                viewBind.tvTime.text = judgeShowTimeStr(item.startTime, item.endTime)
            }
        }


    fun judgeShowTimeStr(startTime: String, endTime: String): String {
        val startTamp = DateUtils.parseTimestamp(startTime, "")
        val endTamp = DateUtils.parseTimestamp(startTime, "")

        val showTime = DateUtils.formatTime(
            startTime,
            "",
            "yyyy-MM-dd HH:mm"
        ) + " - " + DateUtils.formatTime(
            endTime,
            "",
            "yyyy-MM-dd HH:mm"
        )

        if (DateUtils.isToday(startTamp) && DateUtils.isToday(endTamp)) {
            return DateUtils.formatTime(
                startTime,
                "",
                "HH:mm"
            ) + "-" + DateUtils.formatTime(
                endTime,
                "",
                "HH:mm"
            )
        }

        if (DateUtils.isSameDay(startTime, endTime)) {
            return "${
                DateUtils.formatTime(
                    startTime,
                    "",
                    "yyyy-MM-dd"
                )
            } ${
                DateUtils.formatTime(
                    startTime,
                    "",
                    "HH:mm"
                )
            } - ${
                DateUtils.formatTime(
                    endTime,
                    "",
                    "HH:mm"
                )
            }"
        }


        return showTime

    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}