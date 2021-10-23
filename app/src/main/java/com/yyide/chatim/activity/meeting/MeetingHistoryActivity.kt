package com.yyide.chatim.activity.meeting

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.meeting.viewmodel.MeetingHistoryViewModel
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityMeetingHistoryBinding
import com.yyide.chatim.databinding.ItemMeetingHomeBinding
import com.yyide.chatim.model.schedule.ScheduleData
import com.yyide.chatim.utils.DateUtils

/**
 * 历史会议-搜索
 * author lrz
 * time 2021年10月11日17:47:47
 */
class MeetingHistoryActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityMeetingHistoryBinding
    private val viewModel: MeetingHistoryViewModel by viewModels()
    private val size = 15
    private var current = 1

    override fun getContentViewID(): Int {
        return R.layout.activity_meeting_history
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMeetingHistoryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()
        request()
    }

    private fun initView() {
        viewBinding.cancel.setOnClickListener { finish() }
        viewBinding.top.backLayout.setOnClickListener { finish() }
        viewBinding.ivDel.setOnClickListener { viewBinding.edit.text = null }
        viewBinding.top.title.text = getString(R.string.meeting_history_title)
        viewBinding.recyclerview.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerview.adapter = adapter
        adapter.setEmptyView(R.layout.empty)
        adapter.loadMoreModule.setOnLoadMoreListener {
            adapter.loadMoreModule.isEnableLoadMore = true
            //请求数据
            current++
            viewModel.requestMeetingHomeList(size, current)
        }
        adapter.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        adapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
//        adapter.setOnItemClickListener { adapter, view, position ->
//            val item = adapter.getItem(position) as ScheduleData
//            MeetingCreateUpdateActivity.jumpUpdate(this, item.id)
//        }
    }

    private fun request() {
        showLoading()
        viewModel.meetingHistoryLiveData.observe(this) {
            hideLoading()
            val result = it.getOrNull()
            if (result != null) {
                if (current == 1) {
                    adapter.setList(result)
                } else {
                    adapter.addData(result)
                }
                if (result.size < size) {
                    //如果不够一页,显示没有更多数据布局
                    adapter.loadMoreModule.loadMoreEnd()
                } else {
                    adapter.loadMoreModule.loadMoreComplete()
                }
            }
        }
        viewModel.requestMeetingHomeList(size, current)
    }

    private val adapter =
        object : BaseQuickAdapter<ScheduleData, BaseViewHolder>(R.layout.item_meeting_home),
            LoadMoreModule {
            @SuppressLint("SetTextI18n")
            override fun convert(holder: BaseViewHolder, item: ScheduleData) {
                val viewBind = ItemMeetingHomeBinding.bind(holder.itemView)
                viewBind.tvTitle.text = item.name
                viewBind.tvTime.text = DateUtils.formatTime(
                    item.startTime,
                    "",
                    "HH:mm"
                ) + "-" + DateUtils.formatTime(
                    item.endTime,
                    "",
                    "HH:mm"
                )
            }
        }
}