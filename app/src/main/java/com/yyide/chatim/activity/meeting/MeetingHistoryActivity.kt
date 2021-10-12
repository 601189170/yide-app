package com.yyide.chatim.activity.meeting

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityMeetingHistoryBinding
import com.yyide.chatim.databinding.ItemMeetingHomeBinding

/**
 * 历史会议-搜索
 * author lrz
 * time 2021年10月11日17:47:47
 */
class MeetingHistoryActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityMeetingHistoryBinding

    override fun getContentViewID(): Int {
        return R.layout.activity_meeting_history
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMeetingHistoryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()
    }

    private fun initView() {
        viewBinding.recyclerview.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerview.adapter = adapter
        val lists = mutableListOf<String>()
        lists.add("1")
        lists.add("2")
        lists.add("3")
        lists.add("4")
        lists.add("5")
        lists.add("6")
        adapter.setList(lists)
    }

    private val adapter =
        object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_meeting_home) {
            override fun convert(holder: BaseViewHolder, item: String) {
                val viewBind = ItemMeetingHomeBinding.bind(holder.itemView)
                viewBind.tvTitle.text = "本学期研讨会议"
                viewBind.tvTime.text = "09:00-10:00"
            }
        }
}