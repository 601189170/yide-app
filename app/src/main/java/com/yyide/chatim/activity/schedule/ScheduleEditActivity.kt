package com.yyide.chatim.activity.schedule

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.promoterSelf
import com.yyide.chatim.databinding.ActivityScheduleEditBinding
import com.yyide.chatim.model.schedule.*
import com.yyide.chatim.model.schedule.Remind.Companion.getList
import com.yyide.chatim.model.schedule.Remind.Companion.getList2
import com.yyide.chatim.utils.*
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.view.SpacesItemDecoration
import com.yyide.chatim.viewmodel.ScheduleEditViewModel
import com.yyide.chatim.viewmodel.ScheduleMangeViewModel
import java.util.concurrent.atomic.AtomicReference

class ScheduleEditActivity : BaseActivity() {
    lateinit var scheduleEditBinding: ActivityScheduleEditBinding
    private var labelList = mutableListOf<LabelListRsp.DataBean>()
    private val scheduleEditViewModel: ScheduleEditViewModel by viewModels()
    private val scheduleMangeViewModel: ScheduleMangeViewModel by viewModels()
    var dateStart = AtomicReference("")
    var dateEnd = AtomicReference("")
    val list = getList()
    val list2 = getList2()
    var repetition:Boolean = false
    //否是是发起人
    private var promoter = false
    //开启编辑模式
    private var enableEditMode = false
    private var sourceRepetitionRule: Repetition? = null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleEditBinding = ActivityScheduleEditBinding.inflate(layoutInflater)
        setContentView(scheduleEditBinding.root)
        initView()
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_schedule_edit
    }

    /**
     * 设置编辑模式
     * @param enable true设置可编辑 false设置不可编辑
     */
    private fun editEnable(enable:Boolean){
        val editable = enable && promoter
        //标题
        scheduleEditBinding.top.title.text = if (editable) "编辑日程" else "日程详情"
        scheduleEditBinding.top.ivEdit.visibility = if (!editable && promoter) View.VISIBLE else View.GONE
        //日程名称
        scheduleEditBinding.etScheduleTitle.isEnabled = editable
        //日程完成状态
        scheduleEditBinding.checkBox.isEnabled = editable
//        scheduleEditBinding.checkBox.visibility = if (editable) View.VISIBLE else View.GONE
        val status = scheduleEditViewModel.scheduleStatusLiveData.value
        if (status != "1") {
//            scheduleEditBinding.checkBox.visibility = if (editable) View.VISIBLE else View.GONE
        } else {
//            scheduleEditBinding.checkBox.visibility = View.VISIBLE
        }
        //日期选择
        if (!editable) {
            scheduleEditBinding.llVLine.visibility = View.GONE
            scheduleEditBinding.dateTimePicker.visibility = View.GONE
        }
        scheduleEditBinding.clStartTime.isEnabled = editable
        scheduleEditBinding.clEndTime.isEnabled = editable
        scheduleEditBinding.checkBoxAllDay.isEnabled = editable
        //重复性选择
        scheduleEditBinding.ivRepetitionArrow.visibility = if (editable) View.VISIBLE else View.GONE
        scheduleEditBinding.clRepetition.isEnabled = editable
        //提醒选择
        scheduleEditBinding.ivRemindArrow.visibility = if (editable) View.VISIBLE else View.GONE
        scheduleEditBinding.clRemind.isEnabled = editable
        //便签及编辑新增按钮
        scheduleEditBinding.btnAddLabel.visibility = if (editable) View.VISIBLE else View.GONE
        adapter.setList(labelList)
        //参与人participant
        scheduleEditBinding.clParticipant.isEnabled = editable
        val participant = scheduleEditViewModel.participantList.value?: mutableListOf()
        if (participant.isEmpty()) {
            scheduleEditBinding.clParticipant.visibility =
                if (editable) View.VISIBLE else View.GONE
        } else {
            scheduleEditBinding.clParticipant.visibility = View.VISIBLE
        }

        //地址
        scheduleEditBinding.clAddress.isEnabled = editable
        val siteName = scheduleEditViewModel.siteLiveData.value
        if (siteName == null && !enable) {
            scheduleEditBinding.clAddress.visibility = View.GONE
        } else {
            if (editable) {
                scheduleEditBinding.clAddress.visibility = View.VISIBLE
            } else if (siteName != null) {
                scheduleEditBinding.clAddress.visibility = View.VISIBLE
            } else {
                scheduleEditBinding.clAddress.visibility = View.GONE
            }
        }

        //备注 remark
        scheduleEditBinding.clRemark.isEnabled = editable
        val remark = scheduleEditViewModel.remarkLiveData.value
        if (TextUtils.isEmpty(remark) && !enable) {
            scheduleEditBinding.clRemark.visibility = View.GONE
        } else {
            if (editable) {
                scheduleEditBinding.clRemark.visibility = View.VISIBLE
            } else if (!TextUtils.isEmpty(remark)) {
                scheduleEditBinding.clRemark.visibility = View.VISIBLE
            } else {
                scheduleEditBinding.clRemark.visibility = View.GONE
            }
        }
        scheduleEditBinding.edit.isEnabled = editable
        if (TextUtils.isEmpty(remark) && siteName == null && !editable) {
            scheduleEditBinding.vLine4.visibility = View.GONE
        } else {
            scheduleEditBinding.vLine4.visibility = View.VISIBLE
        }
        //保存提交
        scheduleEditBinding.clBtnCommit.visibility = if (enable) View.VISIBLE else View.GONE

        //底部文本
        scheduleEditBinding.clScheduleCreateTime.visibility = if (enable) View.GONE else View.VISIBLE
//        val format = getString(R.string.schedule_create_time_and_status)
//        val startTime = scheduleEditViewModel.startTimeLiveData.value
//        val statusText = if (status == "1") "已完成" else "待完成"
//        val bottomText = String.format(format,DateUtils.formatTime(startTime, "", "MM月dd日 HH:mm"),statusText)
//        scheduleEditBinding.tvScheduleCreateTime.text = bottomText
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initView() {
        scheduleEditBinding.clParticipant.visibility = if (SpData.getIdentityInfo().staffIdentity()) View.VISIBLE else View.GONE
        val stringExtra = intent.getStringExtra("data")
        val scheduleData = JSON.parseObject(stringExtra, ScheduleData::class.java)
        val format = getString(R.string.schedule_create_time_and_status)
        scheduleData?.let {
            //显示日程详情
            //判断是否是发起人，只有发起人才能编辑日程
            promoter = it.promoterSelf()
            //日程id
            scheduleEditViewModel.scheduleIdLiveData.value = it.id
            //日程名称name
            scheduleEditBinding.etScheduleTitle.setText(it.name)
            scheduleEditViewModel.scheduleTitleLiveData.value = it.name
            //日程状态status
            if (!promoter && it.status != "1"){
//                scheduleEditBinding.checkBox.visibility = View.GONE
            }
            scheduleEditBinding.checkBox.isChecked = it.status == "1"
            scheduleEditViewModel.scheduleStatusLiveData.value = it.status
            //日程开始时间startTime 日程结束时间endTime
            scheduleEditBinding.tvDateStart.text =
                DateUtils.formatTime(it.startTime, "", "MM月dd日 HH:mm")
            scheduleEditBinding.tvDateEnd.text =
                DateUtils.formatTime(it.endTime, "", "MM月dd日 HH:mm")
            scheduleEditViewModel.startTimeLiveData.value = it.startTime
            scheduleEditViewModel.endTimeLiveData.value = it.endTime
            val bottomStartTime = DateUtils.formatTime(it.startTime,"","MM月dd HH:mm")
            val bottomEndTime = DateUtils.formatTime(it.endTime,"","MM月dd HH:mm")
            //scheduleEditBinding.tvBottomDate.text = String.format(getString(R.string.schedule_edit_bottom_date),bottomStartTime,bottomEndTime)
            //是否全天isAllDay【0：不是，1：是】
            scheduleEditViewModel.allDayLiveData.value = it.isAllDay == "1"
            //日程重复repetition
            Repetition.getList().forEach { repetition ->
                if ("${repetition.code}" == it.isRepeat) {
                    scheduleEditBinding.tvRepetition.text = repetition.title
                    scheduleEditViewModel.repetitionLiveData.value = repetition
                    sourceRepetitionRule = repetition
                }
            }
            if (it.rrule != null && it.isRepeat == "8") {
                //scheduleEditBinding.tvRepetition.text = "自定义重复"
                scheduleEditBinding.tvRepetition.text =
                    ScheduleRepetitionRuleUtil.ruleToString(JSON.toJSONString(it.rrule))
                val repetition = Repetition(8,"", true, it.rrule)
                scheduleEditViewModel.repetitionLiveData.value = repetition
                sourceRepetitionRule = repetition
            }
            //是否是重复日程
            repetition = it.isRepeat != "0"
            //日程提醒remind
            if (it.remindTypeInfo == "10") {
                scheduleEditBinding.tvRemind.text = Remind.getNotRemind().title
                scheduleEditViewModel.remindLiveData.value = Remind.getNotRemind()
            } else {
                if (it.isAllDay == "1") {
                    list2.forEach { remind ->
                        if (remind.id == it.remindTypeInfo) {
                            scheduleEditBinding.tvRemind.text = remind.title
                            scheduleEditViewModel.remindLiveData.value = remind
                            return@forEach
                        }
                    }
                } else {
                    list.forEach { remind ->
                        if (remind.id == it.remindTypeInfo) {
                            scheduleEditBinding.tvRemind.text = remind.title
                            scheduleEditViewModel.remindLiveData.value = remind
                            return@forEach
                        }
                    }
                }
            }

            //日程便签label
            labelList.clear()
            labelList.addAll(it.label)
            scheduleEditViewModel.labelListLiveData.value = labelList
            //参与人participant
            scheduleEditViewModel.participantList.value = it.participant
            if (SpData.getIdentityInfo().staffIdentity()) {
                if (it.participant.isEmpty()) {
                    scheduleEditBinding.clParticipant.visibility =
                        if (promoter) View.VISIBLE else View.GONE
                } else {
                    scheduleEditBinding.clParticipant.visibility = View.VISIBLE
                    showSelectedParticipant(it.participant)
                }
            }

            //地址 siteId
            if (!TextUtils.isEmpty(it.siteId)) {
                val dataBean = SiteNameRsp.DataBean(it.siteId, it.siteName, true)
                scheduleEditViewModel.siteLiveData.value = dataBean
                scheduleEditBinding.tvAddress.text = it.siteName
                scheduleEditBinding.tvAddress.setTextColor(resources.getColor(R.color.black9))
                scheduleEditBinding.clAddress.visibility = View.VISIBLE
            } else {
                //地址为空的时候，如果是本人创建的日程，可以显示添加地址
                scheduleEditBinding.clAddress.visibility = if (promoter) View.VISIBLE else View.GONE
            }
            //备注 remark
            if (TextUtils.isEmpty(it.remark)) {
                scheduleEditBinding.clRemark.visibility = if (promoter) View.VISIBLE else View.GONE
            } else {
                scheduleEditBinding.clRemark.visibility = View.VISIBLE
                scheduleEditBinding.edit.setText(it.remark)
            }
            scheduleEditViewModel.remarkLiveData.value = it.remark

            if (TextUtils.isEmpty(it.remark) && TextUtils.isEmpty(it.siteId) && !promoter){
                scheduleEditBinding.vLine4.visibility = View.GONE
            }

            //底部文本
            val statusText = if (it.status == "1") "已完成" else "待完成"
            val bottomText = String.format(format,DateUtils.formatTime(it.createdDateTime, "", "MM月dd日 HH:mm"),statusText)
            scheduleEditBinding.tvScheduleCreateTime.text = bottomText
        }

        scheduleEditBinding.top.title.text = "日程详情"
        scheduleEditBinding.top.backLayout.setOnClickListener {
            if (enableEditMode) {
                enableEditMode = false
                editEnable(false)
                return@setOnClickListener
            }
            finish()
        }
        scheduleEditBinding.clBtnCommit.visibility = if (promoter) View.VISIBLE else View.GONE
        scheduleEditBinding.btnCommit.setOnClickListener {
            val title = scheduleEditBinding.etScheduleTitle.text.toString()
            if (TextUtils.isEmpty(title)){
                ToastUtils.showShort("需要输入日程名称")
                return@setOnClickListener
            }
            if (DialogUtil.isHasEmoji(title)){
                ToastUtils.showShort("日程名称含有非字符的数据，请重新输入!")
                return@setOnClickListener
            }
//            val rule = sourceRepetitionRule?.rule
//            scheduleEditViewModel.scheduleTitleLiveData.value = scheduleEditBinding.etScheduleTitle.text.toString()
//            val repetition = scheduleEditViewModel.repetitionLiveData.value
            if (!repetition) {
                //原始数据不是重复日程，所以不需要提示重复日程选择范围
                //if ((repetition == null || repetition.rule?.isEmpty() != false) && (sourceRepetitionRule == null || rule == null)) {
                scheduleEditViewModel.saveOrModifySchedule(true)
                return@setOnClickListener
            }
            DialogUtil.showRepetitionScheduleModifyDialog(this) {
                loge("编辑日程重复性类型：$it")
                if (it == -1) {
                    //finish()
                    return@showRepetitionScheduleModifyDialog
                }
                //修改日程
                scheduleEditViewModel.updateTypeLiveData.value = "$it"
                scheduleEditViewModel.saveOrModifySchedule(true)
            }
        }
        //删除
        scheduleEditBinding.top.ivRight.visibility = if (promoter) View.VISIBLE else View.GONE
        scheduleEditBinding.top.ivRight.setOnClickListener {
            DialogUtil.showScheduleDelDialog(
                this,
                scheduleEditBinding.top.ivRight,
                object : DialogUtil.OnClickListener {
                    override fun onCancel(view: View?) {

                    }

                    override fun onEnsure(view: View?) {
                        val scheduleId = scheduleEditViewModel.scheduleIdLiveData.value ?: ""
                        scheduleEditViewModel.deleteScheduleById(scheduleId)
                    }

                })
        }
        //编辑
        scheduleEditBinding.top.ivEdit.visibility = if (promoter) View.VISIBLE else View.GONE
        scheduleEditBinding.top.ivEdit.setOnClickListener {
            enableEditMode = true
            editEnable(enableEditMode)
        }

        scheduleEditBinding.checkBox.isEnabled = promoter
        scheduleEditBinding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            scheduleEditViewModel.scheduleStatusLiveData.value = if (isChecked) "1" else "0"
        }

        scheduleEditBinding.edit.isEnabled = promoter
        scheduleEditBinding.etScheduleTitle.isEnabled = promoter
        scheduleEditBinding.edit.addTextChangedListener {
            scheduleEditViewModel.remarkLiveData.value = it.toString()
        }
        scheduleEditBinding.etScheduleTitle.addTextChangedListener{
            scheduleEditViewModel.scheduleTitleLiveData.value = it.toString()
        }

        //日期操作
        scheduleEditBinding.clStartTime.setOnClickListener {
            if (scheduleEditBinding.llVLine.visibility == View.GONE) {
                scheduleEditBinding.llVLine.visibility = View.VISIBLE
                scheduleEditBinding.dateTimePicker.visibility = View.VISIBLE
            }
            scheduleEditBinding.vDateTopMarkLeft.visibility = View.VISIBLE
            scheduleEditBinding.vDateTopMarkRight.visibility = View.INVISIBLE
            scheduleEditBinding.dateTimePicker.setDefaultMillisecond(
                DateUtils.formatTime(
                    dateStart.get(),
                    ""
                )
            )
        }

        scheduleEditBinding.clEndTime.setOnClickListener {
            if (scheduleEditBinding.llVLine.visibility == View.GONE) {
                scheduleEditBinding.llVLine.visibility = View.VISIBLE
                scheduleEditBinding.dateTimePicker.visibility = View.VISIBLE
            }
            scheduleEditBinding.vDateTopMarkLeft.visibility = View.INVISIBLE
            scheduleEditBinding.vDateTopMarkRight.visibility = View.VISIBLE
            scheduleEditBinding.dateTimePicker.setDefaultMillisecond(
                DateUtils.formatTime(
                    dateEnd.get(),
                    ""
                )
            )
        }

        val allDay = scheduleEditViewModel.allDayLiveData.value
        val startTime = scheduleEditViewModel.startTimeLiveData.value
        val endTime = scheduleEditViewModel.endTimeLiveData.value
        if (allDay != null && allDay) {
            scheduleEditBinding.checkBoxAllDay.isChecked = true
            scheduleEditBinding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation2)
            scheduleEditBinding.tvTimeStart.visibility = View.GONE
            scheduleEditBinding.tvTimeEnd.visibility = View.GONE
        }
        scheduleEditBinding.checkBoxAllDay.setOnCheckedChangeListener { _, isChecked ->
            //设置是否全天
            scheduleEditViewModel.allDayLiveData.value = isChecked
            if (isChecked) {
                scheduleEditBinding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation2)
                scheduleEditBinding.tvTimeStart.visibility = View.GONE
                scheduleEditBinding.tvTimeEnd.visibility = View.GONE
                //修改提醒类型 全天
                val optionalRemind =
                    list2.stream().filter(Remind::checked).findFirst()
                if (optionalRemind.isPresent) {
                    val remind = optionalRemind.get()
                    scheduleEditViewModel.remindLiveData.value = remind
                    scheduleEditBinding.tvRemind.text = remind.title
                } else {
                    scheduleEditViewModel.remindLiveData.value = Remind.getNotRemind()
                    scheduleEditBinding.tvRemind.text = "不提醒"
                }
            } else {
                scheduleEditBinding.dateTimePicker.setLayout(R.layout.layout_date_picker_segmentation)
                scheduleEditBinding.tvTimeStart.visibility = View.VISIBLE
                scheduleEditBinding.tvTimeEnd.visibility = View.VISIBLE
                //修改提醒类型 非全天
                val optionalRemind =
                    list.stream().filter(Remind::checked).findFirst()
                if (optionalRemind.isPresent) {
                    val remind = optionalRemind.get()
                    scheduleEditViewModel.remindLiveData.value = remind
                    scheduleEditBinding.tvRemind.text = remind.title
                } else {
                    scheduleEditViewModel.remindLiveData.value = Remind.getNotRemind()
                    scheduleEditBinding.tvRemind.text = "不重复"
                }
            }
        }

        scheduleEditBinding.dateTimePicker.setOnDateTimeChangedListener { aLong ->
            val date = DateUtils.getDate(aLong)
            val time = DateUtils.formatTime(date, "", "", true)
            loge("showEditScheduleDialog: $aLong,date=$date, time=$time")
            if (scheduleEditBinding.vDateTopMarkLeft.visibility == View.VISIBLE) {
                //左边选中设置左边的时间数据
                scheduleEditBinding.tvDateStart.text = time
                if (!scheduleEditBinding.checkBox.isChecked) {
                    scheduleEditBinding.tvTimeStart.text =
                        DateUtils.formatTime(date, "", "HH:mm")
                }
                dateStart.set(date)
            } else if (scheduleEditBinding.vDateTopMarkRight.visibility == View.VISIBLE) {
                //右边被选中设置右边的时间数据
                scheduleEditBinding.tvDateEnd.text = time
                if (!scheduleEditBinding.checkBox.isChecked) {
                    scheduleEditBinding.tvTimeEnd.text =
                        DateUtils.formatTime(date, "", "HH:mm")
                }
                dateEnd.set(date)
            } else {
                //第一次设置两边的数据
                if (TextUtils.isEmpty(startTime)) {
                    scheduleEditBinding.tvDateStart.text = time
                    if (!scheduleEditBinding.checkBox.isChecked) {
                        scheduleEditBinding.tvTimeStart.text =
                            DateUtils.formatTime(date, "", "HH:mm")
                    }
                    dateStart.set(date)
                } else {
                    scheduleEditBinding.tvDateStart.text =
                        DateUtils.formatTime(startTime, "", "", true)
                    if (!scheduleEditBinding.checkBox.isChecked) {
                        scheduleEditBinding.tvTimeStart.text =
                            DateUtils.formatTime(startTime, "", "HH:mm")
                    }
                    dateStart.set(startTime)
                }
                if (TextUtils.isEmpty(endTime)) {
                    scheduleEditBinding.tvDateEnd.text = time
                    if (!scheduleEditBinding.checkBox.isChecked) {
                        scheduleEditBinding.tvTimeEnd.text =
                            DateUtils.formatTime(date, "", "HH:mm")
                    }
                    dateEnd.set(date)
                } else {
                    scheduleEditBinding.tvDateEnd.text =
                        DateUtils.formatTime(endTime, "", "", true)
                    if (!scheduleEditBinding.checkBox.isChecked) {
                        scheduleEditBinding.tvTimeEnd.text =
                            DateUtils.formatTime(endTime, "", "HH:mm")
                    }
                    dateEnd.set(endTime)
                }
            }
            null
        }

        scheduleEditBinding.btnAddLabel.visibility = if (promoter) View.VISIBLE else View.GONE
        scheduleEditBinding.btnAddLabel.setOnClickListener {
            val intent = Intent(this, ScheduleAddLabelActivity::class.java)
            intent.putExtra(
                "labelList",
                JSON.toJSONString(scheduleEditViewModel.labelListLiveData.value)
            )
            startActivityForResult(intent, REQUEST_CODE_LABEL_SELECT)
        }

        scheduleEditBinding.ivRemindArrow.visibility = if (promoter) View.VISIBLE else View.GONE
        scheduleEditBinding.clRemind.isEnabled = promoter
        scheduleEditBinding.clRemind.setOnClickListener {
            val intent = Intent(this, ScheduleRemindActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(scheduleEditViewModel.remindLiveData.value))
            intent.putExtra("allDay", scheduleEditViewModel.allDayLiveData.value)
            startActivityForResult(intent, REQUEST_CODE_REMIND_SELECT)
        }

        scheduleEditBinding.ivRepetitionArrow.visibility = if (promoter) View.VISIBLE else View.GONE
        scheduleEditBinding.clRepetition.isEnabled = promoter
        scheduleEditBinding.clRepetition.setOnClickListener {
            val intent = Intent(this, ScheduleRepetitionActivity::class.java)
            intent.putExtra("startTime",scheduleEditViewModel.startTimeLiveData.value.toString())
            intent.putExtra(
                "data",
                JSON.toJSONString(scheduleEditViewModel.repetitionLiveData.value)
            )
            startActivityForResult(intent, REQUEST_CODE_REPETITION_SELECT)
        }

        scheduleEditBinding.clAddress.isEnabled = promoter
        scheduleEditBinding.clAddress.setOnClickListener {
            val intent = Intent(this, ScheduleAddressActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(scheduleEditViewModel.siteLiveData.value))
            startActivityForResult(intent, REQUEST_CODE_SITE_SELECT)
        }


