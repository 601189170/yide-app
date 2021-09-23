package com.yyide.chatim.activity.schedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleRemindBinding
import com.yyide.chatim.model.schedule.Remind

class ScheduleRemindActivity : BaseActivity() {
    lateinit var scheduleRemindBinding: ActivityScheduleRemindBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleRemindBinding = ActivityScheduleRemindBinding.inflate(layoutInflater)
        setContentView(scheduleRemindBinding.root)
        initView()
    }

    private fun initView() {
        val list = Remind.getList()
        scheduleRemindBinding.top.title.text = "日程提醒"
        scheduleRemindBinding.top.backLayout.setOnClickListener {
            finish()
        }
        scheduleRemindBinding.top.tvRight.visibility = View.VISIBLE
        scheduleRemindBinding.top.tvRight.text = "完成"
        scheduleRemindBinding.top.tvRight.setTextColor(resources.getColor(R.color.colorPrimary))
        scheduleRemindBinding.top.tvRight.setOnClickListener {
            val selectList = list.filter { it.checked }
            var remindId = ""
            var remindName = "不提醒"
            if (selectList.isNotEmpty()){
                remindId = selectList.first().id?:""
                remindName = selectList.first().title?:""
            }
            val intent = intent
            intent.putExtra("id",remindId)
            intent.putExtra("name",remindName)
            setResult(RESULT_OK,intent)
            finish()
        }
        val adapter: BaseQuickAdapter<Remind, BaseViewHolder> = object :
            BaseQuickAdapter<Remind, BaseViewHolder>(R.layout.item_dialog_schedule_remind_list) {
            protected override fun convert(baseViewHolder: BaseViewHolder, remind: Remind) {
                baseViewHolder.setText(R.id.tv_title, remind.title)
                val ivRemind = baseViewHolder.getView<ImageView>(R.id.iv_remind)
                ivRemind.visibility = if (remind.checked) View.VISIBLE else View.GONE
                baseViewHolder.itemView.setOnClickListener {
                    for (remind1 in list) {
                        remind1.checked = false
                    }
                    scheduleRemindBinding.ivNotRemind.visibility = View.GONE
                    remind.checked = true
                    notifyDataSetChanged()
                }
            }
        }

        adapter.setList(list)
        scheduleRemindBinding.rvRemindList.setLayoutManager(LinearLayoutManager(this))
        scheduleRemindBinding.rvRemindList.setAdapter(adapter)
        scheduleRemindBinding.clWhetherRemind.setOnClickListener {
            for (remind in list) {
                remind.checked = false
            }
            scheduleRemindBinding.ivNotRemind.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
        }
    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_remind
}