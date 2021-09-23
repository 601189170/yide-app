package com.yyide.chatim.activity.schedule

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
import com.yyide.chatim.utils.loge

class ScheduleRepetitionActivity : BaseActivity() {
    lateinit var scheduleRepetitionBinding: ActivityScheduleRepetitionBinding
    val list = getList()
    private var title = ""
    private var rule = ""
    private lateinit var adapter: BaseQuickAdapter<Repetition, BaseViewHolder>
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
            val selectList = list.filter { it.checked }
            if (selectList.isNotEmpty()) {
                title = selectList.first().title
                rule = selectList.first().rule
            } else {
                title = "自定义重复"
            }
            val intent = intent
            intent.putExtra("title", title)
            intent.putExtra("rule",rule)
            setResult(RESULT_OK, intent)
            finish()
        }

        adapter = object :
            BaseQuickAdapter<Repetition, BaseViewHolder>(R.layout.item_dialog_schedule_remind_list) {
            override fun convert(baseViewHolder: BaseViewHolder, repetition: Repetition) {
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
            val intent = Intent(this, ScheduleCustomRepetitionActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_CUSTOM_REPETITION)
        }
    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_repetition

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CUSTOM_REPETITION && resultCode == RESULT_OK && data != null) {
            rule = data.getStringExtra("rule")?:""
            loge("rule=$rule")
            if (!TextUtils.isEmpty(rule)) {
                list.forEach {
                    it.checked = false
                }
                adapter.setList(list)
                scheduleRepetitionBinding.ivNotRemind.setImageResource(R.drawable.schedule_remind_selected_icon)
            }
        }
    }

    companion object {
        const val REQUEST_CODE_CUSTOM_REPETITION = 100
    }
}