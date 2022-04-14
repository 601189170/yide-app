package com.yyide.chatim_pro.activity.schedule

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.SpData
import com.yyide.chatim_pro.base.BaseActivity
import com.yyide.chatim_pro.databinding.ActivityScheduleParticipantBinding
import com.yyide.chatim_pro.fragment.schedule.StaffParticipantFragment
import com.yyide.chatim_pro.model.schedule.ParticipantRsp
import com.yyide.chatim_pro.utils.DisplayUtils
import com.yyide.chatim_pro.utils.loge
import com.yyide.chatim_pro.view.SpacesFlowItemDecoration
import com.yyide.chatim_pro.viewmodel.ParticipantSharedViewModel

/**
 * 选择参与人
 */
class ScheduleParticipantActivity : BaseActivity() {
    lateinit var scheduleParticipantBinding: ActivityScheduleParticipantBinding
    val fragments = mutableListOf<Fragment>()
    val tabs = mutableListOf<String>()
    private val participantSharedViewModel: ParticipantSharedViewModel by viewModels()

    // 判断是否从会议选择的参与人
    var isFromMeeting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleParticipantBinding = ActivityScheduleParticipantBinding.inflate(layoutInflater)
        setContentView(scheduleParticipantBinding.root)
        initView()
        initData()
        initParticipantSelected()
        initViewPager()
        participantSharedViewModel.curStaffParticipantList.observe(this) {
            loge("当前选中参与人数据发生变化：${it.size}")
            loge("当前选中参与人数据发生变化${JSON.toJSONString(it)}")
            scheduleParticipantBinding.clSearchResult.visibility =
                if (it.isEmpty()) View.GONE else View.VISIBLE
            adapter.setList(it)
            if (it.size > 0) {
                scheduleParticipantBinding.tvYx.visibility = View.VISIBLE
            } else {
                scheduleParticipantBinding.tvYx.visibility = View.GONE
            }

        }
    }

    private fun initParticipantSelected() {
        val flexboxLayoutManager = FlexboxLayoutManager(this)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        scheduleParticipantBinding.rvParticipant.layoutManager = flexboxLayoutManager
        scheduleParticipantBinding.rvParticipant.addItemDecoration(
            SpacesFlowItemDecoration(DisplayUtils.dip2px(this, 10f), DisplayUtils.dip2px(this, 5f))
        )
        adapter.setList(participantSharedViewModel.curStaffParticipantList.value)

        scheduleParticipantBinding.rvParticipant.adapter = adapter
        adapter.setOnItemClickListener { _, _, position ->
            //更新view model里的数据源
            val removeAt = participantSharedViewModel.curStaffParticipantList.value
            if (removeAt?.get(position)?.name != SpData.getUser().name){
                removeAt?.removeAt(position)
            }
            participantSharedViewModel.curStaffParticipantList.postValue(removeAt)
        }
    }

    val adapter = object :
        BaseQuickAdapter<ParticipantRsp.DataBean.ParticipantListBean, BaseViewHolder>(R.layout.item_schedule_participant_flow_list) {
        override fun convert(
            holder: BaseViewHolder,
            item: ParticipantRsp.DataBean.ParticipantListBean
        ) {
            val ivDel = holder.getView<ImageView>(R.id.iv_del)
            ivDel.visibility = if (item.name == SpData.getUser().name) View.GONE else View.VISIBLE
            holder.setText(R.id.tv_participant, item.name)
        }
    }

    private fun initViewPager() {
        fragments.add(StaffParticipantFragment.newInstance(StaffParticipantFragment.PARTICIPANT_TYPE_STAFF))
        ///fragments.add(StaffParticipantFragment.newInstance(StaffParticipantFragment.PARTICIPANT_TYPE_STUDENT))
        if (isFromMeeting) {
            fragments.add(StaffParticipantFragment.newInstance(StaffParticipantFragment.PARTICIPANT_TYPE_MEETING_GUARDIAN))
        } else {
            fragments.add(StaffParticipantFragment.newInstance(StaffParticipantFragment.PARTICIPANT_TYPE_GUARDIAN))
        }
        tabs.add("教职工")
        //tabs.add("学生")
        tabs.add("家长")
        scheduleParticipantBinding.viewpager.adapter =
            object : FragmentPagerAdapter(
                supportFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            ) {
                override fun getCount(): Int = fragments.size

                override fun getItem(position: Int): Fragment {
                    return fragments[position]
                }
            }
        scheduleParticipantBinding.tablayout.setViewPager(
            scheduleParticipantBinding.viewpager,
            tabs.toTypedArray()
        )
    }

    private fun initData() {

    }

    private fun initView() {
        val stringExtra = intent.getStringExtra("data")
        val type = intent.getStringExtra("type") ?: ""
        isFromMeeting = type == "meeting"
        loge("参与人已选中$stringExtra")
        val participantDataList = JSONArray.parseArray(
            stringExtra,
            ParticipantRsp.DataBean.ParticipantListBean::class.java
        )
        if (participantDataList != null) {
            participantDataList.forEach {
                if (it != null) {
                    it.name = it.name
                    it.realname = it.realname
                    if (!TextUtils.isEmpty(it.realname)) {
                        it.name = it.realname
                    }
                }

                //it.realname = it.userName
                //it.teacherId = it.userId
                //it.participantId = it.userId
                //it.id = it.userId
            }
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

        //日程搜索
        scheduleParticipantBinding.clSearch.setOnClickListener {
            val intent = Intent(this, ScheduleParticipantSearchActivity::class.java)
            participantSearchResultHandler.launch(intent)
        }

    }

    /**
     * 参与人搜索结果
     */
    private val participantSearchResultHandler =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val stringExtra = it.data?.getStringExtra("data")
                loge("参与人搜索结果:$stringExtra")
                if (!TextUtils.isEmpty(stringExtra)) {
                    val participantDataList = JSONArray.parseArray(
                        stringExtra,
                        ParticipantRsp.DataBean.ParticipantListBean::class.java
                    )
                    if (participantDataList != null && participantDataList.isNotEmpty()) {
                        val curStaffParticipantList =
                            participantSharedViewModel.curStaffParticipantList.value
                                ?: mutableListOf()
                        participantDataList.forEach { data ->
                            if (curStaffParticipantList.find { it.name == data.name } == null) {
                                curStaffParticipantList.add(data)
                            }
                        }
                        participantSharedViewModel.curStaffParticipantList.value =
                            curStaffParticipantList
                    }
                }
            }
        }

    override fun getContentViewID(): Int = R.layout.activity_schedule_participant
}