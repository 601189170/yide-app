package com.yyide.chatim.activity.schedule

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleAddressBinding
import com.yyide.chatim.model.schedule.Address

class ScheduleAddressActivity : BaseActivity() {
    lateinit var scheduleAddressBinding: ActivityScheduleAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleAddressBinding = ActivityScheduleAddressBinding.inflate(layoutInflater)
        setContentView(scheduleAddressBinding.root)
        initView()
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
            finish()
        }
        val list = Address.getList()
        val adapter: BaseQuickAdapter<Address, BaseViewHolder> = object :
            BaseQuickAdapter<Address, BaseViewHolder>(R.layout.item_dialog_schedule_remind_list) {
            protected override fun convert(holder: BaseViewHolder, item: Address) {
                holder.setText(R.id.tv_title, item.title)
                val ivRemind = holder.getView<ImageView>(R.id.iv_remind)
                ivRemind.visibility = if (item.checked) View.VISIBLE else View.GONE
                holder.itemView.setOnClickListener { v: View? ->
                    for (remind1 in list) {
                        remind1.checked = false
                    }
                    scheduleAddressBinding.ivNotRemind.setVisibility(View.GONE)
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
            scheduleAddressBinding.ivNotRemind.setVisibility(View.VISIBLE)
            adapter.notifyDataSetChanged()
        }
    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_address
}