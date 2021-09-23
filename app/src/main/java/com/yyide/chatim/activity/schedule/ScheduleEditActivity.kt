package com.yyide.chatim.activity.schedule

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.TextView
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
import com.yyide.chatim.model.schedule.Label
import com.yyide.chatim.model.schedule.LabelListRsp
import com.yyide.chatim.model.schedule.SiteNameRsp
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.SpacesItemDecoration

class ScheduleEditActivity : BaseActivity() {
    lateinit var scheduleEditBinding: ActivityScheduleEditBinding
    private var labelList = mutableListOf<LabelListRsp.DataBean>()
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
            finish()
        }

        scheduleEditBinding.btnAddLabel.setOnClickListener {
            val intent = Intent(this, ScheduleAddLabelActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_LABEL_SELECT)
        }

        scheduleEditBinding.clRemind.setOnClickListener {
            startActivity(Intent(this, ScheduleRemindActivity::class.java))
        }
        scheduleEditBinding.clRepetition.setOnClickListener {
            startActivity(Intent(this, ScheduleRepetitionActivity::class.java))
        }
        scheduleEditBinding.clAddress.setOnClickListener {
            val intent = Intent(this, ScheduleAddressActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_SITE_SELECT)
        }
        scheduleEditBinding.clDate.setOnClickListener {
            startActivity(Intent(this, ScheduleDateIntervalActivity::class.java))
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
            drawable.setColor(Color.parseColor(item.colorValue))
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
        if (requestCode == REQUEST_CODE_LABEL_SELECT && resultCode == RESULT_OK && data != null) {
            val stringExtra = data.getStringExtra("labelList")
            loge("onActivityResult:$stringExtra")
            val parseArray:List<LabelListRsp.DataBean> = JSONArray.parseArray(stringExtra, LabelListRsp.DataBean::class.java)
            if (parseArray.isNotEmpty()) {
                labelList.addAll(parseArray)
                adapter.setList(labelList)
            }
            return
        }

        if (requestCode == REQUEST_CODE_SITE_SELECT && resultCode == RESULT_OK && data != null ){
            val stringExtra = data.getStringExtra("address")
            loge("onActivityResult:$stringExtra")
            val siteNameBean:SiteNameRsp.DataBean = JSON.parseObject(stringExtra, SiteNameRsp.DataBean::class.java)
            val name = siteNameBean.name
            scheduleEditBinding.tvAddress.setTextColor(resources.getColor(R.color.black9))
            scheduleEditBinding.tvAddress.text = name
            return
        }
    }

    companion object{
        const val REQUEST_CODE_LABEL_SELECT = 100
        const val REQUEST_CODE_SITE_SELECT = 101
    }
}