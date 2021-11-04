package com.yyide.chatim.activity.meeting

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.meeting.viewmodel.MeetingHomeViewModel
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.databinding.ActivityMeetingHomeBinding
import com.yyide.chatim.databinding.ItemMeetingHomeBinding
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.utils.DateUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 会议首页
 * author lrz
 * time 2021年10月11日17:47:47
 */
class MeetingHomeActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityMeetingHomeBinding
    private val viewModel: MeetingHomeViewModel by viewModels()
    override fun getContentViewID(): Int {
        return R.layout.activity_meeting_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMeetingHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        EventBus.getDefault().register(this)
        setStatusBar(Color.parseColor("#F2F7FA"))
        initView()
    }

    private fun initView() {
        viewBinding.swipeRefreshLayout.isRefreshing = true
        viewBinding.swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        viewBinding.swipeRefreshLayout.setOnRefreshListener {
            request()
        }
        viewBinding.ivAdd.setOnClickListener { MeetingSaveActivity.jumpCreate(this) }
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
        adapter.setOnItemClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as ScheduleData
            MeetingSaveActivity.jumpUpdate(this, item.id)
        }
        request()
    }

    private fun request() {
//        showLoading()
        viewModel.meetingHomeLiveData.observe(this) {
//            hideLoading()
            viewBinding.swipeRefreshLayout.isRefreshing = false
            val result = it.getOrNull()
            if (result != null) {
                adapter.setList(result)
            }
        }
        viewModel.requestMeetingHomeList()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(messageEvent: EventMessage) {
        if (BaseConstant.TYPE_MEETING_UPDATE_LIST == messageEvent.code) {
            viewModel.requestMeetingHomeList()
        }
    }

    private val adapter =
        object : BaseQuickAdapter<ScheduleData, BaseViewHolder>(R.layout.item_meeting_home) {
            @SuppressLint("SetTextI18n")
            override fun convert(holder: BaseViewHolder, item: ScheduleData) {
                val viewBind = ItemMeetingHomeBinding.bind(holder.itemView)
                viewBind.tvTitle.text = item.name
                viewBind.tvTime.text = DateUtils.formatTime(
                    item.startTime,
                    "",
                    "yyyy-MM-dd HH:mm:ss"
                ) + "-" + DateUtils.formatTime(
                    item.endTime,
                    "",
                    "yyyy-MM-dd HH:mm:ss"
                )
            }
        }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}