package com.yyide.chatim.activity.schedule

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
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
import com.yyide.chatim.databinding.ActivityScheduleEdit2Binding
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

class ScheduleEditActivitySimple : BaseActivity() {
    lateinit var scheduleEditBinding: ActivityScheduleEdit2Binding
    private var labelList = mutableListOf<LabelListRsp.DataBean>()
    private val scheduleEditViewModel: ScheduleEditViewModel by viewModels()
    private val scheduleMangeViewModel: ScheduleMangeViewModel by viewModels()
    var dateStart = AtomicReference("")
    var dateEnd = AtomicReference("")
    val list = getList()
    val list2 = getList2()
    var repetition:Boolean = false

    //否是是发起人
    private var promoter = true
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleEditBinding = ActivityScheduleEdit2Binding.inflate(layoutInflater)
        setContentView(scheduleEditBinding.root)
        initView()

    }

    override fun getContentViewID(): Int {
        return R.layout.activity_schedule_edit2
    }

    /**
     * 设置编辑模式
     * @param enable true设置可编辑 false设置不可编辑
     */
    private fun initView(){


        scheduleEditBinding.top.title.text =  "日程详情"
        scheduleEditBinding.top.backLayout.setOnClickListener {
            finish()
        }
        //删除
        scheduleEditBinding.top.ivRight.visibility = View.VISIBLE
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
        scheduleEditBinding.top.ivEdit.visibility=View.VISIBLE;

        val stringExtra = intent.getStringExtra("data")

        val scheduleData = JSON.parseObject(stringExtra, ScheduleData::class.java)
        Log.e("TAG", "scheduleData: "+JSON.toJSONString(scheduleData) )
        scheduleEditBinding.top.ivEdit.setOnClickListener {
            val intent = Intent(this, ScheduleEditActivityMain::class.java)
            intent.putExtra("data", JSON.toJSONString(scheduleData))
            startActivity(intent)
            finish()
        }

        scheduleData?.let {
            promoter = it.promoterSelf()
            scheduleEditBinding.top.ivEdit.isEnabled=promoter;
            scheduleEditBinding.top.ivRight.isEnabled=promoter;
            scheduleEditBinding.edit.setText(it.remark)
            scheduleEditBinding.tvAddress.text = it.siteName
            //日程名称name
            scheduleEditBinding.etScheduleTitle.setText(it.name)
            scheduleEditViewModel.participantList.value = it.participantList
            scheduleEditViewModel.scheduleIdLiveData.value = it.id
            val stringBuilder = StringBuilder()
            it.participantList.map {
                if (it!=null)
                it.name
            }.forEach {
                stringBuilder.append(it).append("  ")
            }
            scheduleEditBinding.tvPerson .setText(stringBuilder)
            scheduleEditBinding.tvStartTime.text =
                    DateUtils.formatTime(it.startTime, "", "MM月dd日 HH:mm")
            scheduleEditBinding.tvEndTime.text =
                    DateUtils.formatTime(it.endTime, "", "MM月dd日 HH:mm")
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
        }

    }


}