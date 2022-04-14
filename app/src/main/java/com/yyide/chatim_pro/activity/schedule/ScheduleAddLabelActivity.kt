package com.yyide.chatim_pro.activity.schedule

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yanzhenjie.recyclerview.widget.DefaultItemDecoration
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.base.BaseActivity
import com.yyide.chatim_pro.databinding.ActivityScheduleAddLabelBinding
import com.yyide.chatim_pro.model.schedule.LabelListRsp
import com.yyide.chatim_pro.utils.ColorUtil
import com.yyide.chatim_pro.utils.loge
import com.yyide.chatim_pro.viewmodel.LabelManageViewModel

class ScheduleAddLabelActivity : BaseActivity() {
    lateinit var labelManageBinding: ActivityScheduleAddLabelBinding
    private var labelList = mutableListOf<LabelListRsp.DataBean>()
    private val labelManageViewModel: LabelManageViewModel by viewModels()
    private lateinit var adapter: BaseQuickAdapter<LabelListRsp.DataBean, BaseViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        labelManageBinding = ActivityScheduleAddLabelBinding.inflate(layoutInflater)
        setContentView(labelManageBinding.root)
        initView()
        val stringExtra = intent.getStringExtra("labelList")
        val labelListSource = JSONArray.parseArray(stringExtra, LabelListRsp.DataBean::class.java)
        labelManageViewModel.getLabelList().observe(this, Observer {
            if (it!=null){
                labelManageBinding.blankPage.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                if (it.isEmpty()) {
                    loge("没有数据")
                    return@Observer
                }
                labelList.clear()
                labelList.addAll(it)
                if (labelListSource != null && labelListSource.isNotEmpty()) {
                    labelList.forEach { dataBean ->
                        if (labelListSource.map { it.id }.contains(dataBean.id)) {
                            dataBean.checked = true
                        }
                    }
                }
                adapter.setList(labelList)
            }

        })
    }

    override fun onRestart() {
        super.onRestart()
        labelManageViewModel.selectLabelList()
    }

    private fun initView() {
        labelManageBinding.top.title.text = "添加标签"
        labelManageBinding.top.backLayout.setOnClickListener {
            finish()
        }
        labelManageBinding.top.tvRight.visibility = View.VISIBLE
        labelManageBinding.top.tvRight.text = "保存"
        labelManageBinding.top.tvRight.setTextColor(resources.getColor(R.color.colorPrimary))
        labelManageBinding.top.tvRight.setOnClickListener {
            val checkedLabelList = labelList.filter { it.checked }
            if (checkedLabelList.isEmpty()) {
                ToastUtils.showShort("请选择标签")
                return@setOnClickListener
            }
            val intent1 = intent
            intent1.putExtra("labelList", JSON.toJSONString(checkedLabelList))
            setResult(RESULT_OK, intent1)
            finish()
        }
        adapter =
            object :
                BaseQuickAdapter<LabelListRsp.DataBean, BaseViewHolder>(R.layout.item_add_label_list) {
                @SuppressLint("WrongConstant")
                override fun convert(holder: BaseViewHolder, item: LabelListRsp.DataBean) {
                    holder.setText(R.id.tv_label, item.labelName)
                    val drawable = GradientDrawable()
//                    drawable.cornerRadius =
//                        DisplayUtils.dip2px(this@ScheduleAddLabelActivity, 2f).toFloat()
//                    drawable.setColor(ColorUtil.parseColor(item.colorValue))
                    drawable.setStroke(1, ColorUtil.parseColor(item.colorValue))

                    drawable.setShape(GradientDrawable.LINEAR_GRADIENT);

                    holder.setTextColor(R.id.tv_label,ColorUtil.parseColor(item.colorValue))
                    holder.getView<TextView>(R.id.tv_label).background = drawable
                    holder.getView<CheckBox>(R.id.checkBox).isSelected = item.checked;
                    holder.itemView.setOnClickListener { v: View? ->
                        item.checked = !item.checked
                        notifyDataSetChanged()
                    }
                }
            }
        adapter.setList(labelList)
        labelManageBinding.cvLabelList.layoutManager = LinearLayoutManager(this)
        labelManageBinding.cvLabelList.adapter = adapter
        labelManageBinding.cvLabelList.addItemDecoration(DefaultItemDecoration(resources.getColor(R.color.default_item_decoration_color)))
        labelManageBinding.clAddLabel.setOnClickListener {
            startActivity(Intent(this, ScheduleLabelCreateActivity::class.java))
        }
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_schedule_add_label
    }
}