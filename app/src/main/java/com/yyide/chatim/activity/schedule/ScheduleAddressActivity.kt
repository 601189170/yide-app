package com.yyide.chatim.activity.schedule

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleAddressBinding
import com.yyide.chatim.model.schedule.SiteNameRsp
import com.yyide.chatim.viewmodel.SiteManageViewModel

class ScheduleAddressActivity : BaseActivity() {
    lateinit var scheduleAddressBinding: ActivityScheduleAddressBinding
    private val siteManageViewModel: SiteManageViewModel by viewModels()
    private val list = mutableListOf<SiteNameRsp.DataBean>()
    private lateinit var adapter: BaseQuickAdapter<SiteNameRsp.DataBean, BaseViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleAddressBinding = ActivityScheduleAddressBinding.inflate(layoutInflater)
        setContentView(scheduleAddressBinding.root)
        initView()
        initData()
    }

    private fun initData() {
        siteManageViewModel.getSiteInfoList().observe(this, {
            list.addAll(it)
            adapter.setList(list)
        })
    }

    private fun initView() {
        scheduleAddressBinding.top.title.text = "选择场地"
        scheduleAddressBinding.top.backLayout.setOnClickListener {
            finish()
        }
        scheduleAddressBinding.top.tvRight.visibility = View.VISIBLE
        scheduleAddressBinding.top.tvRight.text = "完成"
        scheduleAddressBinding.top.tvRight.setTextColor(resources.getColor(R.color.colorPrimary))
        scheduleAddressBinding.top.tvRight.setOnClickListener {
            val selectedList = list.filter { it.checked }
            if (selectedList.isEmpty()) {
                val intent1 = intent
                val site = SiteNameRsp.DataBean("","无场地",true)
                intent1.putExtra("address", JSON.toJSONString(site))
                setResult(RESULT_OK, intent1)
                finish()
                return@setOnClickListener
            }
            val intent1 = intent
            intent1.putExtra("address", JSON.toJSONString(selectedList.first()))
            setResult(RESULT_OK, intent1)
            finish()
        }

        adapter = object :
            BaseQuickAdapter<SiteNameRsp.DataBean, BaseViewHolder>(R.layout.item_dialog_schedule_remind_list) {
            override fun convert(holder: BaseViewHolder, item: SiteNameRsp.DataBean) {
                holder.setText(R.id.tv_title, item.name)
                val ivRemind = holder.getView<ImageView>(R.id.iv_remind)
                ivRemind.visibility = if (item.checked) View.VISIBLE else View.GONE
                holder.itemView.setOnClickListener { v: View? ->
                    for (remind1 in list) {
                        remind1.checked = false
                    }
                    scheduleAddressBinding.ivNotRemind.visibility = View.GONE
                    item.checked = true
                    notifyDataSetChanged()
                }
            }
        }

        adapter.setList(list)
        scheduleAddressBinding.rvRemindList.setLayoutManager(LinearLayoutManager(this))
        scheduleAddressBinding.rvRemindList.setAdapter(adapter)
        scheduleAddressBinding.clWhetherRemind.setOnClickListener { v ->
            for (remind in list) {
                remind.checked = false
            }
            scheduleAddressBinding.ivNotRemind.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
        }
    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_address
}