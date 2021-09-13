package com.yyide.chatim.activity.schedule

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleLabelManageBinding
import com.yyide.chatim.model.schedule.Label
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil

class ScheduleLabelManageActivity : BaseActivity() {
    lateinit var labelManageBinding: ActivityScheduleLabelManageBinding
    private var labelList = mutableListOf<Label>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        labelManageBinding = ActivityScheduleLabelManageBinding.inflate(layoutInflater)
        setContentView(labelManageBinding.root)
        initData()
        initView()
    }

    private fun initData() {
        labelList.add(Label("工作", "#19ADF8", false))
        labelList.add(Label("阅读", "#56D72C", false))
        labelList.add(Label("睡觉", "#FD8208", false))
        labelList.add(Label("吃饭", "#56D72C", false))
        labelList.add(Label("嗨皮", "#FD8208", false))
    }

    private fun initView() {
        labelManageBinding.top.title.text = "标签管理"
        labelManageBinding.top.backLayout.setOnClickListener {
            finish()
        }
        val adapter: BaseQuickAdapter<Label, BaseViewHolder> =
            object : BaseQuickAdapter<Label, BaseViewHolder>(R.layout.item_label_manage_list) {
                override fun convert(baseViewHolder: BaseViewHolder, label: Label) {
                    baseViewHolder.setText(R.id.tv_label, label.title)
                    baseViewHolder.setBackgroundColor(R.id.tv_label, Color.parseColor(label.color))
                    baseViewHolder.itemView.setOnClickListener { v: View? ->

                    }
                    baseViewHolder.getView<TextView>(R.id.tv_modify).setOnClickListener {
                        loge("修改便签")
                    }

                    baseViewHolder.getView<TextView>(R.id.tv_delete).setOnClickListener {
                        loge("删除标签")
                    }
                }
            }
        adapter.setList(labelList)
        labelManageBinding.cvLabelList.layoutManager = LinearLayoutManager(this)
        labelManageBinding.cvLabelList.adapter = adapter

        labelManageBinding.clAddLabel.setOnClickListener {
            startActivity(Intent(this,ScheduleLabelCreateActivity::class.java))
        }
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_schedule_label_manage
    }
}