package com.yyide.chatim.activity.schedule

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleLabelManageBinding
import com.yyide.chatim.model.schedule.LabelListRsp
import com.yyide.chatim.utils.ColorUtil
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.viewmodel.LabelManageViewModel

class ScheduleLabelManageActivity : BaseActivity() {
    lateinit var labelManageBinding: ActivityScheduleLabelManageBinding
    private var labelList = mutableListOf<LabelListRsp.DataBean>()
    private val labelManageViewModel: LabelManageViewModel by viewModels()
    private lateinit var adapter: BaseQuickAdapter<LabelListRsp.DataBean, BaseViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        labelManageBinding = ActivityScheduleLabelManageBinding.inflate(layoutInflater)
        setContentView(labelManageBinding.root)
        initView()
        labelManageViewModel.getLabelList().observe(this, Observer {
            if (it.isEmpty()) {
                loge("没有数据")
                return@Observer
            }
            labelList.clear()
            labelList.addAll(it)
            adapter.setList(labelList)
        })
    }

    override fun onRestart() {
        super.onRestart()
        labelManageViewModel.selectLabelList()
    }

    private fun initView() {
        labelManageBinding.top.title.text = "标签管理"
        labelManageBinding.top.backLayout.setOnClickListener {
            finish()
        }
        adapter =
            object :
                BaseQuickAdapter<LabelListRsp.DataBean, BaseViewHolder>(R.layout.item_label_manage_list) {
                override fun convert(holder: BaseViewHolder, item: LabelListRsp.DataBean) {
                    if (labelList[labelList.lastIndex] === item) {
                        holder.getView<View>(R.id.v_line).visibility = View.GONE
                    }
                    holder.setText(R.id.tv_label, item.labelName)
                    val drawable = GradientDrawable()
                    drawable.cornerRadius =
                        DisplayUtils.dip2px(this@ScheduleLabelManageActivity, 2f).toFloat()
                    drawable.setColor(ColorUtil.parseColor(item.colorValue))
                    holder.getView<TextView>(R.id.tv_label).background = drawable

                    holder.itemView.setOnClickListener { v: View? ->

                    }
                    holder.getView<TextView>(R.id.tv_modify).setOnClickListener {
                        loge("修改便签")
                        val intent = Intent(
                            this@ScheduleLabelManageActivity,
                            ScheduleLabelCreateActivity::class.java
                        )
                        intent.putExtra("id", item.id)
                        intent.putExtra("labelName", item.labelName)
                        intent.putExtra("colorValue", item.colorValue)
                        startActivity(intent)
                    }

                    holder.getView<TextView>(R.id.tv_delete).setOnClickListener {
                        loge("删除标签")
                        labelManageViewModel.deleteLabelById(item.id ?: "-1")
                        labelManageViewModel.getLabelDeleteResult()
                            .observe(this@ScheduleLabelManageActivity, {
                                if (it) {
                                    labelManageViewModel.selectLabelList()
                                }
                            })
                    }
                }
            }
        adapter.setList(labelList)
        labelManageBinding.cvLabelList.layoutManager = LinearLayoutManager(this)
        labelManageBinding.cvLabelList.adapter = adapter

        labelManageBinding.clAddLabel.setOnClickListener {
            startActivity(Intent(this, ScheduleLabelCreateActivity::class.java))
        }
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_schedule_label_manage
    }
}