//        scheduleEditBinding.clDate.isEnabled = promoter
//        scheduleEditBinding.clDate.setOnClickListener {
//            val intent = Intent(this, ScheduleDateIntervalActivity::class.java)
//            val allDay = scheduleEditViewModel.allDayLiveData.value
//            val startTime = scheduleEditViewModel.startTimeLiveData.value
//            val endTime = scheduleEditViewModel.endTimeLiveData.value
//            intent.putExtra("allDay", allDay)
//            intent.putExtra("startTime", startTime)
//            intent.putExtra("endTime", endTime)
//            startActivityForResult(intent, REQUEST_CODE_DATE_SELECT)
//        }

        scheduleEditBinding.clParticipant.isEnabled = promoter
        scheduleEditBinding.clParticipant.setOnClickListener {
            val intent = Intent(this, ScheduleParticipantActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(scheduleEditViewModel.participantList.value))
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
        //日程删除监听
        scheduleEditViewModel.deleteResult.observe(this, {
            if (it) {
                //日程删除成功 并删除本地数据
                val scheduleId = scheduleEditViewModel.scheduleIdLiveData.value ?: ""
                ScheduleDaoUtil.deleteScheduleData(scheduleId)
                finish()
            }
        })
        //日程修改监听
        scheduleEditViewModel.saveOrModifyResult.observe(this, {
            if (it) {
                scheduleMangeViewModel.getAllScheduleList()
                return@observe
            }
            ToastUtils.showShort("修改日程失败")
            finish()
        })
        //获取日程监听
        scheduleMangeViewModel.requestAllScheduleResult.observe(this, {
            if (it) {
                ToastUtils.showShort("修改日程成功")
                enableEditMode = false
                editEnable(enableEditMode)
                //finish()
                return@observe
            }
            ToastUtils.showShort("修改日程失败")
            enableEditMode = false
            editEnable(enableEditMode)
            //finish()
        })
        editEnable(enableEditMode)
        //限制输入框字数
        scheduleEditBinding.edit.filters = arrayOf<InputFilter>(MaxTextLengthFilter(200))
        scheduleEditBinding.etScheduleTitle.filters = arrayOf<InputFilter>(MaxTextLengthFilter(20))
    }

    val adapter = object :
        BaseQuickAdapter<LabelListRsp.DataBean, BaseViewHolder>(R.layout.item_schedule_label_flow_list) {
        override fun convert(holder: BaseViewHolder, item: LabelListRsp.DataBean) {
            holder.getView<ImageView>(R.id.iv_del).visibility =
                if (enableEditMode) View.VISIBLE else View.GONE
            val drawable = GradientDrawable()
            drawable.cornerRadius = DisplayUtils.dip2px(this@ScheduleEditActivity, 2f).toFloat()
            drawable.setColor(ColorUtil.parseColor(item.colorValue))
            holder.getView<TextView>(R.id.tv_label).background = drawable
            holder.setText(R.id.tv_label, item.labelName)
            holder.itemView.setOnClickListener {
                loge("item=$item")
                if (!enableEditMode){
                    loge("日程便签只有发起人才能删除！")
                    return@setOnClickListener
                }
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
            if (allDay) {
                val selectedRemind = list2.filter { it.checked }
                if (selectedRemind.isNotEmpty()) {
                    scheduleEditViewModel.remindLiveData.value = selectedRemind[0]
                    scheduleEditBinding.tvRemind.text = selectedRemind[0].title
                } else {
                    scheduleEditViewModel.remindLiveData.value = Remind.getNotRemind()
                    scheduleEditBinding.tvRemind.text = Remind.getNotRemind().title
                }

            } else {
                val selectedRemind = list.filter { it.checked }
                if (selectedRemind.isNotEmpty()) {
                    scheduleEditViewModel.remindLiveData.value = selectedRemind[0]
                    scheduleEditBinding.tvRemind.text = selectedRemind[0].title
                } else {
                    scheduleEditViewModel.remindLiveData.value = Remind.getNotRemind()
                    scheduleEditBinding.tvRemind.text = Remind.getNotRemind().title
                }
            }
            return
        }
        //选择提醒
        if (requestCode == REQUEST_CODE_REMIND_SELECT && resultCode == RESULT_OK && data != null) {
            val stringExtra = data.getStringExtra("data")
            val remind = JSON.parseObject(stringExtra, Remind::class.java)
            loge("id=${remind.id},name=${remind.title}")
            scheduleEditBinding.tvRemind.text = remind.title
            scheduleEditViewModel.remindLiveData.value = remind
            if (scheduleEditViewModel.allDayLiveData.value == true) {
                list2.forEach {
                    it.checked = it.id == remind.id
                }
            } else {
                list.forEach {
                    it.checked = it.id == remind.id
                }
            }
            return
        }
        //选择重复
        if (requestCode == REQUEST_CODE_REPETITION_SELECT && resultCode == RESULT_OK && data != null) {
            val stringExtra = data.getStringExtra("data")
            val repetition = JSON.parseObject(stringExtra, Repetition::class.java)
            loge("title=${repetition.title},rule=${repetition.rule}")
            if (repetition.code == 8) {
                scheduleEditBinding.tvRepetition.text =
                    ScheduleRepetitionRuleUtil.ruleToString(JSON.toJSONString(repetition.rule))
            } else {
                scheduleEditBinding.tvRepetition.text = repetition.title
            }
            scheduleEditViewModel.repetitionLiveData.value = repetition
            return
        }
        //选择参与人
        if (requestCode == REQUEST_CODE_PARTICIPANT_SELECT && resultCode == RESULT_OK && data != null) {
            val stringExtra = data.getStringExtra("data")
            loge("onActivityResult:$stringExtra")
            val list = JSONArray.parseArray(
                stringExtra,
                ParticipantRsp.DataBean.ParticipantListBean::class.java
            )
            if (list != null) {
                scheduleEditViewModel.participantList.value = list
                //list.map { ScheduleData.ParticipantBean(it.id) }
                //显示参与人
                showSelectedParticipant(list)

            }
            return
        }
    }

    private fun showSelectedParticipant(list: List<ParticipantRsp.DataBean.ParticipantListBean>) {
        val stringBuilder = StringBuilder()
        list.map { it.realname }.forEach {
            stringBuilder.append(it).append("  ")
        }
        if (stringBuilder.isEmpty() || stringBuilder.isBlank()) {
            scheduleEditBinding.tvParticipant.text = "添加参与人"
            scheduleEditBinding.tvParticipant.setTextColor(resources.getColor(R.color.black11))
        } else {
            scheduleEditBinding.tvParticipant.text = stringBuilder
            scheduleEditBinding.tvParticipant.setTextColor(resources.getColor(R.color.black9))
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