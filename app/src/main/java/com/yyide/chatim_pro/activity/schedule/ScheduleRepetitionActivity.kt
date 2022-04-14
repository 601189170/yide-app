package com.yyide.chatim_pro.activity.schedule

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.base.BaseActivity
import com.yyide.chatim_pro.databinding.ActivityScheduleRepetitionBinding
import com.yyide.chatim_pro.model.schedule.Repetition
import com.yyide.chatim_pro.model.schedule.Repetition.Companion.getList
import com.yyide.chatim_pro.utils.ScheduleRepetitionRuleUtil
import com.yyide.chatim_pro.utils.loge

class ScheduleRepetitionActivity : BaseActivity() {
    lateinit var scheduleRepetitionBinding: ActivityScheduleRepetitionBinding
    val list = getList()
    private var title:String? = null
    private var isRepetition: String? = null
    private var rule:MutableMap<String,Any>? = null
    private var startTime = ""
    private lateinit var adapter: BaseQuickAdapter<Repetition, BaseViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleRepetitionBinding = ActivityScheduleRepetitionBinding.inflate(layoutInflater)
        setContentView(scheduleRepetitionBinding.root)
        initView()
    }

    private fun initView() {
        val stringExtra = intent.getStringExtra("data")
        startTime = intent.getStringExtra("startTime")?:""
        loge("stringExtra $stringExtra")
        val selectedRepetition = JSON.parseObject(stringExtra, Repetition::class.java)
        if (selectedRepetition != null){
            title = selectedRepetition.title
            rule = selectedRepetition.rule
            list.forEach {
                it.checked = it.code == selectedRepetition.code
            }
            list.filter { it.checked }.ifEmpty {
                //自定义重复
                if (rule != null) {
                    scheduleRepetitionBinding.tvCustomRule.text = ScheduleRepetitionRuleUtil.ruleToString(JSON.toJSONString(rule))
                }
                scheduleRepetitionBinding.ivNotRemind.setImageResource(R.drawable.schedule_remind_selected_icon)
            }
        }
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
                isRepetition = selectList.first().code.toString()
            } else {
                title = "自定义重复"
                isRepetition = "8"
            }
            val intent = intent
            val code = isRepetition?.toInt()?:0
            intent.putExtra("data",JSON.toJSONString(Repetition(code,title,true,rule)))
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
                    scheduleRepetitionBinding.ivNotRemind.setImageResource(R.drawable.schedule_arrow_right)
                    notifyDataSetChanged()
                }
            }
        }
        adapter.setList(list)
        scheduleRepetitionBinding.rvRepetitionList.setLayoutManager(LinearLayoutManager(this))
        scheduleRepetitionBinding.rvRepetitionList.setAdapter(adapter)

        scheduleRepetitionBinding.clCustom.setOnClickListener {
            val intent = Intent(this, ScheduleCustomRepetitionActivity::class.java)
            intent.putExtra("startTime",startTime)
            list.filter { it.checked }.ifEmpty {
                intent.putExtra("rule",JSON.toJSONString(rule))
            }
            startActivityForResult(intent, REQUEST_CODE_CUSTOM_REPETITION)
        }
    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_repetition

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CUSTOM_REPETITION && resultCode == RESULT_OK && data != null) {
            val rule = data.getStringExtra("rule")
            this.rule = JSON.parseObject(rule,Map::class.java) as MutableMap<String, Any>?
            loge("rule=$rule")
            if (!TextUtils.isEmpty(rule)) {
                list.forEach {
                    it.checked = false
                }
                adapter.setList(list)
                scheduleRepetitionBinding.tvCustomRule.text = ScheduleRepetitionRuleUtil.ruleToString(rule)
                scheduleRepetitionBinding.ivNotRemind.setImageResource(R.drawable.schedule_remind_selected_icon)
            }
        }
    }

    companion object {
        const val REQUEST_CODE_CUSTOM_REPETITION = 100
    }
}