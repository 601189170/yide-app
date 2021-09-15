package com.yyide.chatim.activity.schedule

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleRepetitionBinding
import com.yyide.chatim.model.schedule.Repetition
import com.yyide.chatim.model.schedule.Repetition.Companion.getList

class ScheduleRepetitionActivity : BaseActivity() {
    lateinit var scheduleRepetitionBinding: ActivityScheduleRepetitionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleRepetitionBinding = ActivityScheduleRepetitionBinding.inflate(layoutInflater)
        setContentView(scheduleRepetitionBinding.root)
        initView()
    }

    private fun initView() {
        scheduleRepetitionBinding.top.title.text = "选择重复"
        scheduleRepetitionBinding.top.backLayout.setOnClickListener {
            finish()
        }
        scheduleRepetitionBinding.top.tvRight.visibility = View.VISIBLE
        scheduleRepetitionBinding.top.tvRight.text = "完成"
        scheduleRepetitionBinding.top.tvRight.setTextColor(resources.getColor(R.color.colorPrimary))
        scheduleRepetitionBinding.top.tvRight.setOnClickListener {
            finish()
        }

        val list = getList()
        val adapter: BaseQuickAdapter<Repetition, BaseViewHolder> = object :
            BaseQuickAdapter<Repetition, BaseViewHolder>(R.layout.item_dialog_schedule_remind_list) {
            protected override fun convert(baseViewHolder: BaseViewHolder, repetition: Repetition) {
                baseViewHolder.setText(R.id.tv_title, repetition.title)
                val ivRemind = baseViewHolder.getView<ImageView>(R.id.iv_remind)
                ivRemind.visibility = if (repetition.checked) View.VISIBLE else View.GONE
                baseViewHolder.itemView.setOnClickListener { v: View? ->
                    for (repetition1 in list) {
                        repetition1.checked = false
                    }
                    repetition.checked = true
                    notifyDataSetChanged()
                }
            }
        }
        adapter.setList(list)
        scheduleRepetitionBinding.rvRepetitionList.setLayoutManager(LinearLayoutManager(this))
        scheduleRepetitionBinding.rvRepetitionList.setAdapter(adapter)

        scheduleRepetitionBinding.clCustom.setOnClickListener {
            val intent = Intent(this,ScheduleCustomRepetitionActivity::class.java)
            startActivity(intent)
        }
    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_repetition
}