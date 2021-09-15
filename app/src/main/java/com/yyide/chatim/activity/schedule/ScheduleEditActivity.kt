package com.yyide.chatim.activity.schedule

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleEditBinding
import com.yyide.chatim.model.schedule.Label
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.SpacesItemDecoration

class ScheduleEditActivity : BaseActivity() {
    lateinit var scheduleEditBinding: ActivityScheduleEditBinding
    private var labelList = mutableListOf<Label>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleEditBinding = ActivityScheduleEditBinding.inflate(layoutInflater)
        setContentView(scheduleEditBinding.root)
        initData()
        initView()
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_schedule_edit
    }

    private fun initView() {
        scheduleEditBinding.top.title.text = "日程编辑"
        scheduleEditBinding.top.backLayout.setOnClickListener {
            finish()
        }

        scheduleEditBinding.btnAddLabel.setOnClickListener {
            startActivity(Intent(this, ScheduleLabelManageActivity::class.java))
        }

        scheduleEditBinding.clRemind.setOnClickListener {
            startActivity(Intent(this, ScheduleRemindActivity::class.java))
        }
        scheduleEditBinding.clRepetition.setOnClickListener {
            startActivity(Intent(this, ScheduleRepetitionActivity::class.java))
        }
        scheduleEditBinding.clAddress.setOnClickListener {
            startActivity(Intent(this, ScheduleAddressActivity::class.java))
        }
        scheduleEditBinding.clDate.setOnClickListener {
            startActivity(Intent(this, ScheduleDateIntervalActivity::class.java))
        }

        val flexboxLayoutManager = FlexboxLayoutManager(this)
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        scheduleEditBinding.rvLabelList.layoutManager = flexboxLayoutManager
        scheduleEditBinding.rvLabelList.addItemDecoration(SpacesItemDecoration(SpacesItemDecoration.dip2px(5f)))
        adapter.setList(labelList)
        scheduleEditBinding.rvLabelList.adapter = adapter
    }

    val adapter = object :BaseQuickAdapter<Label,BaseViewHolder>(R.layout.item_schedule_label_flow_list){
        override fun convert(holder: BaseViewHolder, item: Label) {
            val drawable = GradientDrawable()
            drawable.cornerRadius = DisplayUtils.dip2px(this@ScheduleEditActivity,2f).toFloat()
            drawable.setColor(Color.parseColor(item.color))
            holder.getView<TextView>(R.id.tv_label).background = drawable
            holder.setText(R.id.tv_label,item.title)
            holder.itemView.setOnClickListener {
                loge("item=$item")
                labelList.remove(item)
                notifyDataSetChanged()
            }
        }
    }

    private fun initData() {
        labelList.add(Label("工作阅读", "#19ADF8", false))
        labelList.add(Label("阅读阅读阅读", "#56D72C", false))
        labelList.add(Label("睡觉阅读", "#FD8208", false))
        labelList.add(Label("吃饭", "#56D72C", false))
        labelList.add(Label("嗨皮阅读阅读阅读阅读阅读阅读阅读阅读", "#FD8208", false))
    }
}