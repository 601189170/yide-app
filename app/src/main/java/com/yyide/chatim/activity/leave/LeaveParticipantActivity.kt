package com.yyide.chatim.activity.leave

import android.os.Bundle
import android.view.View
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
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityLeaveParticipantBinding
import com.yyide.chatim.fragment.schedule.StaffParticipantFragment
import com.yyide.chatim.model.schedule.ParticipantRsp
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.YDToastUtil
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.SpacesFlowItemDecoration
import com.yyide.chatim.viewmodel.ParticipantSharedViewModel

/**
 * 选择参与人
 */
class LeaveParticipantActivity : BaseActivity() {
    lateinit var participantBinding: ActivityLeaveParticipantBinding
    var selectList = mutableListOf<ParticipantRsp.DataBean.ParticipantListBean>()
    val tabs = mutableListOf<String>()
    private val participantSharedViewModel: ParticipantSharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        participantBinding = ActivityLeaveParticipantBinding.inflate(layoutInflater)
        setContentView(participantBinding.root)
        initView()
    }

    private fun initView() {
        participantBinding.top.backLayout.setOnClickListener { finish() }
        participantBinding.top.title.text = "选择抄送人"
        participantSharedViewModel.curStaffParticipantList.observe(this) {
            loge("当前选中参与人数据发生变化：${it.size}")
            loge("当前选中参与人数据发生变化${JSON.toJSONString(it)}")
            if (it.size > 20) {
                YDToastUtil.showMessage("抄送人最多选择20人")
            } else {
                selectList = it
                participantBinding.tvCount.text = "${it.size}人"
            }
        }
        supportFragmentManager.beginTransaction().replace(
            participantBinding.container.id,
            StaffParticipantFragment.newInstance(StaffParticipantFragment.PARTICIPANT_TYPE_STAFF)
        ).commit()

        participantBinding.btnCommit.setOnClickListener {
            //返回上一页
            val intent = intent
            intent.putExtra("ccList", JSON.toJSONString(selectList))
            this.setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun getContentViewID(): Int = R.layout.activity_leave_participant
}