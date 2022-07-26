package com.yyide.chatim_pro.activity.schedule

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.base.BaseActivity
import com.yyide.chatim_pro.database.ScheduleDaoUtil
import com.yyide.chatim_pro.database.ScheduleDaoUtil.promoterSelf
import com.yyide.chatim_pro.databinding.ActivityScheduleEdit2Binding
import com.yyide.chatim_pro.model.schedule.*
import com.yyide.chatim_pro.model.schedule.Remind.Companion.getList
import com.yyide.chatim_pro.model.schedule.Remind.Companion.getList2
import com.yyide.chatim_pro.utils.*
import com.yyide.chatim_pro.view.DialogUtil
import com.yyide.chatim_pro.view.SpacesItemDecoration
import com.yyide.chatim_pro.viewmodel.ScheduleEditViewModel
import com.yyide.chatim_pro.viewmodel.ScheduleMangeViewModel
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
    var repetition: Boolean = false
    var from: String? = null


    //否是是发起人
    private var promoter = true

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleEditBinding = ActivityScheduleEdit2Binding.inflate(layoutInflater)
        setContentView(scheduleEditBinding.root)
        initView()
        //日程删除监听
        scheduleEditViewModel.deleteResult.observe(this) {
            if (it) {
                //日程删除成功 并删除本地数据
                val scheduleId = scheduleEditViewModel.scheduleIdLiveData.value ?: ""
                ScheduleDaoUtil.deleteScheduleData(scheduleId)
                finish()
            }
        }
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_schedule_edit2
    }

    /**
     * 设置编辑模式
     * @param enable true设置可编辑 false设置不可编辑
     */
    private fun initView() {
        val flexboxLayoutManager = FlexboxLayoutManager(this)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        scheduleEditBinding.rvLabelList.layoutManager = flexboxLayoutManager
        scheduleEditBinding.rvLabelList.layoutManager = flexboxLayoutManager
        scheduleEditBinding.rvLabelList.addItemDecoration(
            SpacesItemDecoration(
                SpacesItemDecoration.dip2px(
                    5f
                )
            )
        )
        scheduleEditBinding.rvLabelList.adapter = adapter

        scheduleEditBinding.top.title.text = "日程详情"
        scheduleEditBinding.top.backLayout.setOnClickListener {
            finish()
        }


        //删除
//        scheduleEditBinding.top.ivRight.visibility = View.VISIBLE
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


        val stringExtra = intent.getStringExtra("data")
        val from = intent.getStringExtra("from")

        val scheduleData = JSON.parseObject(stringExtra, ScheduleData::class.java)
        Log.e("TAG", "scheduleData: " + JSON.toJSONString(scheduleData))
        scheduleEditBinding.top.ivEdit.setOnClickListener {
            val intent = Intent(this, ScheduleEditActivityMain::class.java)
            intent.putExtra("data", JSON.toJSONString(scheduleData))
            intent.putExtra("from", JSON.toJSONString(from))
            startActivity(intent)
            finish()
        }

        scheduleData?.let {
            promoter = it.promoterSelf()
            scheduleEditBinding.top.ivEdit.isEnabled = promoter;
            scheduleEditBinding.top.ivRight.isEnabled = promoter;
            if (promoter) {
                scheduleEditBinding.top.ivEdit.visibility = View.VISIBLE
                scheduleEditBinding.top.ivRight.visibility = View.VISIBLE
            } else {
                scheduleEditBinding.top.ivEdit.visibility = View.GONE
                scheduleEditBinding.top.ivRight.visibility = View.GONE
            }
            scheduleEditBinding.edit.visibility =
                if (it.remark.isNullOrEmpty()) View.GONE else View.VISIBLE
            scheduleEditBinding.tvAddress.visibility =
                if (it.siteName.isNullOrEmpty()) View.GONE else View.VISIBLE
            scheduleEditBinding.edit.text = it.remark
            scheduleEditBinding.tvAddress.text = it.siteName
            //日程名称name
            scheduleEditBinding.etScheduleTitle.text = it.name
            scheduleEditViewModel.participantList.value = it.participantList
            scheduleEditViewModel.scheduleIdLiveData.value = it.id
            val stringBuilder = StringBuilder()
//            for (participantListBean in it.participantList) {
//                if (!TextUtils.isEmpty(it.name)){
//                    stringBuilder.append(it.name).append("  ")
//
//                }
//            }
            // loge(scheduleEditViewModel.participantList.value)
            it.participantList.map {
                it.realname
            }.forEach {
                stringBuilder.append(it).append("  ")
            }
            scheduleEditBinding.tvPerson.setText(stringBuilder.toString())
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
            adapter.setList(it.labelList)


        }


    }

    val adapter = object :
        BaseQuickAdapter<LabelListRsp.DataBean, BaseViewHolder>(R.layout.item_schedule_label_flow_list) {
        @SuppressLint("WrongConstant")
        override fun convert(holder: BaseViewHolder, item: LabelListRsp.DataBean) {
            holder.getView<ImageView>(R.id.iv_del).visibility = View.GONE
            val drawable = GradientDrawable()
//            drawable.cornerRadius = DisplayUtils.dip2px(this@ScheduleEditActivityMain, 2f).toFloat()
//            drawable.setColor(ColorUtil.parseColor(item.colorValue))
            drawable.setStroke(1, ColorUtil.parseColor(item.colorValue))

            drawable.setShape(GradientDrawable.LINEAR_GRADIENT);

            holder.setTextColor(R.id.tv_label, ColorUtil.parseColor(item.colorValue))

            holder.getView<TextView>(R.id.tv_label).background = drawable
            holder.setText(R.id.tv_label, item.labelName)
//            holder.itemView.setOnClickListener {
//                loge("item=$item")
//                if (!enableEditMode){
//                    loge("日程便签只有发起人才能删除！")
//                    return@setOnClickListener
//                }
//                remove(item)
//                labelList.remove(item)
//                notifyDataSetChanged()
//            }
        }
    }
}