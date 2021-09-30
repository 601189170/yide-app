package com.yyide.chatim.activity.schedule

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleParticipantBinding
import com.yyide.chatim.fragment.schedule.StaffParticipantFragment
import com.yyide.chatim.model.schedule.ParticipantRsp
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.SpacesFlowItemDecoration
import com.yyide.chatim.viewmodel.ParticipantSharedViewModel

/**
 * 选择参与人
 */
class ScheduleParticipantActivity : BaseActivity() {
    lateinit var scheduleParticipantBinding: ActivityScheduleParticipantBinding
    val fragments = mutableListOf<Fragment>()
    val tabs = mutableListOf<String>()
    private val participantSharedViewModel: ParticipantSharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleParticipantBinding = ActivityScheduleParticipantBinding.inflate(layoutInflater)
        setContentView(scheduleParticipantBinding.root)
        initView()
        initData()
        initParticipantSelected()
        initViewPager()
        participantSharedViewModel.curStaffParticipantList.observe(this, {
            loge("当前选中参与人数据发生变化：${it.size}")
            scheduleParticipantBinding.clSearchResult.visibility =
                if (it.isEmpty()) View.GONE else View.VISIBLE
            adapter.setList(it)
        })
    }

    private fun initParticipantSelected() {
        val flexboxLayoutManager = FlexboxLayoutManager(this)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        scheduleParticipantBinding.rvParticipant.layoutManager = flexboxLayoutManager
        scheduleParticipantBinding.rvParticipant.addItemDecoration(
            SpacesFlowItemDecoration(DisplayUtils.dip2px(this, 20f), DisplayUtils.dip2px(this, 8f))
        )
        adapter.setList(participantSharedViewModel.curStaffParticipantList.value)
        scheduleParticipantBinding.rvParticipant.adapter = adapter
        adapter.setOnItemClickListener { _, _, position ->
            //更新view model里的数据源
            val removeAt = participantSharedViewModel.curStaffParticipantList.value
            removeAt?.removeAt(position)
            participantSharedViewModel.curStaffParticipantList.postValue(removeAt)
        }
    }

    val adapter = object :
        BaseQuickAdapter<ParticipantRsp.DataBean.ParticipantListBean, BaseViewHolder>(R.layout.item_schedule_participant_flow_list) {
        override fun convert(
            holder: BaseViewHolder,
            item: ParticipantRsp.DataBean.ParticipantListBean
        ) {
            holder.setText(R.id.tv_participant, item.name)
        }
    }

    private fun initViewPager() {
        fragments.add(StaffParticipantFragment.newInstance("教职工列表"))
        fragments.add(StaffParticipantFragment.newInstance("学生列表"))
        fragments.add(StaffParticipantFragment.newInstance("家长列表"))
        tabs.add("教职工")
        tabs.add("学生")
        tabs.add("家长")
        scheduleParticipantBinding.viewpager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragments.size

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }

        }
        TabLayoutMediator(
            scheduleParticipantBinding.tablayout,
            scheduleParticipantBinding.viewpager,
            true
        ) { tab: TabLayout.Tab, position: Int ->
            //这里需要根据position修改tab的样式和文字等
            tab.text = tabs[position]
        }.attach()
    }

    private fun initData() {

    }

    private fun initView() {
        val stringExtra = intent.getStringExtra("data")
        val participantDataList = JSONArray.parseArray(
            stringExtra,
            ParticipantRsp.DataBean.ParticipantListBean::class.java
        )
        if (participantDataList != null) {
            participantSharedViewModel.curStaffParticipantList.value = participantDataList
        }
        scheduleParticipantBinding.top.title.text = "添加参与人"
        scheduleParticipantBinding.top.backLayout.setOnClickListener {
            finish()
        }
        scheduleParticipantBinding.top.tvRight.visibility = View.VISIBLE
        scheduleParticipantBinding.top.tvRight.text = "确定"
        scheduleParticipantBinding.top.tvRight.setTextColor(resources.getColor(R.color.colorPrimary))
        scheduleParticipantBinding.top.tvRight.setOnClickListener {
            val list = participantSharedViewModel.curStaffParticipantList.value
            if (list != null) {
                val intent = intent
                intent.putExtra("data", JSON.toJSONString(list))
                setResult(RESULT_OK, intent)
            }
            finish()
        }

    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_participant
}