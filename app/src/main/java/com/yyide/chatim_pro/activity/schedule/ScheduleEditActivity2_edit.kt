package com.yyide.chatim_pro.activity.schedule

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.base.BaseActivity
import com.yyide.chatim_pro.databinding.ActivityScheduleEdit3Binding
import com.yyide.chatim_pro.model.schedule.*
import com.yyide.chatim_pro.model.schedule.Remind.Companion.getList
import com.yyide.chatim_pro.model.schedule.Remind.Companion.getList2
import com.yyide.chatim_pro.utils.*
import com.yyide.chatim_pro.viewmodel.ScheduleEditViewModel
import com.yyide.chatim_pro.viewmodel.ScheduleMangeViewModel
import java.util.concurrent.atomic.AtomicReference

class ScheduleEditActivity2_edit : BaseActivity() {
    lateinit var scheduleEditBinding: ActivityScheduleEdit3Binding
    private var labelList = mutableListOf<LabelListRsp.DataBean>()
    private val scheduleEditViewModel: ScheduleEditViewModel by viewModels()
    private val scheduleMangeViewModel: ScheduleMangeViewModel by viewModels()
    var dateStart = AtomicReference("")
    var dateEnd = AtomicReference("")
    val list = getList()
    val list2 = getList2()
    var repetition:Boolean = false


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleEditBinding = ActivityScheduleEdit3Binding.inflate(layoutInflater)
        setContentView(scheduleEditBinding.root)
//        initView()

    }

    override fun getContentViewID(): Int {
        return R.layout.activity_schedule_edit2
    }

    /**
     * 设置编辑模式
     * @param enable true设置可编辑 false设置不可编辑
     */
//    private fun initView(){
//        scheduleEditBinding.etScheduleTitle.isEnabled = false;
//        scheduleEditBinding.top.ivEdit.visibility= View.GONE
//        val stringExtra = intent.getStringExtra("data")
//        val scheduleData = JSON.parseObject(stringExtra, ScheduleData::class.java)
//        val format = getString(R.string.schedule_create_time_and_status)
//        scheduleData?.let {
//            scheduleEditBinding.edit.setText(it.remark)
//            scheduleEditBinding.tvAdress.setText(it.siteName)
//            //日程名称name
//            scheduleEditBinding.etScheduleTitle.setText(it.name)
//            scheduleEditViewModel.participantList.value = it.participant
//            val stringBuilder = StringBuilder()
//            it.participant.map { it.realname }.forEach {
//                stringBuilder.append(it).append("  ")
//            }
//            scheduleEditBinding.tvPerson .setText(stringBuilder)
//            scheduleEditBinding.tvStartTime.text =
//                    DateUtils.formatTime(it.startTime, "", "MM月dd日 HH:mm")
//            scheduleEditBinding.tvEndTime.text =
//                    DateUtils.formatTime(it.endTime, "", "MM月dd日 HH:mm")
//            //日程提醒remind
////            if (it.remindTypeInfo == "10") {
////                scheduleEditBinding.tvRemind.text = Remind.getNotRemind().title
////                scheduleEditViewModel.remindLiveData.value = Remind.getNotRemind()
////            } else {
////                if (it.isAllDay == "1") {
////                    list2.forEach { remind ->
////                        if (remind.id == it.remindTypeInfo) {
////                            scheduleEditBinding.tvRemind.text = remind.title
////                            scheduleEditViewModel.remindLiveData.value = remind
////                            return@forEach
////                        }
////                    }
////                } else {
////                    list.forEach { remind ->
////                        if (remind.id == it.remindTypeInfo) {
////                            scheduleEditBinding.tvRemind.text = remind.title
////                            scheduleEditViewModel.remindLiveData.value = remind
////                            return@forEach
////                        }
////                    }
////                }
////            }
//
//
//            scheduleEditBinding.btnCommit.setOnClickListener {
//                val title = scheduleEditBinding.etScheduleTitle.text.toString()
//                if (TextUtils.isEmpty(title)){
//                    ToastUtils.showShort("需要输入日程名称")
//                    return@setOnClickListener
//                }
//                if (DialogUtil.isHasEmoji(title)){
//                    ToastUtils.showShort("日程名称含有非字符的数据，请重新输入!")
//                    return@setOnClickListener
//                }
//                scheduleEditViewModel.startTimeLiveData.value = dateStart.get()
//                scheduleEditViewModel.endTimeLiveData.value = dateEnd.get()
//                //不支持跨天且重复日程
//                val isRepeat = (scheduleEditViewModel.repetitionLiveData.value?.code?:"").toString()
//                val startTime = scheduleEditViewModel.startTimeLiveData.value?.toString()?:""
//                val endTime = scheduleEditViewModel.endTimeLiveData.value?.toString()?:""
//                if (ScheduleRepetitionRuleUtil.moreDayAndRepetitionData(isRepeat,startTime,endTime)){
//                    ToastUtils.showShort("暂时不支持跨天重复日程！")
//                    return@setOnClickListener
//                }
////            val rule = sourceRepetitionRule?.rule
////            scheduleEditViewModel.scheduleTitleLiveData.value = scheduleEditBinding.etScheduleTitle.text.toString()
////            val repetition = scheduleEditViewModel.repetitionLiveData.value
//                if (!repetition) {
//                    //原始数据不是重复日程，所以不需要提示重复日程选择范围
//                    //if ((repetition == null || repetition.rule?.isEmpty() != false) && (sourceRepetitionRule == null || rule == null)) {
//                    scheduleEditViewModel.saveOrModifySchedule(true)
//                    return@setOnClickListener
//                }
//                DialogUtil.showRepetitionScheduleModifyDialog(this) {
//                    loge("编辑日程重复性类型：$it")
//                    if (it == -1) {
//                        //finish()
//                        return@showRepetitionScheduleModifyDialog
//                    }
//                    //修改日程
//                    scheduleEditViewModel.updateTypeLiveData.value = "$it"
//                    scheduleEditViewModel.saveOrModifySchedule(true)
//                }
//            }
//        }
//        scheduleEditBinding.btnAddLabel.setOnClickListener {
//            val intent = Intent(this, ScheduleAddLabelActivity::class.java)
//            intent.putExtra(
//                "labelList",
//                JSON.toJSONString(scheduleEditViewModel.labelListLiveData.value)
//            )
//            startActivityForResult(intent, ScheduleEditActivity.REQUEST_CODE_LABEL_SELECT)
//        }
//    }


}