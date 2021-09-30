package com.yyide.chatim.activity.schedule

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
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
import com.yyide.chatim.databinding.ActivityScheduleEditBinding
import com.yyide.chatim.model.schedule.*
import com.yyide.chatim.utils.ColorUtil
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.view.SpacesItemDecoration
import com.yyide.chatim.viewmodel.ScheduleEditViewModel

class ScheduleEditActivity : BaseActivity() {
    lateinit var scheduleEditBinding: ActivityScheduleEditBinding
    private var labelList = mutableListOf<LabelListRsp.DataBean>()
    private val scheduleEditViewModel:ScheduleEditViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleEditBinding = ActivityScheduleEditBinding.inflate(layoutInflater)
        setContentView(scheduleEditBinding.root)
        initView()
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_schedule_edit
    }

    private fun initView() {
        scheduleEditBinding.top.title.text = "日程编辑"
        scheduleEditBinding.top.backLayout.setOnClickListener {
            //finish()
            DialogUtil.showRepetitionScheduleModifyDialog(this){
                loge("编辑日程重复性类型：$it")
            }
        }
        scheduleEditBinding.top.ivRight.visibility = View.VISIBLE
        scheduleEditBinding.top.ivRight.setOnClickListener {
            DialogUtil.showScheduleDelDialog(this,scheduleEditBinding.top.ivRight)
        }

        scheduleEditBinding.btnAddLabel.setOnClickListener {
            val intent = Intent(this, ScheduleAddLabelActivity::class.java)
            intent.putExtra("labelList",JSON.toJSONString(scheduleEditViewModel.labelListLiveData.value))
            startActivityForResult(intent, REQUEST_CODE_LABEL_SELECT)
        }

        scheduleEditBinding.clRemind.setOnClickListener {
            val intent = Intent(this, ScheduleRemindActivity::class.java)
            intent.putExtra("data",JSON.toJSONString(scheduleEditViewModel.remindLiveData.value))
            startActivityForResult(intent, REQUEST_CODE_REMIND_SELECT)
        }
        scheduleEditBinding.clRepetition.setOnClickListener {
            val intent = Intent(this, ScheduleRepetitionActivity::class.java)
            intent.putExtra("data",JSON.toJSONString(scheduleEditViewModel.repetitionLiveData.value))
            startActivityForResult(intent,REQUEST_CODE_REPETITION_SELECT)
        }
        scheduleEditBinding.clAddress.setOnClickListener {
            val intent = Intent(this, ScheduleAddressActivity::class.java)
            intent.putExtra("data",JSON.toJSONString(scheduleEditViewModel.siteLiveData.value))
            startActivityForResult(intent, REQUEST_CODE_SITE_SELECT)
        }
        scheduleEditBinding.clDate.setOnClickListener {
            val intent = Intent(this, ScheduleDateIntervalActivity::class.java)
            val allDay = scheduleEditViewModel.allDayLiveData.value
            val startTime = scheduleEditViewModel.startTimeLiveData.value
            val endTime = scheduleEditViewModel.endTimeLiveData.value
            intent.putExtra("allDay",allDay)
            intent.putExtra("startTime",startTime)
            intent.putExtra("endTime",endTime)
            startActivityForResult(intent, REQUEST_CODE_DATE_SELECT)
        }
        scheduleEditBinding.clParticipant.setOnClickListener {
            val intent = Intent(this, ScheduleParticipantActivity::class.java)
            intent.putExtra("data",JSON.toJSONString(scheduleEditViewModel.participantList.value))
            startActivityForResult(intent, REQUEST_CODE_PARTICIPANT_SELECT)
        }

        val flexboxLayoutManager = FlexboxLayoutManager(this)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        scheduleEditBinding.rvLabelList.layoutManager = flexboxLayoutManager
        scheduleEditBinding.rvLabelList.addItemDecoration(
            SpacesItemDecoration(
                SpacesItemDecoration.dip2px(
                    5f
                )
            )
        )
        adapter.setList(labelList)
        scheduleEditBinding.rvLabelList.adapter = adapter
    }

    val adapter = object :
        BaseQuickAdapter<LabelListRsp.DataBean, BaseViewHolder>(R.layout.item_schedule_label_flow_list) {
        override fun convert(holder: BaseViewHolder, item: LabelListRsp.DataBean) {
            val drawable = GradientDrawable()
            drawable.cornerRadius = DisplayUtils.dip2px(this@ScheduleEditActivity, 2f).toFloat()
            drawable.setColor(ColorUtil.parseColor(item.colorValue))
            holder.getView<TextView>(R.id.tv_label).background = drawable
            holder.setText(R.id.tv_label, item.labelName)
            holder.itemView.setOnClickListener {
                loge("item=$item")
                remove(item)
                labelList.remove(item)
                notifyDataSetChanged()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //选择标签
        if (requestCode == REQUEST_CODE_LABEL_SELECT && resultCode == RESULT_OK && data != null) {
            val stringExtra = data.getStringExtra("labelList")
            loge("onActivityResult:$stringExtra")
            val parseArray: List<LabelListRsp.DataBean> =
                JSONArray.parseArray(stringExtra, LabelListRsp.DataBean::class.java)
            if (parseArray.isNotEmpty()) {
                labelList.clear()
                labelList.addAll(parseArray)
                scheduleEditViewModel.labelListLiveData.value = labelList
                adapter.setList(labelList)
            }
            return
        }

        //选择场地
        if (requestCode == REQUEST_CODE_SITE_SELECT && resultCode == RESULT_OK && data != null) {
            val stringExtra = data.getStringExtra("address")
            loge("onActivityResult:$stringExtra")
            val siteNameBean: SiteNameRsp.DataBean =
                JSON.parseObject(stringExtra, SiteNameRsp.DataBean::class.java)
            val name = siteNameBean.name
            scheduleEditBinding.tvAddress.setTextColor(resources.getColor(R.color.black9))
            scheduleEditBinding.tvAddress.text = name
            scheduleEditViewModel.siteLiveData.value = siteNameBean
            return
        }

        //选择日期
        if (requestCode == REQUEST_CODE_DATE_SELECT && resultCode == RESULT_OK && data != null) {
            val allDay = data.getBooleanExtra("allDay", false)
            val startTime = data.getStringExtra("startTime")
            val endTime = data.getStringExtra("endTime")
            loge("allDay=$allDay,startTime=$startTime,endTime=$endTime")
            scheduleEditBinding.tvDateStart.text =
                DateUtils.formatTime(startTime, "", "MM月dd日 HH:mm")
            scheduleEditBinding.tvDateEnd.text = DateUtils.formatTime(endTime, "", "MM月dd日 HH:mm")
            scheduleEditViewModel.startTimeLiveData.value = startTime
            scheduleEditViewModel.endTimeLiveData.value = endTime
            scheduleEditViewModel.allDayLiveData.value = allDay
            return
        }
        //选择提醒
        if (requestCode == REQUEST_CODE_REMIND_SELECT && resultCode == RESULT_OK && data != null) {
            val stringExtra = data.getStringExtra("data")
            val remind = JSON.parseObject(stringExtra, Remind::class.java)
            loge("id=${remind.id},name=${remind.title}")
            scheduleEditBinding.tvRemind.text = remind.title
            scheduleEditViewModel.remindLiveData.value = remind
            return
        }
        //选择重复
        if (requestCode == REQUEST_CODE_REPETITION_SELECT && resultCode == RESULT_OK && data != null){
            val stringExtra = data.getStringExtra("data")
            val repetition = JSON.parseObject(stringExtra, Repetition::class.java)
            loge("title=${repetition.title},rule=${repetition.rule}")
            scheduleEditBinding.tvRepetition.text = repetition.title
            scheduleEditViewModel.repetitionLiveData.value = repetition
            return
        }
        //选择参与人
        if (requestCode == REQUEST_CODE_PARTICIPANT_SELECT && resultCode == RESULT_OK && data != null) {
            val stringExtra = data.getStringExtra("data")
            val list = JSONArray.parseArray(
                stringExtra,
                ParticipantRsp.DataBean.ParticipantListBean::class.java
            )
            if (list != null) {
                scheduleEditViewModel.participantList.value = list
                //list.map { ScheduleData.ParticipantBean(it.id) }
                //显示参与人
                val stringBuilder = StringBuilder()
                list.map { it.name }.forEach {
                    stringBuilder.append(it).append("  ")
                }
                if (stringBuilder.isEmpty() || stringBuilder.isBlank()){
                    scheduleEditBinding.tvParticipant.text = "添加参与人"
                    scheduleEditBinding.tvParticipant.setTextColor(resources.getColor(R.color.black11))
                }else{
                    scheduleEditBinding.tvParticipant.text = stringBuilder
                    scheduleEditBinding.tvParticipant.setTextColor(resources.getColor(R.color.black9))
                }

            }
            return
        }
    }

    companion object {
        //标签选择
        const val REQUEST_CODE_LABEL_SELECT = 100

        //场地选择
        const val REQUEST_CODE_SITE_SELECT = 101

        //提醒选择
        const val REQUEST_CODE_REMIND_SELECT = 102

        //重复选择
        const val REQUEST_CODE_REPETITION_SELECT = 103

        //时间选择
        const val REQUEST_CODE_DATE_SELECT = 104

        //选择参与人
        const val REQUEST_CODE_PARTICIPANT_SELECT = 105
    }
}